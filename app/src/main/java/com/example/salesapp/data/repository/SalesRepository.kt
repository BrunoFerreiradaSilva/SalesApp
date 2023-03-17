package com.example.salesapp.data.repository

import com.example.salesapp.model.Item
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getAllOrders(): Flow<List<Order>>
    fun insertItem(listItem: List<Item>): Flow<List<Item>>
    suspend fun saveOrder(listItem: List<Item>)

    fun getOrder(orderId: Int): Flow<Order>

}