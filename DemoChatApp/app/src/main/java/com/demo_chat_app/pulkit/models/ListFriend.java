package com.demo_chat_app.pulkit.models;

import java.util.ArrayList;

/**
 * Created by pulkit on 19/2/18.
 */

public class ListFriend {

    private ArrayList<Friend> listFriend;

    public ListFriend() {

        listFriend = new ArrayList<>();
    }

    public ListFriend(ArrayList<Friend> listFriend) {
        this.listFriend = listFriend;
    }

    public ArrayList<Friend> getListFriend() {
        return listFriend;
    }

    public void setListFriend(ArrayList<Friend> listFriend) {
        this.listFriend = listFriend;
    }

}
