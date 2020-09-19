package com.foxy.currencyconverter.data.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.foxy.currencyconverter.data.model.Currency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCurrencies(currencies: List<Currency>)

    @Query("UPDATE currencies SET nominal = :nominal, value = :value WHERE id = :id")
    fun updateCurrency(id: String, nominal: Int, value: String)

    @Transaction
    fun upsertCurrencies(currencies: List<Currency>) {
        insertCurrencies(currencies)

        for (currency in currencies) {
            updateCurrency(currency.id, currency.nominal, currency.value)
        }
    }

    @Query("DELETE FROM currencies")
    fun deleteCurrencies()

    @Query("SELECT * FROM currencies")
    fun observeCurrencies(): LiveData<List<Currency>>

    @Query("SELECT * FROM currencies")
    fun getCurrencies(): List<Currency>

    @Query("SELECT * FROM currencies LIMIT 1")
    fun getAnyCurrency(): Currency?

    @Query("UPDATE currencies SET isSelected = :isSelected WHERE id = :currencyId")
    fun updateSelected(currencyId: String, isSelected: Boolean)
}