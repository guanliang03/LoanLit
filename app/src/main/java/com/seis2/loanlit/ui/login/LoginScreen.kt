package com.seis2.loanlit.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

import com.seis2.loanlit.R

import com.seis2.loanlit.ui.AppViewModelProvider
import com.seis2.loanlit.ui.navigation.NavigationDestination

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}
@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val loginSuccess by viewModel.loginSuccess.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var formError by remember { mutableStateOf<String?>(null) }  // To hold error message for empty fields

    // Reset the state when the login screen is recomposed
    LaunchedEffect(Unit) {
        viewModel.resetLoginState()  // Reset login state when the screen is re-entered
    }

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navigateToHome()  // Navigate to home on successful login
        }
    }

    Scaffold(
        topBar = {
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                    .statusBarsPadding()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.username)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Show error message for form validation
            if (!formError.isNullOrEmpty()) {
                Text(
                    text = formError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
                )
            }

            // Show error message from login attempt if exists
            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
                )
            }

            // Option to register if not found in database
            TextButton(
                onClick = {
                    viewModel.registerLoginDetails(email, password)
                }
            ) {
                Text(stringResource(R.string.register_account))
            }

            // Login button with validation for empty email and password
            Button(
                onClick = {
                    // Validate email and password
                    if (email.isBlank() || password.isBlank()) {2
                        formError = "Matric Number and Password must not be empty"
                    } else {
                        formError = null
                        viewModel.validateLogin(email, password) // Start login process
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading  // Disable button while loading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.login_action))
                }
            }
        }
    }
}
