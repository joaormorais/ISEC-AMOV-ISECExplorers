package com.example.amovtp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

//TODO: ordenar por ordem alfabética + ordenar de acordo com a distância da nossa localização atual

@Composable
fun Home(
    navController: NavHostController?,
    viewModel: InfoViewModel,
    modifier: Modifier = Modifier
    //TODO: adicionar mais screens?
) {

    Column(
        modifier = modifier
            .background(Color.Green)
            .fillMaxSize()
    ) {

        Box( //TODO: meter o azul por cima do verde
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(16.dp)
                .background(Color.Blue)
                .align(Alignment.CenterHorizontally)
        ) {
            // Conteúdo da Box
        }

    }

}

@Preview
@Composable
fun HomeScreenPreview(navController: NavHostController = rememberNavController()) {
    var viewModel: InfoViewModel? = InfoViewModel()
    Home(navController, viewModel!!)
}