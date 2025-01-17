package com.android.trade.presentation.ui.main.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.trade.domain.ApiResult
import com.android.trade.presentation.databinding.FragmentMainBinding
import com.android.trade.presentation.ui.base.BaseFragment
import com.android.trade.presentation.ui.main.fragment.dialog.CoinExcahngeBottomSheetDialog
import com.android.trade.presentation.ui.main.fragment.dialog.CoinNameBottomSheetDialog
import com.android.trade.presentation.viewmodels.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, CoinViewModel>(FragmentMainBinding::inflate) {
    override val viewModel: CoinViewModel by viewModels()
    override fun setupView() {
        bind {
            btnGetMarket.setOnClickListener {
                val bottomSheet = CoinExcahngeBottomSheetDialog { itemText ->
                    when (itemText) {
                        "Upbit" -> viewModel.fetchUpbitMarket()
                    }
                }
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
            handleState()
        }
    }

    private fun handleState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { uiState ->
                    when(uiState.upbitMarketState){
                        is ApiResult.Success -> {
                            if(uiState.upbitMarketState.value.size > 0) {
                                val bottomSheet = CoinNameBottomSheetDialog(uiState.upbitMarketState.value){viewModel.resetState()}
                                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                            }
                        }
                        is ApiResult.Error -> {}
                        is ApiResult.Loading -> {}
                        is ApiResult.Empty -> {}
                    }
                }
            }
        }
    }
}