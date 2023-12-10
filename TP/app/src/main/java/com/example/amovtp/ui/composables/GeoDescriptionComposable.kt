package com.example.amovtp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.amovtp.R

@Composable
fun GeoDescription(modifier: Modifier = Modifier) {

    var manualString = stringResource(R.string.manual_lat_long)
    var automaticString = stringResource(R.string.automatic_lat_long)
    var lat by remember { mutableStateOf("") }
    var long by remember { mutableStateOf("") }
    var isManual by remember { mutableStateOf(true) }

    Row(
        modifier = modifier
            .padding(16.dp)
            .wrapContentHeight()
    ) {

        Column(
            modifier = modifier
                .weight(0.6f)
                .fillMaxHeight()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(manualString, modifier = modifier.padding(bottom = 6.dp))
            OutlinedTextField(
                value = lat,
                onValueChange = { lat = it },
                label = { Text(stringResource(R.string.latitude)) },
                modifier = modifier.padding(bottom = 16.dp),
                enabled = isManual
            )
            OutlinedTextField(
                value = long,
                onValueChange = { long = it },
                label = { Text(stringResource(R.string.longitude)) },
                modifier = modifier.padding(bottom = 16.dp),
                enabled = isManual
            )
        }

        Column(
            modifier = modifier
                .weight(0.4f)
                .fillMaxHeight()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(automaticString, modifier = modifier
                .padding(bottom = 6.dp))
            Switch(
                checked = !isManual,
                onCheckedChange = {
                    isManual = !it
                }
            )
        }

    }
}


@Preview
@Composable
fun GeoDescriptionPreview() {
    GeoDescription()
}