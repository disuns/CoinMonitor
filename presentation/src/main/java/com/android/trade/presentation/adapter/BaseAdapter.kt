package com.android.trade.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB : ViewBinding>(
    private val items: List<T>,
    private val inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val onItemClick: ((T) -> Unit)? = null
) : RecyclerView.Adapter<BaseAdapter<T, VB>.BaseViewHolder>()  {

    abstract fun onBind(binding: VB, item: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = inflateBinding(LayoutInflater.from(parent.context), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
        onBind(holder.binding, item, position)
    }

    override fun getItemCount(): Int = items.size

    inner class BaseViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}