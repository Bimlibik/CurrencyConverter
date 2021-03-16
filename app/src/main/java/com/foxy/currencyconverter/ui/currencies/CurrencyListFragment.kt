package com.foxy.currencyconverter.ui.currencies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.foxy.currencyconverter.R
import com.foxy.currencyconverter.databinding.FragmentCurrencyListBinding
import com.foxy.currencyconverter.util.getViewModelFactory
import com.foxy.currencyconverter.util.setupSnackbar
import kotlinx.android.synthetic.main.fragment_currency_list.*
import kotlinx.android.synthetic.main.layout_converter.view.*

class CurrencyListFragment : Fragment() {

    private val viewModel by viewModels<CurrenciesViewModel> { getViewModelFactory() }

    private lateinit var viewDataBinding: FragmentCurrencyListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentCurrencyListBinding
            .inflate(inflater, container, false).apply {
                viewModel = this@CurrencyListFragment.viewModel
            }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupToolbar()
        setupAdapter()
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText)
    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewModel
        if (viewModel != null) {
            val currenciesAdapter = CurrenciesAdapter(viewModel)
            viewDataBinding.recycler.adapter = currenciesAdapter
        }
    }

    private fun setupToolbar() {
        viewDataBinding.toolbar.inflateMenu(R.menu.menu_currencies)
        viewDataBinding.toolbar.menu.findItem(R.id.menu_refresh).setOnMenuItemClickListener {
            viewModel.refresh()
            true
        }
    }

}