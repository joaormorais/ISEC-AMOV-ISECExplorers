package com.example.amovtp.ui.screens.infoScreens

import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import com.example.amovtp.R
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointsOfInterestScreen(
    pointsOfInterestViewModel: PointsOfInterestViewModel,
    itemName: String? = null,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val allString = stringResource(R.string.all)
    val defaultString = stringResource(R.string.defaultvalue)
    val latCenterPT = 39.694460831786216
    val longCenterPT = -8.130543343335995

    var pointsOfInterest by remember {
        mutableStateOf(pointsOfInterestViewModel.getPointsOfInterest())
    }
    val selectedLocation by remember {
        mutableStateOf(

            if (itemName != defaultString) {
                pointsOfInterestViewModel.getLocations().find { it.name == itemName }
            } else {
                null
            }

        )
    }


    var isExpanded by remember { mutableStateOf(false) }
    var selectedLocationName by remember {
        mutableStateOf(

            if (selectedLocation != null)
                selectedLocation!!.name
            else
                allString
        )
    }
    var selectedIndex by remember { mutableStateOf(0) }

    var geoPoint by remember {
        mutableStateOf(

            if (itemName != defaultString)
                GeoPoint(selectedLocation!!.lat, selectedLocation!!.long)
            else
                GeoPoint(latCenterPT, longCenterPT)

        )
    }


    LaunchedEffect(key1 = itemName) {
        if (itemName != null)
            if (itemName != defaultString)
                pointsOfInterest = pointsOfInterestViewModel.getPointsFromLocation(itemName)
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                selectedLocationName!!,
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable(onClick = { isExpanded = true })
            )

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .wrapContentWidth()
                    .heightIn(max = 200.dp)
                    .wrapContentHeight(Alignment.Top)
            ) {
                DropdownMenuItem(
                    text = { Text(allString) },
                    onClick = {
                        selectedLocationName = allString
                        selectedIndex = 0
                        isExpanded = false
                        pointsOfInterest =
                            pointsOfInterestViewModel.getPointsOfInterest()
                    }
                )
                pointsOfInterestViewModel.getLocations().forEachIndexed { index, location ->
                    DropdownMenuItem(
                        text = { Text(text = location.name) },
                        onClick = {
                            selectedLocationName = location.name
                            selectedIndex = index + 1
                            isExpanded = false
                            pointsOfInterest =
                                pointsOfInterestViewModel.getPointsFromLocation(location.name)
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
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
                    modifier = Modifier
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