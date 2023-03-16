package com.example.salesapp.ui.orderplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.salesapp.databinding.ItemOrderRecyclerBinding
import com.example.salesapp.model.Order
import java.text.NumberFormat

class OrdersPlacedAdapter(private val listener: OnClickListener) :
    ListAdapter<Order, ViewHolder>(OrdersPlacedAdapter) {

    inner class ItemRecycler(private val itemRecycler: ItemOrderRecyclerBinding) :
        ViewHolder(itemRecycler.root) {
        fun binding(order: Order) {
            itemRecycler.tvOrderNumber.text = "Order number ${order.id.toString()}"
            val sumTotal = order.listItems.sumOf {
                it.total
            }
            itemRecycler.tvTotalOrder.text =
                "Total: ${NumberFormat.getCurrencyInstance().format(sumTotal)}"
            itemRecycler.tvTotalItems.text = "Total items: ${order.listItems.size}"
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
        holder.itemView.setOnClickListener {
            listener.goToDetailsOrder(getItem(position).id)
        }
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

interface OnClickListener {
    fun goToDetailsOrder(orderId: Int)
}