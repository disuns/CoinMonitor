package com.android.trade.presentation.adapter

import com.android.trade.presentation.databinding.ItemCoinNameButtonBinding
import com.android.trade.presentation.models.MarketUiModel

class CoinNameAdapter(
    buttonList: MutableList<MarketUiModel.Item>,
    private val onClick: (String, String) -> Unit
):BaseAdapter<MarketUiModel.Item, ItemCoinNameButtonBinding>(
    buttonList, ItemCoinNameButtonBinding::inflate,
    { item -> onClick(item.code, item.name) }
){
    override fun onBind(
        binding: ItemCoinNameButtonBinding,
        item: MarketUiModel.Item,
        position: Int
    ) {
        binding.tvCoinName.text = item.name
    }
}