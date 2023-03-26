package com.example.salesapp.data.repository

import com.example.salesapp.model.Order
import com.example.salesapp.model.Product
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getAllOrders(): Flow<List<Order>>
    suspend fun saveOrder(listItem: List<Product>,nameClient:String)
    suspend fun getOrder(orderId: Int): Order
    suspend fun deleteOrder(orderId: Int)
}