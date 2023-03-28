package com.example.salesapp.ui.orderregistration

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.R
import com.example.salesapp.databinding.ActivityOrderRegistrationBinding
import com.example.salesapp.model.OrderUiData
import com.example.salesapp.model.OrderValidateError
import com.example.salesapp.ui.insertproduct.InsertProductDialogFragment
import com.example.salesapp.util.gone
import com.example.salesapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrderRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderRegistrationBinding

    private val viewModel: OrderRegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ordersRegistrationAdapter = OrdersRegistrationAdapter(::isEditModeOn)

        binding.rvOrderRegistration.apply {
            adapter = ordersRegistrationAdapter
            layoutManager = GridLayoutManager(this@OrderRegistrationActivity, 1)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { orderUiData ->
                handleOrderUIDataCollected(orderUiData, ordersRegistrationAdapter)
            }
        }

        if (viewModel.isDetailsOrder()){
            binding.apply {
                btnSave.gone()
                btnAddItem.gone()
                btnDelete.visible()
                tieClientName.isEnabled = false
                btnDelete.setOnClickListener {
                    showDeleteOrderDialog(viewModel.getOrderId())
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val nameClient = binding.tieClientName.text.toString()
            val validationErrorNameClient =
                viewModel.validateErrorNameClient(binding.tieClientName.text.toString())

            if (validationErrorNameClient.isEmpty()) {
                viewModel.saveOrder(nameClient)
                finish()
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
            insertProductDialog(viewModel.getOrderId(), 0)
        }

    }

    private fun isEditModeOn(orderId: String, productId: Int) {
        insertProductDialog(orderId = orderId, productId)

    }

    private fun insertProductDialog(orderId: String, productId: Int) {
        val dialogFragment = InsertProductDialogFragment(orderId, productId)
        dialogFragment.show(supportFragmentManager, dialogFragment.tag)
    }

    private fun showDeleteOrderDialog(orderId: String) {
        val alertDialog = AlertDialog.Builder(this@OrderRegistrationActivity)
        alertDialog.setTitle(getString(R.string.title_delete_dialog))
        alertDialog.setPositiveButton(
            getString(R.string.positive_button_dialog)
        ) { _, _ ->
            viewModel.deleteOrder(orderId)
            finish()
        }
        alertDialog.setNegativeButton(getString(R.string.negative_button_dialog), null)
        alertDialog.show()
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