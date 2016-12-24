package com.ay3524.notepadadvanced;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

class DbAdapter {

    DbHelper helper;
    public DbAdapter(Context context){
        helper = new DbHelper(context);
    }
    public long insertData(String content , String date){

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        //contentValues.put(DbHelper.TITLE , title);
        contentValues.put(DbHelper.CONTENT , content);
        contentValues.put(DbHelper.DATE , date);

        return db.insert(DbHelper.TABLE_NAME,null,contentValues);

    }
    public void deleteData(String date){

        SQLiteDatabase db = helper.getWritableDatabase();

        String whereArgs[] = {date};
        db.delete(DbHelper.TABLE_NAME,DbHelper.DATE + "=?",whereArgs);
    }
    public List<Notes> getAllData(){

        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DbHelper.CONTENT,DbHelper.DATE};
        Cursor cursor = db.query(DbHelper.TABLE_NAME,columns,null,null,null,null,null);

        //StringBuffer buffer = new StringBuffer();

        List<Notes> notesList = new ArrayList<>();
        while (cursor.moveToNext()){
            //String title = cursor.getString(0);
            String content = cursor.getString(0);
            String date = cursor.getString(1);
            Notes notes = new Notes();
            //notes.setNoteTitle(title);
            notes.setNoteContent(content);
            notes.setNoteReadableDate(date);

            notesList.add(0,notes);

            //buffer.append(title + " " + content + " " + date + "\n");
        }
        cursor.close();
        return notesList;

    }

    static class DbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "ashdb";
        private static final int DATABASE_VERSION = 2;
        private static final String TABLE_NAME = "ashtable";
        //private static final String TITLE = "title";
        private static final String CONTENT = "content";
        private static final String DATE = "date";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + CONTENT + " VARCHAR," + DATE + " VARCHAR);";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private final Context context;

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

//        String query = "CREATE TABLE " + TABLE_NAME + " (title VARCHAR,content VARCHAR,date VARCHAR);";

            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}