package com.example.amovtp.ui.screens.infoScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import com.example.amovtp.R
import com.example.amovtp.ui.composables.DropDownMenus.DropdownMenuOrders
import com.example.amovtp.ui.screens.Screens
import com.example.amovtp.ui.viewmodels.infoViewModels.LocationsViewModel
import com.example.amovtp.utils.codes.Codes
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
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = modifier
                .wrapContentSize(Alignment.TopStart)
                .align(Alignment.CenterHorizontally)
        ) {
            DropdownMenuOrders(itemPicked = { itemPicked ->
                when (itemPicked) {
                    Codes.ORDER_BY_VOTES -> {
                        locations = locationsViewModel.getLocations().sortedBy { it.votes }
                    }

                    Codes.ORDER_BY_NAME -> {
                        locations = locationsViewModel.getLocationsOrderedByName()
                    }

                    Codes.ORDER_BY_DISTANCE -> {
                        locations = locationsViewModel.getLocationsOrderedByDistance()
                    }

                    else -> {}
                }
                coroutineScope.launch {
                    listState.animateScrollToItem(index = 0)
                }
            })
        }

        Button(
            onClick = {
                navController?.navigate(Screens.POINTS_OF_INTEREST.route)
            },
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.go_to_points_of_interest))
        }

        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
        ) {
            items(locations, key = { it.id }) {

                var isDetailExpanded by remember { mutableStateOf(false) }
                var cardColor by remember { mutableStateOf(Color.DarkGray) }
                LaunchedEffect(key1 = it.votes, block = {
                    if (it.votes < 2)
                        cardColor = Color.Magenta
                    else
                        cardColor = Color.DarkGray
                })

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = modifier
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

                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = modifier
                                .padding(start = 8.dp)
                        )
                        Spacer(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                        Button(
                            onClick = { isDetailExpanded = !isDetailExpanded },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            modifier = modifier
                                .padding(end = 8.dp)
                        ) {
                            Icon(Icons.Rounded.KeyboardArrowDown, "Details")
                        }
                    }

                    if (isDetailExpanded)
                        Column {
                            LazyRow(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(8.dp)
                            ) {

                                items(it.imgs) { img ->
                                    AsyncImage(
                                        model = img,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentDescription = "Background Image"
                                    )
                                }

                            }
                            Column(
                                modifier = modifier
                                    .padding(start = 8.dp)
                            ) {
                                Spacer(modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.latitude_description, it.lat),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.longitude_description, it.long),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.description, it.description),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(16.dp))
                                Text(
                                    text = "Votes: " + it.votes,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(16.dp))
                                if (!it.isApproved)
                                    Text(
                                        text = "This location hasn't been approved by at least 2 users",
                                        fontSize = 12.sp
                                    )
                            }
                        }

                }

            }
        }
    }
}