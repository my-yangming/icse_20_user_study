/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.library.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseViewBottomWindow;
import zuo.biao.library.manager.CityDB;
import zuo.biao.library.model.Entry;
import zuo.biao.library.model.GridPickerConfig;
import zuo.biao.library.ui.GridPickerView.OnTabClickListener;
import zuo.biao.library.util.PlaceUtil;
import zuo.biao.library.util.StringUtil;

/**地�?�选择弹窗
 * @author Lemon
 * @use
 * <br> toActivity或startActivityForResult (PlacePickerWindow.createIntent(...), requestCode);
 * <br> 然�?�在onActivityResult方法内
 * <br> data.getStringExtra(PlacePickerWindow.RESULT_PLACE); �?�得到地�?�
 */
public class PlacePickerWindow extends BaseViewBottomWindow<List<Entry<Integer, String>>, GridPickerView> {
	private static final String TAG = "PlacePickerWindow";

	//�?�动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_MIN_LEVEL = "INTENT_MIN_LEVEL";//最�?深度 �?/... - minLevel = 0; 市/... - minLevel=1;
	public static final String INTENT_MAX_LEVEL = "INTENT_MAX_LEVEL";//最大深度 ...市/ - maxLevel = 1;.../乡(街) - maxLevel=3;

	public static final String RESULT_PLACE_LIST = "RESULT_PLACE_LIST";

	/**�?�动这个Activity的Intent
	 * @param context
	 * @param packageName
	 * @param maxLevel
	 * @return
	 */
	public static Intent createIntent(Context context, String packageName, int maxLevel) {
		return createIntent(context, packageName, 0, maxLevel);
	}
	/**�?�动这个Activity的Intent
	 * @param context
	 * @param packageName
	 * @param minLevel
	 * @param maxLevel
	 * @return
	 */
	public static Intent createIntent(Context context, String packageName, int minLevel, int maxLevel) {
		return new Intent(context, PlacePickerWindow.class).
				putExtra(INTENT_PACKAGE_NAME, packageName).
				putExtra(INTENT_MIN_LEVEL, minLevel).
				putExtra(INTENT_MAX_LEVEL, maxLevel);
	}

	//�?�动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public Activity getActivity() {
		return this;
	}

	public static final String INTENT_PACKAGE_NAME = "INTENT_PACKAGE_NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(�?作UI，但�?存在数�?�获�?�或处�?�代�?，也�?存在事件监�?�代�?)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void initView() {//必须调用
		super.initView();

	}


	private List<Entry<Integer, String>> list;
	private void setPickerView(final int tabPosition, final int itemPositon) {
		runThread(TAG + "setPickerView", new Runnable() {
			@Override
			public void run() {

				list = getList(tabPosition, containerView.getSelectedItemList());
				runUiThread(new Runnable() {
					@Override
					public void run() {
						containerView.bindView(tabPosition, list, itemPositon);
					}
				});
			}
		});
	}


	//UI显示区(�?作UI，但�?存在数�?�获�?�或处�?�代�?，也�?存在事件监�?�代�?)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数�?�区(存在数�?�获�?�或处�?�代�?，但�?存在事件监�?�代�?)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private int minLevel;
	private int maxLevel;

	private CityDB cityDB;
	@Override
	public void initData() {//必须调用
		super.initData();

		minLevel = getIntent().getIntExtra(INTENT_MIN_LEVEL, 0);
		maxLevel = getIntent().getIntExtra(INTENT_MAX_LEVEL, 2);
		if (maxLevel < 0 || minLevel > maxLevel) {
			Log.e(TAG, "initData maxLevel < 0 || minLevel > maxLevel >> finish(); return; ");
			finish();
			return;
		}
		if (minLevel < 0) {
			minLevel = 0;
		}

		runThread(TAG + "initData", new Runnable() {

			@Override
			public void run() {
				if (cityDB == null) {
					cityDB = CityDB.getInstance(context, StringUtil.getTrimedString(
							getIntent().getStringExtra(INTENT_PACKAGE_NAME)));
				}

				final ArrayList<GridPickerConfig> configList = new ArrayList<GridPickerConfig>();
				configList.add(new GridPickerConfig("", "浙江", 10));
				configList.add(new GridPickerConfig("", "�?�州", 0));

				final ArrayList<String> selectedItemNameList = new ArrayList<String>();
				for (GridPickerConfig gpcb : configList) {
					selectedItemNameList.add(gpcb.getSelectedItemName());
				}

				list = getList(selectedItemNameList.size() - 1, selectedItemNameList);
				runUiThread(new Runnable() {

					@Override
					public void run() {
						containerView.init(configList, list);
					}
				});
			}
		});

	}


	private synchronized List<Entry<Integer, String>> getList(int tabPosition, ArrayList<String> selectedItemList) {
		int level = minLevel + tabPosition;
		if (selectedItemList == null || selectedItemList.size() <= 0 || PlaceUtil.isContainLevel(level) == false) {
			return null;
		}

		list = new ArrayList<Entry<Integer, String>>();
		List<String> nameList = null;
		switch (level) {
			case PlaceUtil.LEVEL_PROVINCE:
				nameList = cityDB.getAllProvince();
				break;
			case PlaceUtil.LEVEL_CITY:
				nameList = cityDB.getProvinceAllCity(StringUtil.getTrimedString(selectedItemList.get(0)));
				break;
			case PlaceUtil.LEVEL_DISTRICT:
				break;
			case PlaceUtil.LEVEL_TOWN:
				break;
			case PlaceUtil.LEVEL_ROAD:
				break;
			default:
				break;
		}

		if (nameList != null) {
			for (String name : nameList) {
				list.add(new Entry<Integer, String>(GridPickerAdapter.TYPE_CONTNET_ENABLE, name));
			}
		}
		return list;
	}



	@Override
	public String getTitleName() {
		return "选择地区";
	}
	@Override
	public String getReturnName() {
		return null;
	}
	@Override
	public String getForwardName() {
		return null;
	}

	@Override
	protected GridPickerView createView() {
		return new GridPickerView(context, getResources());
	}

	@Override
	protected void setResult() {
		setResult(RESULT_OK, new Intent().putStringArrayListExtra(
				RESULT_PLACE_LIST, containerView.getSelectedItemList()));
	}


	//Data数�?�区(存在数�?�获�?�或处�?�代�?，但�?存在事件监�?�代�?)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(�?��?存在事件监�?�代�?就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用
		super.initEvent();

		containerView.setOnTabClickListener(onTabClickListener);
		containerView.setOnItemSelectedListener(onItemSelectedListener);
	}


	private OnTabClickListener onTabClickListener = new OnTabClickListener() {

		@Override
		public void onTabClick(int tabPosition, TextView tvTab) {
			setPickerView(tabPosition, containerView.getSelectedItemPosition(tabPosition));
		}
	};

	private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
			containerView.doOnItemSelected(containerView.getCurrentTabPosition()
					, position, containerView.getCurrentSelectedItemName());
			setPickerView(containerView.getCurrentTabPosition() + 1, 0);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) { }
	};


	//系统自带监�?�方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//类相关监�?�<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cityDB = null;
	}


	//类相关监�?�>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监�?�方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(�?��?存在事件监�?�代�?就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽�?少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽�?少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
