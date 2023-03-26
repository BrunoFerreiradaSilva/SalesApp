package com.example.salesapp.ui.orderregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.*
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


    fun saveOrder(nameClient:String) {
        viewModelScope.launch {
            val products = _uiState.value.products
            repository.saveOrder(products,nameClient)
        }
    }

    fun validateErrorNameClient(nameClient:String):List<OrderValidateError>{
        val errorList = mutableListOf<OrderValidateError>()
        if (nameClient.isBlank()) errorList.add(OrderValidateError.NameClientError)
        return errorList.toList()
    }



    fun getOrder(orderId: Int) {
        viewModelScope.launch {
            val order: Order = repository.getOrder(orderId)

            val totalValueOrder = order.products.sumOf { it.total }
            val totalValueOrderFormatForMoney = totalValueOrder.formatToBrazilianCurrency()
            val productsTotalCount = order.products.size

            val updateOrderUiData = OrderUiData(
                clientName = order.clientName,
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
        val totalPrice = productPrice * amount.toDouble()

        val product = Product(
            nameProduct = nameProduct,
            description = descriptionProduct,
            price = productPrice,
            amount = amount.toDouble(),
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

    fun insertClientName(client:String){

    }
}