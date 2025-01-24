package com.android.trade.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.models.WebSocketData
import com.android.trade.presentation.databinding.ItemCoinInfoBinding

class CoinInfoAdapter(
    private var items : MutableList<CoinInfo>,
    private val onRemoveClick: (Int) -> Unit)
    :BaseAdapter<CoinInfo, ItemCoinInfoBinding>(items,ItemCoinInfoBinding::inflate){
    override fun onBind(binding: ItemCoinInfoBinding, item: CoinInfo, position: Int) {
        binding.apply {
            tvCoinMarket.text = item.market
            tvCoinName.text = item.coinName
            tvCoinPrice.text = item.price
            btnDelete.setOnClickListener { onRemoveClick(position) }
        }
    }

    fun updatePrice(socketData: WebSocketData?) {
        if (socketData == null) return
        val position = items.indexOfFirst { it.market == socketData.market && it.code == socketData.code }
        if (position != -1 && items[position].price != socketData.price) {

            val updatedItem = items[position].copy(price = socketData.price ?: "")

            val newItems = items.toMutableList().apply {
                this[position] = updatedItem
            }

            val diffCallback = object : DiffUtil.Callback() {
                override fun getOldListSize() = items.size
                override fun getNewListSize() = newItems.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return items[oldItemPosition].market == newItems[newItemPosition].market &&
                            items[oldItemPosition].code == newItems[newItemPosition].code
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return items[oldItemPosition] == newItems[newItemPosition]
                }

                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                    return if (items[oldItemPosition].price != newItems[newItemPosition].price) {
                        newItems[newItemPosition].price
                    } else {
                        null
                    }
                }
            }

            val diffResult = DiffUtil.calculateDiff(diffCallback)
            items.clear()
            items.addAll(newItems)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    fun updateList(newCoins: MutableList<CoinInfo>) {
        items.clear()
        items.addAll(newCoins)
        notifyDataSetChanged()
    }
}