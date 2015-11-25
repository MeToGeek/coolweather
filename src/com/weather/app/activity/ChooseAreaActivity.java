package com.weather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.app.R;
import com.weather.app.db.DBCool;
import com.weather.app.model.City;
import com.weather.app.model.County;
import com.weather.app.model.Province;
import com.weather.app.util.AnalyticalUtil;
import com.weather.app.util.HttpCallBackListener;
import com.weather.app.util.HttpUtil;

public class ChooseAreaActivity extends Activity {
	// 列表级别
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog mDialog;
	private TextView mTxtTitleView;
	private ListView mListShowView;
	private ArrayAdapter<String> mShowAdapter;
	private DBCool mDbCool;
	// 适配器数据源
	private List<String> mShowData = new ArrayList<String>();
	// 省市县列表
	private List<Province> mProvinceList;
	private List<City> mCityList;
	private List<County> mCountyList;

	// 选中的省市
	private Province mChooseProvince;
	private City mChooseCity;
	private int mCurrentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		init();
	}

	private void init() {
		mTxtTitleView = (TextView) findViewById(R.id.id_top_title);
		mListShowView = (ListView) findViewById(R.id.id_list_show);
		mShowAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mShowData);
		mListShowView.setAdapter(mShowAdapter);
		mDbCool = DBCool.getInstance(this);
		mListShowView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int index, long arg3) {
				if (mCurrentLevel == LEVEL_PROVINCE) {
					mChooseProvince = mProvinceList.get(index);
					queryCity();
				} else if (mCurrentLevel == LEVEL_CITY) {
					mChooseCity = mCityList.get(index);
					queryCounty();
				}
			}
		});
		queryProvice();
	}

	/**
	 * 加载省级列表 首先从本地查找 其次从网络查找
	 */
	private void queryProvice() {
		mProvinceList = mDbCool.loadProvince();
		if (mProvinceList.size() > 0) {
			mShowData.clear();
			for (Province p : mProvinceList) {
				mShowData.add(p.getP_name());
			}
			mShowAdapter.notifyDataSetChanged();
			mListShowView.setSelection(0);
			mTxtTitleView.setText("中国");
			mCurrentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * 加载市级列表 首先从本地查找 其次从网络查找 查找的前提是点击了省
	 */
	protected void queryCity() {
		mCityList = mDbCool.loadCity(mChooseProvince.getId());
		if (mCityList.size() > 0) {
			mShowData.clear();
			for (City city : mCityList) {
				mShowData.add(city.getC_name());
			}
			mShowAdapter.notifyDataSetChanged();
			mListShowView.setSelection(0);
			mTxtTitleView.setText(mChooseProvince.getP_name());
			mCurrentLevel = LEVEL_CITY;
		} else {
			queryFromServer(mChooseProvince.getP_code(), "city");
		}
	}

	/**
	 * 加载县级列表 首先从本地查找 其次从网络查找 查找的前提是点击了市
	 */
	protected void queryCounty() {
		mCountyList = mDbCool.loadCounty(mChooseCity.getId());
		if (mCountyList.size() > 0) {
			mShowData.clear();
			for (County c : mCountyList) {
				mShowData.add(c.getCo_name());
			}
			mShowAdapter.notifyDataSetChanged();
			mListShowView.setSelection(0);
			mTxtTitleView.setText(mChooseCity.getC_name());
			mCurrentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(mChooseCity.getC_code(), "county");
		}
	}

	/**
	 * @param code
	 *            :要查询的省市县的编号
	 * @param levelName
	 *            :确定要查询省市县中的那个级别
	 */
	private void queryFromServer(String code, final String levelName) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if (DBCool.TB_PROVINCE_NAME.equals(levelName)) {
					result = AnalyticalUtil.handleProvincesResponse(mDbCool,
							response);
				} else if (DBCool.TB_CITY_NAME.equals(levelName)) {
					result = AnalyticalUtil.handleCityResponse(mDbCool,
							response, mChooseProvince.getId());
				} else if (DBCool.TB_COUNTY_NAME.equals(levelName)) {
					result = AnalyticalUtil.handleCountyResponse(mDbCool,
							response, mChooseCity.getId());
				}
				// 如果成功了就回到主线程UI中去
				if (result) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if (DBCool.TB_PROVINCE_NAME.equals(levelName)) {
								queryProvice();
							} else if (DBCool.TB_CITY_NAME.equals(levelName)) {
								queryCity();
							} else if (DBCool.TB_COUNTY_NAME.equals(levelName)) {
								queryCounty();
							}
						}

					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void showProgressDialog() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(this);
			mDialog.setMessage("正在加载……");
			mDialog.setCanceledOnTouchOutside(false);
		}
		mDialog.show();
	}

	private void closeProgressDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	/**
	 * 捕获back按键 根据当前级别去判断 是推出还是回到省市县界面
	 */
	@Override
	public void onBackPressed() {
		if (mCurrentLevel == LEVEL_COUNTY) {
			queryCity();
		} else if (mCurrentLevel == LEVEL_CITY) {
			queryProvice();
		} else {
			finish();
		}
	}
}