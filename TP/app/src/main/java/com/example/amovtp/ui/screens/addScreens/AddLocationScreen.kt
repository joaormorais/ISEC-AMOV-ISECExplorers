package com.example.amovtp.ui.screens.addScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import com.example.amovtp.R
import com.example.amovtp.ui.composables.CameraImage
import com.example.amovtp.ui.composables.GalleryImage
import com.example.amovtp.ui.viewmodels.addViewModels.AddLocationViewModel

@Composable
fun AddLocationScreen(
    addLocationViewModel: AddLocationViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        //TODO: Meter aqui o composable com a caixa de texto para o nome e para o texto descritivo (Zé)
        //NameDescription()
        Spacer(modifier = Modifier.height(8.dp))
        GalleryImage()
        Spacer(modifier = Modifier.height(8.dp))
        //TODO: Meter aqui o composable com o botão para a câmera (Sandra)
        CameraImage()

        //TODO: botao para criar
    }

}

/*@Preview
@Composable
fun AddLocationScreenPreview(navController: NavHostController = rememberNavController()) {
    var viewModel: AddLocationViewModel? = AddLocationViewModel()
    AddLocationScreen(navController, viewModel!!)
}*/