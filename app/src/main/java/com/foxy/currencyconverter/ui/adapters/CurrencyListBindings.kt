package com.foxy.currencyconverter.ui.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foxy.currencyconverter.data.model.Currency

@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: List<Currency>?) {
    items?.let {
        (recyclerView.adapter as CurrenciesAdapter).submitList(items)
    }
}