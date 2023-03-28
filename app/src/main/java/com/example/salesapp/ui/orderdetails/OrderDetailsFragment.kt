package com.example.salesapp.ui.orderdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salesapp.R
import com.example.salesapp.databinding.FragmentOrderRegistrationBinding
import com.example.salesapp.model.OrderUiData
import com.example.salesapp.ui.insertproduct.InsertProductDialogFragment
import com.example.salesapp.ui.orderregistration.OrdersRegistrationAdapter
import com.example.salesapp.util.gone
import com.example.salesapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {

    private lateinit var binding : FragmentOrderRegistrationBinding
    private val viewModel:OrderDetailsViewModel by viewModels()
    private val args: OrderDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderRegistrationBinding.inflate(layoutInflater)

        val ordersRegistrationAdapter = OrdersRegistrationAdapter(::isEditModeOn)

        binding.rvOrderRegistration.apply {
            adapter = ordersRegistrationAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }

        viewModel.getProductOrderById(args.orderId)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { orderUiData ->
                handleOrderUIDataCollected(orderUiData, ordersRegistrationAdapter)
            }
        }

        binding.apply {
            btnSave.gone()
            btnAddItem.gone()
            btnDelete.visible()
            tieClientName.isEnabled = false
            btnDelete.setOnClickListener {
                showDeleteOrderDialog(args.orderId,getString(R.string.title_delete_dialog))
            }
        }

        return binding.root
    }

    private fun showDeleteOrderDialog(orderId: String, titleText:String) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(titleText)
        alertDialog.setPositiveButton(
            getString(R.string.positive_button_dialog)
        ) { _, _ ->
            viewModel.deleteOrder(orderId)
            findNavController().navigateUp()
        }
        alertDialog.setNegativeButton(getString(R.string.negative_button_dialog), null)
        alertDialog.show()
    }

    private fun isEditModeOn(orderId: String, productId: Int) {
        insertProductDialog(orderId = orderId, productId)

    }

    private fun insertProductDialog(orderId: String, productId: Int) {
        val dialogFragment = InsertProductDialogFragment(orderId, productId)
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