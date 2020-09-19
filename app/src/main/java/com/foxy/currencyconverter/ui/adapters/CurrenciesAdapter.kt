package com.foxy.currencyconverter.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.databinding.ItemCurrencyBinding
import com.foxy.currencyconverter.ui.adapters.CurrenciesAdapter.*
import com.foxy.currencyconverter.view_models.CurrenciesViewModel

class CurrenciesAdapter(
    private val viewModel: CurrenciesViewModel) :
    ListAdapter<Currency, CurrencyViewHolder>(CurrenciesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = getItem(position)
        holder.bind(viewModel, currency)
    }


    class CurrencyViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: CurrenciesViewModel, currency: Currency) {
            binding.viewModel = viewModel
            binding.currency = currency
        }
    }

}


class CurrenciesDiffCallback : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem == newItem
    }

}