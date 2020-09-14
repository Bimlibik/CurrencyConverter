package com.foxy.currencyconverter.view_models

import androidx.lifecycle.*
import com.foxy.currencyconverter.Event
import com.foxy.currencyconverter.R
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.Success
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository
import com.foxy.currencyconverter.util.round
import kotlinx.coroutines.launch

class CurrenciesViewModel(private val repository: ICurrenciesRepository) : ViewModel() {

    private lateinit var selectedCurrency: Currency

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
            repository.refreshCurrencies(forceUpdate)
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

    private val _collapseLabel = MutableLiveData(R.string.collapse)
    val collapseLabel: LiveData<Int> = _collapseLabel

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText


    init {
        loadCurrencies(false)
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
            amountFrom.value = newAmount
            computeWithNewAmount(newAmount.toInt())
        }
    }

    private fun computeWithNewAmount(newAmount: Int) {
        if (this::selectedCurrency.isInitialized) {
            val result = (newAmount / selectedCurrency.getRate()).round()
            amountTo.value = result.toString()
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