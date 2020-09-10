package com.foxy.currencyconverter.data.repository.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private lateinit var retrofit: Retrofit

    val client: Retrofit
        get() {
            retrofit = Retrofit.Builder()
                .baseUrl("https://www.cbr-xml-daily.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }
}