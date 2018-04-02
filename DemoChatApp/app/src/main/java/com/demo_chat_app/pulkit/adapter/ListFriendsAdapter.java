package com.demo_chat_app.pulkit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo_chat_app.pulkit.R;
import com.demo_chat_app.pulkit.config.AppConstant;
import com.demo_chat_app.pulkit.models.Friend;
import com.demo_chat_app.pulkit.models.ListFriend;
import com.demo_chat_app.pulkit.ui.activities.ChatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pulkit on 19/2/18.
 */

public class ListFriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ListFriend listFriend;
    private onClickListener onClickListener;

    public ListFriendsAdapter(Context context, ListFriend listFriend) {
        this.context = context;
        this.listFriend = listFriend;
        this.onClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClickList(int position, int id, Friend friend);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_friend, parent, false);

        return new ItemFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ItemFriendViewHolder itemFriendViewHolder = (ItemFriendViewHolder) holder;

        final String name = listFriend.getListFriend().get(position).name;
        final String id = listFriend.getListFriend().get(position).id;
        final String idRoom = listFriend.getListFriend().get(position).idRoom;
        String avatar = listFriend.getListFriend().get(position).avatar;
        String timestamp = String.valueOf(listFriend.getListFriend().get(position).message.timestamp);

        itemFriendViewHolder.tvName.setText(name);
        itemFriendViewHolder.tvTime.setText(timestamp);

        itemFriendViewHolder.llList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();

                itemFriendViewHolder.tvName.setTypeface(Typeface.DEFAULT);
                itemFriendViewHolder.tvMessage.setTypeface(Typeface.DEFAULT);

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(AppConstant.KEY_NAME_OF_FRIEND, name);                              //Pass name

                idFriend.add(id);
                intent.putCharSequenceArrayListExtra(AppConstant.KEY_FRIEND_ID, idFriend);          //Pass Friend name
                intent.putExtra(AppConstant.KEY_CHAT_ROOM_ID, idRoom);                              //Pass Room Id


                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listFriend.getListFriend() != null ? listFriend.getListFriend().size() : 0;
    }

    private class ItemFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CircleImageView ivAvatar;
        public TextView tvName, tvTime, tvMessage;
        public LinearLayout llList;

        public ItemFriendViewHolder(View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.icon_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvMessage = itemView.findViewById(R.id.tv_message);
            llList = itemView.findViewById(R.id.ll_list);

            llList.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
//            onClickListener.onClickList(getLayoutPosition(), view.getId(), listFriend.getListFriend().get(getLayoutPosition()));
        }

    }

}
