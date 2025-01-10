package com.example.loanapp.ui.screen.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.loanapp.PrinterApplication
import com.example.loanapp.data.PrinterAppRepository
import com.example.loanapp.model.Order
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class OrderDetailsViewModel(private val printAppRepository: PrinterAppRepository) : ViewModel() {
    var orderUiState: OrderUiState by mutableStateOf(OrderUiState.Loading)
        private set

    init {
    }

    sealed interface OrderUiState {
        data class Success(val order: Order) : OrderUiState
//        data class OrderModificationSuccess(val response: Response<Void>) : OrdersUiState
        data class Error(val exception: Throwable? = null) : OrderUiState
        object Loading : OrderUiState
    }

    fun getOrder(id: String) {
        viewModelScope.launch {
            orderUiState = OrderUiState.Loading
            orderUiState = try {
                OrderUiState.Success(printAppRepository.getOrderById(id))
            } catch (e: IOException) {
                OrderUiState.Error(e)
            } catch (e: HttpException) {
                OrderUiState.Error(e)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PrinterApplication)
                val printAppRepository = application.container.printerAppRepository
                OrderDetailsViewModel(printAppRepository = printAppRepository)
            }
        }

    }


}