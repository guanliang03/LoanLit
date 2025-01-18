/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seis2.loanlit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.seis2.loanlit.data.LoginRepository
import com.seis2.loanlit.data.LoginTable
import com.seis2.loanlit.ui.login.LoginViewModel
import com.seis2.loanlit.ui.theme.InventoryTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()  // Set up edge-to-edge UI
        super.onCreate(savedInstanceState)
        val loginRepository = (application as InventoryApplication).container.loginRepository
        val loginViewModel = LoginViewModel(application = application, loginRepository = loginRepository)

        // Set the content view with the InventoryApp UI
        setContent {
            InventoryTheme {
                InventoryApp()
            }
        }
    }
}





