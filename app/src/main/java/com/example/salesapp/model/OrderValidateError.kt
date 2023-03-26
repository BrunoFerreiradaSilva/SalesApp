package com.example.salesapp.model

sealed class OrderValidateError {
    object NameClientError : OrderValidateError()
}