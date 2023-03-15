package com.example.salesapp.ui.orderregistration

import androidx.lifecycle.ViewModel
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrderRegistrationViewModel @Inject constructor(private val repository: SalesRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<List<Order>> =
        MutableStateFlow(emptyList())

    val uiState = _uiState.asStateFlow()


}