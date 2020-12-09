package com.example.memo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 12/8/20
 * @description
 */
public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    DBHelper(Context context) {
        super(context, "my_memo", null, 1);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS memo(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS category(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS memo");
        db.execSQL("DROP TABLE IF EXISTS category");
        onCreate(db);
    }

    public void addCategory(String category) {
        Cursor cursor = db.query("category", null, "name=?", new String[]{category}, null, null, null);
        if (!cursor.moveToNext()) {
            db.execSQL("INSERT INTO category (name) VALUES(?)", new Object[]{category});
        }
    }

    public void addMemo(String title) {
        db.execSQL("INSERT INTO memo (name) VALUES(?)", new Object[]{title});
    }

    public List<MemoTypeBean> getCategories() {
        List<MemoTypeBean> list = new ArrayList<>();
        Cursor cursor = db.query("category", null, null, null, null, null, "_id ASC");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(new MemoTypeBean(id, name));
        }
        return list;
    }

    public void deleteCategory(int id) {
        db.delete("category", "_id=?", new String[]{id + ""});
    }
}
