package com.example.salesapp.data.repository

import com.example.salesapp.model.Product
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getAllOrders(): Flow<List<Order>>
    fun insertItem(listItem: List<Product>): Flow<List<Product>>
    suspend fun saveOrder(listItem: List<Product>)

    fun getOrder(orderId: Int): Flow<Order>

}