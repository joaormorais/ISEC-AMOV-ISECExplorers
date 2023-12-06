package com.example.amovtp.ui.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.amovtp.utils.FileUtils

@Composable
fun GalleryImage(modifier: Modifier = Modifier){

    val context = LocalContext.current
    var imagePath by remember { mutableStateOf<String?>(null) }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri == null) {
                imagePath = null
                return@rememberLauncherForActivityResult
            }

            imagePath = FileUtils.createFileFromUri(context, uri)

        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Button(onClick = {
            galleryLauncher.launch(PickVisualMediaRequest())
        }) {
            Text(text = "Selecionar imagem da galeria")
        }
        /*Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
        ) {
            if (imagePath != null) {
                AsyncImage(
                    model = imagePath,
                    modifier = Modifier.matchParentSize(),
                    contentDescription = "Background Image"
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.coimbra),
                    contentDescription = "Default Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.matchParentSize()
                )
            }
        }*/
    }

}