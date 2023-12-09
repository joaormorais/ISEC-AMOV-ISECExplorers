package com.example.amovtp.ui.screens.infoScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.amovtp.R
import com.example.amovtp.data.Location
import com.example.amovtp.ui.screens.Screens
import com.example.amovtp.ui.viewmodels.infoViewModels.LocationsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    locationsViewModel: LocationsViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    var locations by remember {
        mutableStateOf(locationsViewModel.getLocations())
    }

    var isExpanded by remember { mutableStateOf(false) }
    val allString = stringResource(R.string.all_locations)
    val nameString = stringResource(R.string.ordered_name)
    val distanceString = stringResource(R.string.ordered_distance)
    val items = listOf(allString, nameString, distanceString)
    var selectedIndex by remember { mutableStateOf(0) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
                items[selectedIndex],
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable(onClick = { isExpanded = true })
            )

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {

                items.forEachIndexed { index, s ->
                    DropdownMenuItem(
                        text = { Text(text = s) },
                        onClick = {
                            selectedIndex = index
                            isExpanded = false

                            when (s) {

                                allString -> {
                                    locations = locationsViewModel.getLocations()
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index = 0)
                                    }
                                }

                                nameString -> {
                                    locations = locationsViewModel.getLocationsOrderedByName()
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index = 0)
                                    }
                                }

                                distanceString -> {
                                    locations = locationsViewModel.getLocationsOrderedByDistance()
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index = 0)
                                    }
                                }

                            }

                        })
                }

            }
        }

        Button(
            onClick = {
                // Navegar para PointsOfInterestScreen sem alterar setPointLocationSearch
                navController?.navigate(Screens.POINTS_OF_INTEREST.route)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text("Ver Todos os Pontos de Interesse")
        }

        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
        ) {
            items(locations, key = { it.id }) {

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
                        navController?.navigate("PointsOfInterest?itemName=${it.name}")
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
                            text = it.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.description, it.description),
                            fontSize = 12.sp
                        )
                    }

                }

            }
        }
    }
}