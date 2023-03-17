package com.example.salesapp.ui.orderplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersPlacedViewModel @Inject constructor(private val repository: SalesRepository) :
    ViewModel() {

    private val _uiState: MutableStateFlow<List<Order>> =
        MutableStateFlow(emptyList())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllOrders().collect(::handleGetOrders)
        }
    }

    private fun handleGetOrders(listOrder: List<Order>) {
        _uiState.value = listOrder
    }

    fun updateList() {
        viewModelScope.launch {
            repository.getAllOrders().collect(::handleGetOrders)
        }
    }

}