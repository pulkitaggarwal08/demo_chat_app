package com.demo_chat_app.pulkit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pulkit on 21/2/18.
 */

public class Conversations implements Serializable {

    private ArrayList<Message> listMessageData;

    public Conversations() {
        listMessageData = new ArrayList<>();
    }

    public ArrayList<Message> getListMessageData() {
        return listMessageData;
    }

    public void setListMessageData(ArrayList<Message> listMessageData) {
        this.listMessageData = listMessageData;
    }
}
