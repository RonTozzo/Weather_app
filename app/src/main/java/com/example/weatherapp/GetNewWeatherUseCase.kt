package com.example.weatherapp

import WeatherData
import WeatherResponse
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface WeatherDataCallback {
    fun onSuccess(weatherData: WeatherData)
    fun onFailure(throwable: Throwable)
}

class GetNewWeatherUseCase {
    fun getNewWeather(city: String, callback: WeatherDataCallback) {
        val weatherService = createWeatherService()

        weatherService.getWeather(city, "BR", "3c9db5d9b5f84e14bd164857d59b28fa", "pt")
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    val weatherData = response.body()?.data?.first()
                    if (weatherData != null) {
                        callback.onSuccess(weatherData)
                    } else {
                        Log.e("erro", "Cidade não encontrada")
                        callback.onFailure(Throwable("Cidade não encontrada"))
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("erro", t.toString())
                    callback.onFailure(t)
                }
            })

    }


    private fun createWeatherService(): WeatherBitService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.weatherbit.io/v2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WeatherBitService::class.java)
    }


}