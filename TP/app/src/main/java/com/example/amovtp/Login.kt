package com.example.amovtp

import android.widget.Button
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Login(
    navController: NavHostController?,
    vararg options: Screens
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                label = { Text("Nome") },
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
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                for (option in options) {
                    Button(
                        onClick = {
                            // Verifica se o texto do botão é "Login" e se o nome de usuário e a senha são válidos antes de navegar
                            if (option.display == "Home" && isLoginValid(name, password)) {
                                navController?.navigate(option.route)
                            } else if (option.display != "Home") {
                                // Para outros botões que não sejam o de login, navegue sem validação
                                navController?.navigate(option.route)
                            }
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = if (option.display == "Home") "Login" else option.display,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

            }
        }
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
    Login(navController, Screens.REGISTER, Screens.HOME)
}