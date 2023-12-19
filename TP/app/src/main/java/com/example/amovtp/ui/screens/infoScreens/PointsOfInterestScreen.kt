package com.example.amovtp.ui.screens.infoScreens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.amovtp.R
import com.example.amovtp.ui.composables.DropDownMenus.DropdownMenuFilters
import com.example.amovtp.ui.composables.DropDownMenus.DropdownMenuOrders
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import com.example.amovtp.utils.Consts
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointsOfInterestScreen(
    pointsOfInterestViewModel: PointsOfInterestViewModel,
    itemName: String,
    modifier: Modifier = Modifier
) {

    // Common
    var isHelperExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Main vars and vals
    val currentLocation by remember { mutableStateOf(pointsOfInterestViewModel.getCurrentLocation()) } // current location (being updated)
    var geoPoint by remember {
        mutableStateOf(
            GeoPoint(
                0.0,
                0.0
            )
        )
    } // used to mark a location on the map
    val locations by remember { mutableStateOf(pointsOfInterestViewModel.getLocations()) } // locations used to filter the points of interest
    var pointsOfInterest by remember { mutableStateOf(pointsOfInterestViewModel.getPointsOfInterest()) } // points of interest being shown to the user (with or without filters)


    LaunchedEffect(key1 = currentLocation) {
        geoPoint = GeoPoint(currentLocation.value!!.latitude, currentLocation.value!!.longitude)
    }

    LaunchedEffect(key1 = itemName) {
        if (itemName != Consts.DEFAULT_VALUE) {
            pointsOfInterest = pointsOfInterestViewModel.getPointsFromLocation(itemName)
            val tempLoc = locations.find { it.name == itemName }
            geoPoint = GeoPoint(tempLoc!!.lat, tempLoc.long)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                isHelperExpanded = !isHelperExpanded
            },
            modifier = modifier
                .wrapContentWidth()
                .padding(top = 8.dp),
        ) {
            Row {
                Text(stringResource(R.string.show_search_helpers))
                if (!isHelperExpanded)
                    Icon(Icons.Rounded.KeyboardArrowDown, "down")
                else
                    Icon(Icons.Rounded.KeyboardArrowUp, "up")
            }
        }

        if (isHelperExpanded)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.order_by), modifier = modifier.padding(top = 8.dp))
                Box(
                    modifier = modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    DropdownMenuOrders(itemPicked = { itemPicked ->
                        when (itemPicked) {
                            Consts.ORDER_BY_VOTES -> {
                                pointsOfInterest = pointsOfInterest.sortedBy { it.votes }
                            }

                            Consts.ORDER_BY_NAME -> {
                                pointsOfInterest = pointsOfInterest.sortedBy { it.name }
                            }

                            Consts.ORDER_BY_DISTANCE -> {
                                pointsOfInterest =
                                    pointsOfInterestViewModel.getPointsOfInterestOrderedByDistance(
                                        pointsOfInterest
                                    )
                            }

                            else -> {}
                        }
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)
                        }
                    })
                }
                Text(stringResource(R.string.filters), modifier = modifier.padding(top = 8.dp))
                Box(
                    modifier = modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {

                    DropdownMenuFilters(
                        pointsOfInterestViewModel = pointsOfInterestViewModel,
                        itemNameForLocation = itemName,
                        itemsLocations = locations,
                        itemPicked = { itemPicked ->
                            pointsOfInterest =
                                pointsOfInterestViewModel.getPointsWithFilters(itemPicked, null)
                        },
                        newGeoPoint = { newGeoPoint ->
                            geoPoint = newGeoPoint
                        }
                    )
                }
                Box(
                    modifier = modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    DropdownMenuFilters(
                        pointsOfInterestViewModel = pointsOfInterestViewModel,
                        itemNameForLocation = null,
                        itemsLocations = null,
                        itemPicked = { itemPicked ->
                            pointsOfInterest =
                                pointsOfInterestViewModel.getPointsWithFilters(null, itemPicked)
                        },
                        newGeoPoint = {}
                    )
                }
            }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxSize(0.5f)
                .padding(8.dp)
                .clipToBounds()
                .background(
                    Color(255, 240, 128)
                )
        ) {
            AndroidView(factory = { context ->

                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(18.0)
                    controller.setCenter(geoPoint)
                    for (i in pointsOfInterestViewModel.getPointsOfInterest()) {
                        overlays.add(
                            Marker(this).apply {
                                position = GeoPoint(i.lat, i.long)
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = i.name
                            }
                        )
                    }
                }

            },
                update = {
                    it.controller.setCenter(geoPoint)
                })
        }

        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(pointsOfInterest, key = { it.id }) {

                var isDetailExpanded by remember { mutableStateOf(false) }
                var isVotedByUser by remember {
                    mutableStateOf(
                        pointsOfInterestViewModel.findVoteForApprovedPointOfInterest(it.id)
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
                        geoPoint = GeoPoint(it.lat, it.long)
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
                                        R.string.category_description,
                                        it.category
                                    ),
                                    fontSize = 12.sp
                                )
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
                                if (!isLocationApproved) {
                                    //TODO: quem criou o ponto não deve poder aprovar a mesma
                                    Divider(color = Color.DarkGray, thickness = 1.dp)
                                    Spacer(modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.not_approved_location),
                                        fontSize = 12.sp,
                                        color = Consts.WARNING_COLOR
                                    )
                                    Spacer(modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.votes_for_approval) + it.votes,
                                        fontSize = 12.sp
                                    )
                                    if (!isVotedByUser) {
                                        Spacer(modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                isVotedByUser = true
                                                pointsOfInterestViewModel.voteForApprovalPointOfInterest(
                                                    it.id
                                                )
                                                isLocationApproved = it.isApproved
                                            },
                                        ) {
                                            Row {
                                                Text(stringResource(R.string.approve))
                                                Icon(Icons.Rounded.ThumbUp, "Details")
                                            }
                                        }
                                    } else {
                                        Spacer(modifier.height(8.dp))
                                        Text(
                                            text = stringResource(R.string.point_of_interest_voted),
                                            fontSize = 12.sp,
                                            color = Consts.CONFIRMATION_COLOR
                                        )
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