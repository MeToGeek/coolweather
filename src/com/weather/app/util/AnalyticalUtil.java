package com.weather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.weather.app.db.DBCool;
import com.weather.app.model.City;
import com.weather.app.model.County;
import com.weather.app.model.Province;

public class AnalyticalUtil {
	/**
	 * @param db_cool
	 *            :���ݿ��������
	 * @param response
	 *            :ʡ-�ַ���
	 * @return
	 */
	public synchronized static boolean handleProvincesResponse(DBCool db_cool,
			String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvince = response.split(",");
			if (allProvince != null && allProvince.length > 0) {
				for (String p : allProvince) {
					if (p != null && p.contains("|")) {
						LogUtil.i("������ȷ��" + p);
						String[] oneProvince = p.split("\\|");
						Province province = new Province(oneProvince[0],
								oneProvince[1]);
						db_cool.saveProvince(province);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param db_cool
	 *            :���ݿ��������
	 * @param response
	 *            :��-�ַ���
	 * @param pid
	 *            :ʡ-id
	 * @return
	 */
	public static boolean handleCityResponse(DBCool db_cool, String response,
			int pid) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCity = response.split(",");
			if (allCity != null && allCity.length > 0) {
				for (String c : allCity) {
					if (c != null && c.contains("|")) {
						LogUtil.i("������ȷ��" + c);
						String[] oneCity = c.split("\\|");
						City city = new City(oneCity[0], oneCity[1], pid);
						db_cool.saveCity(city);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param db_cool
	 *            :���ݿ��������
	 * @param response
	 *            :��-�ַ���
	 * @param cid
	 *            :��-id
	 * @return
	 */
	public static boolean handleCountyResponse(DBCool db_cool, String response,
			int cid) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounty = response.split(",");
			if (allCounty != null && allCounty.length > 0) {
				for (String c : allCounty) {
					if (c != null && c.contains("|")) {
						LogUtil.i("������ȷ��" + c);
						String[] oneCounty = c.split("\\|");
						County County = new County(oneCounty[0], oneCounty[1],
								cid);
						db_cool.saveCounty(County);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param context
	 * @param response
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String city = weatherInfo.getString("city");
			String cityid = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weather = weatherInfo.getString("weather");
			String ptime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, city, cityid, temp1, temp2, weather, ptime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����в��������浽sp�ļ���
	 */
	private static void saveWeatherInfo(Context context, String city,
			String cityid, String temp1, String temp2, String weather,
			String ptime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d", Locale.CHINA);
		SharedPreferences.Editor edit = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		edit.putBoolean("city_selected", true);
		edit.putString("city", city);
		edit.putString("cityid", cityid);
		edit.putString("temp1", temp1);
		edit.putString("temp2", temp2);
		edit.putString("weather", weather);
		edit.putString("ptime", ptime);
		edit.putString("ctime", format.format(new Date()));
		edit.commit();
	}
}
