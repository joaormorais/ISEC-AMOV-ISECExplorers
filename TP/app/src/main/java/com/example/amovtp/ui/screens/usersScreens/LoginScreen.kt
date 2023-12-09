package com.example.amovtp.ui.screens.usersScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.amovtp.MyApplication
import com.example.amovtp.R
import com.example.amovtp.ui.screens.Screens
import com.example.amovtp.ui.viewmodels.usersViewModels.LoginViewModel
import com.example.amovtp.ui.viewmodels.usersViewModels.LoginViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navController: NavHostController?,
    vararg options: Screens
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            // Campo de texto para nome de usuário
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .fillMaxWidth()
            )

            // Espaçamento pequeno entre nome de usuário e senha
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de texto para senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .fillMaxWidth()
            )

            // Espaçamento maior antes dos botões
            Spacer(modifier = Modifier.height(56.dp))

            // Row para os botões
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in options) {
                    Button(
                        onClick = {
                            if (i.path == "Locations") {
                                if (isLoginValid(name, password)) {
                                    navController?.navigate(i.route)
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Nome de utilizador e password são necessários."
                                        )
                                    }
                                }
                            } else {
                                navController?.navigate(i.route)
                            }
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = if (i.path == "Locations") "Login" else i.path,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


fun isLoginValid(
    name: String,
    password: String,
): Boolean {
    if (name.isBlank() || password.isBlank()) {
        return false
    }
    return true
}

@Preview
@Composable
fun LoginPreview(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val app = context.applicationContext as MyApplication

    // Specify the type parameter for viewModel explicitly
    val loginViewModel = viewModel<LoginViewModel>(factory = LoginViewModelFactory(app.usersData))

    // Create a preview of the LoginScreen
    LoginScreen(
        loginViewModel = loginViewModel,
        navController = navController,
        Screens.REGISTER,
        Screens.LOCATIONS
    )
}


