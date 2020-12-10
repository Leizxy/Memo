package com.example.memo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

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
                "name TEXT," +
                "category INTEGER," +
                "imgs TEXT," +
                "time BIGINT)");
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

    public void addMemo(int id, String title, int category, String imgs, long time) {
        db.execSQL("INSERT or REPLACE INTO memo (_id,name,category,imgs,time) VALUES(?,?,?,?,?)", new Object[]{id, title, category, imgs, time});
    }

    public void addMemo(String title, int category, String imgs, long time) {
        db.execSQL("INSERT INTO memo (name,category,imgs,time) VALUES(?,?,?,?)", new Object[]{title, category, imgs, time});
    }

    public List<MemoBean> getMemos() {
        List<MemoBean> list = new ArrayList<>();
        Cursor cursor = db.query("memo", null, null, null, null, null, "_id ASC");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int category = cursor.getInt(cursor.getColumnIndex("category"));
            String imgs = cursor.getString(cursor.getColumnIndex("imgs"));
            long time = cursor.getLong(cursor.getColumnIndex("time"));
            list.add(new MemoBean(id, name, category, imgs, time));
        }
        return list;
    }

    public List<MemoBean> getExpiredMemos(long cur) {
        List<MemoBean> list = new ArrayList<>();
        Cursor cursor = db.query("memo", null, "time<=?", new String[]{cur + ""}, null, null, "time DESC");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int category = cursor.getInt(cursor.getColumnIndex("category"));
            String imgs = cursor.getString(cursor.getColumnIndex("imgs"));
            long time = cursor.getLong(cursor.getColumnIndex("time"));
            list.add(new MemoBean(id, name, category, imgs, time));
        }
        return list;
    }

    public List<MemoBean> getTodoMemos(long cur) {
        List<MemoBean> list = new ArrayList<>();
        Cursor cursor = db.query("memo", null, "time>?", new String[]{cur + ""}, null, null, "time ASC");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int category = cursor.getInt(cursor.getColumnIndex("category"));
            String imgs = cursor.getString(cursor.getColumnIndex("imgs"));
            long time = cursor.getLong(cursor.getColumnIndex("time"));
            list.add(new MemoBean(id, name, category, imgs, time));
        }
        return list;
    }

    public MemoBean getMemo(int memoId) {
        if (memoId > 0) {
            Cursor cursor = db.query("memo", null, "_id=?", new String[]{memoId + ""}, null, null, "_id ASC");
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int category = cursor.getInt(cursor.getColumnIndex("category"));
                String imgs = cursor.getString(cursor.getColumnIndex("imgs"));
                long time = cursor.getLong(cursor.getColumnIndex("time"));
                return new MemoBean(id, name, category, imgs, time);
            }
        }
        return null;
    }

    public List<MemoBean> getMemo(String content) {
        List<MemoBean> list = new ArrayList<>();
        if (!TextUtils.isEmpty(content)) {
            Cursor cursor = db.query("memo", null, "name like ?", new String[]{"%" + content + "%"}, null, null, "_id ASC");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int category = cursor.getInt(cursor.getColumnIndex("category"));
                String imgs = cursor.getString(cursor.getColumnIndex("imgs"));
                long time = cursor.getLong(cursor.getColumnIndex("time"));
                list.add(new MemoBean(id, name, category, imgs, time));
            }
        }
        return list;
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

    public String getCategory(int category) {
        if (category != -1) {
            Cursor cursor = db.query("category", null, "_id=?", new String[]{category + ""}, null, null, "_id ASC");
            if (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex("name"));
            }
        }
        return "未分类";
    }

}
