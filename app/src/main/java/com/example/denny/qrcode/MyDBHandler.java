//package com.example.denny.qrcode;
//
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.Cursor;
//import android.content.Context;
//import android.content.ContentValues;
//
//public class MyDBHandler extends SQLiteOpenHelper{
//
//	private static final int DATABASE_VERSION = 1;
//	private static final String DATABASE_NAME = "cache.db";
//	public static final String TABLE_CACHE = "cache";
//	public static final String COLUMN_ID = "_id";
//	public static final String COLUMN_WEB = "webname";
//	public static final String COLUMN_MAL = "malicious";
//
//	public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//	super(context, DATABASE_NAME, factory, DATABASE_VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db){
//		String query = "CREATE TABLE" + "TABLE_CACHE" + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COLUMN_WEB + "TEXT " + COLUMN_MAL + " TEXT " + ");";
//		db.execSQL(query);
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
//		db.execSQL("DROP TABLE IF EXISTS" + TABLE_CACHE);
//		onCeate(db);
//	}
//
//	//Add a row to the db
//	public void addCache(cacheData cache){
//		ContentValues values = new ContentValues();
//		values.put(COLUMN_WEB, cacheData.get_webname());
//		SQLiteDatabase db = getWritableDatabase();
//		db.insert(TABLE_CACHE, null, values);
//		db.close();
//	}
//
//	//Remove a row from the db
//	public void deleteCache(String webname){
//		SQLiteDatabase db = getWritableDatabase();
//		db.execSQL("DELETE FROM " +  TABLE_CACHE + " WHERE " + COLUMN_WEB + "=\"" + webname + "\";");
//	}
//}
