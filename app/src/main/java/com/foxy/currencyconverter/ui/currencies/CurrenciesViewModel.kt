package com.foxy.currencyconverter.ui.currencies

import android.text.format.DateUtils
import androidx.lifecycle.*
import com.foxy.currencyconverter.Event
import com.foxy.currencyconverter.EventCountDown
import com.foxy.currencyconverter.R
import com.foxy.currencyconverter.TimerListener
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.Success
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class CurrenciesViewModel(private val repository: ICurrenciesRepository) : ViewModel() {

    private lateinit var selectedCurrency: Currency

    private lateinit var timer: EventCountDown

    private val formatter = DecimalFormat(FORMAT_PATTERN)

    val amount = MutableLiveData<String>()

    private val _amountFrom: LiveData<String> = amount.switchMap { newAmount ->
        computeAmountFrom(newAmount)
    }
    val amountFrom: LiveData<String> = _amountFrom

    private val _amountTo: LiveData<String> = amount.switchMap { amount ->
        computeWithNewAmount(amount)
    }
    val amountTo: LiveData<String> = _amountTo

    val currencyCode = MutableLiveData<String>(null)

    private val _forceUpdate = MutableLiveData(false)

    private val _currencies: LiveData<List<Currency>> = _forceUpdate.switchMap { forceUpdate ->
        viewModelScope.launch {
            repository.refreshCurrencies(forceUpdate) { isSuccess ->
                if (isSuccess) {
                    _snackbarText.value = Event(R.string.snackbar_msg_loading_success)
                } else {
                    _snackbarText.value = Event(R.string.snackbar_msg_error_loading_from_network)
                }
            }
        }
        repository.observeCurrencies().distinctUntilChanged().switchMap { computeResult(it) }
    }

    val currencies: LiveData<List<Currency>> = _currencies

    val empty: LiveData<Boolean> = Transformations.map(_currencies) {
        it.isEmpty()
    }

    private val _timeUntilUpdate = MutableLiveData<Long>()
    val timeUntilUpdate: LiveData<String> = _timeUntilUpdate.map {
        DateUtils.formatElapsedTime(it)
    }
    val progress: LiveData<Int> = _timeUntilUpdate.map {
        it.toInt()
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText


    init {
        loadCurrencies(false)
        createTimer()
    }

    fun refresh() {
        loadCurrencies(true)
        timer.start()
    }

    fun setSelectedCurrency(currency: Currency) {
        if (currency.isEmpty) {
            _snackbarText.value = Event(R.string.snackbar_msg_currency_is_empty)
            return
        }
        updateSelected(currency)
    }

    private fun updateSelected(currency: Currency) {
        viewModelScope.launch {
            if (this@CurrenciesViewModel::selectedCurrency.isInitialized) {
                repository.updateSelected(selectedCurrency.id, false)
            }
            repository.updateSelected(currency.id, true)
        }
        selectedCurrency = currency
        currencyCode.value = currency.charCode
        amount.value = amount.value
    }

    private fun computeAmountFrom(newAmount: String): LiveData<String> {
        val result = MutableLiveData<String>()

        if (newAmount.isEmpty()) return result

        try {
            result.value = formatter.format(newAmount.toLong())
        } catch (e: NumberFormatException) {
            result.value = null
        }
        return result
    }

    private fun computeWithNewAmount(newAmount: String): LiveData<String> {
        val result = MutableLiveData<String>()

        if (newAmount.isEmpty()) return result

        if (this::selectedCurrency.isInitialized) {
            try {
                val amountToValue = selectedCurrency.convert(newAmount.toLong())
                result.value = formatter.format(amountToValue)
            } catch (e: NumberFormatException) {
                _snackbarText.value = Event(R.string.snackbar_msg_compute_error)
                result.value = null
            }
        }
        return result
    }

    private fun createTimer() {
        timer = EventCountDown(object : TimerListener {
            override fun onTick(time: Long) {
                _timeUntilUpdate.value = time
            }

            override fun onFinish() {
                refresh()
            }
        })
        timer.start()
    }

    private fun loadCurrencies(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun computeResult(currenciesResult: Result<List<Currency>>): LiveData<List<Currency>> {
        val result = MutableLiveData<List<Currency>>()

        if (currenciesResult is Success) {
            for (currency in currenciesResult.data) {
                if (currency.isSelected) {
                    selectedCurrency = currency
                    currencyCode.value = currency.charCode
                    break
                }
            }
            result.value = currenciesResult.data
        } else {
            result.value = emptyList()
            _snackbarText.value = Event(R.string.snackbar_msg_error_loading_from_db)
        }
        return result
    }
}


private const val FORMAT_PATTERN = "#,###,###.##"