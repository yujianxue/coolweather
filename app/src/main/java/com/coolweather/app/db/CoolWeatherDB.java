package com.coolweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
		boolean flag = false;
		Log.d("tag", "saveProvince");
		if (province != null) {
			Cursor cursor = db.query("Province", null, null, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					flag = false;
					if (!(province.getProvinceName().equals(cursor.getString(cursor.getColumnIndex("province_name"))))) {
						flag = true;
					}
				} while (cursor.moveToNext());
			} else {
				flag = true;
			}
			// values.put("province_code",
			// province.getProvinceCode());
			if (cursor != null) {
				cursor.close();
			}

		}
		if (flag) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			db.insert("Province", null, values);
			Log.d("tag", province.getProvinceName());
		}
	}

	/* 从数据库读取全国所有的省份信息 */
	public List<Province> loadProvince() {
		List<Province> list = new ArrayList<Province>();
		//-----------------写死，避免使用付费api------------------
		//boolean flagP=false;//在线查询
		boolean flagP=true;//写死查询
		if (flagP)
		{
			Province province1=new Province();
			province1.setId(1);
			province1.setProvinceName("北京");
			list.add(province1);
			return list;
		}
		//-----------------写死，避免使用付费api------------------

		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			} while (cursor.moveToNext());
		}

		if (cursor != null) {
			cursor.close();
		}
		return list;

	}

	/* 将City实例存储到数据库 */
	public void saveCity(City city) {
		boolean flag = false;
		Log.d("tag", "saveCity");
		if (city != null) {
			Cursor cursor = db.query("City", null, null, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					flag = false;
					if (!(city.getCityName().equals(cursor.getString(cursor.getColumnIndex("city_name"))))) {
						flag = true;
					}
				} while (cursor.moveToNext());
			} else {
				flag = true;
			}

			if (cursor != null) {
				cursor.close();
			}

		}
		if (flag) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("province_name", city.getProvinceName());
			db.insert("City", null, values);
			Log.d("tag", city.getCityName());
		}
	}

	/* 从数据库读取全国所有的城市信息 */
	public List<City> loadCities(String provinceName) {
		Log.d("tag", "loadCities");
		List<City> list = new ArrayList<City>();
		//-----------------写死，避免使用付费api------------------
		//boolean flagP=false;//在线查询
		boolean flagP=true;//写死查询
		if (flagP)
		{
			City city1 = new City();
			city1.setId(1);
			city1.setCityName("北京");
			city1.setProvinceName("北京");
			list.add(city1);
			return list;
		}
		//-----------------写死，避免使用付费api------------------

		/*
		 * public Cursor query （boolean distinct， String table， String[]
		 * columns， String selection， String[] selectionArgs， String groupBy，
		 * String having， String orderBy， String limit）
		 * 其中各种参数意思如下（如果其中某个参数不设置，可以指定为null）：
		 * table：表名。相当于select语句from关键字后面的部分。如果是多表联合查询，可以用逗号将两个表名分开。
		 * columns：要查询出来的列名。相当于select语句select关键字后面的部分。
		 * selection：查询条件子句，相当于select语句where关键字后面的部分，在条件子句允许使用占位符“？”
		 * selectionArgs：对应于selection语句中占位符的值，值在数组中的位置与占位符在语句中的位置必须一致，否则就会有异常。
		 * groupBy：相当于select语句groupby关键字后面的部分 having：相当于select语句having关键字后面的部分
		 * orderBy：相当于select语句orderby关键字后面的部分
		 * limit：指定偏移量和获取的记录数，相当于select语句limit关键字后面的部分
		 */
		// province_name=? 为创建表中的列名
		Cursor cursor = db.query("City", null, "province_name=?", new String[] { provinceName }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceName(provinceName);
				list.add(city);
			} while (cursor.moveToNext());
		}

		if (cursor != null) {
			cursor.close();
		}
		return list;

	}

	/* 将County实例存储到数据库 */
	public void saveCounty(County county) {
		boolean flag = false;
		Log.d("tag", "saveCounty");
		if (county != null) {
			Cursor cursor = db.query("County", null, null, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					flag = false;
					if (!(county.getCountyName().equals(cursor.getString(cursor.getColumnIndex("county_name"))))) {
						flag = true;
					}
				} while (cursor.moveToNext());
			} else {
				flag = true;
			}

			if (cursor != null) {
				cursor.close();
			}

		}
		if (flag) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("city_name", county.getCityName());
			db.insert("County", null, values);
			Log.d("tag", county.getCountyName());
		}

	}

	/* 从数据库读取全国所有的县信息 */
	public List<County> loadCounties(String cityName) {
		Log.d("tag", "loadCounties");
		List<County> list = new ArrayList<County>();
		//-----------------写死，避免使用付费api------------------
		//boolean flagP=false;//在线查询
		boolean flagP=true;//写死查询
		if (flagP)
		{
			County county1 = new County();
			county1.setId(1);
			county1.setCountyName("北京");
			county1.setCityName("北京");
			list.add(county1);
			County county2 = new County();
			county2.setId(2);
			county2.setCountyName("海淀");
			county2.setCityName("北京");
			list.add(county2);
			County county3 = new County();
			county3.setId(3);
			county3.setCountyName("朝阳");
			county3.setCityName("北京");
			list.add(county3);
			return list;
		}
		//-----------------写死，避免使用付费api------------------
		Cursor cursor = db.query("County", null, "city_name=?", new String[] { cityName }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCityName(cityName);
				list.add(county);
			} while (cursor.moveToNext());
		}

		if (cursor != null) {
			cursor.close();
		}
		return list;

	}

}
