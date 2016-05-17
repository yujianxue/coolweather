package com.coolweather.app.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/12.
 */
public class Utility {
	/* 解析和处理服务器返回的省级数据 */
	// split() 方法用于把一个字符串分割成字符串数组
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {

		if (!TextUtils.isEmpty(response)) {
			Log.d("tag", "handleProvincesResponse");
			try {
				JSONObject jsonObject = new JSONObject(response);
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				for (int i = 0; i < jsonArray.length(); i++) {
					Province province = new Province();
					province.setProvinceName(jsonArray.getJSONObject(i).getString("province"));
					// 将解析出来的数据存储到Province表
					coolWeatherDB.saveProvince(province);
					Log.d("tag", String.valueOf(i));
				}

				return true;

			} catch (JSONException e) {
				e.printStackTrace();
			}

			// String[] allProvinces = result.split(",");

			/*
			 * //String[] allProvinces = result.split(","); Log.d("tagpro",
			 * allProvinces[0]); if (allProvinces != null && allProvinces.length
			 * > 0) { for (String p : allProvinces) { String[] array =
			 * p.split("\\|"); Province province = new Province();
			 * province.setProvinceCode(array[0]);
			 * province.setProvinceName(array[1]); // 将解析出来的数据存储到Province表
			 * coolWeatherDB.saveProvince(province); } return true; }
			 */
		}
		return false;

	}

	/* 解析和处理服务器返回的市级数据 */
	public static boolean handleCitesResponse(CoolWeatherDB coolWeatherDB, String response, String provinceName) {
		if (!TextUtils.isEmpty(response)) {
			Log.d("tag", "handleCitesResponse");
			try {
				JSONObject jsonObject = new JSONObject(response);
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				for (int i = 0; i < jsonArray.length(); i++) {
					City city = new City();
					//比较2个字符串用    A.equals(B)
					if (jsonArray.getJSONObject(i).getString("province").equals(provinceName)) {
						city.setCityName(jsonArray.getJSONObject(i).getString("city"));
						city.setProvinceName(provinceName);
						// 将解析出来的数据存储到City表
						coolWeatherDB.saveCity(city);
					}

				}

				return true;

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		return false;

	}

	/* 解析和处理服务器返回的县级数据 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, String cityName) {
		if (!TextUtils.isEmpty(response)) {
			Log.d("tag", "handleCountiesResponse");
			try {
				JSONObject jsonObject = new JSONObject(response);
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				for (int i = 0; i < jsonArray.length(); i++) {
					County county = new County();
					//比较2个字符串用    A.equals(B)
					if (jsonArray.getJSONObject(i).getString("city").equals(cityName)) {
						county.setCountyName(jsonArray.getJSONObject(i).getString("district"));
						county.setCityName(cityName);
						// 将解析出来的数据存储到County表
						coolWeatherDB.saveCounty(county);
					}

				}

				return true;

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	// JSON格式数据为：
	// response
	/*
	 * {"weatherinfo":
	 * {“city”:"北京","cityid":"101010100","temp1":"21°C","temp2":"9°C","weather":
	 * "多云转小雨"，"ptime":"11:00"} }
	 */
	// String response="
	// {\"resultcode\":\"200\",\"reason\":\"successed!\",\"result\":{\"sk\":{\"temp\":\"27\",\"wind_direction\":\"西风\",\"wind_strength\":\"2级\",\"humidity\":\"24%\",\"time\":\"11:18\"},\"today\":{\"temperature\":\"18℃~30℃\",\"weather\":\"晴\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"00\"},\"wind\":\"西南风3-4
	// 级\",\"week\":\"星期一\",\"city\":\"天津\",\"date_y\":\"2016年05月16日\",\"dressing_index\":\"热\",\"dressing_advice\":\"天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。\",\"uv_index\":\"很强\",\"comfort_index\":\"\",\"wash_index\":\"较适宜\",\"travel_index\":\"较适宜\",\"exercise_index\":\"较适宜\",\"drying_index\":\"\"},\"future\":[{\"temperature\":\"18℃~30℃\",\"weather\":\"晴\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"00\"},\"wind\":\"西南风3-4
	// 级\",\"week\":\"星期一\",\"date\":\"20160516\"},{\"temperature\":\"19℃~32℃\",\"weather\":\"晴转多云\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"01\"},\"wind\":\"西南风3-4
	// 级\",\"week\":\"星期二\",\"date\":\"20160517\"},{\"temperature\":\"18℃~29℃\",\"weather\":\"阴\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"02\"},\"wind\":\"南风3-4
	// 级\",\"week\":\"星期三\",\"date\":\"20160518\"},{\"temperature\":\"18℃~28℃\",\"weather\":\"多云\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"01\"},\"wind\":\"东南风微风\",\"week\":\"星期四\",\"date\":\"20160519\"},{\"temperature\":\"18℃~28℃\",\"weather\":\"阴\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"02\"},\"wind\":\"东南风微风\",\"week\":\"星期五\",\"date\":\"20160520\"},{\"temperature\":\"19℃~32℃\",\"weather\":\"晴转多云\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"01\"},\"wind\":\"西南风3-4
	// 级\",\"week\":\"星期六\",\"date\":\"20160521\"},{\"temperature\":\"18℃~29℃\",\"weather\":\"阴\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"02\"},\"wind\":\"南风3-4
	// 级\",\"week\":\"星期日\",\"date\":\"20160522\"}]},\"error_code\":0}";

	/* 解析服务器返回的JSON数据，并将解析出的数据存储到本地 */
	public static void handleWeatherResponse(Context context, String response) {
		Log.d("tag", "handleWeatherResponse");
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject result = jsonObject.getJSONObject("result");
			JSONObject data = result.getJSONObject("data");
			JSONObject realtime = data.getJSONObject("realtime");
			JSONObject weather = realtime.getJSONObject("weather");

			String cityName = realtime.getString("city_name");
			String temp = weather.getString("temperature");
			String weatherDesp = weather.getString("info");
			String publishTime = realtime.getString("time");
			saveWeatherInfo(context, cityName, temp, weatherDesp, publishTime);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/* 将服务器返回的所有天气信息存储到SharedPreferences文件中 */
	// SharedPreferences是Android平台上一个轻量级的存储类，用来保存应用的一些常用配置
	private static void saveWeatherInfo(Context context, String cityName,  String temp, String weatherDesp,
			String publishTime) {
		Log.d("tag", "saveWeatherInfo");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("temp", temp);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();

	}

}
