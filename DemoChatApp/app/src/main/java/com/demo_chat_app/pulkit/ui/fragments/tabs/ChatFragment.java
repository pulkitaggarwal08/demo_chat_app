package com.demo_chat_app.pulkit.ui.fragments.tabs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo_chat_app.pulkit.R;
import com.demo_chat_app.pulkit.adapter.ListFriendsAdapter;
import com.demo_chat_app.pulkit.config.AppConstant;
import com.demo_chat_app.pulkit.db.FriendDB;
import com.demo_chat_app.pulkit.models.Friend;
import com.demo_chat_app.pulkit.models.ListFriend;
import com.demo_chat_app.pulkit.ui.activities.ChatActivity;
import com.demo_chat_app.pulkit.utils.CommonUtils;
import com.demo_chat_app.pulkit.utils.SnackbarUtil;
import com.demo_chat_app.pulkit.utils.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pulkit on 18/2/18.
 */

public class ChatFragment extends Fragment {

    @BindView(R.id.fab)
    public FloatingActionButton floatButton;
    @BindView(R.id.rv_list_friend)
    public RecyclerView rvListFriend;

    private ListFriendsAdapter adapter;
    private ListFriend dataListFriend = null;

    private ArrayList<String> arrayListFriends = new ArrayList<>();

    private String email;

    private Activity activity;

    private FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, view);
        activity = getActivity();

        firebaseDatabase = FirebaseDatabase.getInstance();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {

        if (dataListFriend == null) {

            //Todo: get Data from the database
            dataListFriend = FriendDB.getInstance(getContext()).getListFriend();
            if (dataListFriend.getListFriend().size() > 0) {

                for (Friend friend : dataListFriend.getListFriend()) {

                    //Todo: Add data in arrayList of type String
                    arrayListFriends.add(friend.id);
                }
                //Todo: detect Friends online
//                detectFriendOnline
            }
        }

        rvListFriend.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new ListFriendsAdapter(getContext(), dataListFriend);
        rvListFriend.setAdapter(adapter);

    }

    @OnClick(R.id.fab)
    public void onClickFloatButton() {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.send_message);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        final EditText etEmail = dialog.findViewById(R.id.editTextName);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();

                if (validate()) {

                    findIDEmail(email);
                    dialog.dismiss();

                } else {
                    SnackbarUtil.showSuccessLongSnackbar(rvListFriend, getMessage(R.string.some_network_issue_please_try));

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void findIDEmail(String email) {

        CommonUtils.showProgressDilaog(activity);

        /*Search id to send the request first*/
        firebaseDatabase.getReference().child("user").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        CommonUtils.dismissProgressDilaog();

                        if (dataSnapshot.getValue() == null) {

                            Drawable errorImage = getResources().getDrawable(R.drawable.ic_error_red);
                            alertDialogBox("Fail", "Email not found", errorImage);

                        } else {
                            String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();

                            if (id.equals(AppConstant.UID)) {
                                Drawable errorImage = getResources().getDrawable(R.drawable.ic_error_red);
                                alertDialogBox("Fail", "Email not valid", errorImage);

                            } else {

                                HashMap hashMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                                Friend userFriend = new Friend();

                                userFriend.name = (String) hashMap.get("name");
                                userFriend.email = (String) hashMap.get("email");
                                userFriend.avatar = (String) hashMap.get("avatar");
                                userFriend.id = id;
                                userFriend.idRoom = id.compareTo(AppConstant.UID) > 0 ? (AppConstant.UID + id).hashCode() +
                                        "" : "" + (id + AppConstant.UID).hashCode();
                                checkBeforAddFriend(id, userFriend);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.getMessage();
                    }
                });
    }

    private void checkBeforAddFriend(String idFriend, Friend userFriend) {

        addFriend(idFriend, true);

        //Todo: add data in ArrayList of type String
        arrayListFriends.add(idFriend);

        //Todo: add data in ArrayList of type Friend
        dataListFriend.getListFriend().add(userFriend);

        //Todo: add data in database
        FriendDB.getInstance(getContext()).addFriend(userFriend);

        adapter.notifyDataSetChanged();

    }

    private void addFriend(final String idFriend, boolean isIdFriend) {

        if (idFriend != null) {
            if (isIdFriend) {
                firebaseDatabase.getReference().child("friend/" + AppConstant.UID).push().setValue(idFriend)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    addFriend(idFriend, false);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Drawable errorImage = getResources().getDrawable(R.drawable.ic_error_red);
                                alertDialogBox("False", "Fail to add friend success", errorImage);

                            }
                        });
            } else {
                firebaseDatabase.getReference().child("friend/" + idFriend).push().setValue(AppConstant.UID)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    addFriend(null, false);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Drawable errorImage = getResources().getDrawable(R.drawable.ic_error_red);
                                alertDialogBox("False", "Fail to add friend success", errorImage);

                            }
                        });
            }
        } else {
            Drawable errorImage = getResources().getDrawable(R.drawable.ic_right_green);
            alertDialogBox("Success", "Added friend success", errorImage);

        }
    }

    private void alertDialogBox(String title, String message, Drawable drawable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(drawable);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private boolean validate() {

        if (StringUtils.isEmpty(email)) {
            SnackbarUtil.showWarningShortSnackbar(rvListFriend, getMessage(R.string.empty_email));
            return false;

        } else if (!StringUtils.isEmailMatch(email)) {
            return false;

        }
        return true;
    }

    private String getMessage(int id) {
        return getResources().getString(id);
    }


}
