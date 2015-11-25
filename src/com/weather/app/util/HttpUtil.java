package com.weather.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static void sendHttpRequest(final String path,
			final HttpCallBackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection conn = null;
				URL url = null;
				try {
					url = new URL(path);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(8 * 1000);
					conn.setConnectTimeout(8 * 1000);
					if (conn.getResponseCode() == 200) {
						InputStream itsm = conn.getInputStream();
						BufferedReader bdrr = new BufferedReader(
								new InputStreamReader(itsm));
						StringBuilder sgbr = new StringBuilder();
						String line;
						while ((line = bdrr.readLine()) != null) {
							sgbr.append(line);
						}
						LogUtil.i(sgbr.toString());
						if (listener != null) {
							listener.onFinish(sgbr.toString());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}).start();
	}
}