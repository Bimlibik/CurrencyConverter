package com.foxy.currencyconverter.data.repository.remote

import com.foxy.currencyconverter.data.model.CurrencyResponse
import retrofit2.http.GET

interface CurrencyApiInterface {

    @GET("daily_json.js")
    suspend fun getCurrencies(): CurrencyResponse
}