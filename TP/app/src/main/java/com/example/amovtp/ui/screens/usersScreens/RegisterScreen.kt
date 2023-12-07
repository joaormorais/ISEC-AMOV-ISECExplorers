package com.example.amovtp.ui.screens.usersScreens

import android.content.Context //TODO: podemos usar este import?

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amovtp.MyApplication
import com.example.amovtp.R
import com.example.amovtp.ui.screens.Screens
import com.example.amovtp.ui.viewmodels.usersViewModels.RegisterViewModel
import com.example.amovtp.ui.viewmodels.usersViewModels.RegisterViewModelFactory

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    navController: NavHostController?,
    loginScreen: Screens
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember {SnackbarHostState()} //para mostrar as mensagens de erro
    var showSnackBar by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    if (isRegisterValid(context,name, email, password, confirmPassword) { msg ->
                            errorMessage = msg
                        }) {
                        navController?.navigate(loginScreen.route)
                    } else {
                        showSnackBar = true
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.register))
            }
        }
    }

    LaunchedEffect(showSnackBar) {
        if (showSnackBar) {
            snackbarHostState.showSnackbar(errorMessage ?: "Erro desconhecido")
            showSnackBar = false
        }
    }
}

fun isRegisterValid(
    context: Context,
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    errorMessage: (String) -> Unit
): Boolean {


    if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
        errorMessage(context.getString(R.string.fill_every_field))
        return false
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        errorMessage(context.getString(R.string.invalid_email))
        return false
    }

    if (password != confirmPassword) {
        errorMessage(context.getString(R.string.pws_dont_match))
        return false
    }

    return true
}

@Preview
@Composable
fun RegisterPreview(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val app = context.applicationContext as MyApplication

    // Specify the type parameter for viewModel explicitly
    val registerViewModel = viewModel<RegisterViewModel>(factory = RegisterViewModelFactory(app.usersData))

    // Create a preview of the RegisterScreen
    RegisterScreen(
        registerViewModel = registerViewModel,
        navController = navController,
        Screens.LOGIN
    )
}
