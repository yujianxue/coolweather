package com.coolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import java.io.BufferedReader;

/**
 * Created by Administrator on 2016/5/13.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
	private LinearLayout weatherInfoLayout;
	// 用于显示城市名
	private TextView cityNameText;
	// 用于显示发布时间
	private TextView publishText;
	// 用于显示天气描述信息
	private TextView weatherDespText;
	// 用于显示当前温度
	private TextView tempText;
	// 用于显示当前日期
	private TextView currentDateText;
	// 切换城市按钮
	private Button switchCity;
	// 更新天气按钮
	private Button refreshWeather;

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
		tempText = (TextView) findViewById(R.id.temp);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyName = getIntent().getStringExtra("county_name");
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);

		if (!TextUtils.isEmpty(countyName)) {
			// 直接从上一级跳转过来的才会有传递的countyName
			// 有县级名称就去查询天气
			publishText.setText("同步中…");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeather(countyName);

		} else {
			// 当关掉进程，由于定时更新服务的开启，点击软件按钮，进入直接从上次服务保存的数据中,从而showWeather
			// 此时县级名称即为null
			// 没有县级名称就直接显示本地天气
			showWeather();
		}

	}

	// 查询县级名称所对应的天气
	private void queryWeather(String countyName) {
		Log.d("tag", "queryWeather");
		String address = "http://op.juhe.cn/onebox/weather/query?cityname=" + countyName + "&key=ee9ab03b377c948ccf7c5fd3cdc9f88e";
		queryWeatherFromServer(address);
	}

	// 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	private void queryWeatherFromServer(final String address) {
		Log.d("tag", "queryWeatherFromServer");
		// String response = "
		// {"reason":"successed!","result":{"data":{"realtime":{"wind":{"windspeed":"8.0","direct":"东南风","power":"1级","offset":null},
		// "time":"14:00:00","weather":{"humidity":"74","img":"2","info":"阴","temperature":"19"},
		// "dataUptime":1463554270,"date":"2016-05-18","city_code":"101210701","city_name":"温州","week":3,"moon":"四月十二"},
		// "life":{"date":"2016-5-18","info":{"kongtiao":["较少开启","您将感到很舒适，一般不需要开启空调。"],
		// "yundong":["较适宜","天气较好，户外运动请注意防晒。推荐您进行室内运动。"],
		// "ziwaixian":["弱","紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"],
		// "ganmao":["少发","各项气象条件适宜，无明显降温过程，发生感冒机率较低。"],
		// "xiche":["较适宜","较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"],
		// "wuran":["中","气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"],
		// "chuanyi":["舒适","建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"]}},
		// "weather":[{"date":"2016-05-18","info":{"night":["1","多云","17","东北风","微风","18:41"],
		// "day":["1","多云","25","东北风","微风","05:05"]},"week":"三","nongli":"四月十二"},
		// {"date":"2016-05-19","info":{"dawn":["1","多云","17","东北风","微风","18:41"],
		// "night":["2","阴","19","东北风","微风","18:42"],"day":["1","多云","27","东北风","微风","05:05"]},
		// "week":"四","nongli":"四月十三"},{"date":"2016-05-20","info":{"dawn":["2","阴","19","东北风","微风","18:42"],
		// "night":["3","阵雨","20","东北风","微风","18:43"],"day":["3","阵雨","24","东北风","微风","05:04"]},"week":"五","nongli":"四月十四"},
		// {"date":"2016-05-21","info":{"dawn":["3","阵雨","20","东北风","微风","18:43"],"night":["8","中雨","20","东北风","微风","18:43"],
		// "day":["3","阵雨","25","东北风","微风","05:04"]},"week":"六","nongli":"四月十五"},{"date":"2016-05-22",
		// "info":{"dawn":["8","中雨","20","东北风","微风","18:43"],"night":["3","阵雨","21","东北风","微风","18:44"],
		// "day":["3","阵雨","27","东北风","微风","05:04"]},"week":"日","nongli":"四月十六"},
		// {"date":"2016-05-23","info":{"night":["2","阴","19","东南风","微风","19:30"],
		// "day":["1","多云","27","东南风","微风","07:30"]},"week":"一","nongli":"四月十七"},
		// {"date":"2016-05-24","info":{"night":["3","阵雨","19","东北风","微风","19:30"],
		// "day":["3","阵雨","27","东北风","微风","07:30"]},"week":"二","nongli":"四月十八"}],
		// "pm25":{"key":"","show_desc":0,"pm25":{"curPm":"50","pm25":"27","pm10":"50","level":1,
		// "quality":"优","des":"今天的空气质量令人满意，各类人群可正常活动。"},
		// "dateTime":"2016年05月18日14时","cityName":"温州"},"date":null,"isForeign":0}},"error_code":0}

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				if (!TextUtils.isEmpty(response)) {
					// 处理服务器返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					// runOnUiThread更新主线程
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
		Log.d("tag", "showWeather");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		tempText.setText(prefs.getString("temp", "") + "°C");
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		// 开启定时服务
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中…");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String countyName = prefs.getString("city_name", "");// 注意这里键是city_name,这里pres之前保存的是界面显示各种信息
			if (!TextUtils.isEmpty(countyName)) {
				queryWeather(countyName);
				break;
			}
		default:
			break;

		}
	}
}
