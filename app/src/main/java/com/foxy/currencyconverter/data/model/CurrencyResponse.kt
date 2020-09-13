package com.foxy.currencyconverter.data.model

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(

    @SerializedName("Valute")
    val currencies: Map<String, Currency>
)