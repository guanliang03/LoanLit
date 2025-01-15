package com.seis2.loanlit.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seis2.loanlit.BuildConfig
import com.seis2.loanlit.R
import com.seis2.loanlit.data.LoginTable
import com.seis2.loanlit.ui.AppViewModelProvider
import com.seis2.loanlit.ui.navigation.NavigationDestination
import com.seis2.loanlit.ui.theme.InventoryTheme
import kotlinx.coroutines.launch
object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        // Insert test data for debugging
        val loginData = LoginTable(email = "test@example.com", password = "password123")
        viewModel.insertTestData(loginData)
    }

    LaunchedEffect(Unit) {
        // Insert test data only in debug mode
        if (BuildConfig.DEBUG) {
            viewModel.insertTestLoginData()
        }
    }

    val loginUiState = viewModel.loginUiState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    ) { innerPadding ->
        LoginBody(
            loginUiState = loginUiState,
            onUsernameChange = { viewModel.updateUsername(it) },
            onPasswordChange = { viewModel.updatePassword(it) },
            onLoginClick = {
                coroutineScope.launch {
                    val loginSuccess = viewModel.validateLogin()
                    if (loginSuccess) {
                        navigateToHome()
                    }
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}
@Composable
fun LoginBody(
    loginUiState: LoginUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        OutlinedTextField(
            value = loginUiState.username,
            onValueChange = onUsernameChange,
            label = { Text(stringResource(R.string.username)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = loginUiState.password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (!loginUiState.errorMessage.isNullOrEmpty()) {
            Text(
                text = loginUiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
        Button(
            onClick = onLoginClick,
            enabled = !loginUiState.isLoading,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.login_action))
        }
    }
}




@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    InventoryTheme {
        LoginBody(
            loginUiState = LoginUiState(username = "JohnDoe", password = "", errorMessage = null),
            onUsernameChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}
