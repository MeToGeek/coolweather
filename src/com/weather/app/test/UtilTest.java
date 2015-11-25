package com.weather.app.test;

import android.test.AndroidTestCase;

import com.weather.app.util.HttpUtil;

public class UtilTest extends AndroidTestCase {
	public void onHttpUtilTest() {
		HttpUtil.sendHttpRequest(
				"http://www.weather.com.cn/data/list3/city.xml", null);
	}

	public void onAnalyticalUtilTest() {
	}
}