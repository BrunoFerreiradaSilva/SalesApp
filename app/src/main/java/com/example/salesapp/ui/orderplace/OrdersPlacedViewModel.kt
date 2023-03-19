package com.example.salesapp.ui.orderplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.Order
import com.example.salesapp.model.OrderUi
import com.example.salesapp.util.formatForMoney
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class OrdersPlacedViewModel @Inject constructor(private val repository: SalesRepository) :
    ViewModel() {

    private val _uiState: MutableStateFlow<List<OrderUi>> =
        MutableStateFlow(emptyList())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllOrders().collect(::handleGetOrders)
        }
    }

    private fun handleGetOrders(listOrder: List<Order>) {
        val listOrderUi = listOrder.map { order ->
            val sum = order.products.sumOf { it.total }
            val orderTotal = sum.formatForMoney()
            val orderProductCount = order.products.size.toString()
            OrderUi(
                orderId = order.id,
                orderName = "Pedido numero ${order.id}",
                orderTotal = orderTotal,
                orderProductCount = orderProductCount
            )
        }
        _uiState.value = listOrderUi
    }
}