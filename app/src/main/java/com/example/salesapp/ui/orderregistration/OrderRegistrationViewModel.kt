package com.example.salesapp.ui.orderregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.*
import com.example.salesapp.util.formatToBrazilianCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OrderRegistrationViewModel @Inject constructor(private val repository: SalesRepository) :
    ViewModel() {

    private val _uiState: MutableStateFlow<OrderUiData> = MutableStateFlow(OrderUiData())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getProductById(temporaryOrderId).collect { product ->
                product?.let {
                    updateProductList(it)
                }
            }
        }
    }

    private fun updateProductList(product: Product) {
        val updateProductList = mutableListOf<Product>()
        val currentProductList: List<Product> = _uiState.value.products

        updateProductList.addAll(currentProductList)
        updateProductList.add(product)

        updateOrderUiData(updateProductList)

    }

    private fun updateOrderUiData(updateProductList: MutableList<Product>) {
        val totalValueOrder = updateProductList.sumOf { it.total }
        val totalFormattedInBrazilianCurrency = totalValueOrder.formatToBrazilianCurrency()
        val showEmptyState = updateProductList.isEmpty()
        val showSaveButton = updateProductList.isNotEmpty()
        val productsTotalCount = updateProductList.size

        val orderUiData = OrderUiData(
            products = updateProductList,
            totalValueOrder = totalFormattedInBrazilianCurrency,
            showEmptyState = showEmptyState,
            showSaveButton = showSaveButton,
            productsTotalCount = "$productsTotalCount"
        )
        _uiState.value = orderUiData
    }


    private val temporaryOrderId = UUID.randomUUID().toString()
    fun saveOrder(nameClient: String) {
        viewModelScope.launch {
            repository.saveOrder(nameClient, temporaryOrderId)
        }
    }

    fun validateErrorNameClient(nameClient: String): List<OrderValidateError> {
        val errorList = mutableListOf<OrderValidateError>()
        if (nameClient.isBlank()) errorList.add(OrderValidateError.NameClientError)
        return errorList.toList()
    }


    fun getOrder(orderId: String) {
        viewModelScope.launch {
            val order: OrderAndProduct = repository.getOrder(orderId)

            val totalValueOrder = order.products.sumOf { it.total }
            val totalValueOrderFormatForMoney = totalValueOrder.formatToBrazilianCurrency()
            val productsTotalCount = order.products.size

            val updateOrderUiData = OrderUiData(
                clientName = order.order.clientName,
                products = order.products,
                totalValueOrder = totalValueOrderFormatForMoney,
                showEmptyState = false,
                showSaveButton = false,
                productsTotalCount = "$productsTotalCount"
            )

            _uiState.value = updateOrderUiData
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            repository.deleteOrder(orderId)
        }
    }

    fun getOrderId(): String {
        return temporaryOrderId
    }

}