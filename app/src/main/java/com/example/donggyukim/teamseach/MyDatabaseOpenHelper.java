package com.example.donggyukim.teamseach;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper
{
    public static final String tableName = "name";
    public static final String tableName1 = "contact";
    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createTable(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " + tableName + "(name text)";
        String sql1 = "CREATE TABLE " + tableName1 + "(name text)";
        try
        {
            db.execSQL(sql);db.execSQL(sql1);
        }
        catch (SQLException e)
        {
        }
    }

    public void insertName(SQLiteDatabase db, String name)
    {
        db.beginTransaction();
        try
        {
            String sql = "insert into " + tableName + "(name)" + " values('" + name + "')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }
    public void insertContact(SQLiteDatabase db, String contact)
    {
        db.beginTransaction();
        try
        {
            String sql = "insert into " + tableName1 + "(contact)" + " values('" + contact + "')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }
}


