package com.weather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weather.app.model.City;
import com.weather.app.model.County;
import com.weather.app.model.Province;

public class DBCool {
	public static final String DB_NAME = "weather.db";
	public static final String TB_PROVINCE_NAME = "province";
	public static final String TB_CITY_NAME = "city";
	public static final String TB_COUNTY_NAME = "county";
	public static final int DB_VERSION = 1;
	private static DBCool DB_Cool;
	private SQLiteDatabase db;

	private DBCool(Context context) {
		DBSQLiteHelper helper = new DBSQLiteHelper(context, DB_NAME, null,
				DB_VERSION);
		db = helper.getWritableDatabase();
	}

	public synchronized static DBCool getInstance(Context context) {
		if (DB_Cool == null) {
			DB_Cool = new DBCool(context);
		}
		return DB_Cool;
	};

	/**
	 * 省份的写入
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getP_name());
			values.put("province_code", province.getP_code());
			db.insert(TB_PROVINCE_NAME, null, values);
		}
	}

	/**
	 * 省份的读取
	 */
	public List<Province> loadProvince() {
		List<Province> list = new ArrayList<Province>();
		Cursor query = db.rawQuery("select * from " + TB_PROVINCE_NAME, null);
		if (query != null && query.getCount() > 0) {
			query.moveToFirst();
			while (query.isAfterLast() == false) {
				int id = query.getInt(query.getColumnIndex("id"));
				String name = query.getString(query
						.getColumnIndex("province_name"));
				String code = query.getString(query
						.getColumnIndex("province_code"));
				Province province = new Province(id, name, code);
				list.add(province);
				query.moveToNext();
			}
		}
		return list;
	}

	/**
	 * 市的写入
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("province_id", city.getP_id());
			values.put("city_name", city.getC_name());
			values.put("city_code", city.getC_code());
			db.insert(TB_CITY_NAME, null, values);
		}
	}

	/**
	 * 市的读取
	 */
	public List<City> loadCity(int p_id) {
		List<City> list = new ArrayList<City>();
		Cursor query = db
				.rawQuery("select * from " + TB_CITY_NAME
						+ " where province_id=?",
						new String[] { String.valueOf(p_id) });
		if (query != null && query.getCount() > 0) {
			query.moveToFirst();
			while (query.isAfterLast() == false) {
				int id = query.getInt(query.getColumnIndex("id"));
				String name = query
						.getString(query.getColumnIndex("city_name"));
				String code = query
						.getString(query.getColumnIndex("city_code"));
				City city = new City(id, p_id, name, code);
				list.add(city);
				query.moveToNext();
			}
		}
		return list;
	}

	/**
	 * 县的写入
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("city_id", county.getC_id());
			values.put("county_name", county.getCo_name());
			values.put("county_code", county.getCo_code());
			db.insert(TB_COUNTY_NAME, null, values);
		}
	}

	/**
	 * 县的读取
	 */
	public List<County> loadCounty(int c_id) {
		List<County> list = new ArrayList<County>();
		Cursor query = db.rawQuery("select * from " + TB_COUNTY_NAME
				+ " where city_id=?", new String[] { String.valueOf(c_id) });
		if (query != null && query.getCount() > 0) {
			query.moveToFirst();
			while (query.isAfterLast() == false) {
				int id = query.getInt(query.getColumnIndex("id"));
				String name = query.getString(query
						.getColumnIndex("county_name"));
				String code = query.getString(query
						.getColumnIndex("county_code"));
				County county = new County(id, c_id, name, code);
				list.add(county);
				query.moveToNext();
			}
		}
		return list;
	}
}