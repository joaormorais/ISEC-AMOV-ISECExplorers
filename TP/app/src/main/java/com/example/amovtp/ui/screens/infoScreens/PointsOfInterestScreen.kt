package com.example.amovtp.ui.screens.infoScreens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.amovtp.R
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointsOfInterestScreen(
    pointsOfInterestViewModel: PointsOfInterestViewModel,
    itemName: String? = null,
    navController: NavController,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit
) {

    var pointsOfInterest by remember {
        mutableStateOf(pointsOfInterestViewModel.getPointsOfInterest())
    }

    //TODO SANDRA
    var isExpanded2 by remember { mutableStateOf(false) }
    val locations = pointsOfInterestViewModel.getLocations()
    var selectedLocation by remember {
        mutableStateOf(locations.firstOrNull()?.name ?: "")
    }
    var selectedIndex by remember { mutableStateOf(0) }

    val defaultString = stringResource(R.string.defaultvalue)
    LaunchedEffect(key1 = itemName){
        if (itemName != null)
            if (itemName != defaultString)
                pointsOfInterest = pointsOfInterestViewModel.getPointsFromLocation(itemName)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                selectedLocation,
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable(onClick = {isExpanded2 = true})
            )

            DropdownMenu(
                expanded = isExpanded2,
                onDismissRequest = { isExpanded2 = false },
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                locations.forEachIndexed{ index, location->
                        DropdownMenuItem(
                            text = { Text(text = location.name)},
                            onClick = {
                                selectedLocation = location.name
                                selectedIndex = index
                                isExpanded2 = false
                                pointsOfInterest = pointsOfInterestViewModel.getPointsFromLocation(location.name)
                            }
                        )
                }
            }
        }

        //TODO: (zé) box com o texto, dropdownMenu que quando se clica no texto a dizer categorias, mostra todas as categorias existentes

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(pointsOfInterest, key = { it.id }) {

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    onClick = {

                    }
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        //TODO: caso esta informação pertença a um utilizador, deve aparecer o botão de editar
                        Text(
                            text = stringResource(R.string.point_of_interest, it.name),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.description, it.description),
                            fontSize = 12.sp
                        )
                        Spacer(modifier.height(16.dp))
                        Text(text = "Localização: " + it.locations.toString(), fontSize = 12.sp)
                        Spacer(modifier.height(16.dp))
                        Text(text = stringResource(R.string.latitude, it.lat), fontSize = 12.sp)
                        Spacer(modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.longitude, it.long),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}