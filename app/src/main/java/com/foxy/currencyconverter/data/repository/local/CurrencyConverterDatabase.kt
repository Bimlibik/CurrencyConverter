package com.foxy.currencyconverter.data.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.foxy.currencyconverter.data.model.Currency

@Database(entities = [Currency::class], version = 1, exportSchema = false)
abstract class CurrencyConverterDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object {
        private const val DATABASE_NAME = "currencyConverterDb"

        @Volatile
        private var instance: CurrencyConverterDatabase? = null

        fun getInstance(context: Context): CurrencyConverterDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CurrencyConverterDatabase {
            return Room.databaseBuilder(
                context,
                CurrencyConverterDatabase::class.java,
                DATABASE_NAME
            )
                .build()
        }
    }
}