package com.android.trade.presentation.adapter

import com.android.trade.presentation.databinding.ItemCoinExchangeButtonBinding

class CoinExchangeAdapter(
    buttonList: MutableList<String>,
    private val onClick: (String) -> Unit
):BaseAdapter<String, ItemCoinExchangeButtonBinding>(
    buttonList, ItemCoinExchangeButtonBinding::inflate,
    {item -> onClick(item)}
){
    override fun onBind(binding: ItemCoinExchangeButtonBinding, item: String, position: Int) {
        binding.tvCoinExchange.text = item
    }
}