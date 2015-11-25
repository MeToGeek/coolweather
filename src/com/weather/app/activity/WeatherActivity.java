package com.weather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weather.app.R;
import com.weather.app.util.AnalyticalUtil;
import com.weather.app.util.HttpCallBackListener;
import com.weather.app.util.HttpUtil;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout mWeatherLayout;
	private TextView mTxtCityName;
	// 发布时间
	private TextView mTxtPusTime;
	// 天气描述
	private TextView mTxtWeatherDesp;
	// 气温1、2
	private TextView mTxtTemp1, mTxtTemp2;
	// 显示当前日期
	private TextView mTxtCurDate;
	private String mStrCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		init();
		show();
	}

	private void init() {
		mWeatherLayout = (LinearLayout) findViewById(R.id.id_weather_info);
		mTxtCityName = (TextView) findViewById(R.id.id_city_name);
		mTxtPusTime = (TextView) findViewById(R.id.id_publish_txt);
		mTxtWeatherDesp = (TextView) findViewById(R.id.id_weather_desp);
		mTxtTemp1 = (TextView) findViewById(R.id.id_temp1);
		mTxtTemp2 = (TextView) findViewById(R.id.id_temp2);
		mTxtCurDate = (TextView) findViewById(R.id.id_current_data);
		findViewById(R.id.id_back).setOnClickListener(this);
		findViewById(R.id.id_refresh).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.id_back:
			Editor edit = PreferenceManager.getDefaultSharedPreferences(this)
					.edit();
			edit.putBoolean("city_selected", false);
			edit.commit();
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.id_refresh:
			if (mStrCode != null) {
				queryWeaterCode(mStrCode);
			} else {
				showWeather();
			}
			break;
		}
	}

	/**
	 * 根据传入的path路径 向服务器查询天气代号 或者天气信息
	 */
	private void queryFromServer(String path, final String type) {
		HttpUtil.sendHttpRequest(path, new HttpCallBackListener() {
			@Override
			public void onFinish(String response) {
				if ("county_code".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// 从服务器解析出天气代号
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							queryWeatherInfo(array[1]);
						}
					}
				} else if ("weather_code".equals(type)) {
					AnalyticalUtil.handleWeatherResponse(WeatherActivity.this,
							response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mTxtPusTime.setText("同步失败");
					}
				});
			}
		});
	}

	private void show() {
		mStrCode = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(mStrCode)) {
			mTxtPusTime.setText("同步中…");
			mWeatherLayout.setVisibility(View.INVISIBLE);
			mTxtCityName.setVisibility(View.INVISIBLE);
			queryWeaterCode(mStrCode);
		} else {
			showWeather();
		}
		// mBtnSwitch.setOnClickListener(this);
		// mBtnRefresh.setOnClickListener(this);
	}

	private void showWeather() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		mWeatherLayout.setVisibility(View.VISIBLE);
		mTxtCityName.setVisibility(View.VISIBLE);
		mTxtCityName.setText(sp.getString("city", ""));
		mTxtTemp1.setText(sp.getString("temp1", ""));
		mTxtTemp2.setText(sp.getString("temp2", ""));
		mTxtWeatherDesp.setText(sp.getString("weather", ""));
		mTxtPusTime.setText("今天" + sp.getString("ptime", "") + "发布");
		mTxtCurDate.setText(sp.getString("ctime", ""));
	}

	/**
	 * 查询天气代号对应的天气
	 */
	private void queryWeatherInfo(String code) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + code
				+ ".html";
		queryFromServer(address, "weather_code");
	}

	/**
	 * 查询县级代号所对应的天气
	 */
	private void queryWeaterCode(String code) {
		String address = "http://www.weather.com.cn/data/list3/city" + code
				+ ".xml";
		queryFromServer(address, "county_code");
	}
}