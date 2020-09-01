package com.cs2020.capstone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBActivityHelper {
    private static final String DATABASE_NAME = "User.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DBActivity._CREATE0);
            db.execSQL(DBActivity._CREATE1);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DBActivity._TABLENAME);
            db.execSQL("DROP TABLE IF EXISTS "+DBActivity._TABLENAME2);
            onCreate(db);
        }
    }

    public DBActivityHelper(Context context){
        this.mCtx = context;
    }

    public DBActivityHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public long insertColumn(String name, String cate , int lyear, int lmonth, int lday, int ayear, int amonth, int aday,
                             String company, String memo, String image){ //user table insert
        ContentValues values = new ContentValues();
        values.put(DBActivity.COL_NAME, name);
        values.put(DBActivity.COL_CATE, cate);
        values.put(DBActivity.COL_LYEAR, lyear);
        values.put(DBActivity.COL_LMONTH, lmonth);
        values.put(DBActivity.COL_LDAY, lday);
        values.put(DBActivity.COL_AYEAR, ayear);
        values.put(DBActivity.COL_AMONTH, amonth);
        values.put(DBActivity.COL_ADAY, aday);
        values.put(DBActivity.COL_COM, company);
        values.put(DBActivity.COL_MEMO, memo);
        values.put(DBActivity.COL_IMAGE, image);
        return mDB.insert(DBActivity._TABLENAME, null, values);
    }

    public long insertCate(String cate , int amount){ //category table insert
        ContentValues values = new ContentValues();
        values.put(DBActivity.COL_CATE, cate);
        values.put(DBActivity.COL_AMOUNT, amount);
        return mDB.insert(DBActivity._TABLENAME2, null, values);
    }



    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    public Cursor select(String[] colums,
                        String selection,
                        String[] selectionArgs,
                        String groupBy,
                        String having,
                        String orderby)
    { //select 인자에 맞춘 질의문입니다. 조건에 맞춰 null을 사용하면 됩니다.
        return mDB.query(DBActivity._TABLENAME,
                colums,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderby);
    }

    public Cursor selectCate(String[] colums, //category select
                         String selection,
                         String[] selectionArgs,
                         String groupBy,
                         String having,
                         String orderby)
    { //select 인자에 맞춘 질의문입니다. 조건에 맞춰 null을 사용하면 됩니다.
        return mDB.query(DBActivity._TABLENAME2,
                colums,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderby);
    }


    //갱신할 때 사용하는 갱신문입니다
    public boolean updateColumn(int id, String name, String cate , int lyear, int lmonth, int lday, int ayear, int amonth, int aday
                                , String company, String memo, String image){
        ContentValues values = new ContentValues();
        values.put(DBActivity.COL_NAME, name);
        values.put(DBActivity.COL_CATE, cate);
        values.put(DBActivity.COL_LYEAR, lyear);
        values.put(DBActivity.COL_LMONTH, lmonth);
        values.put(DBActivity.COL_LDAY, lday);
        values.put(DBActivity.COL_AYEAR, ayear);
        values.put(DBActivity.COL_AMONTH, amonth);
        values.put(DBActivity.COL_ADAY, aday);
        values.put(DBActivity.COL_COM, company);
        values.put(DBActivity.COL_MEMO, memo);
        values.put(DBActivity.COL_IMAGE, image);
        return mDB.update(DBActivity._TABLENAME, values, "_id=" + id, null)> 0 ;
    }

    public void updateAllCate(String cate, String Ncate){
        mDB.execSQL("UPDATE "+DBActivity._TABLENAME+" SET "+DBActivity.COL_CATE +" ="+" '"+Ncate+"'"+
                " WHERE "+DBActivity.COL_CATE+" ="+" '"+cate+"'");
    }


    public boolean updateCate(String cate , int amount){ //category table update
        ContentValues values = new ContentValues();
        values.put(DBActivity.COL_AMOUNT, amount);
        return mDB.update(DBActivity._TABLENAME2, values, "category= " +"'"+cate+"'", null)> 0 ;
    }


    // user table의 모든 행을 삭제하는 문장입니다
    public void deleteAllColumns() {
        mDB.delete(DBActivity._TABLENAME, null, null);
    }

    // category table의 모든 행 삭제
    public void deleteAllCate() {
        mDB.delete(DBActivity._TABLENAME2, null, null);
    }


    // 해당하는 Column만 삭제하는 문장입니다
    public boolean deleteColumn(int id) {
        return mDB.delete(DBActivity._TABLENAME, "_id=" + id, null) > 0;
    }

    public boolean deleteCate(String cate) {
        return mDB.delete(DBActivity._TABLENAME2, "category= " +"'"+cate+"'", null) > 0;
    }



}
