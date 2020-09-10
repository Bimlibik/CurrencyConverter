package com.foxy.currencyconverter.data.model

import com.google.gson.annotations.SerializedName

data class Valute(

    @SerializedName("AUD")
    val aud: Currency,

    @SerializedName("AZN")
    val azn: Currency,

    @SerializedName("GBP")
    val gbp: Currency,

    @SerializedName("AMD")
    val amd: Currency,

    @SerializedName("BYN")
    val byn: Currency,

    @SerializedName("BGN")
    val bgn: Currency,

    @SerializedName("BRL")
    val brl: Currency,

    @SerializedName("HUF")
    val huf: Currency,

    @SerializedName("HKD")
    val hkd: Currency,

    @SerializedName("DKK")
    val dkk: Currency,

    @SerializedName("USD")
    val usd: Currency,

    @SerializedName("EUR")
    val eur: Currency,

    @SerializedName("INR")
    val inr: Currency,

    @SerializedName("KZT")
    val kzt: Currency,

    @SerializedName("CAD")
    val cad: Currency,

    @SerializedName("KGS")
    val kgs: Currency,

    @SerializedName("CNY")
    val cny: Currency,

    @SerializedName("MDL")
    val mdl: Currency,

    @SerializedName("NOK")
    val nok: Currency,

    @SerializedName("PLN")
    val pln: Currency,

    @SerializedName("RON")
    val ron: Currency,

    @SerializedName("XDR")
    val xdr: Currency,

    @SerializedName("SGD")
    val sgd: Currency,

    @SerializedName("TJS")
    val tjs: Currency,

    @SerializedName("TRY")
    val tryValute: Currency,

    @SerializedName("TMT")
    val tmt: Currency,

    @SerializedName("UZS")
    val uzs: Currency,

    @SerializedName("UAH")
    val uah: Currency,

    @SerializedName("CZK")
    val czk: Currency,

    @SerializedName("SEK")
    val sek: Currency,

    @SerializedName("CHF")
    val chf: Currency,

    @SerializedName("ZAR")
    val zar: Currency,

    @SerializedName("KRW")
    val krw: Currency,

    @SerializedName("JPY")
    val jpy: Currency
) {
    fun getCurrencies(): List<Currency> = mutableListOf<Currency>().apply {
        add(aud)
        add(azn)
        add(gbp)
        add(amd)
        add(byn)
        add(bgn)
        add(brl)
        add(huf)
        add(hkd)
        add(dkk)
        add(usd)
        add(eur)
        add(inr)
        add(kzt)
        add(cad)
        add(kgs)
        add(cny)
        add(mdl)
        add(nok)
        add(pln)
        add(ron)
        add(xdr)
        add(sgd)
        add(tjs)
        add(tryValute)
        add(tmt)
        add(uzs)
        add(uah)
        add(czk)
        add(sek)
        add(chf)
        add(zar)
        add(krw)
        add(jpy)
    }

}