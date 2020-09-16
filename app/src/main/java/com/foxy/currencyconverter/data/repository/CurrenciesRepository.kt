package com.foxy.currencyconverter.data.repository


import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.Error
import com.foxy.currencyconverter.data.Result.Success
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CurrenciesRepository(
    private val currenciesLocalDataSource: CurrenciesDataSource,
    private val currenciesRemoteDataSource: CurrenciesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICurrenciesRepository {

    override fun observeCurrencies(): LiveData<Result<List<Currency>>> {
        return currenciesLocalDataSource.observeCurrencies()
    }

    override suspend fun getCurrencies(forceUpdate: Boolean, callback: LoadCurrenciesCallback): Result<List<Currency>> {
        if (forceUpdate) {
            try {
                updateCurrenciesFromNetwork(callback)
            } catch (e: Exception) {
                return Error(e)
            }
        }
        return currenciesLocalDataSource.getCurrencies()
    }

    override suspend fun refreshCurrencies(forceUpdate: Boolean, callback: LoadCurrenciesCallback) {
        if (forceUpdate) {
            updateCurrenciesFromNetwork(callback)
        } else {
            if (currenciesLocalDataSource.isEmpty()) {
                updateCurrenciesFromNetwork(callback)
            }
        }
    }

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        coroutineScope {
            launch { currenciesLocalDataSource.saveCurrencies(currencies) }
        }
    }

    override suspend fun updateCurrencies(currencies: List<Currency>) {
        coroutineScope {
            launch { currenciesLocalDataSource.updateCurrencies(currencies) }
        }
    }

    private suspend fun updateCurrenciesFromNetwork(callback: LoadCurrenciesCallback) {
        val remoteCurrencies = currenciesRemoteDataSource.getCurrencies()

        if (remoteCurrencies is Success) {
            currenciesLocalDataSource.deleteCurrencies()
            currenciesLocalDataSource.saveCurrencies(remoteCurrencies.data)
            callback.success()
        } else {
            callback.error()
        }
    }
}