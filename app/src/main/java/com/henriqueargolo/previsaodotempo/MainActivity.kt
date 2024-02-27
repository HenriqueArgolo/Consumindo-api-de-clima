package com.henriqueargolo.previsaodotempo


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.henriqueargolo.previsaodotempo.databinding.ActivityMainBinding
import com.henriqueargolo.previsaodotempo.model.Weather
import com.henriqueargolo.previsaodotempo.services.Api
import io.ktor.util.toLowerCasePreservingASCIIRules
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var weatherInfo: Weather = Weather()
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var biding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        biding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(biding.root)
        sharedPreference = getSharedPreferences("weatherInfo", Context.MODE_PRIVATE)
        val api = Api()
        recoverLastSearh(api)
        searchForResults(api)
        darkMode()

    }


    private fun searchForResults(api: Api) {
        biding.btnSearch.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val textSearch = biding.textSearch.text.toString().toLowerCasePreservingASCIIRules()
                    api.getWeather(textSearch).collect { info ->
                        if (info.main?.temp != null) {
                            Log.d("MainActivity", "Temperatura: ${info.main?.temp}")
                            weatherInfo = info
                            setInfoWeather(weatherInfo)
                            saveCityName(textSearch)
                        } else {
                            Toast.makeText(this@MainActivity, "NADA ENCONTRADO", Toast.LENGTH_LONG)
                                .show()
                        }

                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Exception: ${e.message}", e)

                }
            }
        }
    }

    private fun darkMode() {

        val state = sharedPreference.getBoolean("switchStates", false)
        if (state) {
            biding.switchTheme.isChecked = true
            biding.main1.setBackgroundColor(resources.getColor(R.color.dark_mode))
        }


        biding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveSwitchStat(true)
                biding.main1.setBackgroundColor(resources.getColor(R.color.dark_mode))
            } else {
                saveSwitchStat(false)
                biding.main1.setBackgroundColor(resources.getColor(R.color.dark_blue_300))
            }

        }
    }

    private fun recoverLastSearh(api: Api) {
        val city = sharedPreference.getString("cityName", null)
        if (city != null) {
            lifecycleScope.launch {
                api.getWeather(city).collect { info ->
                    weatherInfo = info
                    setInfoWeather(weatherInfo)
                }
            }
        }
    }


    private fun translateWeather(weather: String): String {
        return when (weather) {
            "Clear" -> "Céu limpo"
            "Clouds" -> "Nuvens"
            "Drizzle" -> "Chuviscos"
            "Rain" -> "Chuva"
            "Thunderstorm" -> "Tempestade"
            "Snow" -> "Neve"
            "Mist" -> "Neblina"
            "Smoke" -> "Fumaça"
            "Haze" -> "Nevoeiro"
            "Dust" -> "Poeira"
            "Fog" -> "Névoa"
            "Sand" -> "Areia"
            "Ash" -> "Cinzas"
            "Squall" -> "Rajadas de vento"
            "Tornado" -> "Tornado"
            else -> "Desconhecido"
        }
    }


    private fun saveCityName(cityInfo: String) {
        val name = sharedPreference.edit()
        name.putString("cityName", cityInfo)
        name.apply()

    }

    private fun saveSwitchStat(states: Boolean) {
        val state = sharedPreference.edit()
        state.putBoolean("switchStates", states)
        state.apply()
    }


    @SuppressLint("SetTextI18n")
    private fun setInfoWeather(weatherMain: Weather) {
        var name = weatherInfo.weather[0].main.toString()
        biding.textCity.text = weatherInfo.name + " - " + weatherInfo.sys.country
        biding.textClima.text = translateWeather(weatherInfo.weather[0].main.toString())
        biding.textDegress.text = (weatherInfo.main?.temp?.minus(273)?.toInt()).toString() + "ºC"
        biding.textUmidade.text = weatherInfo.main?.humidity.toString() + "%"
        biding.textTempMin.text = (weatherInfo.main?.tempMin?.minus(273)?.toInt()).toString() + "ºC"
        biding.textTempMax.text = (weatherInfo.main?.tempMax?.minus(273)?.toInt()).toString() + "ºC"

        if(name == "Clear") biding.imageClima.setImageResource(R.drawable.clearsky)
        if(name == "Snow") biding.imageClima.setImageResource(R.drawable.snow)
        if(name == "Thunderstorm") biding.imageClima.setImageResource(R.drawable.trunderstorm)
        if(name == "Clouds") biding.imageClima.setImageResource(R.drawable.brokenclouds)
        if(name == "Rain") biding.imageClima.setImageResource(R.drawable.rain)

    }


}
