package com.android.trade.presentation.ui.main.fragment.dialog

import androidx.recyclerview.widget.LinearLayoutManager
import com.android.trade.presentation.adapter.CoinNameAdapter
import com.android.trade.presentation.databinding.BottomSheetCoinNameBinding
import com.android.trade.presentation.models.MarketUiModel
import com.android.trade.presentation.ui.base.BaseBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoinNameBottomSheetDialog @Inject constructor(
    val exchange: MarketUiModel,
    private val onClick: (String, String) -> Unit
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

            val adapter = CoinNameAdapter(exchange.items) { itemCode, itemCoin->
                onClick(itemCode, itemCoin)
                dismiss()
            }

            rvCoinName.layoutManager = LinearLayoutManager(context)
            rvCoinName.adapter = adapter
        }
    }
}