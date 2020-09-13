package com.foxy.currencyconverter.data.repository.remote

import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.Error
import com.foxy.currencyconverter.data.Result.Success
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.repository.CurrenciesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrenciesRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrenciesDataSource {

    override fun observeCurrencies(): LiveData<Result<List<Currency>>> {
        TODO("TODO")
    }

    override suspend fun getCurrencies(): Result<List<Currency>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(loadCurrenciesFromNetwork())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun refreshCurrencies() {

    }

    override suspend fun saveCurrency(currency: Currency) {
        TODO("TODO")
    }

    override suspend fun saveCurrencies(currencies: List<Currency>) {

    }

    override suspend fun updateCurrencies(currencies: List<Currency>) {

    }

    override suspend fun deleteCurrencies() {

    }

    private suspend fun loadCurrenciesFromNetwork(): List<Currency> {
        val apiClient = ApiClient.client.create(CurrencyApiInterface::class.java)
        return apiClient.getCurrencies().currencies.values.toList()
    }

}