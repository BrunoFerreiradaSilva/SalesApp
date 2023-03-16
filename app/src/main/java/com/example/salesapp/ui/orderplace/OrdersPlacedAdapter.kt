package com.example.salesapp.ui.orderplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.salesapp.databinding.ItemOrderRecyclerBinding
import com.example.salesapp.model.Order
import com.example.salesapp.util.formatForTwoDecimalPlaces

class OrdersPlacedAdapter : ListAdapter<Order, ViewHolder>(OrdersPlacedAdapter) {

    inner class ItemRecycler(private val itemRecycler: ItemOrderRecyclerBinding) :
        ViewHolder(itemRecycler.root) {
        fun binding(order: Order) {
            itemRecycler.tvOrderNumber.text = order.id.toString()
            order.listItems.forEach {
                itemRecycler.tvTotalOrder.text = "R$ ${it.total.formatForTwoDecimalPlaces()}"
            }


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemOrderView = ItemOrderRecyclerBinding.inflate(layoutInflater)
        return ItemRecycler(itemOrderView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ItemRecycler).binding(getItem(position))
    }

    private companion object DiffUtilCallBack : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Order,
            newItem: Order
        ): Boolean {
            return oldItem == newItem
        }
    }
}