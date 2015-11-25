package com.weather.app.util;

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
}
