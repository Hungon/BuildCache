package com.trials.mobiledeveloperchallenge

import com.google.gson.annotations.SerializedName


data class Currency(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>
)

data class Rate(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    @SerializedName("timestamp")
    val timeStamp: Long,
    val source: String,
    val quotes: Map<String, Double>
)