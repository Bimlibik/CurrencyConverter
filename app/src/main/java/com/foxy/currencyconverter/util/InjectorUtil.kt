package com.foxy.currencyconverter.util

import com.foxy.currencyconverter.CurrencyConverterApp
import com.foxy.currencyconverter.data.repository.CurrenciesDataSource
import com.foxy.currencyconverter.data.repository.CurrenciesRepository
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository
import com.foxy.currencyconverter.data.repository.local.CurrenciesLocalDataSource
import com.foxy.currencyconverter.data.repository.remote.CurrenciesRemoteDataSource

object InjectorUtil {

    fun getCurrenciesRepository(): ICurrenciesRepository {
       return CurrenciesRepository(createLocalDataSource(), createRemoteDataSource())
    }

    private fun createLocalDataSource(): CurrenciesDataSource {
        return CurrenciesLocalDataSource(CurrencyConverterApp.get().getDatabase().currencyDao())
    }

    private fun createRemoteDataSource(): CurrenciesDataSource {
        return CurrenciesRemoteDataSource()
    }
}