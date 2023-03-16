package com.example.salesapp.data.repository

import com.example.salesapp.data.database.OrderDAO
import com.example.salesapp.helpers.DataState
import com.example.salesapp.model.Item
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SalesRepositoryImp @Inject constructor(private val dao: OrderDAO) : SalesRepository {
    override fun getOrders(): Flow<DataState<List<Order>>> = flow {
        val geAllOrder = dao.getAllOrders()
        emit(DataState.Data(geAllOrder))
    }

    override fun insertItem(listItem: List<Item>): Flow<DataState<List<Item>>> = flow {
        emit(DataState.Data(data = listItem))
    }

    override suspend fun saveOrder(listItem: List<Item>){
        val orderList = Order(listItems = listItem)
        dao.insertOrder(orderList)
    }

}