package com.foxy.currencyconverter.data.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.foxy.currencyconverter.data.model.Currency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCurrencies(currencies: List<Currency>)

    @Insert
    fun insertCurrency(currency: Currency)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateCurrencies(currencies: List<Currency>)

    @Query("DELETE FROM currencies")
    fun deleteCurrencies()

    @Query("SELECT * FROM currencies")
    fun observeCurrencies(): LiveData<List<Currency>>

    @Query("SELECT * FROM currencies")
    fun getCurrencies(): List<Currency>

    @Query("SELECT * FROM currencies LIMIT 1")
    fun getAnyCurrency(): Currency?
}