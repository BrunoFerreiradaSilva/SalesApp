package com.example.salesapp.data.repository

import com.example.salesapp.model.Order
import com.example.salesapp.model.OrderAndProduct
import com.example.salesapp.model.Product
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getAllOrders(): Flow<List<OrderAndProduct>>
    fun getProductById(orderId: String):Flow<Product>
    suspend fun saveOrder(nameClient: String, orderId: String)
    suspend fun getOrder(orderId: String): OrderAndProduct
    suspend fun deleteOrder(orderId: String)
    suspend fun insertProduct(product: Product)
}