package com.demo_chat_app.pulkit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pulkit on 20/2/18.
 */

public class FriendDBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FriendChat.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "friend";
    public static final String COLUMN_NAME_ID = "friendID";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_ID_ROOM = "idRoom";
    public static final String COLUMN_NAME_AVATAR = "avatar";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME_NAME + " TEXT," +
            COLUMN_NAME_EMAIL + " TEXT," +
            COLUMN_NAME_ID_ROOM + " TEXT," +
            COLUMN_NAME_AVATAR + " TEXT)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public FriendDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }


}
