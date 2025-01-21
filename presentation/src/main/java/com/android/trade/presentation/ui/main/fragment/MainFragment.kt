package com.android.trade.presentation.ui.main.fragment

import android.os.Build
import android.os.Bundle
import android.view.WindowMetrics
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, CoinViewModel>(FragmentMainBinding::inflate) {

    // Get the ad size with screen width.
    private val mAdSize: AdSize
        get() {
            val displayMetrics = resources.displayMetrics
            val adWidthPixels =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowMetrics: WindowMetrics = requireActivity().windowManager.currentWindowMetrics
                    windowMetrics.bounds.width()
                } else {
                    displayMetrics.widthPixels
                }
            val density = displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireActivity(), adWidth)
        }

    override val viewModel: CoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // AdMob 초기화
        MobileAds.initialize(requireActivity())
    }

    override fun setupView() {
        bind {
            setAndPlayAdMob()

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

    private fun setAndPlayAdMob() {
        bind {
            val adView = AdView(requireActivity()).apply {
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // 실제 ID : ca-app-pub-1122670623771851/3536074882
                setAdSize(mAdSize)
                loadAd(AdRequest.Builder().build())
            }

            adViewContainer.apply {
                removeAllViews()
                addView(adView)
            }
        }
    }
}