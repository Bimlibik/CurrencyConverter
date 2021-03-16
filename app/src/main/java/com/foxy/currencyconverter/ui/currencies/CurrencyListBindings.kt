package com.foxy.currencyconverter.ui.currencies

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.foxy.currencyconverter.data.model.Currency

@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: List<Currency>?) {
    items?.let {
        (recyclerView.adapter as CurrenciesAdapter).submitList(items)

        for ((i, item) in items.withIndex()) {
            if (item.isSelected) {
                recyclerView.scrollToPosition(i)
                break
            }
        }
    }
}

@BindingAdapter("app:textVisibility")
fun charCodeVisibility(view: TextView, isEmpty: Boolean) {
    view.visibility = if (isEmpty) View.GONE else View.VISIBLE
}
