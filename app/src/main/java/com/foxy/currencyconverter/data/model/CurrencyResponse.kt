package com.foxy.currencyconverter.data.model

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(

    @SerializedName("Valute")
    val valute: Valute
)