package com.example.salesapp.model

import androidx.room.Embedded
import androidx.room.Relation


data class OrderAndProduct(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val products: List<Product>
)
