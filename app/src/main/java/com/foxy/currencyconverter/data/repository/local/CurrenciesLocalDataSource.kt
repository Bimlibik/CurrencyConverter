package com.foxy.currencyconverter.data.repository.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.foxy.currencyconverter.data.repository.CurrenciesDataSource
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CurrenciesLocalDataSource(
    private val currenciesDao: CurrencyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrenciesDataSource {

    override fun observeCurrencies(): LiveData<Result<List<Currency>>> {
        return currenciesDao.observeCurrencies().map {
            Success(it)
        }
    }

    override suspend fun getCurrencies(): Result<List<Currency>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(currenciesDao.getCurrencies())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun refreshCurrencies() {

    }

    override suspend fun saveCurrencies(currencies: List<Currency>) = withContext(ioDispatcher) {
        currenciesDao.insertCurrencies(currencies)
    }

    override suspend fun updateCurrencies(currencies: List<Currency>) = withContext(ioDispatcher) {
        currenciesDao.updateCurrencies(currencies)
    }

    override suspend fun deleteCurrencies() = withContext(ioDispatcher) {
        currenciesDao.deleteCurrencies()
    }
}