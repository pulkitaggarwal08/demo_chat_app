package com.demo_chat_app.pulkit.config;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by pulkit on 27/12/17.
 */

public class App extends Application {

    private static Context context;
    private static AppPreferences appPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        context = this;
        appPreferences = AppPreferences.init(context);

    }

    public static AppPreferences getPreferences() {
        return appPreferences;
    }

    public static Context getAppContext() {
        return context;
    }

}
