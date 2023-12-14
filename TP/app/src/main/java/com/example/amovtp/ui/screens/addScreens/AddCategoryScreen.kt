package com.example.amovtp.ui.screens.addScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amovtp.R
import com.example.amovtp.ui.composables.CameraImage
import com.example.amovtp.ui.composables.GalleryImage
import com.example.amovtp.ui.composables.NameDescription
import com.example.amovtp.ui.viewmodels.addViewModels.AddCategoryViewModel

@Composable
fun AddCategoryScreen(
    addCategoryViewModel: AddCategoryViewModel,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item{

            NameDescription(nameChanged = {}, descriptionChanged = {})
            Spacer(modifier = modifier.height(8.dp))
            GalleryImage(imagesPathChanged = {
                    path ->
            })
            Spacer(modifier = modifier.height(8.dp))
            CameraImage(imagesPathChanged = {
                    path ->
            })

            Button(
                onClick = {
                },
                modifier = modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.add_category))
            }

        }

    }

}