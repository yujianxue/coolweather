package com.coolweather.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import java.util.Date;

/**
 * Created by Administrator on 2016/5/13.
 */
public class WeatherActivity extends Activity {
	private LinearLayout weatherInfoLayout;
	// 用于显示城市名
	private TextView cityNameText;
	// 用于显示发布时间
	private TextView publishText;
	// 用于显示天气描述信息
	private TextView weatherDespText;
	// 用于显示当前温度
	private TextView temp1Text;
	// 用于显示温度范围
	private TextView temp2Text;
	// 用于显示当前日期
	private TextView currentDateText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		// 初始化各控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyName = getIntent().getStringExtra("county_name");

		if (!TextUtils.isEmpty(countyName)) {
			// 有县级名称就去查询天气
			publishText.setText("同步中…");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeather(countyName);

		} else {
			// 没有县级代号是就直接显示本地天气
			showWeather();
		}

	}

	// 查询县级代号所对应的天气代号
	private void queryWeather(String countyName) {
		Log.d("tag", "queryWeather");
		String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + countyName + "&key=d4ffbe5903dd7f4ff8bc8dcbf25e73e9";
		queryWeatherFromServer(address);
	}

	// 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	private void queryWeatherFromServer(final String address) {
        Log.d("tag", "queryWeatherFromServer");
		//String response = " {\"resultcode\":\"200\",\"reason\":\"successed!\",\"result\":{\"sk\":{\"temp\":\"27\",\"wind_direction\":\"西风\",\"wind_strength\":\"2级\",\"humidity\":\"24%\",\"time\":\"11:18\"},\"today\":{\"temperature\":\"18℃~30℃\",\"weather\":\"晴\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"00\"},\"wind\":\"西南风3-4 级\",\"week\":\"星期一\",\"city\":\"天津\",\"date_y\":\"2016年05月16日\",\"dressing_index\":\"热\",\"dressing_advice\":\"天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。\",\"uv_index\":\"很强\",\"comfort_index\":\"\",\"wash_index\":\"较适宜\",\"travel_index\":\"较适宜\",\"exercise_index\":\"较适宜\",\"drying_index\":\"\"},\"future\":[{\"temperature\":\"18℃~30℃\",\"weather\":\"晴\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"00\"},\"wind\":\"西南风3-4 级\",\"week\":\"星期一\",\"date\":\"20160516\"},{\"temperature\":\"19℃~32℃\",\"weather\":\"晴转多云\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"01\"},\"wind\":\"西南风3-4 级\",\"week\":\"星期二\",\"date\":\"20160517\"},{\"temperature\":\"18℃~29℃\",\"weather\":\"阴\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"02\"},\"wind\":\"南风3-4 级\",\"week\":\"星期三\",\"date\":\"20160518\"},{\"temperature\":\"18℃~28℃\",\"weather\":\"多云\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"01\"},\"wind\":\"东南风微风\",\"week\":\"星期四\",\"date\":\"20160519\"},{\"temperature\":\"18℃~28℃\",\"weather\":\"阴\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"02\"},\"wind\":\"东南风微风\",\"week\":\"星期五\",\"date\":\"20160520\"},{\"temperature\":\"19℃~32℃\",\"weather\":\"晴转多云\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"01\"},\"wind\":\"西南风3-4 级\",\"week\":\"星期六\",\"date\":\"20160521\"},{\"temperature\":\"18℃~29℃\",\"weather\":\"阴\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"02\"},\"wind\":\"南风3-4 级\",\"week\":\"星期日\",\"date\":\"20160522\"}]},\"error_code\":0}";
		// runOnUiThread更新主线程
		  HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {


              @Override
              public void onFinish(String response) {
                  if (!TextUtils.isEmpty(response)) {
                      //处理服务器返回的天气信息
                      Utility.handleWeatherResponse(WeatherActivity.this, response);
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Log.d("tag", "showWeather");
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
                              publishText.setText("同步失败");
                          }
                      });
                  
              }
          });
    }



	// 从SharePreferences文件中读取存储的天气信息，并显示到界面上
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", "") + "°C");
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
}
