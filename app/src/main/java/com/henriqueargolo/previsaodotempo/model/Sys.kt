package com.henriqueargolo.previsaodotempo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sys(
    @SerialName("serialName") var type: Int? = null,
    @SerialName("id") var id: Int? = null,
    @SerialName("country") var country: String? = null,
    @SerialName("sunrise") var sunrise: Int? = null,
    @SerialName("sunset") var sunset: Int? = null
)



