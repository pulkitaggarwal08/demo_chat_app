package com.demo_chat_app.pulkit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.demo_chat_app.pulkit.models.Conversations;
import com.demo_chat_app.pulkit.ui.activities.ChatActivity;

/**
 * Created by pulkit on 22/2/18.
 */

public class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Conversations conversations;

    public ListMessageAdapter(ChatActivity chatActivity, Conversations conversations) {


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
