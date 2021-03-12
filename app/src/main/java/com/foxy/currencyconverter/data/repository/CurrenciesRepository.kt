package com.foxy.currencyconverter.data.repository


import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.Success
import com.foxy.currencyconverter.data.model.Currency
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CurrenciesRepository(
    private val currenciesLocalDataSource: CurrenciesDataSource,
    private val currenciesRemoteDataSource: CurrenciesDataSource
) : ICurrenciesRepository {

    override fun observeCurrencies(): LiveData<Result<List<Currency>>> {
        return currenciesLocalDataSource.observeCurrencies()
    }

    override suspend fun refreshCurrencies(forceUpdate: Boolean, callback: (isSuccess: Boolean) -> Unit) {
        if (forceUpdate) {
            updateCurrenciesFromNetwork(callback)
        } else {
            if (currenciesLocalDataSource.isEmpty()) {
                updateCurrenciesFromNetwork(callback)
            }
        }
    }

    override suspend fun updateSelected(currencyId: String, isSelected: Boolean) {
        coroutineScope {
            launch {
                currenciesLocalDataSource.updateSelected(currencyId, isSelected)
            }
        }
    }

    private suspend fun updateCurrenciesFromNetwork(callback: (isSuccess: Boolean) -> Unit) {
        val remoteCurrencies = currenciesRemoteDataSource.getCurrencies()

        if (remoteCurrencies is Success) {
            currenciesLocalDataSource.saveCurrencies(remoteCurrencies.data)
            callback(true)
        } else {
            callback(false)
        }
    }
}