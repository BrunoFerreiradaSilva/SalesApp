package com.example.salesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_order")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val listItems: List<Item>,
    val total:Int
)
