import retrofit2.HttpException
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
import java.io.IOException


class CustomerDashboardViewModel(private val printAppRepository: PrinterAppRepository) : ViewModel() {
    var ordersUiState: OrdersUiState by mutableStateOf(OrdersUiState.Loading)
        private set

    init {
    }

    sealed interface OrdersUiState {
        data class Success(val orders: List<Order>) : OrdersUiState
        data class Error(val exception: Throwable? = null) : OrdersUiState
        object Loading : OrdersUiState
    }

    fun getAllOrdersByCustId(id: String) {
        viewModelScope.launch {
            ordersUiState = OrdersUiState.Loading
            ordersUiState = try {
                OrdersUiState.Success(printAppRepository.getAllOrdersByCustId(id))
            } catch (e: IOException) {
                OrdersUiState.Error(e)
            } catch (e: HttpException) {
                OrdersUiState.Error(e)
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PrinterApplication)
                val printAppRepository = application.container.printerAppRepository
                CustomerDashboardViewModel(printAppRepository = printAppRepository)
            }
        }

    }

}