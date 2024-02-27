package com.henriqueargolo.previsaodotempo.model

import com.example.example.Main
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    @SerialName("main") var main: Main? = Main(),
    @SerialName("weather") var weather: List<AditionalInfo> = listOf(AditionalInfo()),
    @SerialName("name") var name: String? = null
)