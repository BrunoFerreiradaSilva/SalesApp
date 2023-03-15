package com.example.salesapp.ui.orderregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.helpers.DataState
import com.example.salesapp.model.Item
import com.example.salesapp.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderRegistrationViewModel @Inject constructor(private val repository: SalesRepository) :
    ViewModel() {


    private val _uiState: MutableStateFlow<List<Item>> =
        MutableStateFlow(emptyList())

    val uiState = _uiState.asStateFlow()
    private val listItem = mutableListOf<Item>()
    private fun handleGetOrder(state: DataState<List<Item>>) {
        when (state) {
            is DataState.Data -> {
                _uiState.value = state.data
            }
            else -> {}
        }
    }

    fun verifyFields(
        nameProduct: String,
        descriptionProduct: String,
        price: String,
        amount: String
    ): Boolean {
        return nameProduct.isNotEmpty() &&
                descriptionProduct.isNotEmpty() &&
                price.isNotEmpty() &&
                amount.isNotEmpty()
    }

    fun insertProduct(
        nameProduct: String,
        descriptionProduct: String,
        price: String,
        amount: String
    ) {
        viewModelScope.launch {
            val item = Item(
                nameProduct = nameProduct,
                description = descriptionProduct,
                price = price,
                amount = amount.toInt()
            )
            listItem.add(item)
            repository.insertItem(listItem).collect(::handleGetOrder)
        }
    }
}