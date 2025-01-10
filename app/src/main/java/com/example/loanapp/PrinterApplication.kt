package com.example.loanapp

import android.app.Application
import android.content.Context
import com.example.loanapp.data.AppContainer
import com.example.loanapp.data.DefaultAppContainer
import com.example.loanapp.ui.PrinterAppViewModel


class PrinterApplication : Application () {
    lateinit var container : AppContainer
    override fun onCreate(){
        super.onCreate()
        container = DefaultAppContainer()
        instance = this
    }

    companion object {
        lateinit var instance: PrinterApplication
            private set
        lateinit var appViewModel: PrinterAppViewModel
    }

    fun saveGlobalValue(key: String, value: String) {
        val sharedPref = getSharedPreferences("MyGlobalPreferences", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getGlobalValue(key: String): String? {
        val sharedPref = getSharedPreferences("MyGlobalPreferences", MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }



}