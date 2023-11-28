package com.example.amovtp.ui.screens

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

@Composable
fun RegisterScreen(
    navController: NavHostController?,// ? para nullsafety?
    loginScreen: Screens
) {
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
                label = { Text("Nome") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    if (isRegisterValid(name, email, password, confirmPassword) { msg ->
                            errorMessage = msg
                        }) {
                        navController?.navigate(loginScreen.route)
                    } else {
                        showSnackBar = true
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Register")
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
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    errorMessage: (String) -> Unit
): Boolean {
    if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
        errorMessage("Todos os campos são obrigatórios.")
        return false
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        errorMessage("Formato de e-mail inválido.")
        return false
    }

    if (password != confirmPassword) {
        errorMessage("As passwords não coincidem.")
        return false
    }

    return true
}

@Preview
@Composable
fun RegisterPreview(navController: NavHostController = rememberNavController()) {
    RegisterScreen(navController, Screens.REGISTER)
}