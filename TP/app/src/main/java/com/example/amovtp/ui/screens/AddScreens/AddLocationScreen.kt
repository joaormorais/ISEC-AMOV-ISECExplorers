package com.example.amovtp.ui.screens.AddScreens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import com.example.amovtp.R
import com.example.amovtp.ui.composables.CameraImage
import com.example.amovtp.ui.composables.GalleryImage
import com.example.amovtp.ui.screens.LocationsScreen
import com.example.amovtp.ui.viewmodels.AddViewModels.AddLocationViewModel
import com.example.amovtp.ui.viewmodels.LocationsViewModel
import com.example.amovtp.utils.FileUtils

@Composable
fun AddLocationScreen(
    navController: NavHostController?,
    viewModel: AddLocationViewModel,
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

@Preview
@Composable
fun AddLocationScreenPreview(navController: NavHostController = rememberNavController()) {
    var viewModel: AddLocationViewModel? = AddLocationViewModel()
    AddLocationScreen(navController, viewModel!!)
}