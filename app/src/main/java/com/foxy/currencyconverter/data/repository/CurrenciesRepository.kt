package com.foxy.currencyconverter.data.repository


import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.*
import com.foxy.currencyconverter.data.model.Currency
import kotlinx.coroutines.*
import java.lang.Exception

class CurrenciesRepository(
    private val currenciesLocalDataSource: CurrenciesDataSource,
    private val currenciesRemoteDataSource: CurrenciesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICurrenciesRepository {

    override fun observeCurrencies(): LiveData<Result<List<Currency>>> {
        return currenciesLocalDataSource.observeCurrencies()
    }

    override suspend fun getCurrencies(forceUpdate: Boolean): Result<List<Currency>> {
        if (forceUpdate) {
            try {
                updateCurrenciesFromNetwork()
            } catch (e: Exception) {
                return Error(e)
            }
        }
        return currenciesLocalDataSource.getCurrencies()
    }

    override suspend fun refreshCurrencies() {
        updateCurrenciesFromNetwork()
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

    private suspend fun updateCurrenciesFromNetwork() {
        val remoteCurrencies = currenciesRemoteDataSource.getCurrencies()

        if (remoteCurrencies is Success) {
            currenciesLocalDataSource.deleteCurrencies()
            currenciesLocalDataSource.saveCurrencies(remoteCurrencies.data)
        } else if (remoteCurrencies is Error) {
            throw remoteCurrencies.exception
        }
    }
}