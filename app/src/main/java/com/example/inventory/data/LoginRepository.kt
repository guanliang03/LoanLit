package com.example.inventory.data

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginRepository(application: Application) {
    private val loginDao: LoginDao
    val allLoginData: LiveData<List<LoginTable>>

    init {
        val database = LoginDatabase.getDatabase(application)
        loginDao = database.loginDao()
        allLoginData = loginDao.getDetails() // Ensure DAO return type matches LiveData<List<LoginTable>>
    }

    suspend fun insertLoginData(loginData: LoginTable) {
        loginDao.insertDetails(loginData)
    }

    suspend fun deleteAllLoginData() {
        loginDao.deleteAllData()
    }


}


