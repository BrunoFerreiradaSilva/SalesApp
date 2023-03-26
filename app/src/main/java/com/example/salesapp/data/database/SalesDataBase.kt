package com.example.salesapp.data.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.salesapp.data.database.converter.Converters
import com.example.salesapp.model.Order


private const val DB_NAME = "sales_database"

@Database(
    entities = [Order::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2, spec = SalesDataBase.Migration1to2::class)]
)
@TypeConverters(Converters::class)
abstract class SalesDataBase : RoomDatabase() {

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
                    DB_NAME
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


    @DeleteColumn(
        tableName = "table_order",
        columnName = "name_client"
    )
    class Migration1to2 : AutoMigrationSpec

}