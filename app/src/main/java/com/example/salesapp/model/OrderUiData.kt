package com.example.salesapp.model

data class OrderUiData(
    val products: List<Product> = emptyList(),
    val totalValueOrder: String = "",
    val showEmptyState: Boolean = true,
    val showSaveButton: Boolean = false,
    val productsTotalCount: String = ""

)