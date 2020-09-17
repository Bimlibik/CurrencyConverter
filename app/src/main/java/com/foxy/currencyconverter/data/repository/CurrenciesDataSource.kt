package com.foxy.currencyconverter.data.repository

import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.model.Currency

interface CurrenciesDataSource {

    fun observeCurrencies(): LiveData<Result<List<Currency>>>

    suspend fun getCurrencies(): Result<List<Currency>>

    suspend fun saveCurrencies(currencies: List<Currency>)

    suspend fun deleteCurrencies()

    suspend fun isEmpty(): Boolean

    suspend fun updateSelected(currencyId: String, isSelected: Boolean)

}