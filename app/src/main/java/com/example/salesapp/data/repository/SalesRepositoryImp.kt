package com.example.salesapp.data.repository

import com.example.salesapp.data.database.OrderDAO
import com.example.salesapp.model.Item
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SalesRepositoryImp @Inject constructor(private val dao: OrderDAO) : SalesRepository {
    override fun getAllOrders(): Flow<List<Order>> = flow {
        val geAllOrder = dao.getAllOrders()
        emit(geAllOrder)
    }

    override fun insertItem(listItem: List<Item>): Flow<List<Item>> = flow {
        emit(listItem)
    }

    override suspend fun saveOrder(listItem: List<Item>){
        val orderList = Order(listItems = listItem)
        dao.insertOrder(orderList)
    }

    override fun getOrder(orderId: Int):Flow<Order> = flow{
        emit(dao.getOrder(orderId))
    }

}