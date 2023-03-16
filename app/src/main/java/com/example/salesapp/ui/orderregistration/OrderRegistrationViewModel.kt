package com.example.salesapp.ui.orderregistration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.helpers.DataState
import com.example.salesapp.model.Item
import com.example.salesapp.util.formatForTwoDecimalPlaces
import com.example.salesapp.util.removeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderRegistrationViewModel @Inject constructor(private val repository: SalesRepository) :
    ViewModel() {

    val amountValue: LiveData<String>
        get() = _amountValue
    private val _amountValue = MutableLiveData<String>()

    val priceValue: LiveData<String>
        get() = _priceValue
    private val _priceValue = MutableLiveData<String>()

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
        val replaceDot = price.removeFormatter()
        val priceForDouble = replaceDot.toDouble()




        viewModelScope.launch {
            val item = Item(
                nameProduct = nameProduct,
                description = descriptionProduct,
                price = priceForDouble,
                amount = amount.toInt(),
                total = priceForDouble * amount.toInt()
            )
            listItem.add(item)

            val sumTotal = listItem.sumOf {
                it.total
            }

            _priceValue.postValue("R$ ${sumTotal.formatForTwoDecimalPlaces()}")
            _amountValue.postValue(listItem.size.toString())
            repository.insertItem(listItem).collect(::handleGetOrder)
        }
    }

    fun saveOrder() {
        viewModelScope.launch {
            repository.saveOrder(listItem)
        }
    }
}