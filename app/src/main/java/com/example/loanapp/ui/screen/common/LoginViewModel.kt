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
import com.example.loanapp.model.User
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import com.example.loanapp.ui.PrinterAppViewModel


class LoginViewModel(private val printAppRepository: PrinterAppRepository) : ViewModel() {
    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Loading)
        private set

    init {
    }

    sealed interface LoginUiState {
        data class LoginReceive(val response: Response<User>) : LoginUiState
        data class Error(val exception: Throwable? = null) : LoginUiState
        object Loading : LoginUiState
    }

    fun loginUser(username: String, password: String, printerAPpViewModel: PrinterAppViewModel){
        viewModelScope.launch {
            loginUiState = LoginUiState.Loading
            loginUiState = try {
                val response = printAppRepository.loginUser(User(username, password, "test", null))


                if (response.code() == 200) {
                    val user = response.body()
                    user?.let {
                        loginUiState = LoginUiState.LoginReceive(response)
                    }

                    if (user != null){
                        printerAPpViewModel.update_user(user)
                    }

                    LoginUiState.LoginReceive(response)
                } else {

                    LoginUiState.Error(Exception("Failed to create order. Status code: ${response.code()}"))
                }
            } catch (e: IOException) {
                LoginUiState.Error(e)
            } catch (e: HttpException) {
                LoginUiState.Error(e)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PrinterApplication)
                val printAppRepository = application.container.printerAppRepository
                LoginViewModel(printAppRepository = printAppRepository)
            }
        }

    }

}