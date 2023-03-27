package com.example.salesapp.data.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.salesapp.data.database.converter.Converters
import com.example.salesapp.model.Order
import com.example.salesapp.model.Product


private const val DB_NAME = "sales_database"

@Database(
    entities = [Order::class, Product::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class SalesDataBase : RoomDatabase() {

    abstract fun orderDAO(): OrderDAO

    companion object {
        @Volatile
        private var INSTANCE: SalesDataBase? = null

        fun getDatabase(
            context: Context,
        ): SalesDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SalesDataBase::class.java,
                    DB_NAME
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}