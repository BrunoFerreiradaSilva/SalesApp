package com.example.salesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderId: String,
    val nameProduct: String,
    val description: String,
    val price: Double,
    val amount: Double,
    val total: Double
)
