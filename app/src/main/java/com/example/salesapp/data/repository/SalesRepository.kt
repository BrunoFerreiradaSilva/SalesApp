package com.example.salesapp.data.repository

import com.example.salesapp.helpers.DataState
import com.example.salesapp.model.Item
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getAllOrders(): Flow<DataState<List<Order>>>
    fun insertItem(listItem: List<Item>): Flow<DataState<List<Item>>>
    suspend fun saveOrder(listItem: List<Item>)

     fun getOrder(orderId:Int):Flow<DataState<Order>>

}