package com.fic.mobile_app_base_compose.data.remote

import com.google.gson.annotations.SerializedName

data class CornPriceResponse(
    @SerializedName("name") val name: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("data") val data: List<PriceData>
)

data class PriceData(
    @SerializedName("date") val date: String,
    @SerializedName("value") val value: String
)
