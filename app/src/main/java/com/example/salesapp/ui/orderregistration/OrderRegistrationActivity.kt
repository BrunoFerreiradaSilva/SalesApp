package com.example.salesapp.ui.orderregistration

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.R
import com.example.salesapp.databinding.ActivityOrderRegistrationBinding
import com.example.salesapp.databinding.LayoutIncludeProductBinding
import com.example.salesapp.model.OrderUiData
import com.example.salesapp.ui.orderplace.INTENT_EXTRA_ORDER_ID
import com.example.salesapp.util.addCurrencyFormatter
import com.example.salesapp.util.gone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

        val orderId = intent.getIntExtra(INTENT_EXTRA_ORDER_ID, 0)

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

        if (orderId != 0) {
            viewModel.getOrder(orderId)
            binding.apply {
                btnSave.gone()
                btnAddItem.gone()
                tvNumberOrder.text = getString(R.string.order_number, orderId.toString())
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveOrder()
            finish()
        }

        binding.btnAddItem.setOnClickListener {
            setupBottomDialog()
        }

    }

    private fun handleOrderUIDataCollected(
        orderUiData: OrderUiData,
        ordersRegistrationAdapter: OrdersRegistrationAdapter
    ) {
        orderUiData.apply {
            ordersRegistrationAdapter.submitList(products)

            binding.cvButtons.isVisible = showSaveButton
            binding.emptyList.clEmptyList.isVisible = showEmptyState
            binding.emptyList.tvMessage.text = getString(R.string.message_no_item_add)
            binding.tvResultTotalItems.text = productsTotalCount
            binding.tvResultTotalValue.text = totalValueOrder
        }
    }

    private fun setupBottomDialog() {
        val bottomSheet = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        val bindingProduct = LayoutIncludeProductBinding.inflate(layoutInflater)
        bottomSheet.setContentView(bindingProduct.root)
        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheet.show()

        bindingProduct.tiePrice.addCurrencyFormatter()

        bindingProduct.btnIncludeProduct.setOnClickListener {
            verifyFields(bindingProduct, bottomSheet)
        }
    }

    private fun verifyFields(
        bindingProduct: LayoutIncludeProductBinding,
        bottomSheet: BottomSheetDialog
    ) {
        bindingProduct.apply {
            val productName = tieProductName.text.toString()
            val productDescription = tieProductDescription.text.toString()
            val productPrice = tiePrice.text.toString()
            val productAmount = tieAmount.text.toString()

            val isAllFieldsValid = viewModel.verifyFields(productName, productDescription, productPrice, productAmount)

            if (isAllFieldsValid) {
                viewModel.insertProduct(productName, productDescription, productPrice, productAmount)
                bottomSheet.dismiss()
            } else {
                tieProductName.error = "Digite o nome do produto"
                tieProductDescription.error = "Digite a descrição do produto"
                tiePrice.error = "Digite o valor do produto"
                tiePrice.error = "Valor tem que ser maior que 0"
                tieAmount.error = "Digite a quantidade do produto"
                tieAmount.error = "Quantidade deve ser maior que 0"
            }
        }
    }
}