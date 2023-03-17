package com.example.salesapp.ui.orderplace

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.databinding.ActivityOrdersPlacedBinding
import com.example.salesapp.ui.orderregistration.OrderRegistrationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrdersPlacedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersPlacedBinding
    private val viewModel: OrdersPlacedViewModel by viewModels()
    private val ordersPlacedAdapter = OrdersPlacedAdapter(goToDetailsOfOrder())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersPlacedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setAnimation()
        setupAdapter(ordersPlacedAdapter)

        lifecycleScope.launch {
            viewModel.uiState.collect { listOrder ->
                ordersPlacedAdapter.submitList(listOrder)
                binding.emptyList.clEmptyList.isVisible = listOrder.isEmpty()
            }
        }



        binding.fbCreatedOrder.setOnClickListener {
            val intent = Intent(this, OrderRegistrationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                com.google.android.material.R.anim.m3_side_sheet_slide_in,
                com.google.android.material.R.anim.m3_side_sheet_slide_out)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateList()
    }

    private fun setupAdapter(ordersPlacedAdapter: OrdersPlacedAdapter) {

        binding.rvOrdersPlaced.apply {
            adapter = ordersPlacedAdapter
            layoutManager = GridLayoutManager(this@OrdersPlacedActivity, 1)
        }
    }

    private fun goToDetailsOfOrder():OnClickListener{
        return object :OnClickListener{
            override fun goToDetailsOrder(orderId: Int) {
               val intent = Intent(this@OrdersPlacedActivity,OrderRegistrationActivity::class.java)
               intent.putExtra("idOrder", orderId)
               startActivity(intent)
            }
        }
    }
}