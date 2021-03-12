package com.foxy.currencyconverter.ui.currencies

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.foxy.currencyconverter.R
import com.foxy.currencyconverter.databinding.FragmentCurrencyListBinding
import com.foxy.currencyconverter.util.getViewModelFactory
import com.foxy.currencyconverter.util.setupSnackbar
import kotlinx.android.synthetic.main.fragment_currency_list.*

class CurrencyListFragment : Fragment() {

    private val viewModel by viewModels<CurrenciesViewModel> {
        getViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentCurrencyListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentCurrencyListBinding
            .inflate(inflater, container, false).apply {
                viewModel = this@CurrencyListFragment.viewModel
            }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.refresh()
                true
            }
            else -> false
        }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_currencies, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupToolbar()
        setupAdapter()
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText)
    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewModel
        if (viewModel != null) {
            val currenciesAdapter = CurrenciesAdapter(viewModel)
            viewDataBinding.recycler.adapter = currenciesAdapter
        }
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }
}