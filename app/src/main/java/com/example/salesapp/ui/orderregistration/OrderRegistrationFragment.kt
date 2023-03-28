package com.example.salesapp.ui.orderregistration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.window.Dialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.R
import com.example.salesapp.databinding.FragmentOrderRegistrationBinding
import com.example.salesapp.model.OrderUiData
import com.example.salesapp.model.OrderValidateError
import com.example.salesapp.ui.insertproduct.InsertProductDialogFragment
import com.example.salesapp.util.gone
import com.example.salesapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrderRegistrationFragment : Fragment() {

    private lateinit var binding: FragmentOrderRegistrationBinding

    private val viewModel: OrderRegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentOrderRegistrationBinding.inflate(layoutInflater)

        val ordersRegistrationAdapter = OrdersRegistrationAdapter()

        binding.rvOrderRegistration.apply {
            adapter = ordersRegistrationAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { orderUiData ->
                handleOrderUIDataCollected(orderUiData, ordersRegistrationAdapter)

                binding.btnBack.setOnClickListener {
                    orderUiData.products.forEach {
                        viewModel.deleteProducts(it.id)
                    }
                    findNavController().navigateUp()
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val nameClient = binding.tieClientName.text.toString()
            val validationErrorNameClient =
                viewModel.validateErrorNameClient(binding.tieClientName.text.toString())

            if (validationErrorNameClient.isEmpty()) {
                viewModel.saveOrder(nameClient)
                findNavController().navigateUp()
            } else {
                validationErrorNameClient.forEach { validateErrorName ->
                    when (validateErrorName) {
                        OrderValidateError.NameClientError -> binding.tieClientName.error =
                            getString(R.string.error_name_client_is_empty)
                    }
                }
            }
        }

        binding.btnAddItem.setOnClickListener {
            insertProductDialog()
        }

        return binding.root
    }

    private fun insertProductDialog() {
        val dialogFragment = InsertProductDialogFragment(null, null)
        dialogFragment.show(parentFragmentManager, dialogFragment.tag)
    }

    private fun handleOrderUIDataCollected(
        orderUiData: OrderUiData,
        ordersRegistrationAdapter: OrdersRegistrationAdapter
    ) {
        orderUiData.apply {
            ordersRegistrationAdapter.submitList(products)
            binding.apply {
                cvButtons.isVisible = showSaveButton
                emptyList.clEmptyList.isVisible = showEmptyState
                emptyList.tvMessage.text = getString(R.string.message_no_item_add)
                tvResultTotalItems.text = productsTotalCount
                tvResultTotalValue.text = totalValueOrder
                tieClientName.setText(clientName)
                tvNumberOrder.text = getString(R.string.order_number, clientName)
            }
        }
    }
}