package com.wcs.sms.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {  
	  
    private final static String DATABASE_NAME = "smsEncryph";  
    private final static int DATABASE_VERSION = 1;  
    private final static String TABLE_NAME = "privateKey";  

    //���캯�����������ݿ�  
    public SQLiteHelper(Context context) {  
           super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }  

    @Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME   
                + "(_id INTEGER PRIMARY KEY,"   
                + " userName VARCHAR(30)  NOT NULL,"   
                + " privateKey VARCHAR(200))";  
        db.execSQL(sql);  
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;  
         db.execSQL(sql);  
         onCreate(db);  
	}  
	
    //����  
//    public void onCreate(SQLiteDatabase db) {  
//           String sql = "CREATE TABLE " + TABLE_NAME   
//                   + "(_id INTEGER PRIMARY KEY,"   
//                   + " BookName VARCHAR(30)  NOT NULL,"   
//                   + " Author VARCHAR(20),"  
//                   + " Publisher VARCHAR(30))";  
//           db.execSQL(sql);  
//    }  


//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
//           String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;  
//           db.execSQL(sql);  
//           onCreate(db);  
//    }  

    //��ȡ�α�  
    public Cursor select() {  
           SQLiteDatabase db = this.getReadableDatabase();  
           Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);  
           return cursor;  
    }  

    //����һ����¼  
    public long insert(String userName,String privateKey ) {  
           SQLiteDatabase db = this.getWritableDatabase();  
           ContentValues cv = new ContentValues();  
           cv.put("userName", userName);  
           cv.put("privateKey", privateKey);  
           long row = db.insert(TABLE_NAME, null, cv);  
           return row;  
    }  
    //����
    public void update(String userName,String privateKey )  
    {  
        SQLiteDatabase db = this.getWritableDatabase();  
        db.execSQL("UPDATE privateKey SET privateKey = ? WHERE userName = ?",  
            new String[]{privateKey,userName});  
    } 
    
    //����������ѯ  
    public String queryPrivateKey(String userName) {  
        SQLiteDatabase db = this.getReadableDatabase();  
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE userName = ?", new String[]{userName});  
        String privateKey = null;
        if (cursor.moveToFirst()) {  
            for (int i = 0; i < cursor.getCount(); i++) {  
            	privateKey = cursor.getString(cursor.getColumnIndex("privateKey"));  
            	cursor.moveToNext();  
            }  
        }  
        return privateKey;  
    }  

    //ɾ����¼  
    public void delete(int id) {  
           SQLiteDatabase db = this.getWritableDatabase();  
           String where ="_id = ?";  
           String[] whereValue = { Integer.toString(id) };  
           db.delete(TABLE_NAME, where, whereValue);  
    }  

  
	
}  
