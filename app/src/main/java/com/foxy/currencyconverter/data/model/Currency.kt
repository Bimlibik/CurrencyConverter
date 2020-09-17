package com.foxy.currencyconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "currencies")
data class Currency(
    @SerializedName("ID")
    @PrimaryKey val id: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("CharCode")
    val charCode: String,

    @SerializedName("Nominal")
    val nominal: Int,

    @SerializedName("Value")
    val value: String,

    val isSelected: Boolean
) {

    val isEmpty get() =
        value == null || nominal == null || value.isEmpty() || nominal == 0

    fun getRate(): Double {
        return value.toDouble() / nominal
    }
}