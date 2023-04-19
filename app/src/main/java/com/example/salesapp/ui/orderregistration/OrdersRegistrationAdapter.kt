package com.example.salesapp.ui.orderregistration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.salesapp.databinding.ItemRegistrationOrderBinding
import com.example.salesapp.model.Product
import com.example.salesapp.util.formatToBrazilianCurrency

class OrdersRegistrationAdapter : ListAdapter<Product, ViewHolder>(OrdersRegistrationAdapter) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemOrderView = ItemRegistrationOrderBinding.inflate(layoutInflater)
        return ItemRecycler(itemOrderView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as ItemRecycler).binding(product)
    }

    inner class ItemRecycler(private val itemRecycler: ItemRegistrationOrderBinding) :
        ViewHolder(itemRecycler.root) {
        fun binding(item: Product) {
            itemRecycler.apply {
                tvResultNameClient.text = item.nameProduct
                tvResultDescriptionClient.text = item.description
                tvAmountProduct.text = "${item.amount}"
                tvPriceUn.text = item.price.formatToBrazilianCurrency()
                tvTotalValue.text = item.total.formatToBrazilianCurrency()
            }
        }
    }

    private companion object DiffUtilCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
}