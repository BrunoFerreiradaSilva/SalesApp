package com.example.salesapp.data.database


import androidx.room.*
import com.example.salesapp.model.Order
import com.example.salesapp.model.OrderAndProduct
import com.example.salesapp.model.Product
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface OrderDAO {
    @Transaction
    @Query("SELECT * from `order`")
    fun getAllOrders(): Flow<List<OrderAndProduct>>

    @Query("SELECT * FROM `product` WHERE orderId = :orderId")
    fun getProductsById(orderId: String) : Flow<List<Product>>

    @Query("SELECT * FROM `product` WHERE id = :productId")
    suspend fun getProductId(productId:Int) : Product

    @Transaction
    @Query("SELECT * FROM `order` WHERE orderId = :orderId")
    suspend fun getOrder(orderId: String): OrderAndProduct

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("DELETE FROM `order` WHERE orderId = :orderId")
    suspend fun deleteOrder(orderId: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduct(product: Product)
}