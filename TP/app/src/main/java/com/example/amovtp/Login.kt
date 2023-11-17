package com.example.amovtp

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Login(
    navController: NavHostController?,// ? para nullsafety?
    vararg options: Screens
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            for (i in options)
                Button(
                    onClick = { navController?.navigate(i.route) },
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = if (i.display == "Home") "Login" else i.display, //TODO: alterar Login para uma string
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

        }

    }

}

@Preview
@Composable
fun LoginPreview(navController: NavHostController = rememberNavController()) {
    Login(navController, Screens.REGISTER, Screens.HOME)
}