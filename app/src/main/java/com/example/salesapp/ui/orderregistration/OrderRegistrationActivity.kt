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
import com.example.salesapp.model.ProductUi
import com.example.salesapp.ui.insertproduct.InsertProductDialogFragment
import com.example.salesapp.ui.orderplace.INTENT_EXTRA_INVALID_DEFAULT_ORDER_ID
import com.example.salesapp.ui.orderplace.INTENT_EXTRA_ORDER_ID
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

        val orderId = intent.getIntExtra(INTENT_EXTRA_ORDER_ID, INTENT_EXTRA_INVALID_DEFAULT_ORDER_ID)

        val ordersRegistrationAdapter = OrdersRegistrationAdapter()

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

        if (orderId != INTENT_EXTRA_INVALID_DEFAULT_ORDER_ID) {
            viewModel.getOrder(orderId)
            binding.apply {
                btnSave.gone()
                btnAddItem.gone()
                tvNumberOrder.text = getString(R.string.order_number, orderId.toString())
                btnDelete.visible()
                tieClientName.isEnabled = false
                btnDelete.setOnClickListener {
                    showDeleteOrderDialog(orderId)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.insertClientName(binding.tieClientName.text.toString())
            val nameClient = binding.tieClientName.text.toString()
            val validationErrorNameClient = viewModel.validateErrorNameClient(binding.tieClientName.text.toString())

            if (validationErrorNameClient.isEmpty()){
                viewModel.saveOrder(nameClient)
                finish()
            }else{
                validationErrorNameClient.forEach {validateErrorName->
                    when(validateErrorName){
                        OrderValidateError.NameClientError -> binding.tieClientName.error = getString(R.string.error_name_client_is_empty)
                    }
                }
            }
        }

        binding.btnAddItem.setOnClickListener {
            val dialogFragment = InsertProductDialogFragment { product ->
                insertProduct(product)
            }
            dialogFragment.show(supportFragmentManager, dialogFragment.tag)
        }
    }

    private fun showDeleteOrderDialog(orderId: Int) {
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

    private fun insertProduct(product: ProductUi) {
        viewModel.insertProduct(
            product.nameProduct,
            product.description,
            product.price,
            product.amount
        )
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
            }
        }
    }
}