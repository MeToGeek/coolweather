package com.weather.app.activity;

import android.app.Activity;
import android.os.Bundle;

import com.weather.app.db.DBSQLiteHelper;

public class StartActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBSQLiteHelper helper = new DBSQLiteHelper(this, "weather.db", null, 1);
		helper.getWritableDatabase();
	}
}
