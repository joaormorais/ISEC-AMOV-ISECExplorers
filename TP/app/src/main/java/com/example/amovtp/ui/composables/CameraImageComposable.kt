package com.example.amovtp.ui.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.amovtp.R
import com.example.amovtp.utils.FileUtils
import java.io.File
import coil.compose.AsyncImage

@Composable
fun CameraImage(modifier: Modifier = Modifier){
    var tempFile by remember { mutableStateOf("") }
    val context = LocalContext.current
    var imagePath by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (!success) {
            imagePath = null
            return@rememberLauncherForActivityResult
        }
        imagePath = tempFile
    }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Button(onClick = {
            tempFile = FileUtils.getTempFilename(context)
            val fileUri = FileProvider.getUriForFile(
                context, "com.example.amovtp.android.fileprovider",
                File(tempFile)
            )
            cameraLauncher.launch(fileUri)
        }) {
            Text(text = stringResource(R.string.camera_image))
        }

//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxSize()) {
//            //a box vai ocupar todo o espaço existente
//            if(imagePath != null){
//                AsyncImage(
//                    model = imagePath,
//                    modifier = Modifier.matchParentSize(),
//                    contentDescription = "Background Image")
//            }
//            else{
//                Image(
//                    painter = painterResource(id = R.drawable.coimbra),
//                    contentDescription = "Image Default",
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier.matchParentSize()
//                )
//            }
//
//        }
    }
}
