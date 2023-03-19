package com.example.salesapp.ui.insertproduct

import androidx.lifecycle.ViewModel
import com.example.salesapp.model.ProductValidationError
import com.example.salesapp.util.removeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InsertProductViewModel @Inject constructor() : ViewModel() {

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
        if (amount.isNotEmpty() && amount.toInt() == 0)
            productValidationErrors.add(ProductValidationError.AmountIsZeroError)

        return productValidationErrors.toList()
    }

}