package com.example.salesapp.ui.orderregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.Order
import com.example.salesapp.model.OrderUiData
import com.example.salesapp.model.Product
import com.example.salesapp.util.formatToBrazilianCurrency
import com.example.salesapp.util.removeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderRegistrationViewModel @Inject constructor(private val repository: SalesRepository) :
    ViewModel() {

    private val _uiState: MutableStateFlow<OrderUiData> = MutableStateFlow(OrderUiData())

    val uiState = _uiState.asStateFlow()


    fun saveOrder() {
        viewModelScope.launch {
            val products = _uiState.value.products
            repository.saveOrder(products)
        }
    }

    fun getOrder(orderId: Int) {
        viewModelScope.launch {
            val order: Order = repository.getOrder(orderId)

            val totalValueOrder = order.products.sumOf { it.total }
            val totalValueOrderFormatForMoney = totalValueOrder.formatToBrazilianCurrency()
            val productsTotalCount = order.products.size

            val updateOrderUiData = OrderUiData(
                products = order.products,
                totalValueOrder = totalValueOrderFormatForMoney,
                showEmptyState = false,
                showSaveButton = false,
                productsTotalCount = "$productsTotalCount"
            )

            _uiState.value = updateOrderUiData
        }
    }

    fun deleteOrder(orderId: Int) {
        viewModelScope.launch {
            repository.deleteOrder(orderId)
        }
    }

    fun insertProduct(
        nameProduct: String,
        descriptionProduct: String,
        price: String,
        amount: String
    ) {

        val productPrice = price.removeFormatter().toDouble()
        val totalPrice = productPrice * amount.toInt()

        val product = Product(
            nameProduct = nameProduct,
            description = descriptionProduct,
            price = productPrice,
            amount = amount.toInt(),
            total = totalPrice
        )

        val updateOrderUiData = updateOrderUIData(product)
        _uiState.value = updateOrderUiData
    }

    private fun updateOrderUIData(product: Product): OrderUiData {
        val updateProductList = mutableListOf<Product>()
        val currentProductList: List<Product> = _uiState.value.products

        updateProductList.addAll(currentProductList)
        updateProductList.add(product)

        val totalValueOrder = updateProductList.sumOf { it.total }
        val totalFormattedInBrazilianCurrency = totalValueOrder.formatToBrazilianCurrency()
        val showEmptyState = updateProductList.isEmpty()
        val showSaveButton = updateProductList.isNotEmpty()
        val productsTotalCount = updateProductList.size

        return OrderUiData(
            products = updateProductList,
            totalValueOrder = totalFormattedInBrazilianCurrency,
            showEmptyState = showEmptyState,
            showSaveButton = showSaveButton,
            productsTotalCount = "$productsTotalCount"
        )
    }
}