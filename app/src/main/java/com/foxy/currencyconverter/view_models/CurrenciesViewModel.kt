package com.foxy.currencyconverter.view_models

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
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository.LoadCurrenciesCallback
import com.foxy.currencyconverter.util.round
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class CurrenciesViewModel(private val repository: ICurrenciesRepository) : ViewModel() {

    private lateinit var selectedCurrency: Currency

    private lateinit var timer: EventCountDown

    private val formatter = DecimalFormat(FORMAT_PATTERN)

    // Two-way databinding
    val amount = MutableLiveData<String>()
    private val amountObserver = Observer<String> { newAmount ->
        if (newAmount != null) onAmountChanged(newAmount)
    }

    // Two-way databinding
    val amountFrom = MutableLiveData(DEFAULT_AMOUNT)

    // Two-way databinding
    val amountTo = MutableLiveData(DEFAULT_AMOUNT)

    // Two-way databinding
    val currencyCode = MutableLiveData<String>(null)

    private val _forceUpdate = MutableLiveData(false)

    private val _currencies: LiveData<List<Currency>> = _forceUpdate.switchMap { forceUpdate ->
        viewModelScope.launch {
            repository.refreshCurrencies(forceUpdate, object : LoadCurrenciesCallback {
                override fun success() {
                    _snackbarText.value = Event(R.string.snackbar_msg_loading_success)
                }

                override fun error() {
                    _snackbarText.value = Event(R.string.snackbar_msg_loading_error)
                }

            })
        }
        repository.observeCurrencies().distinctUntilChanged().switchMap { computeResult(it) }
    }

    val currencies: LiveData<List<Currency>> = _currencies

    val empty: LiveData<Boolean> = Transformations.map(_currencies) {
        it.isEmpty()
    }

    private val _isCollapse = MutableLiveData(false)
    val isCollapse: LiveData<Boolean> = _isCollapse

    private val _collapseIconRes = MutableLiveData(R.drawable.ic_collapse)
    val collapseIconRes: LiveData<Int> = _collapseIconRes

    private val _timeUntilUpdate = MutableLiveData<Long>()
    val timeUntilUpdate: LiveData<String> = _timeUntilUpdate.map {
        DateUtils.formatElapsedTime(it)
    }
    val progress: LiveData<Int> = _timeUntilUpdate.map {
        it.toInt()
    }

    private val _collapseLabel = MutableLiveData(R.string.collapse)
    val collapseLabel: LiveData<Int> = _collapseLabel

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText


    init {
        loadCurrencies(false)
        createTimer()
        amount.observeForever(amountObserver)
    }

    fun refresh() {
        loadCurrencies(true)
    }

    fun saveSelectedCurrency(currency: Currency) {
        if (currency.isEmpty) {
            _snackbarText.value = Event(R.string.snackbar_msg_currency_is_empty)
            return
        }
        selectedCurrency = currency
        currencyCode.value = currency.charCode
        amount.value = amount.value
    }

    fun showHideConverterPanel(isShown: Boolean) {
        _isCollapse.value = isShown
        changeCollapseInfo(isShown)
    }

    private fun onAmountChanged(newAmount: String) {
        if (newAmount.isEmpty()) {
            amountFrom.value = DEFAULT_AMOUNT
            amountTo.value = DEFAULT_AMOUNT
        } else {
            try {
                amountFrom.value = formatter.format(newAmount.toLong())
                computeWithNewAmount(newAmount.toLong())
            } catch (e: NumberFormatException) {
                _snackbarText.value = Event(R.string.snackbar_msg_amount_error)
                amountFrom.value = DEFAULT_AMOUNT
                amountTo.value = DEFAULT_AMOUNT
            }

        }
    }

    private fun computeWithNewAmount(newAmount: Long) {
        if (this::selectedCurrency.isInitialized) {
            try {
                val result = (newAmount / selectedCurrency.getRate()).round()
                amountTo.value = formatter.format(result)
            } catch (e: NumberFormatException) {
                _snackbarText.value = Event(R.string.snackbar_msg_compute_error)
                amountTo.value = DEFAULT_AMOUNT
            }
        }
    }

    private fun changeCollapseInfo(isCollapse: Boolean) {
        if (isCollapse) {
            _collapseIconRes.value = R.drawable.ic_expand
            _collapseLabel.value = R.string.expand
        } else {
            _collapseIconRes.value = R.drawable.ic_collapse
            _collapseLabel.value = R.string.collapse
        }
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
            result.value = currenciesResult.data
        } else {
            result.value = emptyList()
            _snackbarText.value = Event(R.string.snackbar_msg_loading_error)
        }
        return result
    }
}


private const val DEFAULT_AMOUNT = "N/A"
private const val FORMAT_PATTERN = "#,###,###.##"