package com.android.trade.presentation.adapter

import com.android.trade.presentation.databinding.ItemCoinNameButtonBinding
import com.android.trade.presentation.models.UpbitMarketUiModel

class CoinNameAdapter(
    buttonList: UpbitMarketUiModel,
    private val onClick: (String) -> Unit
):BaseAdapter<UpbitMarketUiModel.Item, ItemCoinNameButtonBinding>(
    buttonList, ItemCoinNameButtonBinding::inflate,
    { item -> onClick(item.name) }
){
    override fun onBind(
        binding: ItemCoinNameButtonBinding,
        item: UpbitMarketUiModel.Item,
        position: Int
    ) {
        binding.tvCoinName.text = item.name
    }
}