package com.hritik.groupandlock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;

public class MyHelper extends SQLiteOpenHelper
{
    public MyHelper(Context context){
        super(context,"gnl.db",null,1);
    }
    public void onCreate(SQLiteDatabase db){
        try {
            db.execSQL("create table pass(ps text)");
            db.execSQL("create table grp(grp_name text)");
            db.execSQL("create table app_dts(app_name text,grp_name text,pkg_name text)");
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }}
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("drop table if exists pass");
        db.execSQL("drop table if exists grp");
        db.execSQL("drop table if exists app_dts");
        onCreate(db);
    }

    public Cursor getAllData(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+table;
        Cursor  cursor = db.rawQuery(query,null);
        return cursor;
    }
    public Cursor getCondnData(String table,String grp){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+table+" WHERE grp_name= '"+grp+"'";
        Cursor  cursor = db.rawQuery(query,null);
        return cursor;
    }

    public void onDeleteOne(String table,String col,String con){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+table+" WHERE "+col+" = '" + con + "'";
        db.execSQL(query);
    }
    public Cursor getCategoryNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT cname FROM catnm";
        Cursor  cursor = db.rawQuery(query,null);
        return cursor;
    }

    public int getAppSectionCount(String sect){
        String countQuery = "SELECT  * FROM app_dts WHERE grp_name='"+sect+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public void onDeleteAll(String tb){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+tb;
        db.execSQL(query);
        db.close();
    }
    public void onDeleteAllTables(){
        ArrayList<String> tables = new ArrayList<String>();
        tables.add("ach");
        tables.add("dec");
        tables.add("obj");
        tables.add("sk");
        tables.add("prg");
        tables.add("edu");
        tables.add("intr");
        tables.add("exp");
        tables.add("psl");

        for(int i=0;i<tables.size();i++) {
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                String query = "DELETE FROM " + tables.get(i);
                db.execSQL(query);
            }
            catch (Exception e){e.printStackTrace();}
            db.close();
        }

    }

}


