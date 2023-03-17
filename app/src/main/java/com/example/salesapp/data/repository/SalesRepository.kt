package com.example.salesapp.data.repository

import com.example.salesapp.model.Product
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getAllOrders(): Flow<List<Order>>
    suspend fun saveOrder(listItem: List<Product>)
    suspend fun getOrder(orderId: Int): Order

}