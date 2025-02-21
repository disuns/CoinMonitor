package com.android.trade.presentation.ui.main.fragment

import android.view.View
import android.os.Build
import android.os.Bundle
import android.view.WindowMetrics
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.trade.common.enum.MarketType
import com.android.trade.common.utils.logMessage
import com.android.trade.domain.models.CoinInfo
import com.android.trade.presentation.R
import com.android.trade.presentation.adapter.CoinInfoAdapter
import com.android.trade.presentation.databinding.FragmentMainBinding
import com.android.trade.presentation.ui.base.BaseFragment
import com.android.trade.presentation.ui.main.fragment.dialog.CoinExcahngeBottomSheetDialog
import com.android.trade.presentation.ui.main.fragment.dialog.CoinNameBottomSheetDialog
import com.android.trade.presentation.viewmodels.CoinViewModel
import com.android.trade.presentation.viewmodels.RoomAndWebSocketViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    val coinViewModel: CoinViewModel by viewModels()
    val roomAndWebSocketViewModel: RoomAndWebSocketViewModel by viewModels()

    private lateinit var adapter: CoinInfoAdapter
    private var coins: MutableList<CoinInfo> = mutableListOf()

    // Get the ad size with screen width.
    private val mAdSize: AdSize
        get() {
            val displayMetrics = resources.displayMetrics
            val adWidthPixels =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowMetrics: WindowMetrics =
                        requireActivity().windowManager.currentWindowMetrics
                    windowMetrics.bounds.width()
                } else {
                    displayMetrics.widthPixels
                }
            val density = displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                requireActivity(),
                adWidth
            )
        }

    override fun setupView(savedInstanceState: Bundle?) {
        bind {
            setAndPlayAdMob()

            if(savedInstanceState == null){
                callMarkets()
                }


//            리스트 전체 새로고침
//            {
//                callMarkets()
//            }

            adapter = CoinInfoAdapter(coins) { position ->
                roomAndWebSocketViewModel.deleteCoin(coins[position])
            }
            rvCoins.layoutManager = LinearLayoutManager(requireContext())
            rvCoins.adapter = adapter

            btnGetMarket.setOnClickListener {
                val bottomSheet = CoinExcahngeBottomSheetDialog { itemText ->
                    coinNameBottomSheetDialog(itemText)
                }
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
            handleState()
        }
    }

    private fun handleState() {
        collectState(roomAndWebSocketViewModel.state.coinsListState) { uiState ->
            if (uiState != coins) {
                coinViewModel.fetchTicker(uiState.toMutableList(), roomAndWebSocketViewModel) {
                    coins = uiState.toMutableList()

                    adapter.updateList(coins)
                    coins.forEach {
                        roomAndWebSocketViewModel.connectWebSocket(it.market)
                    }
                    bind {
                        btnGetMarket.visibility = if (coins.size >= 3) View.GONE
                        else View.VISIBLE
                    }
                }
            }
        }

        roomAndWebSocketViewModel.messages.observe(viewLifecycleOwner) { message ->
//            logMessage(message)
            adapter.updatePrice(message)
        }
    }

    override fun onDestroyView() {
        if(activity?.isChangingConfigurations == true) {
            super.onDestroyView()
            return
        }

        roomAndWebSocketViewModel.deleteCoinList()
        roomAndWebSocketViewModel.disconnectAll()
        super.onDestroyView()
    }

    private fun setAndPlayAdMob() {

        // AdMob 초기화
        MobileAds.initialize(requireActivity())

        bind {
            val adView = AdView(requireActivity()).apply {
                adUnitId =
                    "ca-app-pub-3940256099942544/6300978111" // 실제 ID : ca-app-pub-1122670623771851/3536074882
                setAdSize(mAdSize)
                loadAd(AdRequest.Builder().build())
            }

            adViewContainer.apply {
                removeAllViews()
                addView(adView)
            }
        }
    }

    private fun coinNameBottomSheetDialog(market: String) {
        lifecycleScope.launch {
            roomAndWebSocketViewModel.getCoinList(market)?.let { item ->
                val bottomSheet = CoinNameBottomSheetDialog(item) { code, coin ->
                    roomAndWebSocketViewModel.insertCoin(CoinInfo(item.market, code, coin))
                }
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
        }
    }

    private fun updateCallMarketsBtn(fetchResult : Boolean){
        bind {
            btnGetMarket.isEnabled = fetchResult

            val btnText = if(fetchResult) R.string.callMarketsCoins
            else R.string.loadingMarkets

            btnGetMarket.text = resources.getString(btnText)
        }
    }

    private fun callMarkets(){
        updateCallMarketsBtn(false)
        coinViewModel.fetchMarketSequentially(
            MarketType.entries.map { it.id },
            roomAndWebSocketViewModel
        ){
            updateCallMarketsBtn(true)
        }
    }
}