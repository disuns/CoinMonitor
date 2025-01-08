package com.android.trade.coinmonitor.ui.fragment

import androidx.fragment.app.viewModels
import com.android.trade.coinmonitor.databinding.FragmentMainBinding
import com.android.trade.presentation.viewmodels.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, CoinViewModel>(FragmentMainBinding::inflate){

    override val viewModel: CoinViewModel by viewModels()

    override fun setupView() {
        bind {
        }
    }
}