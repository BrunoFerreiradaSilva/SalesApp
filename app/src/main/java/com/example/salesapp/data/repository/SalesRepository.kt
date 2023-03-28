package com.example.salesapp.data.repository

import com.example.salesapp.model.OrderAndProduct
import com.example.salesapp.model.Product
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getAllOrders(): Flow<List<OrderAndProduct>>
    fun getProductsById(orderId: String):Flow<List<Product>?>
    suspend fun getProductId(orderId: Int):Product
    suspend fun saveOrder(nameClient: String, orderId: String)
    suspend fun getOrder(orderId: String): OrderAndProduct
    suspend fun deleteOrder(orderId: String)
    suspend fun insertProduct(product: Product)
    suspend fun saveUpdateProduct(product: Product)
    suspend fun deleteProducts(orderId: Int)
}