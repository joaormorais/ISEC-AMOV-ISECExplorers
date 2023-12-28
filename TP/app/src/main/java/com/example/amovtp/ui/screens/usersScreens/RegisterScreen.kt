package com.example.amovtp.ui.screens.usersScreens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.amovtp.R
import com.example.amovtp.ui.screens.Screens
import com.example.amovtp.ui.viewmodels.usersViewModels.RegisterViewModel

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackBar by remember { mutableStateOf(false) }
    val unkownError = stringResource(R.string.unknown_error)
    val emailNeeded = stringResource(R.string.email_needed)
    val pwNeeded = stringResource(R.string.pw_needed)
    val invalidEmailError = stringResource(R.string.invalid_email)
    val passwordsDontMatchError = stringResource(R.string.pws_dont_match)

    LaunchedEffect(showSnackBar) {
        if (showSnackBar) {
            snackbarHostState.showSnackbar(errorMessage ?: unkownError)
            showSnackBar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier=modifier.padding(top = 32.dp)
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    if (isRegisterValid(
                            email,
                            password,
                            confirmPassword,
                            emailNeeded,
                            pwNeeded,
                            invalidEmailError,
                            passwordsDontMatchError
                        )
                        { msg ->
                            errorMessage = msg
                        }
                    ) {
                        registerViewModel.register(email,password){exception ->
                            if(exception!=null){
                                //TODO: mostrar aqui a excecao
                                Log.d("RegisterScreen",
                                    "exception recebida = ${exception.message}")
                                errorMessage = exception.message
                                showSnackBar = true
                            }else
                                navController?.navigate(Screens.LOGIN.route)
                        }
                    } else {
                        showSnackBar = true
                    }
                },
                modifier = modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.register))
            }
        }
    }
}

fun isRegisterValid(
    email: String,
    password: String,
    confirmPassword: String,
    emailNeeded: String,
    pwNeeded: String,
    invalidEmailError: String,
    passwordsDontMatchError: String,
    errorMessage: (String) -> Unit
): Boolean {

    //TODO: dividir em dois popups -> fazer um para o nome e depois pw
    if (email.isBlank()){
        errorMessage(emailNeeded)
        return false
    }

    if(password.isBlank() || confirmPassword.isBlank()) {
        errorMessage(pwNeeded)
        return false
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        errorMessage(invalidEmailError)
        return false
    }

    if (password != confirmPassword) {
        errorMessage(passwordsDontMatchError)
        return false
    }

    return true
}