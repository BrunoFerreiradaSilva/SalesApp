package com.example.salesapp.ui.orderregistration

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.MoneyTextWatcher
import com.example.salesapp.R
import com.example.salesapp.databinding.ActivityOrderRegistrationBinding
import com.example.salesapp.databinding.LayoutIncludeProductBinding
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

        binding.btnAddItem.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this, R.style.DialogStyle)
            val bindingProduct = LayoutIncludeProductBinding.inflate(layoutInflater)
            bottomSheet.setContentView(bindingProduct.root)
            bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED

            bottomSheet.show()


            bindingProduct.tiePrice.apply {
                addTextChangedListener(MoneyTextWatcher(this))
            }


            bindingProduct.btnIncludeProduct.setOnClickListener {
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
                        return@setOnClickListener
                    }

                }
            }
        }
    }
}