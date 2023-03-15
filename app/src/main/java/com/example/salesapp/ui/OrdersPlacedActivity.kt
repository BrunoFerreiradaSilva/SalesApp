package com.example.salesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.databinding.ActivityOrdersPlacedBinding
import com.example.salesapp.model.Mock

class OrdersPlacedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersPlacedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersPlacedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
    }

    private fun setupAdapter() {
        val ordersPlacedAdapter = OrdersPlacedAdapter()
        binding.rvOrdersPlaced.apply {
            adapter = ordersPlacedAdapter
            layoutManager = GridLayoutManager(this@OrdersPlacedActivity, 1)
            ordersPlacedAdapter.submitList(Mock.generateOrders())
        }
    }
}