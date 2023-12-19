package com.example.amovtp.ui.screens.infoScreens

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
                Icon(Icons.Rounded.KeyboardArrowRight, "Details")
            }
        }
        Text(stringResource(R.string.order_by), modifier = modifier.padding(top = 8.dp))
        Box(
            modifier = modifier
                .wrapContentSize(Alignment.TopStart)
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

        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(locations, key = { it.id }) {

                var isDetailExpanded by remember { mutableStateOf(false) }
                var isVotedByUser by remember {
                    mutableStateOf(
                        locationsViewModel.findVoteForApprovedLocation(it.id)
                    )
                }
                var isLocationApproved by remember { mutableStateOf(it.isApproved) }

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 4.dp),
                    colors =
                    if (!isLocationApproved)
                        CardDefaults.cardColors(
                            containerColor = Color(225, 120, 120, 255),
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
                            modifier = modifier
                                .padding(end = 8.dp)
                        ) {
                            Icon(Icons.Rounded.KeyboardArrowDown, "Details")
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
                                    text = stringResource(R.string.latitude_description, it.lat),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.longitude_description, it.long),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.description, it.description),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(8.dp))
                                if (!isVotedByUser) {
                                    //TODO: quem criou a localização não deve poder aprovar a mesma
                                    Divider(color = Color.Black, thickness = 1.dp)
                                    Spacer(modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.not_approved_location),
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.votes_for_approval) + it.votes,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            isVotedByUser = true
                                            locationsViewModel.voteForApprovalLocation(it.id)
                                            isLocationApproved = it.isApproved
                                        },
                                    ) {
                                        Row {
                                            Text("Approve")
                                            Icon(Icons.Rounded.ThumbUp, "Details")
                                        }
                                    }
                                }
                            }
                        }

                }

            }
        }
    }
}
