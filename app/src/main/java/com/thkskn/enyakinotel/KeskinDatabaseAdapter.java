package com.thkskn.enyakinotel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tahaKeskin on 8/15/15.
 */
public class KeskinDatabaseAdapter {

    KeskinHelper helper;
    public KeskinDatabaseAdapter(Context context){
        helper = new KeskinHelper(context);
    }

    public long insertData(String name,String fee,String statu,String count){

        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KeskinHelper.NAME, name);
        contentValues.put(KeskinHelper.FEE, fee);
        contentValues.put(KeskinHelper.STATU, statu);
        contentValues.put(KeskinHelper.COUNT, count);
        long id = db.insert(KeskinHelper.TABLE_NAME,null,contentValues);
        return id;
    }

    public String getAllData(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {KeskinHelper.UID,KeskinHelper.NAME,KeskinHelper.FEE,KeskinHelper.STATU,KeskinHelper.COUNT};
        Cursor cursor = db.query(KeskinHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            //int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            String fee = cursor.getString(2);
            //String statu = cursor.getString(3);
            //String count = cursor.getString(4);
            //buffer.append(cid + " "+ name + " " + fee + " " + statu + " " + count + "\n");
            buffer.append(name + " " + fee + "\n");
        }
        return buffer.toString();
    }
    public String getData(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {KeskinHelper.UID,KeskinHelper.NAME,KeskinHelper.FEE,KeskinHelper.STATU,KeskinHelper.COUNT};
        Cursor cursor = db.query(KeskinHelper.TABLE_NAME, columns, KeskinHelper.NAME+" = '"+name+"'", null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int index1 = cursor.getColumnIndex(KeskinHelper.NAME);
            String hotelName = cursor.getString(index1);
            buffer.append(hotelName);
        }
        return buffer.toString();

    }
    public int updateStatu(String oldStatu,String newStatu){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KeskinHelper.STATU,newStatu);
        String[] whereArgs = {oldStatu};
        int count = db.update(KeskinHelper.TABLE_NAME,contentValues,KeskinHelper.STATU+" =? ",whereArgs);
        return count;
    }
    public String getAllStatu(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {KeskinHelper.UID,KeskinHelper.NAME,KeskinHelper.FEE,KeskinHelper.STATU,KeskinHelper.COUNT};
        Cursor cursor = db.query(KeskinHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            String statu = cursor.getString(3);
            buffer.append(statu);
        }
        return buffer.toString();
    }

    public String getLastRecord(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {KeskinHelper.UID,KeskinHelper.NAME,KeskinHelper.FEE,KeskinHelper.STATU,KeskinHelper.COUNT};
        Cursor cursor = db.query(KeskinHelper.TABLE_NAME, columns, KeskinHelper.STATU + " = '" + true + "'", null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int index1 = cursor.getColumnIndex(KeskinHelper.NAME);
            String hotelName = cursor.getString(index1);
            buffer.append(hotelName);
        }
        return buffer.toString();

    }

    static class KeskinHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "keskindb";
        private static final String TABLE_NAME = "ACCOUNT";
        private static final int DATABASE_VERSION = 9;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String FEE = "Fee";
        private static final String STATU = "Statu";
        private static final String COUNT = "COUNT";
        private static final String CREATE = "CREATE TABLE "+TABLE_NAME+" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME+" VARCHAR(255),"+FEE+" VARCHAR(255),"+STATU+" VARCHAR(255),"+COUNT+" VARCHAR(255));";
        private static final String DROP = "DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public KeskinHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
            this.context = context;
            //Message.message(context,"Contructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE);
                //Message.message(context, "onCrateCalled");
            } catch (SQLException e) {
                Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //Message.message(context,"onUpgrade Called");
                db.execSQL(DROP);
                onCreate(db);
            } catch (SQLException e) {
                Message.message(context,""+e);
            }
        }
    }
}
