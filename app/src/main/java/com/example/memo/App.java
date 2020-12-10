package com.example.memo;

import android.app.Application;

/**
 * @date 12/8/20
 * @description
 */
public class App extends Application {
    private static App instance;
    private DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dbHelper = new DBHelper(this);
        /*dbHelper.addCategory("工作");
        dbHelper.addCategory("个人");
        dbHelper.addCategory("购物");*/
    }

    public static App getInstance() {
        return instance;
    }

    public DBHelper getHelper() {
        return dbHelper;
    }
}
