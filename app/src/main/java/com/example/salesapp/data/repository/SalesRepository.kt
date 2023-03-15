package com.example.salesapp.data.repository

import com.example.salesapp.helpers.DataState
import com.example.salesapp.model.Item
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
     fun getOrders():Flow<DataState<List<Order>>>
     suspend fun setOrder(order: Order):Flow<DataState<List<Order>>>
     suspend fun insertItem(listItem:List<Item>):Flow<DataState<List<Item>>>
}