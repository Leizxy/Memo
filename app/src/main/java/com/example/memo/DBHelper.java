package com.example.memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @date 12/8/20
 * @description
 */
public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, "memo", null, 1);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS memo(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS type(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS memo");
        db.execSQL("DROP TABLE IF EXISTS type");
        onCreate(db);
    }
}
