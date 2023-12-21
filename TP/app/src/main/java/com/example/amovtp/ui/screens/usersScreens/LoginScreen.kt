package com.example.amovtp.ui.screens.usersScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.amovtp.R
import com.example.amovtp.ui.screens.Screens
import com.example.amovtp.ui.theme.AMOVTPTheme
import com.example.amovtp.ui.viewmodels.usersViewModels.LoginViewModel
import com.example.amovtp.ui.viewmodels.utils.FirebaseViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navController: NavHostController?,
    vararg options: Screens,
    modifier: Modifier = Modifier
) {

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackBar by remember { mutableStateOf(false) }
    val unkownError = stringResource(R.string.unknown_error)
    val namePwNeeded = stringResource(R.string.name_pw_needed)

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
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = modifier
                    .widthIn(max = 300.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = modifier
                    .widthIn(max = 300.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = modifier.height(50.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier.fillMaxWidth()
            ) {
                for (i in options) {
                    Button(
                        onClick = {
                            if (i.path == Screens.LOCATIONS.route) {
                                if (isLoginValid(
                                        name,
                                        password,
                                        namePwNeeded
                                    )
                                    { msg ->
                                        errorMessage = msg
                                    }
                                ) {
                                    navController?.navigate(i.route)
                                } else {
                                    showSnackBar = true
                                }
                            } else {
                                navController?.navigate(i.route)
                            }
                        },
                        modifier = modifier.padding(4.dp)
                    ) {
                        Text(
                            text = if (i.path == Screens.LOCATIONS.route) Screens.LOGIN.route else i.path,
                            modifier = modifier.padding(16.dp)
                        )
                    }
                }

            }
            Button(
                onClick = {
                    navController?.navigate(Screens.LOCATIONS.route)
                },
                modifier = modifier.padding(4.dp)
            ) {
                Text(
                    text = "seguir sem login",
                    modifier = modifier.padding(16.dp)
                )
            }
        }
    }
}

fun isLoginValid(
    name: String,
    password: String,
    namePwNeeded: String,
    errorMessage: (String) -> Unit
): Boolean {
    if (name.isBlank() || password.isBlank()) {
        errorMessage(namePwNeeded)
        return false
    }
    return true
}