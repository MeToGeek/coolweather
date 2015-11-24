package com.weather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSQLiteHelper extends SQLiteOpenHelper {
	private String table_province = "create table province(id integer primary key autoincrement,province_name text,province_code text)";
	private String table_city = "create table city(id integer primary key autoincrement,city_name text,city_code text,province_id integer)";
	private String table_county = "create table county(id integer primary key autoincrement,county_name text,county_code text,city_id integer)";

	public DBSQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(table_province);
		db.execSQL(table_city);
		db.execSQL(table_county);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// 如果更新数据库版本时，
		// 为了保留原来表中数据，
		// 可以在这里判断数据库版本，
		// 如果没有就全部执行，
		// 如果有就之执行更新的部分。
	}
}