package com.example.salesapp.ui.insertproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.example.salesapp.R
import com.example.salesapp.databinding.LayoutIncludeProductBinding
import com.example.salesapp.model.ProductUi
import com.example.salesapp.model.ProductValidationError
import com.example.salesapp.util.addCurrencyFormatter
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InsertProductDialogFragment (private val getOrderId:String):
    BottomSheetDialogFragment() {

    private lateinit var binding: LayoutIncludeProductBinding

    private val viewModel: InsertProductViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        (dialog as BottomSheetDialog).behavior.state = STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)


        binding = LayoutIncludeProductBinding.inflate(layoutInflater)

        binding.tiePrice.addCurrencyFormatter()

        binding.btnIncludeProduct.setOnClickListener {
            validateFields()
        }

        viewModel.getIdOrder(getOrderId)

        return binding.root
    }


    private fun validateFields() {

        binding.apply {
            val productName = tieProductName.text.toString()
            val productDescription = tieProductDescription.text.toString()
            val productPrice = tiePrice.text.toString()
            val productAmount = tieAmount.text.toString()

            val validationErrors = viewModel.validateFields(
                productName, productDescription, productPrice, productAmount
            )

            if (validationErrors.isEmpty()) {
                val productUi =
                    ProductUi(productName, productDescription, productPrice, productAmount)
                insertProduct(productUi)
                dialog?.dismiss()
            } else {
                validationErrors.forEach { validationError ->
                    when (validationError) {
                        ProductValidationError.EmptyProductAmount -> tieAmount.error =
                            getString(R.string.error_product_amount_is_empty)
                        ProductValidationError.EmptyProductDescription -> tieProductDescription.error =
                            getString(
                                R.string.error_product_description_is_empty
                            )
                        ProductValidationError.EmptyProductNameError -> tieProductName.error =
                            getString(
                                R.string.error_product_name_is_empty
                            )
                        ProductValidationError.EmptyProductPrice -> tiePrice.error =
                            getString(R.string.error_product_price_is_empty)
                        ProductValidationError.PriceIsZeroError -> tiePrice.error =
                            getString(R.string.error_product_price_is_zero)
                        ProductValidationError.AmountIsZeroError -> tieAmount.error =
                            getString(R.string.error_amount_is_zero)
                    }
                }
            }
        }
    }

    private fun insertProduct(product: ProductUi) {
        viewModel.insertProduct(
            product.nameProduct,
            product.description,
            product.price,
            product.amount
        )
    }
}