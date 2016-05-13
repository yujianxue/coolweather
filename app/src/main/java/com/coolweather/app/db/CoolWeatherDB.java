package com.coolweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class CoolWeatherDB {
	/* 数据库名 */
	public static final String DB_NAME = "cool_weather";
	/* 数据库版本 */
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	/* 将构造方法私有化 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/* 获取CoolWeatherDB的实例 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/* 将Province实例存储到数据库 */

	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("province", null, values);
		}

	}

	/* 从数据库读取全国所有的省份信息 */
	public List<Province> loadProvince() {
		List<Province> list = new ArrayList<Province>();
		/*Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}*/
		//测试用
		Province province1 = new Province();
		province1.setId(0);
		province1.setProvinceName("北京");
		province1.setProvinceCode("01");
		list.add(province1);
		Province province2 = new Province();
		province2.setId(1);
		province2.setProvinceName("上海");
		province2.setProvinceCode("02");
		list.add(province2);
		Province province3 = new Province();
		province3.setId(2);
		province3.setProvinceName("天津");
		province3.setProvinceCode("03");
		list.add(province3);



		/*if (cursor != null) {
			cursor.close();
		}*/
		return list;

	}

	/* 将City实例存储到数据库 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("city", null, values);
		}

	}

	/* 从数据库读取全国所有的城市信息 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		/*Cursor cursor = db.query("City", null, "provinceId=?", new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}*/

		//测试用
		City city1 = new City();
		city1.setId(0);
		city1.setCityName("北京");
		city1.setCityCode("0101");
		city1.setProvinceId(provinceId);
		list.add(city1);



		/*if (cursor != null) {
			cursor.close();
		}*/
		return list;

	}

	/* 将County实例存储到数据库 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("County_name", county.getCountyName());
			values.put("County_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}

	}

	/* 从数据库读取全国所有的县信息 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		/*Cursor cursor = db.query("County", null, "cityId=?", new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}*/

		//测试用
		County county = new County();
		county.setId(0);
		county.setCountyName("北京");
		county.setCountyCode("010101");
		county.setCityId(cityId);
		list.add(county);
		County county2 = new County();
		county2.setId(1);
		county2.setCountyName("海淀");
		county2.setCountyCode("010102");
		county2.setCityId(cityId);
		list.add(county2);


		/*if (cursor != null) {
			cursor.close();
		}*/
		return list;

	}

}
