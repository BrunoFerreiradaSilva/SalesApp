package com.example.salesapp.model

sealed class ProductValidationError{
    object EmptyProductNameError: ProductValidationError()
    object EmptyProductDescription: ProductValidationError()
    object EmptyProductPrice: ProductValidationError()
    object EmptyProductAmount: ProductValidationError()
    object PriceIsZeroError: ProductValidationError()
    object AmountIsZeroError: ProductValidationError()
}
