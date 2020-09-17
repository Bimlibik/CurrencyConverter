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
        TODO("Observe to remote data source")
    }

    override suspend fun getCurrencies(): Result<List<Currency>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(loadCurrenciesFromNetwork())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveCurrencies(currencies: List<Currency>) {

    }

    override suspend fun deleteCurrencies() {

    }

    override suspend fun isEmpty(): Boolean {
        TODO("Check if remote data source is empty")
    }

    override suspend fun updateSelected(currencyId: String, isSelected: Boolean) {
        TODO("TODO")
    }

    private suspend fun loadCurrenciesFromNetwork(): List<Currency> {
        val apiClient = ApiClient.client.create(CurrencyApiInterface::class.java)
        return apiClient.getCurrencies().currencies.values.toList()
    }

}