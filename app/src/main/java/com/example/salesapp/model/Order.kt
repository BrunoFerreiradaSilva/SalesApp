package com.example.salesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
    @PrimaryKey
    val orderId: String,
    val clientName: String,
)