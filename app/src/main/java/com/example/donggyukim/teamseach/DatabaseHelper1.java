package com.example.donggyukim.teamseach;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */

public class DatabaseHelper1 extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Company.db";
    public static final String TABLE_NAME = "compahy_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "COMPANY";
    public static final String COL_3 = "PHONE";

//version 을 2로 바꿈
    public DatabaseHelper1(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,COMPANY TEXT,PHONE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2,surname);
        contentValues.put(COL_3,marks);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getAllIno(String a) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select *  from "+TABLE_NAME+" WHERE COMPANY = "+"scm",null);
        return res;
    }


    public boolean updateData(String id,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);

        contentValues.put(COL_2,surname);
        contentValues.put(COL_3,marks);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}