package com.henriqueargolo.previsaodotempo


import android.annotation.SuppressLint
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

    private lateinit var biding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        biding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(biding.root)

        val api = Api()
        searchForResults(api)
        darkMode()

    }

    private fun searchForResults(api: Api){
        biding.btnSearch.setOnClickListener{
            lifecycleScope.launch {
                try {
                    var textSearch = biding.textSearch.text.toString().toLowerCasePreservingASCIIRules()
                    api.getWeather(textSearch).collect { info ->
                        if(info.main?.temp != null){
                            Log.d("MainActivity", "Temperatura: ${info.main?.temp}")
                            weatherInfo = info
                            setInfoWeather(weatherInfo)
                            Toast.makeText(this@MainActivity, "TUDO CERTO", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@MainActivity, "NADA ENCONTRADO", Toast.LENGTH_LONG).show()
                        }

                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Exception: ${e.message}", e)

                }
            }
        }
    }
    private fun darkMode() {
        biding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                biding.main1.setBackgroundColor(resources.getColor(R.color.dark_blue_600))
            } else {
                biding.main1.setBackgroundColor((resources.getColor(R.color.dark_blue_300)))
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setInfoWeather(weatherMain: Weather){
        biding.textCity.text = weatherInfo.name
        biding.textDegress.text =(weatherInfo.main?.temp?.minus(273)?.toInt()).toString() + "ºC"
        biding.textUmidade.text = weatherInfo.main?.humidity.toString() + "%"
        biding.textTempMin.text =(weatherInfo.main?.tempMin?.minus(273)?.toInt()).toString() + "ºC"
        biding.textTempMax.text =( weatherInfo.main?.tempMax?.minus(273)?.toInt()).toString() + "ºC"
    }


}
