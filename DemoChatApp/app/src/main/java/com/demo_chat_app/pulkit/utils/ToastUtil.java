package com.demo_chat_app.pulkit.utils;

import android.widget.Toast;

import com.demo_chat_app.pulkit.config.App;

/**
 * Created by pulkit on 15/2/18.
 */

public class ToastUtil {

    public static void shortToast(String message){

        Toast.makeText(App.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(String message){

        Toast.makeText(App.getAppContext(), message, Toast.LENGTH_LONG).show();
    }

}
