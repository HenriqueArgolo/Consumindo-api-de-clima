package com.henriqueargolo.previsaodotempo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AditionalInfo(
    @SerialName("id") val id: Int? = null,
    @SerialName("main")val main: String? = null,
    @SerialName("description")val description: String? = null,
    @SerialName("icon")val icon: String? = null
)