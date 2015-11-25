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
	// �б���
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog mDialog;
	private TextView mTxtTitleView;
	private ListView mListShowView;
	private ArrayAdapter<String> mShowAdapter;
	private DBCool mDbCool;
	// ����������Դ
	private List<String> mShowData = new ArrayList<String>();
	// ʡ�����б�
	private List<Province> mProvinceList;
	private List<City> mCityList;
	private List<County> mCountyList;

	// ѡ�е�ʡ��
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
	 * ����ʡ���б� ���ȴӱ��ز��� ��δ��������
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
			mTxtTitleView.setText("�й�");
			mCurrentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * �����м��б� ���ȴӱ��ز��� ��δ�������� ���ҵ�ǰ���ǵ����ʡ
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
	 * �����ؼ��б� ���ȴӱ��ز��� ��δ�������� ���ҵ�ǰ���ǵ������
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
	 *            :Ҫ��ѯ��ʡ���صı��
	 * @param levelName
	 *            :ȷ��Ҫ��ѯʡ�����е��Ǹ�����
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
				// ����ɹ��˾ͻص����߳�UI��ȥ
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
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void showProgressDialog() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(this);
			mDialog.setMessage("���ڼ��ء���");
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
	 * ����back���� ���ݵ�ǰ����ȥ�ж� ���Ƴ����ǻص�ʡ���ؽ���
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