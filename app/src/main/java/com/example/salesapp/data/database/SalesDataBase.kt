package com.example.salesapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.salesapp.data.database.converter.Converters
import com.example.salesapp.model.Order

@Database(entities = [Order::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SalesDataBase  : RoomDatabase() {

    abstract fun dao(): OrderDAO

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
                    "sales_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}