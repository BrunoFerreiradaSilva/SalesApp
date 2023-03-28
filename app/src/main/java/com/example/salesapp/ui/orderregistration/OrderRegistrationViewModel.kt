package com.example.salesapp.ui.orderregistration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.*
import com.example.salesapp.ui.orderplace.INTENT_EXTRA_ORDER_ID
import com.example.salesapp.util.formatToBrazilianCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OrderRegistrationViewModel @Inject constructor(
    private val repository: SalesRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _uiState: MutableStateFlow<OrderUiData> = MutableStateFlow(OrderUiData())

    val uiState = _uiState.asStateFlow()
    private val orderId: String =
        savedStateHandle.get<String>(INTENT_EXTRA_ORDER_ID) ?: UUID.randomUUID().toString()

    init {
        getProductOrderById(orderId)
    }

    private fun getProductOrderById(orderId: String) {
        viewModelScope.launch {
            repository.getProductsById(orderId).collect { products ->
                products?.let {
                    updateOrderUiData(it)
                }
            }
        }
    }

    private fun updateOrderUiData(updateProductList: List<Product>) {
        val totalValueOrder = updateProductList.sumOf { it.total }
        val totalFormattedInBrazilianCurrency = totalValueOrder.formatToBrazilianCurrency()
        val showEmptyState = updateProductList.isEmpty()
        val showSaveButton = updateProductList.isNotEmpty()
        val productsTotalCount = updateProductList.size
        val nameClient = _uiState.value.clientName

        val orderUiData = OrderUiData(
            products = updateProductList,
            totalValueOrder = totalFormattedInBrazilianCurrency,
            showEmptyState = showEmptyState,
            showSaveButton = showSaveButton,
            productsTotalCount = "$productsTotalCount",
            clientName = nameClient
        )
        _uiState.value = orderUiData
    }


    fun saveOrder(nameClient: String) {
        viewModelScope.launch {
            repository.saveOrder(nameClient, orderId)
        }
    }

    fun validateErrorNameClient(nameClient: String): List<OrderValidateError> {
        val errorList = mutableListOf<OrderValidateError>()
        if (nameClient.isBlank()) errorList.add(OrderValidateError.NameClientError)
        return errorList.toList()
    }

    fun getOrderId(): String {
        return orderId
    }

    fun deleteProducts(orderId: Int){
        viewModelScope.launch {
            repository.deleteProducts(orderId)
        }
    }
}