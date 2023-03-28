package com.example.salesapp.ui.orderdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.OrderUiData
import com.example.salesapp.model.OrderValidateError
import com.example.salesapp.model.Product
import com.example.salesapp.ui.orderplace.INTENT_EXTRA_ORDER_ID
import com.example.salesapp.util.formatToBrazilianCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val repository: SalesRepository) :
    ViewModel() {
    private val _uiState: MutableStateFlow<OrderUiData> = MutableStateFlow(OrderUiData())

    val uiState = _uiState.asStateFlow()


    fun getProductOrderById(orderId: String) {
        viewModelScope.launch {
            repository.getProductsById(orderId).collect { products ->
                products?.let {
                    updateOrderUiData(it)
                }
            }
        }
    }

    private fun updateOrderUiData(updateProductList: List<Product>) {
        viewModelScope.launch {
            val totalValueOrder = updateProductList.sumOf { it.total }
            val totalFormattedInBrazilianCurrency = totalValueOrder.formatToBrazilianCurrency()
            val showEmptyState = updateProductList.isEmpty()
            val showSaveButton = updateProductList.isNotEmpty()
            val productsTotalCount = updateProductList.size
            val orderId = updateProductList.first().orderId

            val orderUiData = OrderUiData(
                products = updateProductList,
                totalValueOrder = totalFormattedInBrazilianCurrency,
                showEmptyState = showEmptyState,
                showSaveButton = showSaveButton,
                productsTotalCount = "$productsTotalCount",
                clientName = repository.getOrder(orderId).order.clientName
            )
            _uiState.value = orderUiData
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            repository.deleteOrder(orderId)
        }
    }

}