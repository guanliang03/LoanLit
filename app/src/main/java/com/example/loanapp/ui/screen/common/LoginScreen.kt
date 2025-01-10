@file:Suppress("CanBeVal")

package com.example.loanapp.ui.screen.common

import LoginViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loanapp.PrinterApplication
import com.example.loanapp.ui.ErrorScreen
import com.example.loanapp.ui.LoadingScreen
import com.example.loanapp.ui.MinimalDialog
import com.example.loanapp.ui.PrinterAppViewModel
import com.example.loanapp.ui.theme.loanAppTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: (String) -> Unit = {},
    loginViewModel: LoginViewModel? = viewModel(factory = LoginViewModel.Factory),
    printerAPpViewModel: PrinterAppViewModel? = null
) {
    Surface(
        modifier = modifier.widthIn(min = 400.dp, max = 600.dp),
    ) {
        var usernameInput by remember { mutableStateOf("customer_1") }
        var passwordInput by remember { mutableStateOf("customer_password") }
        var showDialog by remember { mutableStateOf(false) }

        var onLoginCLick = {_: String, _: String -> }

        if(loginViewModel != null && printerAPpViewModel != null){
            onLoginCLick = {username: String, password: String ->
                showDialog = true
                loginViewModel.loginUser(username, password,printerAPpViewModel)
            }

            var minimalDialogClose by remember { mutableStateOf({
                showDialog = false
            })}

            if (showDialog) {
                MinimalDialog(
                    onDismissRequest = minimalDialogClose
                ) {
                    when (loginViewModel.loginUiState) {
                        is LoginViewModel.LoginUiState.Loading -> {
                            LoadingScreen()
                        }

                        is LoginViewModel.LoginUiState.LoginReceive -> {
                            Text(
                                text = "Logged in Successfully",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.displaySmall
                            )



                            val body = (loginViewModel.loginUiState as
                                    LoginViewModel.LoginUiState.LoginReceive).response.body()

                            PrinterApplication.instance
                                .saveGlobalValue("username",body?.username ?: "username")

                            minimalDialogClose = {
                                showDialog = false
                                onLoginSuccess(body?.userRole ?: "customer")
                            }
                        }

                        is LoginViewModel.LoginUiState.Error -> {
                            (loginViewModel.loginUiState as LoginViewModel.LoginUiState.Error)
                                .exception?.message?.let {
                                Text(it)
                                ErrorScreen(
                                    retryAction = {
                                        showDialog = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }


        }

        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 30.dp)
                ,
            verticalArrangement = Arrangement.Center

        ) {
            Text("username")
            TextField(
                value = usernameInput,
                onValueChange = { usernameInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            )
            Text("password")
            TextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            )
            Button(
                onClick = {
                    //onLoginSuccess("customer")
                    onLoginCLick(usernameInput, passwordInput)
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )

            ) {
                Text("Login")
            }
        }


    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginPreview() {
    loanAppTheme {
        LoginScreen(loginViewModel = null)
    }
}