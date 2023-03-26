package com.example.salesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "table_order")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val products: List<Product>,
    @ColumnInfo(name = "client_name", defaultValue = "")
    val clientName: String,
)