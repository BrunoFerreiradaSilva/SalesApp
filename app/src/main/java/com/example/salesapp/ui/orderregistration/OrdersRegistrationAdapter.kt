package com.example.salesapp.ui.orderregistration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.salesapp.databinding.ItemOrderRecyclerBinding
import com.example.salesapp.databinding.ItemRegistrationOrderBinding
import com.example.salesapp.model.Item

class OrdersRegistrationAdapter : ListAdapter<Item, ViewHolder>(OrdersRegistrationAdapter) {

    inner class ItemRecycler(private val itemRecycler: ItemRegistrationOrderBinding) :
        ViewHolder(itemRecycler.root) {
        fun binding(item: Item) {
            itemRecycler.apply {
                tvResultNameClient.text = item.nameProduct
                tvResultDescriptionClient.text = item.description
                val priceString = String.format("%.2f",item.price)
                tvResultPriceClient.text = "R$ ${priceString}"
                tvResultAmountClient.text = "${item.amount}"
                val priceTotalString = String.format("%.2f",item.total)
                tvResultTotalClient.text = "R$ $priceTotalString"
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemOrderView = ItemRegistrationOrderBinding.inflate(layoutInflater)
        return ItemRecycler(itemOrderView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ItemRecycler).binding(getItem(position))
    }

    private companion object DiffUtilCallBack : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem == newItem
        }
    }
}