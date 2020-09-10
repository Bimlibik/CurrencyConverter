package com.foxy.currencyconverter.data.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.foxy.currencyconverter.data.model.Currency

@Dao
interface CurrencyDao {

    @Insert
    fun insertCurrencies(currencies: List<Currency>)

    @Update
    fun updateCurrencies(currencies: List<Currency>)

    @Query("DELETE FROM currencies")
    fun deleteCurrencies()

    @Query("SELECT * FROM currencies")
    fun observeCurrencies(): LiveData<List<Currency>>

    @Query("SELECT * FROM currencies")
    fun getCurrencies(): List<Currency>
}