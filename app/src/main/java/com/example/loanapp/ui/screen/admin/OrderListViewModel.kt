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
import com.example.loanapp.model.User
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class OrderListViewModel(private val printAppRepository: PrinterAppRepository) : ViewModel() {
    var ordersUiState: OrdersUiState by mutableStateOf(OrdersUiState.Loading)
        private set

    var prepareUiState: PrepareUiState by mutableStateOf(PrepareUiState.Loading)
        private set

    init {
    }

    sealed interface OrdersUiState {
        data class Success(val orders: List<Order>) : OrdersUiState
        data class Error(val exception: Throwable? = null) : OrdersUiState
        object Loading : OrdersUiState
    }

    sealed interface PrepareUiState {
        data class Success(val orders: Response<Void>) : PrepareUiState
        data class Error(val exception: Throwable? = null) : PrepareUiState
        object Loading : PrepareUiState
    }

    fun getAllOrdersByStatus(status: String) {
        viewModelScope.launch {
            ordersUiState = OrdersUiState.Loading
            ordersUiState = try {
                OrdersUiState.Success(printAppRepository.getAllOrdersByStatus(status))
            } catch (e: IOException) {
                OrdersUiState.Error(e)
            } catch (e: HttpException) {
                OrdersUiState.Error(e)
            }
        }
    }

    fun getAllOrdersByAdmIdStatus(AdmId: String?,status: String) {
        viewModelScope.launch {
            ordersUiState = OrdersUiState.Loading
            ordersUiState = try {
                OrdersUiState.Success(printAppRepository.getAllOrdersByAdmIdStatus(AdmId ?: "",status))
            } catch (e: IOException) {
                OrdersUiState.Error(e)
            } catch (e: HttpException) {
                OrdersUiState.Error(e)
            }
        }
    }

    fun prepareOrder(user: User?, id: String, status: String) {

        viewModelScope.launch {
            prepareUiState = PrepareUiState.Loading
            prepareUiState = try {

                PrepareUiState.Success(printAppRepository.prepareOrder(user ?: User("","","", ""),id,status))
            } catch (e: IOException) {
                PrepareUiState.Error(e)
            } catch (e: HttpException) {
                PrepareUiState.Error(e)
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PrinterApplication)
                val printAppRepository = application.container.printerAppRepository
                OrderListViewModel(printAppRepository = printAppRepository)
            }
        }

    }

}