package com.android.trade.presentation.ui.main.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.trade.common.utils.logMessage
import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinInfo
import com.android.trade.presentation.adapter.CoinInfoAdapter
import com.android.trade.presentation.databinding.FragmentMainBinding
import com.android.trade.presentation.ui.base.BaseFragment
import com.android.trade.presentation.ui.main.fragment.dialog.CoinExcahngeBottomSheetDialog
import com.android.trade.presentation.ui.main.fragment.dialog.CoinNameBottomSheetDialog
import com.android.trade.presentation.viewmodels.CoinViewModel
import com.android.trade.presentation.viewmodels.RoomAndWebSocketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    val coinViewModel: CoinViewModel by viewModels()
    val roomAndWebSocketViewModel: RoomAndWebSocketViewModel by viewModels()

    private lateinit var adapter: CoinInfoAdapter
    private var coins: MutableList<CoinInfo> = mutableListOf()

    override fun setupView() {
        bind {
            roomAndWebSocketViewModel.getAllCoin()
            adapter = CoinInfoAdapter(coins){position->
                roomAndWebSocketViewModel.deleteCoin(coins[position])
            }
            rvCoins.layoutManager = LinearLayoutManager(requireContext())
            rvCoins.adapter = adapter

            btnGetMarket.setOnClickListener {
                val bottomSheet = CoinExcahngeBottomSheetDialog { itemText ->
                    roomAndWebSocketViewModel.getAllCoin()
                    coinViewModel.fetchMarket(itemText)
                }
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
            handleState()
        }
    }

    private fun handleState(){
        collectState(coinViewModel.state.marketState){ uiState->
            when(uiState){
                is ApiResult.Success -> {
                    if(uiState.value.items.size > 0) {
                        val bottomSheet = CoinNameBottomSheetDialog(uiState.value){code, coin ->
                            roomAndWebSocketViewModel.insertCoin(CoinInfo(uiState.value.market, code, coin))
                            coinViewModel.resetState()
                        }
                        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                    }
                }
                is ApiResult.Error -> {}
                is ApiResult.Loading -> {}
                is ApiResult.Empty -> {}
            }
        }

        collectState(roomAndWebSocketViewModel.state.coinsListState){ uiState->
            if (uiState != coins) {
                coins = uiState.toMutableList()
                adapter.updateList(coins)
                coins.forEach {
                    roomAndWebSocketViewModel.connectWebSocket(it.market)
                }
                bind {
                    btnGetMarket.visibility = if(coins.size >=3) View.GONE
                    else View.VISIBLE
                }
            }
        }

        roomAndWebSocketViewModel.messages.observe(viewLifecycleOwner){message ->
//            logMessage(message)
            when(message){
                null->{
                    roomAndWebSocketViewModel.sendAllMessage()
                }
                else-> {
                    adapter.updatePrice(message)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        roomAndWebSocketViewModel.disconnectAll()
    }
}