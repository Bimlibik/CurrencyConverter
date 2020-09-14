package com.foxy.currencyconverter.data.repository

import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.Result

interface ICurrenciesRepository {

    fun observeCurrencies(): LiveData<Result<List<Currency>>>

    suspend fun getCurrencies(forceUpdate: Boolean = false): Result<List<Currency>>

    suspend fun refreshCurrencies(forceUpdate: Boolean)

    suspend fun saveCurrencies(currencies: List<Currency>)

    suspend fun updateCurrencies(currencies: List<Currency>)
}