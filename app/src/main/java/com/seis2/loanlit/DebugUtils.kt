package com.seis2.loanlit

import com.seis2.loanlit.data.LoginRepository
import com.seis2.loanlit.data.LoginTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun insertTestLoginData(loginRepository: LoginRepository) {
    CoroutineScope(Dispatchers.IO).launch {
        val loginData = LoginTable(email = "test@example.com", password = "password123")
        loginRepository.insertLoginData(loginData)
    }
}