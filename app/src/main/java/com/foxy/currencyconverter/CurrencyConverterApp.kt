package com.foxy.currencyconverter

import android.app.Application
import com.foxy.currencyconverter.data.repository.local.CurrencyConverterDatabase

class CurrencyConverterApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun getDatabase(): CurrencyConverterDatabase = CurrencyConverterDatabase.getInstance(INSTANCE)

    companion object {
        private lateinit var INSTANCE: CurrencyConverterApp

        fun get(): CurrencyConverterApp = INSTANCE
    }
}