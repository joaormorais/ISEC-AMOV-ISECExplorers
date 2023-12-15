package com.example.amovtp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amovtp.R

@Composable
fun NameDescription(
    nameChanged: (String) -> Unit,
    descriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { newName ->
                name = newName
                nameChanged(newName)
            },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = description,
            onValueChange = { newDescription ->
                description = newDescription
                descriptionChanged(newDescription)
            },
            label = { Text(stringResource(R.string.description_solo)) },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(200.dp)
        )

    }

}