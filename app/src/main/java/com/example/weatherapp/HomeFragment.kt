package com.example.weatherapp

import WeatherData
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.roundToInt


class HomeFragment : Fragment() {
    private var city = "Passo Fundo"
    private lateinit var weather: WeatherData
    private lateinit var binding: FragmentHomeBinding
    private var getNewWeatherUseCase: GetNewWeatherUseCase = GetNewWeatherUseCase()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setButtonClickers()
        // Buscar previsão
        getNewWeather()

        return binding.root
    }


    private fun getNewWeather() {
        val callback = object : WeatherDataCallback {
            override fun onSuccess(weatherData: WeatherData) {
                setNewTextViews(weatherData)
            }

            override fun onFailure(throwable: Throwable) {
                showError()
            }
        }
        getNewWeatherUseCase.getNewWeather(city, callback)

    }


    private fun setNewTextViews(data: WeatherData) {
        binding.temperatureTxt.text = roundValueToString(data.temp) + " ºc"
        binding.locationTxt.text = "${data.city}, ${data.country}"
        binding.windTxt.text = roundValueToString(data.humidity) + " Umidade"
        binding.precipTxt.text = roundValueToString(data.precip) + " Precipitação"
        binding.humidityTxt.text = roundValueToString(data.humidity) + " Úmidade"
        binding.weatherTxt.text = data.weather.description
    }

    private fun roundValueToString(value: Double): String {
        return value.roundToInt().toString()
    }

    private fun showError() {
        MaterialAlertDialogBuilder(
            requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).setMessage("Oops, tivemos um problema ao carregar os dados").show()
    }

    private fun setButtonClickers() {
        binding.reloadWeather.setOnClickListener { getNewWeather() }
        binding.shareWeather.setOnClickListener { shareWeather() }
        binding.searchWeather.setOnClickListener {
            showDialogWithInput(requireContext()) {
                city = it
                getNewWeather()
            }
        }
    }

    private fun shareWeather() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Hoje na cidade ${weather.city}, está fazendo ${weather.temp}ºc e o tempo está ${weather.weather.description}"
            )
            type = "text/plain"
        }
        startActivity(sendIntent)
    }
}


private fun showDialogWithInput(context: Context, callback: (String) -> Unit) {
    val editText = EditText(context)

    val dialog = AlertDialog.Builder(context)
        .setTitle("Digite o nome da sua cidade aqui")
        .setMessage("Apenas cidades brasileiras são válidas")
        .setView(editText)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            val inputString = editText.text.toString()
            callback(inputString)
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        .create()

    dialog.show()
}
