package com.example.amovtp.ui.screens.infoScreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.amovtp.data.Location
import com.example.amovtp.ui.composables.DropDownMenus.DropdownMenuOrders
import com.example.amovtp.ui.screens.Screens
import com.example.amovtp.ui.viewmodels.infoViewModels.LocationsViewModel
import com.example.amovtp.utils.Consts
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    locationsViewModel: LocationsViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    val locationsDM = locationsViewModel.getLocations()
    var locationsUI by remember {
        mutableStateOf<List<Location>>(emptyList())
    }
    LaunchedEffect(key1 = locationsDM.value, block = {
        locationsUI = locationsDM.value.toList()
    })
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = modifier.padding(top = 8.dp),
            onClick = {
                navController?.navigate(Screens.POINTS_OF_INTEREST.route)
            }
        ) {
            Row {
                Text(stringResource(R.string.go_to_points_of_interest))
                Icon(Icons.Rounded.KeyboardArrowRight, "Next Screen")
            }
        }
        Text(stringResource(R.string.order_by), modifier = modifier.padding(top = 8.dp))
        Box(
            modifier = modifier
                .wrapContentSize(Alignment.TopStart)
        ) {
            DropdownMenuOrders(
                orderFor = Consts.ORDER_FOR_LOCATIONS,
                itemPicked = { itemPicked ->
                    when (itemPicked) {
                        Consts.ORDER_BY_NAME -> {
                            locationsUI = locationsUI.sortedBy { it.name }
                            /*Log.d("LocationnsScreen", "locations aqui = " + locationsUI)
                            Log.d(
                                "LocationnsScreen",
                                "locations no modelo de dados = " + locationsViewModel.getLocations()
                            )*/
                        }

                        Consts.ORDER_BY_DISTANCE -> {
                            locationsUI = locationsViewModel.getLocationsOrderedByDistance()
                            /*Log.d("LocationnsScreen", "locations aqui = " + locationsUI)
                            Log.d(
                                "LocationnsScreen",
                                "locations no modelo de dados = " + locationsViewModel.getLocations()
                            )*/
                        }

                        else -> {}
                    }
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 0)
                    }
                })
        }

        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(locationsUI) {

                var isDetailExpanded by remember { mutableStateOf(false) }
                var isVotedByUser by remember {
                    mutableStateOf(
                        locationsViewModel.findVoteForApprovedLocationByUser(it.name)
                    )
                }
                var isLocationApproved by remember { mutableStateOf(it.isApproved) }

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 4.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    colors =
                    if (!isLocationApproved)
                        CardDefaults.cardColors(
                            containerColor = Consts.NOT_APPROVED_COLOR,
                            contentColor = Color.White
                        )
                    else
                        CardDefaults.cardColors(
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
                        Column(
                            modifier = modifier.weight(1f)
                        ) {
                            Text(
                                text = it.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = modifier
                                    .padding(start = 8.dp, end = 8.dp)
                            )
                        }
                        Spacer(modifier.width(12.dp))
                        Column {
                            Button(
                                onClick = { isDetailExpanded = !isDetailExpanded },
                                modifier = modifier
                                    .padding(start = 8.dp, end = 8.dp)
                            ) {
                                Icon(Icons.Rounded.KeyboardArrowDown, "Details")
                            }
                        }
                    }

                    if (isDetailExpanded)
                        Column {
                            Spacer(modifier.height(8.dp))
                            LazyRow(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .background(Color.White)
                                    .padding(bottom = 3.dp, top = 3.dp)
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
                                Spacer(modifier.height(8.dp))
                                Text(
                                    text = stringResource(
                                        R.string.points_of_interest_description,
                                        it.pointsOfInterest.toString()
                                    ),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.latitude_description, it.lat),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = stringResource(R.string.longitude_description, it.long),
                                    fontSize = 12.sp
                                )
                                if (it.isManualCoords) {
                                    Text(
                                        text = stringResource(R.string.manual_coordinates),
                                        fontSize = 12.sp
                                    )
                                } else {
                                    Text(
                                        text = stringResource(R.string.automatically_coordinates),
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.description, it.description),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(8.dp))
                                if (!isLocationApproved) {
                                    //TODO: quem criou a localização não deve poder aprovar a mesma
                                    Divider(color = Color.DarkGray, thickness = 1.dp)
                                    Spacer(modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.not_approved_location),
                                        fontSize = 12.sp,
                                        color = Consts.WARNING_COLOR
                                    )
                                    Spacer(modifier.height(8.dp))
                                    Text(
                                        text = stringResource(
                                            R.string.votes_for_approval,
                                            it.votesForApproval
                                        ),
                                        fontSize = 12.sp
                                    )
                                    if (!isVotedByUser) {
                                        Spacer(modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                isVotedByUser = true
                                                locationsViewModel.voteForApprovalLocationByUser(it.name)
                                                isLocationApproved = it.isApproved
                                            },
                                        ) {
                                            Row {
                                                Text(stringResource(R.string.approve))
                                                Icon(
                                                    Icons.Rounded.ThumbUp,
                                                    "Approve",
                                                    modifier = modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                    } else {
                                        Spacer(modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                isVotedByUser = false
                                                locationsViewModel.removeVoteForApprovalLocationByUser(
                                                    it.name
                                                )
                                                isLocationApproved = it.isApproved
                                            },
                                        ) {
                                            Row {
                                                Text(stringResource(R.string.disapprove))
                                                Icon(
                                                    Icons.Rounded.Close,
                                                    "Disapprove",
                                                    modifier = modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier.height(8.dp))
                                }
                            }
                        }
                }
            }
        }
    }
}
