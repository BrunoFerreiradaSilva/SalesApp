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
import com.example.salesapp.util.addCurrencyFormatter
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
        val ordersRegistrationAdapter = OrdersRegistrationAdapter()


        binding.rvOrderRegistration.apply {
            adapter = ordersRegistrationAdapter
            layoutManager = GridLayoutManager(this@OrderRegistrationActivity,1)
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { listItem ->
                ordersRegistrationAdapter.submitList(listItem)
                binding.cvButtons.isVisible = listItem.isNotEmpty()
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveOrder()
            finish()
        }

        binding.btnAddItem.setOnClickListener {
            setupBottomDialog()
        }

        viewModel.amountValue.observe(this){amount->
            binding.tvResultTotalItems.text = amount
        }
        viewModel.priceValue.observe(this){price->
            binding.tvResultTotalValue.text = price
        }
    }

    private fun setupBottomDialog() {
        val bottomSheet = BottomSheetDialog(this, R.style.DialogStyle)
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
            if (viewModel.verifyFields(
                    tieProductName.text.toString(),
                    tieAmount.text.toString(),
                    tiePrice.text.toString(),
                    tieProductDescription.text.toString()
                )
            ) {
                viewModel.insertProduct(
                    tieProductName.text.toString(),
                    tieProductDescription.text.toString(),
                    tiePrice.text.toString(),
                    tieAmount.text.toString()
                )
                bottomSheet.dismiss()
            } else {
                return
            }

        }
    }
}