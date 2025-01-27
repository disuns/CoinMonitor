package com.android.trade.presentation.ui.main.fragment.dialog

import androidx.recyclerview.widget.LinearLayoutManager
import com.android.trade.presentation.R
import com.android.trade.presentation.adapter.CoinExchangeAdapter
import com.android.trade.presentation.databinding.BottomSheetCoinExchangeBinding
import com.android.trade.presentation.ui.base.BaseBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoinExcahngeBottomSheetDialog @Inject constructor(
    private val onClick: (String) -> Unit
) : BaseBottomSheetDialog<BottomSheetCoinExchangeBinding>(BottomSheetCoinExchangeBinding::inflate) {
    override fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        bind {
            isCancelable =false
            (dialog as? BottomSheetDialog)?.behavior?.apply {
                isDraggable = false
            }
            val buttonText = resources.getStringArray(R.array.exchange).toMutableList()
            val adapter = CoinExchangeAdapter(buttonText) { itemText->
                onClick(itemText)
                dismiss()
            }

            rvCoinExchange.layoutManager = LinearLayoutManager(context)
            rvCoinExchange.adapter = adapter
        }
    }
}