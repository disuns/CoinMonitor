package com.android.trade.presentation.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.trade.domain.ApiResult
import com.android.trade.presentation.databinding.FragmentMainBinding
import com.android.trade.presentation.viewmodels.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, CoinViewModel>(FragmentMainBinding::inflate) {
    override val viewModel: CoinViewModel by activityViewModels()
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

    fun handleState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { uiState ->
                    when(uiState.upbitMarketState){
                        is ApiResult.Success -> {
                            if(uiState.upbitMarketState.value.size > 0) {
                                val bottomSheet = CoinNameBottomSheetDialog(uiState.upbitMarketState.value)
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