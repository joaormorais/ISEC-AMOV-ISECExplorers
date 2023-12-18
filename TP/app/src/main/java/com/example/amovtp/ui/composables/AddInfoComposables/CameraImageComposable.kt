package com.example.amovtp.ui.composables.AddInfoComposables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.amovtp.R
import com.example.amovtp.utils.file.FileUtils
import java.io.File

@Composable
fun CameraImage(
    imagesPathChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier)
{
    var tempFile by remember { mutableStateOf("") }
    val context = LocalContext.current
    val imagesPath = remember { mutableStateListOf<String>()}

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess  ->
        if (!isSuccess ) {
            return@rememberLauncherForActivityResult
        }

        imagesPath.add(FileUtils.createFileFromUri(context, Uri.fromFile(File(tempFile))))
        imagesPathChanged(imagesPath.toList())
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


        Spacer(modifier = Modifier.height(8.dp))
        if (!imagesPath.isEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                items(imagesPath) { img ->
                    AsyncImage(
                        model = img,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentDescription = "Background Image"
                    )
                }

            }
        }
    }
}
