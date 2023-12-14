package com.example.amovtp.ui.screens.infoScreens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.amovtp.R
import com.example.amovtp.ui.composables.DropDownMenus.DropdownMenuFilters
import com.example.amovtp.ui.composables.DropDownMenus.DropdownMenuOrders
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import com.example.amovtp.utils.codes.Codes
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
    val categories = pointsOfInterestViewModel.getCategories() // every category
    val locations by remember { mutableStateOf(pointsOfInterestViewModel.getLocations()) } // locations used to filter the points of interest
    var pointsOfInterest by remember { mutableStateOf(pointsOfInterestViewModel.getPointsOfInterest()) } // points of interest being shown to the user (with or without filters)


    LaunchedEffect(key1 = currentLocation) {
        geoPoint = GeoPoint(currentLocation.value!!.latitude, currentLocation.value!!.longitude)
    }

    LaunchedEffect(key1 = itemName) {
        if (itemName != Codes.DEFAULT_VALUE.toString()) {
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

        Row {

            Box(
                modifier = modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(end = 8.dp)
            ) {

                DropdownMenuFilters(
                    pointsOfInterestViewModel = pointsOfInterestViewModel,
                    itemNameForLocation = itemName,
                    itemsLocations = locations,
                    itemsCategory = null,
                    itemPicked = { itemPicked ->
                        pointsOfInterest = pointsOfInterestViewModel.getPointsWithFilters(itemPicked, null)
                    },
                    newGeoPoint = { newGeoPoint ->
                        geoPoint = newGeoPoint
                    }
                )
            }

            Box(
                modifier = modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(start = 8.dp)
                    .padding(end = 8.dp)
            ) {

                DropdownMenuFilters(
                    pointsOfInterestViewModel = pointsOfInterestViewModel,
                    itemNameForLocation = null,
                    itemsLocations = null,
                    itemsCategory = categories,
                    itemPicked = { itemPicked ->
                        pointsOfInterest = pointsOfInterestViewModel.getPointsWithFilters(null, itemPicked)
                    },
                    newGeoPoint = {}
                )
            }

            Box(
                modifier = modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(start = 8.dp)
                    .padding(end = 8.dp)
            ) {
                DropdownMenuOrders(itemPicked = { itemPicked ->
                    when (itemPicked) {
                        Codes.ORDER_BY_VOTES -> {
                            pointsOfInterest = pointsOfInterest.sortedBy { it.votes }
                        }

                        Codes.ORDER_BY_NAME -> {
                            pointsOfInterest = pointsOfInterest.sortedBy { it.name }
                        }

                        Codes.ORDER_BY_DISTANCE -> {
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
        }

        Box(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxSize(0.5f)
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
            modifier = modifier
                .fillMaxSize()
        ) {
            items(pointsOfInterest, key = { it.id }) {

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
                        geoPoint = GeoPoint(it.lat, it.long)
                    }
                ) {

                    Column(
                        modifier = modifier
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
                            text = "Category: " + it.category,
                            fontSize = 12.sp
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