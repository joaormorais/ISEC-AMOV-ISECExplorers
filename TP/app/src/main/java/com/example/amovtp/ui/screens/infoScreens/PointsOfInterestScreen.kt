package com.example.amovtp.ui.screens.infoScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.amovtp.data.Category
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
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
    val defaultString = stringResource(R.string.defaultvalue) // string for the composable
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Main vars and vals
    val currentLocation by remember { mutableStateOf(pointsOfInterestViewModel.getCurrentLocation()) } // current location (being updated)
    var geoPoint by remember { mutableStateOf(GeoPoint(0.0, 0.0)) } // used to mark a location on tthe map
    val categories = pointsOfInterestViewModel.getCategories() // every category
    val locations by remember { mutableStateOf(pointsOfInterestViewModel.getLocations()) } // locations used to filter the points of interest
    var pointsOfInterest by remember { mutableStateOf(pointsOfInterestViewModel.getPointsOfInterest()) } // points of interest being shown to the user (with or without filters)

    // Dropdown menu for locations
    var isExpandedLocations by remember { mutableStateOf(false) } // boolean for the dropdown menu
    val allLocationsString = stringResource(R.string.all_locations) // string for the composable
    pointsOfInterestViewModel.setAllLocationsString(allLocationsString) // save the strings in the VM
    val selectedLocation by remember {
        mutableStateOf(
            if (itemName != defaultString) {
                locations.find { it.name == itemName }
            } else {
                null
            }
        )
    }
    var selectedLocationName by remember {
        mutableStateOf(
            if (selectedLocation != null)
                selectedLocation!!.name
            else
                allLocationsString
        )
    }

    // Dropdown menu for categories
    var isExpandedCategories by remember { mutableStateOf(false) } // boolean for the dropdown menu
    val allCategoriesString = stringResource(R.string.all_categories) // string for the composable
    pointsOfInterestViewModel.setAllCategoriesStringAndCategoryName(allCategoriesString) // save the strings in the VM
    val selectedCategory by remember {
        mutableStateOf<Category?>(null)
    }
    var selectedCategoryName by remember {
        mutableStateOf(
            if (selectedCategory != null)
                selectedCategory!!.name
            else
                allCategoriesString
        )
    }

    // Dropdown menu for orders
    var isExpandedOrder by remember { mutableStateOf(false) } // boolean for the dropdown menu
    val orderVotesString = stringResource(R.string.ordered_by_votes) // string for the composable
    val orderNameString = stringResource(R.string.ordered_by_name) // string for the composable
    val orderDistanceString = stringResource(R.string.ordered_by_distance) // string for the composable
    val itemsOrderList = listOf(orderVotesString, orderNameString, orderDistanceString)
    var selectedIndex by remember { mutableStateOf(0) } // index of the dropdown menu


    LaunchedEffect(key1 = currentLocation) {
        geoPoint = GeoPoint(currentLocation.value!!.latitude, currentLocation.value!!.longitude)
    }

    LaunchedEffect(key1 = itemName) {
        if (itemName != defaultString) {
            pointsOfInterest = pointsOfInterestViewModel.getPointsFromLocation(itemName)
            pointsOfInterestViewModel.setLocationNameFilter(itemName)
            geoPoint = GeoPoint(selectedLocation!!.lat, selectedLocation!!.long)
        } else
            pointsOfInterestViewModel.setLocationNameFilter(allLocationsString)
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row{

            Box(
                modifier = modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(end = 8.dp)
            ) {
                Text(
                    selectedLocationName!!,
                    modifier = modifier
                        .wrapContentWidth()
                        .clickable(onClick = { isExpandedLocations = true })
                )
                DropdownMenu(
                    expanded = isExpandedLocations,
                    onDismissRequest = { isExpandedLocations = false },
                    modifier = modifier
                        .wrapContentWidth()
                        .heightIn(max = 200.dp)
                        .wrapContentHeight(Alignment.Top)
                ) {
                    DropdownMenuItem(
                        text = { Text(allLocationsString) },
                        onClick = {
                            selectedLocationName = allLocationsString
                            isExpandedLocations = false
                            pointsOfInterest =
                                pointsOfInterestViewModel.getPointsWithFilters(
                                    allLocationsString,
                                    ""
                                )
                        }
                    )
                    locations.forEachIndexed { index, location ->
                        DropdownMenuItem(
                            text = { Text(text = location.name) },
                            onClick = {
                                selectedLocationName = location.name
                                isExpandedLocations = false
                                pointsOfInterest =
                                    pointsOfInterestViewModel.getPointsWithFilters(
                                        location.name,
                                        ""
                                    )
                                geoPoint = GeoPoint(location.lat, location.long)
                            }
                        )
                    }
                }
            }

            Box(
                modifier = modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(start = 8.dp)
                    .padding(end = 8.dp)
            ) {
                Text(
                    selectedCategoryName!!,
                    modifier = modifier
                        .wrapContentWidth()
                        .clickable(onClick = { isExpandedCategories = true })
                )
                DropdownMenu(
                    expanded = isExpandedCategories,
                    onDismissRequest = { isExpandedCategories = false },
                    modifier = modifier
                        .wrapContentWidth()
                        .heightIn(max = 200.dp)
                        .wrapContentHeight(Alignment.Top)
                ) {
                    DropdownMenuItem(
                        text = { Text(allCategoriesString) },
                        onClick = {
                            selectedCategoryName = allCategoriesString
                            isExpandedCategories = false
                            pointsOfInterest = pointsOfInterestViewModel.getPointsWithFilters(
                                "",
                                allCategoriesString
                            )
                        }
                    )
                    categories.forEachIndexed { index, categories ->
                        DropdownMenuItem(
                            text = { Text(text = categories.name) },
                            onClick = {
                                selectedCategoryName = categories.name
                                isExpandedCategories = false
                                pointsOfInterest =
                                    pointsOfInterestViewModel.getPointsWithFilters(
                                        "",
                                        categories.name
                                    )
                            }
                        )
                    }
                }
            }

            Box(
                modifier = modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(start = 8.dp)
                    .padding(end = 8.dp)
            ) {
                Text(
                    itemsOrderList[selectedIndex],
                    modifier = modifier
                        .wrapContentWidth()
                        .clickable(onClick = { isExpandedOrder = true })
                )

                DropdownMenu(
                    expanded = isExpandedOrder,
                    onDismissRequest = { isExpandedOrder = false },
                    modifier = modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                ) {
                    itemsOrderList.forEachIndexed { index, s ->
                        DropdownMenuItem(
                            text = { Text(text = s) },
                            onClick = {
                                selectedIndex = index
                                isExpandedOrder = false

                                when (s) {
                                    orderVotesString -> {
                                        pointsOfInterest = pointsOfInterest.sortedBy { it.votes }

                                    }

                                    orderNameString -> {
                                        pointsOfInterest = pointsOfInterest.sortedBy { it.name }
                                    }

                                    orderDistanceString -> {
                                        pointsOfInterest =
                                            pointsOfInterestViewModel.getPointsOfInterestOrderedByDistance(
                                                pointsOfInterest
                                            )
                                    }
                                }

                                coroutineScope.launch {
                                    listState.animateScrollToItem(index = 0)
                                }

                            })
                    }
                }
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