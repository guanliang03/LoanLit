package com.seis2.loanlit.ui.login

import android.app.Application


import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seis2.loanlit.data.LoginRepository
import com.seis2.loanlit.data.LoginTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * ViewModel to handle login functionality and validate input credentials.
 */
class LoginViewModel(
    application: Application,
    private val loginRepository: LoginRepository
) : AndroidViewModel(application) {

    private val repository = LoginRepository(application)
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun updateUsername(username: String) {
        _loginUiState.update { it.copy(username = username) }
    }

    fun updatePassword(password: String) {
        _loginUiState.update { it.copy(password = password) }
    }


    init {
        // Insert test data into the database
        val loginData = LoginTable(email = "test@example.com", password = "password123")
        insertTestData(loginData)
    }

    fun insertTestData(loginData: LoginTable) {
        viewModelScope.launch {
            loginRepository.insertLoginData(loginData)
        }
    }

    fun insertTestLoginData() {
        CoroutineScope(Dispatchers.IO).launch {
            val loginData = LoginTable(email = "test@example.com", password = "password123")
            loginRepository.insertLoginData(loginData)
        }
    }

    fun validateLogin(): Boolean {
        val state = _loginUiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            _loginUiState.update { it.copy(errorMessage = "Username or Password cannot be empty") }
            return false
        }

        // Fetch login data from the database
        val allLoginData = repository.allLoginData.value ?: emptyList()
        val match = allLoginData.find { it.email == state.username && it.password == state.password }

        return if (match != null) {
            _loginUiState.update { it.copy(errorMessage = null) }
            true
        } else {
            _loginUiState.update { it.copy(errorMessage = "Invalid Username or Password") }
            false
        }
    }
}






data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

