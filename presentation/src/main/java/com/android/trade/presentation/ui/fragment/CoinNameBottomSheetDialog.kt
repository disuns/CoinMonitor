package com.android.trade.presentation.ui.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.trade.presentation.adapter.CoinNameAdapter
import com.android.trade.presentation.databinding.BottomSheetCoinNameBinding
import com.android.trade.presentation.models.UpbitMarketUiModel
import com.android.trade.presentation.viewmodels.CoinViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoinNameBottomSheetDialog @Inject constructor(
    val exchange : UpbitMarketUiModel
): BaseBottomSheetDialog<BottomSheetCoinNameBinding, CoinViewModel>(
    BottomSheetCoinNameBinding::inflate) {
    override val viewModel: CoinViewModel by viewModels()
    override fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        bind {
            isCancelable =false
            (dialog as? BottomSheetDialog)?.behavior?.apply {
                isDraggable = false
            }

            val adapter = CoinNameAdapter(exchange) { itemText->
                dismiss()
            }

            rvCoinName.layoutManager = LinearLayoutManager(context)
            rvCoinName.adapter = adapter
        }
    }
}