package com.example.salesapp.ui.orderplace

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.R
import com.example.salesapp.databinding.FragmentOrdersPlacedBinding
import com.example.salesapp.ui.orderregistration.OrderRegistrationFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


const val INTENT_EXTRA_ORDER_ID = "idOrder"

@AndroidEntryPoint
class OrdersPlacedActivity : Fragment() {

    private lateinit var binding: FragmentOrdersPlacedBinding

    private val viewModel: OrdersPlacedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentOrdersPlacedBinding.inflate(layoutInflater)

        val ordersPlacedAdapter = OrdersPlacedAdapter(::openOrderDetailScreen)

        setupAdapter(ordersPlacedAdapter)

        lifecycleScope.launch {
            viewModel.uiState.collect { listOrder ->
                ordersPlacedAdapter.submitList(listOrder)
                binding.emptyList.clEmptyList.isVisible = listOrder.isEmpty()
            }
        }

        binding.fbCreatedOrder.setOnClickListener {
            val action = R.id.action_navigation_orders_place_to_navigation_registration_order
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun setupAdapter(ordersPlacedAdapter: OrdersPlacedAdapter) {
        binding.rvOrdersPlaced.apply {
            adapter = ordersPlacedAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }
    }

    private fun openOrderDetailScreen(orderId: String) {
        val action = OrdersPlacedActivityDirections.actionNavigationOrdersPlaceToNavigationDetailsOrder(orderId)
        findNavController().navigate(action)
    }
}