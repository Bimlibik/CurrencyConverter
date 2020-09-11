package com.foxy.currencyconverter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.foxy.currencyconverter.databinding.FragmentCurrencyListBinding
import com.foxy.currencyconverter.util.getViewModelFactory
import com.foxy.currencyconverter.view_models.CurrenciesViewModel

class CurrencyListFragment : Fragment() {

    private val viewModel by viewModels<CurrenciesViewModel> {
        getViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentCurrencyListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

}