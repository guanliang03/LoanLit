package com.example.inventory

import com.example.inventory.data.LoginRepository
import com.example.inventory.data.LoginTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun insertTestLoginData(loginRepository: LoginRepository) {
    CoroutineScope(Dispatchers.IO).launch {
        val loginData = LoginTable(email = "test@example.com", password = "password123")
        loginRepository.insertLoginData(loginData)
    }
}