package com.example.salesapp.ui.orderplace

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.databinding.ActivityOrdersPlacedBinding
import com.example.salesapp.ui.orderregistration.OrderRegistrationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersPlacedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersPlacedBinding
    private val viewModel: OrdersPlacedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersPlacedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()

        binding.fbCreatedOrder.setOnClickListener {
            startActivity(Intent(this, OrderRegistrationActivity::class.java))
        }
    }

    private fun setupAdapter() {
        val ordersPlacedAdapter = OrdersPlacedAdapter()
        binding.rvOrdersPlaced.apply {
            adapter = ordersPlacedAdapter
            layoutManager = GridLayoutManager(this@OrdersPlacedActivity, 1)

        }
    }
}