package com.example.salesapp.ui.orderregistration

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.DialogFragment
import com.example.salesapp.R
import com.example.salesapp.databinding.ActivityOrderRegistrationBinding
import com.example.salesapp.databinding.LayoutIncludeProductBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderRegistrationBinding
    private val viewModel:OrderRegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddItem.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this, R.style.DialogStyle)
            val bindingProduct = LayoutIncludeProductBinding.inflate(layoutInflater)
            bottomSheet.setContentView(bindingProduct.root)
            bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED

            bindingProduct.btnIncludeProduct.setOnClickListener {
                bottomSheet.dismiss()
            }
            bottomSheet.show()

        }

    }
}