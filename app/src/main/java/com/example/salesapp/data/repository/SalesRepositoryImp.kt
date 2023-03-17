package com.example.salesapp.data.repository

import com.example.salesapp.data.database.OrderDAO
import com.example.salesapp.model.Product
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SalesRepositoryImp @Inject constructor(private val dao: OrderDAO) : SalesRepository {
    override fun getAllOrders(): Flow<List<Order>> = flow {
        dao.getAllOrders().collect{listOrder->
            emit(listOrder)
        }
    }

    override fun insertItem(listItem: List<Product>): Flow<List<Product>> = flow {
        emit(listItem)
    }

    override suspend fun saveOrder(listItem: List<Product>){
        val orderList = Order(products = listItem)
        dao.insertOrder(orderList)
    }

    override fun getOrder(orderId: Int):Flow<Order> = flow{
        emit(dao.getOrder(orderId))
    }

}