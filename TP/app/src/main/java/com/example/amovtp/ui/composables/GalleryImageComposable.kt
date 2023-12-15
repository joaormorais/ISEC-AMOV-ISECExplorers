package com.example.amovtp.ui.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.amovtp.R
import com.example.amovtp.utils.file.FileUtils

@Composable
fun GalleryImage(
    imagesPathChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val imagesPath = remember { mutableStateListOf<String>() }


    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri == null) {
                return@rememberLauncherForActivityResult
            }

            imagesPath.add(FileUtils.createFileFromUri(context, uri))
            imagesPathChanged(imagesPath.toList())

        }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Button(onClick = {
            galleryLauncher.launch(PickVisualMediaRequest())
        }) {
            Text(text = stringResource(R.string.gallery_image))
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (!imagesPath.isEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
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