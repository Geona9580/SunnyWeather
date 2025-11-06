package com.example.sunnyweather.login.network

import retrofit2.http.GET
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.login.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}