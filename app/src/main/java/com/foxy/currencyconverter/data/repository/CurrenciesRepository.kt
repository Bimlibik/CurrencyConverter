package com.foxy.currencyconverter.data.repository


import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.Success
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository.LoadCurrenciesCallback

class CurrenciesRepository(
    private val currenciesLocalDataSource: CurrenciesDataSource,
    private val currenciesRemoteDataSource: CurrenciesDataSource
) : ICurrenciesRepository {

    override fun observeCurrencies(): LiveData<Result<List<Currency>>> {
        return currenciesLocalDataSource.observeCurrencies()
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