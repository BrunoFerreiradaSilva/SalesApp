package com.example.salesapp.ui.orderdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.salesapp.databinding.ItemRegistrationOrderBinding
import com.example.salesapp.model.Product
import com.example.salesapp.util.formatToBrazilianCurrency
import com.example.salesapp.util.visible

class OrdersDetailsAdapter(private val onEditClicked: (orderId:String?, productId:Int?) -> Unit) : ListAdapter<Product, ViewHolder>(OrdersDetailsAdapter) {

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
                itemRecycler.btnEdit.visible()
                itemRecycler.btnEdit.setOnClickListener {
                    onEditClicked(item.orderId, item.id)
                }
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