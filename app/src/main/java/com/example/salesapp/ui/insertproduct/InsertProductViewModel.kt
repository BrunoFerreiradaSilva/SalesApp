package com.example.salesapp.ui.insertproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.Product
import com.example.salesapp.model.ProductUi
import com.example.salesapp.model.ProductValidationError
import com.example.salesapp.util.removeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertProductViewModel @Inject constructor(private val repository: SalesRepository) :
    ViewModel() {
    private val _uiState: MutableStateFlow<ProductUi> = MutableStateFlow(ProductUi())

    val uiState = _uiState.asStateFlow()

    private lateinit var temporaryOrderId: String

    fun validateFields(
        nameProduct: String, descriptionProduct: String, price: String, amount: String
    ): List<ProductValidationError> {
        val productValidationErrors = mutableListOf<ProductValidationError>()

        if (nameProduct.isBlank()) productValidationErrors.add(ProductValidationError.EmptyProductNameError)
        if (descriptionProduct.isBlank()) productValidationErrors.add(ProductValidationError.EmptyProductDescription)
        if (price.isEmpty()) productValidationErrors.add(ProductValidationError.EmptyProductPrice)
        if (amount.isEmpty()) productValidationErrors.add(ProductValidationError.EmptyProductAmount)
        if (price.removeFormatter().isNotEmpty() && price.removeFormatter().toDouble() == 0.0)
            productValidationErrors.add(ProductValidationError.PriceIsZeroError)
        if (amount.isNotEmpty() && amount.toDouble() == 0.0)
            productValidationErrors.add(ProductValidationError.AmountIsZeroError)

        return productValidationErrors.toList()
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
            orderId = temporaryOrderId,
            nameProduct = nameProduct,
            description = descriptionProduct,
            price = productPrice,
            amount = amount.toDouble(),
            total = totalPrice
        )
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun getIdOrder(orderId: String): String {
        temporaryOrderId = orderId
        return temporaryOrderId
    }

    fun updateProduct(orderId: Int) {
        viewModelScope.launch {
            val product = repository.getProductId(orderId)

            val updateProduct = ProductUi(
                nameProduct = product.nameProduct,
                description = product.description,
                price = product.price.toString(),
                amount = product.amount.toString(),
                total = product.total.toString()
            )
            _uiState.value = updateProduct
        }
    }

    fun saveUpdate(
        orderId: String,
        productId: Int,
        nameProduct: String,
        descriptionProduct: String,
        price: String,
        amount: String
    ) {
        val productPrice = price.removeFormatter().toDouble()
        val totalPrice = productPrice * amount.toDouble()

        val product = Product(
            id = productId,
            orderId = orderId,
            nameProduct = nameProduct,
            description = descriptionProduct,
            price = productPrice,
            amount = amount.toDouble(),
            total = totalPrice
        )
        viewModelScope.launch {
            repository.saveUpdateProduct(product)
        }

    }
}