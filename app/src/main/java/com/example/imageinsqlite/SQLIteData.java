package com.example.imageinsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class SQLIteData extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Cameraphoto.db";
    public SQLIteData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table save(id integer PRIMARY KEY AUTOINCREMENT,photo blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insert(byte [] photo1)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("photo",photo1);
        long res= db.insert("save",null,cv);
        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor search(int a)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from save where id=?",new String[]{String.valueOf(a)});
        return res;
    }

}
