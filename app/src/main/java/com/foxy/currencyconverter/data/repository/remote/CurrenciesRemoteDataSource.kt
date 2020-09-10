package com.foxy.currencyconverter.data.repository.remote

import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.*
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.model.CurrencyResponse
import com.foxy.currencyconverter.data.repository.CurrenciesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

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

    override suspend fun saveCurrencies(currencies: List<Currency>) {

    }

    override suspend fun updateCurrencies(currencies: List<Currency>) {

    }

    override suspend fun deleteCurrencies() {

    }

    private fun loadCurrenciesFromNetwork(): List<Currency> {
        val apiClient = ApiClient.client.create(CurrencyApiInterface::class.java)
        val currencies = mutableListOf<Currency>().apply { emptyList<Currency>() }

        apiClient.getCurrencies().enqueue(object : Callback<CurrencyResponse> {

            override fun onResponse(
                call: Call<CurrencyResponse>, response: Response<CurrencyResponse>
            ) {
                if (response != null && response.isSuccessful) {
                    response.body()?.let { currencies.addAll(it.valute.getCurrencies()) }
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                Error(Exception(t.message))
            }
        })
        return currencies
    }

}