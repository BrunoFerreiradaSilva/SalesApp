package com.example.salesapp.data.repository

import com.example.salesapp.data.database.OrderDAO
import com.example.salesapp.model.Order
import com.example.salesapp.model.OrderAndProduct
import com.example.salesapp.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SalesRepositoryImp @Inject constructor(private val dao: OrderDAO) : SalesRepository {

    override fun getAllOrders(): Flow<List<OrderAndProduct>> = flow {
        dao.getAllOrders().collect { listOrder ->
            emit(listOrder)
        }
    }

    override fun getProductById(orderId: String): Flow<Product> = flow{
        dao.getProductsById(orderId).collect{product ->
            emit(product)
        }
    }

    override suspend fun saveOrder(nameClient: String, orderId: String) {
        val orderList = Order(clientName = nameClient, orderId = orderId)

        dao.insertOrder(orderList)
    }

    override suspend fun getOrder(orderId: String): OrderAndProduct {
        return dao.getOrder(orderId)
    }

    override suspend fun deleteOrder(orderId: String) {
        dao.deleteOrder(orderId)
    }

    override suspend fun insertProduct(product: Product) {
        dao.insertProduct(product)
    }

}