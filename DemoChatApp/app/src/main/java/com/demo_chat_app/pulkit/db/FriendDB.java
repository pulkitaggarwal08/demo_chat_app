package com.demo_chat_app.pulkit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo_chat_app.pulkit.models.Friend;
import com.demo_chat_app.pulkit.models.ListFriend;

/**
 * Created by pulkit on 20/2/18.
 */

public class FriendDB {

    private SQLiteDatabase sqLiteDatabase;
    private static FriendDBHandler friendDBHandler;
    private static FriendDB instance = null;

    private FriendDB() {
    }

    public static FriendDB getInstance(Context context) {
        if (instance == null) {
            instance = new FriendDB();
            friendDBHandler = new FriendDBHandler(context);
        }
        return instance;
    }

    public FriendDB open() throws Exception {
        sqLiteDatabase = friendDBHandler.getWritableDatabase();
        return this;
    }

    public void close() {
        friendDBHandler.close();
    }

    /*Add new Item*/
    public long addFriend(Friend friend) {
        sqLiteDatabase = friendDBHandler.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(FriendDBHandler.COLUMN_NAME_ID, friend.id);
        contentValues.put(FriendDBHandler.COLUMN_NAME_NAME, friend.name);
        contentValues.put(FriendDBHandler.COLUMN_NAME_EMAIL, friend.email);
        contentValues.put(FriendDBHandler.COLUMN_NAME_ID_ROOM, friend.idRoom);
        contentValues.put(FriendDBHandler.COLUMN_NAME_AVATAR, friend.avatar);

        return sqLiteDatabase.insert(FriendDBHandler.TABLE_NAME, null, contentValues);
    }

    public void addListFriend(ListFriend listFriend) {
        for (Friend friend : listFriend.getListFriend()) {
            addFriend(friend);
        }
    }

    /*Get item by id*/
//    public AddNewItem getItem(String id) {
//        AddNewItem contact = null;
//        SQLiteDatabase db = dataBaseHandler.getReadableDatabase();
//
//        Cursor cursor = db.query(DataBaseHandler.TABLE_KEYPAD, new String[]{DataBaseHandler.COLUMN_NEW_ITEM_NAME,
//                        DataBaseHandler.COLUMN_NEW_ITEM_PRICE, DataBaseHandler.COLUMN_NEW_ITEM_QUANTITY,
//                        DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE, DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID,
//                        DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE_PLUS_TAX},
//                DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID + "=?",
//                new String[]{id}, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            if (cursor.moveToFirst() == true) {
//                contact = new AddNewItem(cursor.getString(0), cursor.getString(1),
//                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
//            } else {
//                return contact;
//            }
//        }
//
//        // return contact
//        return contact;
//    }

    /*Get All Items*/
    public ListFriend getListFriend() {

        ListFriend listFriend = new ListFriend();
        sqLiteDatabase = friendDBHandler.getWritableDatabase();

        try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + FriendDBHandler.TABLE_NAME, null);
            while (cursor.moveToNext()) {
                Friend friend = new Friend();
                friend.id = cursor.getString(0);
                friend.name = cursor.getString(1);
                friend.email = cursor.getString(2);
                friend.idRoom = cursor.getString(3);
                friend.avatar = cursor.getString(4);

                listFriend.getListFriend().add(friend);
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            return new ListFriend();
        }
        return listFriend;
    }

    public void dropDB() {
        SQLiteDatabase db = friendDBHandler.getWritableDatabase();
        db.execSQL(FriendDBHandler.SQL_DELETE_ENTRIES);
        db.execSQL(FriendDBHandler.CREATE_TABLE);
    }

//    public int updateNewItem(String new_item_price, String new_item_total_price, String new_item_key_id) {
//
//        ContentValues values = new ContentValues();
//        values.put(FriendDBHandler.COLUMN_NEW_ITEM_QUANTITY, new_item_price);
//        values.put(FriendDBHandler.COLUMN_NEW_ITEM_TOTAL_PRICE, new_item_total_price);
//
//        // updating row
//        return sqLiteDatabase.update(FriendDBHandler.TABLE_KEYPAD, values,
//                FriendDBHandler.COLUMN_NEW_ITEM_KEY_ID + " = ?", new String[]{new_item_key_id});
//
//    }

    /*Delete item by id*/
//    public void deleteNewItem(String new_item_key_id) {
//        sqLiteDatabase.delete(DataBaseHandler.TABLE_KEYPAD,
//                FriendDBHandler.COLUMN_NEW_ITEM_KEY_ID + "= ?", new String[]{new_item_key_id});
//    }

    /*Remove All*/
//    public void removeAll() {
//        SQLiteDatabase db = friendDBHandler.getWritableDatabase();
//        db.delete(DataBaseHandler.TABLE_KEYPAD, null, null);
//    }


}
