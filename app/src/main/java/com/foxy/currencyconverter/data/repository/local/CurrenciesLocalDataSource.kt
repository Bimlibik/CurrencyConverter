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

    override suspend fun saveCurrencies(currencies: List<Currency>) = withContext(ioDispatcher) {
        currenciesDao.upsertCurrencies(currencies)
    }

    override suspend fun deleteCurrencies() = withContext(ioDispatcher) {
        currenciesDao.deleteCurrencies()
    }

    override suspend fun isEmpty(): Boolean = withContext(ioDispatcher) {
        return@withContext currenciesDao.getAnyCurrency() == null
    }

    override suspend fun updateSelected(currencyId: String, isSelected: Boolean) = withContext(ioDispatcher) {
        currenciesDao.updateSelected(currencyId, isSelected)
    }
}