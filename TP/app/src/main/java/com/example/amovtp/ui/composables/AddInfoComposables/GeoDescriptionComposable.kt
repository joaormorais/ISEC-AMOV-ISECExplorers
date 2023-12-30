package com.example.amovtp.ui.composables.AddInfoComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import com.example.amovtp.R

@Composable
fun GeoDescription(
    editingLat: String,
    editingLong: String,
    editingIsManual: Boolean,
    latChanged: (Double) -> Unit,
    longChanged: (Double) -> Unit,
    manualChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val manualString = stringResource(R.string.manual_lat_long)
    val automaticString = stringResource(R.string.automatic_lat_long)
    var isLatValid by remember { mutableStateOf(true) }
    var isLongValid by remember { mutableStateOf(true) }

    var lat by remember { mutableStateOf(editingLat) }
    var long by remember { mutableStateOf(editingLong) }
    var isManual by remember { mutableStateOf(editingIsManual) }

    manualChanged(isManual)

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
                onValueChange = { newLat ->
                    if (isValidInput(newLat)) {
                        lat = newLat
                        if (isValidCoordinate(newLat)) {
                            isLatValid = true
                            latChanged(newLat.toDouble())
                            manualChanged(isManual)
                        } else {
                            isLatValid = false
                        }
                    }
                },
                label = { Text(stringResource(R.string.latitude)) },
                modifier = modifier.padding(bottom = 16.dp),
                enabled = isManual,
                isError = !isLatValid
            )
            OutlinedTextField(
                value = long,
                onValueChange = { newLong ->
                    if (isValidInput(newLong)) {
                        long = newLong
                        if (isValidCoordinate(newLong)) {
                            isLongValid = true
                            longChanged(newLong.toDouble())
                            manualChanged(isManual)
                        } else {
                            isLongValid = false
                        }
                    }
                },
                label = { Text(stringResource(R.string.longitude)) },
                modifier = modifier.padding(bottom = 16.dp),
                enabled = isManual,
                isError = !isLongValid
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
            Text(
                automaticString, modifier = modifier
                    .padding(bottom = 6.dp)
            )
            Switch(
                checked = !isManual,
                onCheckedChange = {
                    isManual = !it
                    manualChanged(!it)
                }
            )
        }

    }
}

private fun isValidInput(input: String): Boolean {
    val lastChar = input.lastOrNull()
    return lastChar == null || lastChar.isDigit() || lastChar == '.' || lastChar == '-'
}

private fun isValidCoordinate(newLat: String): Boolean {
    val regex = """^-?\d+\.\d+$""".toRegex()
    return regex.matches(newLat)
}