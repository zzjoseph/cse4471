
package com.example.denny.qrcode;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

public class MyDBHandler extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "cache.db";
	public static final String TABLE_CACHE = "cache";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WEB = "webname";
	public static final String COLUMN_MAL = "malicious";

	public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
	super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		Log.d("Debug", "create db");
		String query = "CREATE TABLE " + TABLE_CACHE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_WEB + "TEXT, " + COLUMN_MAL + " TEXT " + ");";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_CACHE);
		onCreate(db);
	}

	//Add a row to the db
	public void addCache(cacheData cache){
		ContentValues values = new ContentValues();
		values.put(COLUMN_WEB, cache.get_webname());
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_CACHE, null, values);
		db.close();
	}

	//Remove a row from the db
	public void deleteCache(String webname){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " +  TABLE_CACHE + " WHERE " + COLUMN_WEB + "=\"" + webname + "\";");
	}

	//Print db as sting
	public String dbString(){
		String dbString = "";
		SQLiteDatabase db = getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_CACHE + "WHERE 1";

		Cursor cur = db.rawQuery(query, null);
		cur.moveToFirst();

		while(!cur.isAfterLast()){
			if(cur.getString(cur.getColumnIndex("webname"))!=null){
				dbString += cur.getString(cur.getColumnIndex("webname"));
				dbString += "\n";
			}
		}

		db.close();
		return dbString();
	}
	
	//Returns if website is in cache and status of website if in cache.
	//0, not in cache, 1 unknown, 2 safe, 3 unsafe
	public int isInTable(String website){
		int ans;
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT * FROM " + TABLE_CACHE + " WHERE " + COLUMN_WEB + " = \"" + website + "\";";
		Cursor cur = db.rawQuery(query, null);
		if(cur == null){
			ans = 0;
		}
		else{
			String stat = cur.getString(cur.getColumnIndex("malicious"));
			if(stat.equals("unknown")){
				ans = 1;
			}
			else if(stat.equals("safe")){
				ans = 2;
			}
			else{
				ans = 3;
			}
		}
		return ans;
	}
}

