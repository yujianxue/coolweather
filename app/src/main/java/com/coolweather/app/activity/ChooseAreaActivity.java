package com.coolweather.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<String>();
    /* 省列表 */
    private List<Province> provinceList;
    /* 市列表 */
    private List<City> cityList;
    /* 县列表 */
    private List<County> countyList;
    /* 选中的省份 */
    private Province selectedProvince;
    /* 选中城市 */
    private City selectedCity;
    /* 当前选中的级别 */
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();

                }
            }
        });
        queryProvinces();//加载省级数据
    }

    /*查询全国所有的省，优先从数据库查询，如果没有查询到再到服务器上查询*/
    private void queryProvinces() {
        provinceList = coolWeatherDB.loadProvince();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());

            }
            //notifyDataSetChanged()可以在修改适配器绑定的数组后，不用重新刷新Activity，从而动态刷新ListView
            adapter.notifyDataSetChanged();
            //setSelection()，传入一个index整型数值，就可以让ListView定位到指定Item的位置。
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }

    /*查询选中省内所有的市，优先从数据库查询，如果没有查询到再到服务器上查询*/
    private void queryCities() {
        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            //notifyDataSetChanged()可以在修改适配器绑定的数组后，不用重新刷新Activity，从而动态刷新ListView
            adapter.notifyDataSetChanged();
            //setSelection()，传入一个index整型数值，就可以让ListView定位到指定Item的位置。
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }


    /*查询选中省内所有的县，优先从数据库查询，如果没有查询到再到服务器上查询*/
    private void queryCounties() {
        countyList = coolWeatherDB.loadCounties(selectedCity.getId());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            //notifyDataSetChanged()可以在修改适配器绑定的数组后，不用重新刷新Activity，从而动态刷新ListView
            adapter.notifyDataSetChanged();
            //setSelection()，传入一个index整型数值，就可以让ListView定位到指定Item的位置。
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectedCity.getCityCode(), "city");
        }
    }

    //根据传入的代号和类型从服务器上查询省市县数据
    private void queryFromServer(final String code, final String type) {

    }

    /*显示进度对话框*/
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /*关闭进度对话框*/
    private void closeProgressDialog() {
        if (progressDialog == null) {
            progressDialog.dismiss();
        }
    }

    /*捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出*/
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            finish();
        }
    }
}
