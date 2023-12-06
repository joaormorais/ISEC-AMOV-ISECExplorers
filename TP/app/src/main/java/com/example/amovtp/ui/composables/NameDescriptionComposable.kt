package com.example.amovtp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
fun NameDescription(modifier: Modifier = Modifier){

    // Seu estado e UI para adicionar uma nova categoria
    var categoryName by remember { mutableStateOf("") }
    var categoryDescription by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = categoryDescription,
            onValueChange = { categoryDescription = it },
            label = { Text(stringResource(R.string.description_solo)) },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(200.dp)
        )

    }

}