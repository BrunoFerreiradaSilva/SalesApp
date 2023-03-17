package com.example.salesapp.ui.orderplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.salesapp.databinding.ItemOrderRecyclerBinding
import com.example.salesapp.model.OrderUi

class OrdersPlacedAdapter(private val onOrderClicked: (orderId: Int) -> Unit) :
    ListAdapter<OrderUi, OrdersPlacedAdapter.ItemOrderViewHolder>(OrdersPlacedAdapter) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemOrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemOrderView = ItemOrderRecyclerBinding.inflate(layoutInflater)
        return ItemOrderViewHolder(itemOrderView)
    }

    override fun onBindViewHolder(holder: OrdersPlacedAdapter.ItemOrderViewHolder, position: Int) {
        holder.binding(getItem(position))
        holder.itemView.setOnClickListener {
            val orderId = getItem(position).orderId
            onOrderClicked(orderId)
        }
    }

    inner class ItemOrderViewHolder(private val itemRecycler: ItemOrderRecyclerBinding) :
        ViewHolder(itemRecycler.root) {
        fun binding(orderUi: OrderUi) {
            itemRecycler.tvOrderNumber.text = orderUi.orderName
            itemRecycler.tvTotalOrder.text = orderUi.orderTotal
            itemRecycler.tvTotalItems.text = orderUi.orderProductCount
        }
    }

    private companion object DiffUtilCallBack : DiffUtil.ItemCallback<OrderUi>() {
        override fun areItemsTheSame(oldItem: OrderUi, newItem: OrderUi): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(
            oldItem: OrderUi,
            newItem: OrderUi
        ): Boolean {
            return oldItem == newItem
        }
    }
}