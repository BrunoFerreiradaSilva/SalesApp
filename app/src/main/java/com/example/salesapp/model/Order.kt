package com.example.salesapp.model

data class Order(
    val id:Int,
    val listItems: List<Item>,
    val total:Int
)
