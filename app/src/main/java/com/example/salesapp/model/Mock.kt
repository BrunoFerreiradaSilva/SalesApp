package com.example.salesapp.model

object Mock {
    private var count = 0
    private val listItems = listOf(
        Item(name = "banana", description = "is a fruit", price = 5, amount = 10),
        Item(name = "apple", description = "is a fruit", price = 2, amount = 10),
        Item(name = "orange", description = "is a fruit", price = 4, amount = 10),
        Item(name = "pie", description = "is a food", price = 7, amount = 10),
        Item(name = "hammer", description = "is a object", price = 15, amount = 10),
    )


     fun generateOrders(): List<Order> {
        listItems.forEach {
            count += it.amount
        }
        return listOf(
            Order(id = 1, listItems = listItems, total = count),
            Order(id = 2, listItems = listItems, total = count)
        )
    }
}