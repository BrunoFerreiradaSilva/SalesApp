package com.example.salesapp.data.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.salesapp.model.Order
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface OrderDAO {
    @Query("SELECT * from table_order ORDER BY id")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM table_order WHERE id = :orderId")
    suspend fun getOrder(orderId: Int): Order

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("DELETE FROM table_order WHERE id = :orderId")
    suspend fun deleteOrder(orderId: Int)
}