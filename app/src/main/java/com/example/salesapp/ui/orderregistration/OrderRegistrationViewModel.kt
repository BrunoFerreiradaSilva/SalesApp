package com.example.salesapp.ui.orderregistration

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.Product
import com.example.salesapp.model.Order
import com.example.salesapp.util.removeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
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

    private val _uiState: MutableStateFlow<List<Product>> =
        MutableStateFlow(emptyList())

    val uiState = _uiState.asStateFlow()
    private val listItem = mutableListOf<Product>()
    private fun handleGetOrder(listItems: List<Product>) {
        _uiState.value = listItems
    }

    fun verifyFields(
        nameProduct: EditText,
        descriptionProduct: EditText,
        price: EditText,
        amount: EditText
    ): Boolean {
        return if (nameProduct.text.isEmpty()) {
            nameProduct.error = "Digite o nome do produto"
            false
        } else if (descriptionProduct.text.isEmpty()) {
            descriptionProduct.error = "Digite a descrição do produto"
            false
        } else if (price.text.isEmpty()) {
            price.error = "Digite o valor do produto"
            false
        } else if (amount.text.isEmpty()) {
            amount.error = "Digite a quantidade do produto"
            false
        } else if (price.text.toString().removeFormatter().toDouble() == 0.0) {
            price.error = "Valor tem que ser maior que 0"
            false
        } else if (amount.text.toString().toInt() == 0) {
            amount.error = "Quantidade deve ser maior que 0"
            false
        } else {
            true
        }
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
            val item = Product(
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

            _priceValue.postValue("${NumberFormat.getCurrencyInstance().format(sumTotal)}")
            _amountValue.postValue(listItem.size.toString())
            repository.insertItem(listItem).collect(::handleGetOrder)
        }
    }

    fun saveOrder() {
        viewModelScope.launch {
            repository.saveOrder(listItem)
        }
    }

    fun getOrders(orderId: Int) {
        viewModelScope.launch {
            repository.getOrder(orderId).collect(::getOrder)
        }
    }

    private fun getOrder(order: Order) {
        val listSaved = order.products
        _uiState.value = listSaved

        val sumTotal = listSaved.sumOf {
            it.total
        }
        _priceValue.postValue("${NumberFormat.getCurrencyInstance().format(sumTotal)}")
        _amountValue.postValue(listSaved.size.toString())
    }
}