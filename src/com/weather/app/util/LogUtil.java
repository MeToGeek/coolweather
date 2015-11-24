package com.weather.app.util;

import android.util.Log;

public class LogUtil {
	private static final int v = 1;
	private static final int i = 2;
	private static final int d = 3;
	private static final int w = 4;
	private static final int e = 5;
	private static final int l = v;
	private static final String tag = "main";

	public static void v(String msg) {
		if (l <= v) {
			Log.v(tag, msg);
		}
	}

	public static void i(String msg) {
		if (l <= i) {
			Log.i(tag, msg);
		}
	}

	public static void d(String msg) {
		if (l <= d) {
			Log.d(tag, msg);
		}
	}

	public static void w(String msg) {
		if (l <= w) {
			Log.w(tag, msg);
		}
	}

	public static void e(String msg) {
		if (l <= e) {
			Log.e(tag, msg);
		}
	}
}
