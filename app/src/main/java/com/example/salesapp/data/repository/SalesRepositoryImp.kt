package com.example.salesapp.data.repository

import com.example.salesapp.data.database.OrderDAO
import com.example.salesapp.model.Order
import com.example.salesapp.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SalesRepositoryImp @Inject constructor(private val dao: OrderDAO) : SalesRepository {
    override fun getAllOrders(): Flow<List<Order>> = flow {
        dao.getAllOrders().collect { listOrder ->
            emit(listOrder)
        }
    }

    override suspend fun saveOrder(listItem: List<Product>) {
        val orderList = Order(products = listItem)
        dao.insertOrder(orderList)
    }

    override suspend fun getOrder(orderId: Int): Order{
        return dao.getOrder(orderId)
    }

}