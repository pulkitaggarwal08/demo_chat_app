package com.demo_chat_app.pulkit.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.demo_chat_app.pulkit.R;
import com.demo_chat_app.pulkit.adapter.ListMessageAdapter;
import com.demo_chat_app.pulkit.config.AppConstant;
import com.demo_chat_app.pulkit.models.Conversations;
import com.demo_chat_app.pulkit.models.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.button_emoji)
    public ImageView buttonEmoji;
    @BindView(R.id.rv_recycler_chat)
    public RecyclerView rvRecyclerChat;
    @BindView(R.id.iv_attachment)
    public ImageView ivAttachment;
    @BindView(R.id.iv_send_message)
    public ImageView ivSendMessage;
    @BindView(R.id.content_root)
    public View contentRoot;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

//    public EditText et_keyboard;

    private String roomId;
    private ArrayList<CharSequence> idFriend;
    private String nameFriend;

    @BindView(R.id.et_emojicon)
    public EmojiconEditText etEmojicon;
    private EmojIconActions emojIconActions;

    private RecyclerView.LayoutManager layoutManager;

    private Conversations conversations;
    private ListMessageAdapter listMessageAdapter;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        firebaseDatabase = FirebaseDatabase.getInstance();

        setupToolbar();
        init();
    }

    private void setupToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Intent intentData = getIntent();
        idFriend = intentData.getCharSequenceArrayListExtra(AppConstant.KEY_FRIEND_ID);
        roomId = intentData.getStringExtra(AppConstant.KEY_CHAT_ROOM_ID);
        nameFriend = intentData.getStringExtra(AppConstant.KEY_NAME_OF_FRIEND);

        /*Set emoji Actions*/
        ivSendMessage = findViewById(R.id.iv_send_message);
        buttonEmoji = findViewById(R.id.button_emoji);
        contentRoot = findViewById(R.id.content_root);
        etEmojicon = findViewById(R.id.et_emojicon);
        ivAttachment = findViewById(R.id.iv_attachment);

        emojIconActions = new EmojIconActions(this, contentRoot, etEmojicon, buttonEmoji);
        emojIconActions.ShowEmojIcon();

//        et_keyboard = findViewById(R.id.et_emojicon);

        ivSendMessage.setOnClickListener(this);
        ivAttachment.setOnClickListener(this);

        if (idFriend != null && nameFriend != null) {

            /*Set the friend name on ToolBar*/
            toolbar.setTitle(nameFriend);

            conversations = new Conversations();

            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvRecyclerChat.setLayoutManager(layoutManager);

            listMessageAdapter = new ListMessageAdapter(this, conversations);

            firebaseDatabase.getReference().child("messages/" + roomId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null) {

                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                        Message newMessage = new Message();

                        newMessage.idReceiver = (String) mapMessage.get("idReceiver");
                        newMessage.idSender = (String) mapMessage.get("idSender");
                        newMessage.text = (String) mapMessage.get("text");
                        newMessage.timestamp = (long) mapMessage.get("timestamp");

                        conversations.getListMessageData().add(newMessage);
                        listMessageAdapter.notifyDataSetChanged();

                        //Todo: check the scrol position
                        layoutManager.scrollToPosition(conversations.getListMessageData().size() - 1);

                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            rvRecyclerChat.setAdapter(listMessageAdapter);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_send_message) {
            String content = etEmojicon.getText().toString().trim();

            if (content.length() > 0) {

                etEmojicon.setText("");
                Message newMessage = new Message();
                newMessage.text = content;
                newMessage.idSender = AppConstant.UID;
//                newMessage.idReceiver = roomId;
                newMessage.idReceiver = idFriend.get(0).toString();
                newMessage.timestamp = System.currentTimeMillis();

                firebaseDatabase.getReference().child("message/" + roomId).push().setValue(newMessage);

            } else if (view.getId() == R.id.iv_attachment) {
                photoFromGalleryIntent();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent result = new Intent();
            result.putExtra("idFriend", idFriend.get(0));
            setResult(RESULT_OK, result);
            this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent result = new Intent();
        result.putExtra("idFriend", idFriend.get(0));
        setResult(RESULT_OK, result);
        this.finish();
    }

    private void photoFromGalleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), AppConstant.IMAGE_GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AppConstant.IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {

                /*1) For Convert image*/
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                UploadPostTask uploadPostTask = new UploadPostTask();
//                uploadPostTask.execute(imageBitmap);

                 /*2) For Uri, without convert, load original image*/
                Uri selectedImageUri = data.getData();

                //Todo: send file to firebase storage

            }
        }

    }

     /*1) For Convert image*/
//    private class UploadPostTask extends AsyncTask<Bitmap, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Bitmap... params) {
//            Bitmap bitmap = params[0];
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
//            storageRef.child(UUID.randomUUID().toString() + "jpg").putBytes(byteArrayOutputStream.toByteArray())
//                    .addOnSuccessListener(
//                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    if (taskSnapshot.getDownloadUrl() != null) {
//                                        String imageUrl = taskSnapshot.getDownloadUrl().toString();
//                                        final Message message = new Message(imageUrl);
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                messagesAdapter.addMessage(message);
//                                            }
//                                        });
//                                    }
//                                }
//                            });
//
//            return null;
//        }
//    }


}














