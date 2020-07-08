package com.mao.service.his

/**
 * 天气数据返回体
 */
data class WeatherResult(var status: Int, var desc: String?, var data: WeatherData)

/**
 * 天气数据
 */
data class WeatherData(var yesterday: YesterdayWeather, var city: String, var aqi: String?, var forecast: List<ForecastWeather>, var ganmao: String, var wendu: String)

/**
 * 昨日天气数据
 */
data class YesterdayWeather(var date: String, var high: String, var fx: String, var low: String, var fl: String, var type: String)

/**
 * 未来天气数据
 */
data class ForecastWeather(var date: String, var high: String, var fengli: String, var low: String, var fengxiang: String, var type: String)