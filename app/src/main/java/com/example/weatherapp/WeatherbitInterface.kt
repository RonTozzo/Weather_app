package com.example.weatherapp

import WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherBitService {
    @GET("current")
    fun getWeather(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("key") key: String,
        @Query("lang") lang: String,
    ): Call<WeatherResponse>
}