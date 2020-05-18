package com.example.hp.jsonparsingretrofit;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

  //  private Resources mResources;
    private static final String DATABASE_NAME ="userdata.db";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_NAME = "users";
    private static final String COL1= "id";
    private static final String COL2 = "name";
    private static final String COL3 = "email";
    private static final String COL4 = "city";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //mResources = context.getResources();
       // mContext=context;
      //  SQLiteDatabase  db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT" + ")";
        db.execSQL(CreateTable);

     //   db.execSQL("create table users " +
        //        "(name text,email text,city text)");

        Log.d(TAG,"Database Created Successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item1,String item2, String item3) {
         SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", item1);
        contentValues.put("email", item2);
        contentValues.put("city", item3);

        Log.d(TAG, "addData: adding " + item1 + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        //db.insert(TABLE_NAME,null,contentValues);

       if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<String> getallData() {
        ArrayList<String> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery= "SELECT * FROM " +TABLE_NAME;
        Cursor res = db.rawQuery(selectQuery, null);

        String res1 = "",res2="",res3="";

        if(res.moveToFirst()) {
            do {

                res1=  res.getString(res.getColumnIndex("name"));
                res2=  res.getString(res.getColumnIndex("email"));
                res3=  res.getString(res.getColumnIndex("city"));

                 //userList.add(res.getString(res.getColumnIndex("city")));
                 //userList.add(res.getString(res.getColumnIndex("name")));




            } while (res.moveToNext());

            userList.add(res1);
            userList.add(res2);
            userList.add(res3);


        }
        return userList;
    }

    public boolean deleteUser(String id)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        long del = db.delete(TABLE_NAME,COL2 + "=?",new String[] {id});

         if(del>0)
         {
             return true;
         }

         else
         {
             return false;
         }
    }

    public Integer getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public Cursor getUersList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
         db.delete(TABLE_NAME,null,null);
        db.execSQL("delete  from "+ TABLE_NAME);
        db.close();
    }


}

