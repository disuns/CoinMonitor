package com.android.trade.presentation.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.android.trade.presentation.adapter.CoinNameAdapter
import com.android.trade.presentation.databinding.BottomSheetCoinNameBinding
import com.android.trade.presentation.models.UpbitMarketUiModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoinNameBottomSheetDialog @Inject constructor(
    val exchange: UpbitMarketUiModel,
    private val onClick: () -> Unit
): BaseBottomSheetDialog<BottomSheetCoinNameBinding>(
    BottomSheetCoinNameBinding::inflate) {
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
                onClick()
                dismiss()
            }

            rvCoinName.layoutManager = LinearLayoutManager(context)
            rvCoinName.adapter = adapter
        }
    }
}