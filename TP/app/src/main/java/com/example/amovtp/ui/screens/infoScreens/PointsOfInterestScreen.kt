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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.amovtp.R
import com.example.amovtp.data.LocalUser
import com.example.amovtp.data.PointOfInterest
import com.example.amovtp.ui.composables.DropDownMenus.DropdownMenuFilters
import com.example.amovtp.ui.composables.DropDownMenus.DropdownMenuOrders
import com.example.amovtp.ui.screens.Screens
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
    itemId: String,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    // utils for the lazy column
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // utils for the dropdown menus and open street map
    var isHelperExpanded by remember { mutableStateOf(false) }
    val locations by remember { pointsOfInterestViewModel.getLocations() }
    val currentLocation by remember { mutableStateOf(pointsOfInterestViewModel.getCurrentLocation()) }
    var geoPoint by remember {
        mutableStateOf(
            GeoPoint(
                currentLocation.value!!.latitude,
                currentLocation.value!!.longitude
            )
        )
    }

    // info for the card
    val pointsOfInterestDM = pointsOfInterestViewModel.getPointsOfInterest()
    var pointsOfInterestUI by remember {
        mutableStateOf<List<PointOfInterest>>(emptyList())
    }
    LaunchedEffect(key1 = pointsOfInterestDM.value, block = {
        pointsOfInterestUI = pointsOfInterestDM.value.toList()
    })

    // info for the buttons in the card
    val localUserDM =
        pointsOfInterestViewModel.getLocalUser()
    var localUserUI by remember { mutableStateOf(LocalUser()) }
    LaunchedEffect(key1 = localUserDM.value, block = {
        localUserUI = localUserDM.value
    })

    // info for the filter of a unnique location
    LaunchedEffect(key1 = itemId) { //TODO: ver se de facto é preciso um LaunchedEffet, ou não
        if (itemId != Consts.DEFAULT_VALUE) {
            pointsOfInterestUI = pointsOfInterestViewModel.getPointsFromLocation(itemId)
            val tempLoc = locations.find { it.id == itemId }
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
                    DropdownMenuOrders(
                        orderFor = Consts.ORDER_FOR_POINTS_OF_INTEREST,
                        itemPicked = { itemPicked ->
                            when (itemPicked) {
                                Consts.ORDER_BY_NAME -> {
                                    pointsOfInterestUI = pointsOfInterestUI.sortedBy { it.name }
                                }

                                Consts.ORDER_BY_CATEGORY -> {
                                    pointsOfInterestUI = pointsOfInterestUI.sortedBy { it.category }
                                }

                                Consts.ORDER_BY_DISTANCE -> {
                                    pointsOfInterestUI =
                                        pointsOfInterestViewModel.getPointsOfInterestOrderedByDistance(
                                            pointsOfInterestUI
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
                        itemIdForLocation = itemId,
                        itemsLocations = locations,
                        itemPicked = { itemPicked ->
                            pointsOfInterestUI =
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
                    Row {
                        DropdownMenuFilters(
                            pointsOfInterestViewModel = pointsOfInterestViewModel,
                            itemIdForLocation = null,
                            itemsLocations = null,
                            itemPicked = { itemPicked ->
                                pointsOfInterestUI =
                                    pointsOfInterestViewModel.getPointsWithFilters(null, itemPicked)
                            },
                            newGeoPoint = {}
                        )
                        IconButton(
                            onClick = { navController?.navigate(Screens.CATEGORIES.route) },
                        ) {
                            Icon(
                                Icons.Rounded.Info,
                                "info",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
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
                    for (i in pointsOfInterestViewModel.getPointsOfInterest().value) {
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
            items(pointsOfInterestUI, key = { it.id }) {

                var isPointOfInterestApproved by remember { mutableStateOf(it.isApproved) }
                var isDetailExpanded by remember { mutableStateOf(false) }

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 4.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    colors =
                    if (!isPointOfInterestApproved)
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
                                modifier = modifier
                                    .wrapContentWidth()
                                    .background(Color.White)
                                    .padding(bottom = 3.dp, top = 3.dp)
                            ) {
                                items(it.imgs) { img ->
                                    AsyncImage(
                                        model = img,
                                        modifier = modifier
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
                                    text = stringResource(
                                        R.string.locations_description,
                                        it.locations.toString()
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

                                if (!isPointOfInterestApproved) {
                                    Divider(color = Color.DarkGray, thickness = 1.dp)
                                    Spacer(modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.not_approved_point),
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

                                    if (localUserUI.userId != "")
                                        if (localUserUI.userId != it.userId) {
                                            if (!pointsOfInterestViewModel.findVoteForApprovedPointOfInterestByUser(
                                                    it.id
                                                )
                                            ) {
                                                Spacer(modifier.height(8.dp))
                                                Button(
                                                    onClick = {
                                                        //isVotedByUser = true
                                                        pointsOfInterestViewModel.voteForApprovalPointOfInterestByUser(
                                                            it.id
                                                        )
                                                        isPointOfInterestApproved = it.isApproved
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
                                                        //isVotedByUser = false
                                                        pointsOfInterestViewModel.removeVoteForApprovalPointOfInterestByUser(
                                                            it.id
                                                        )
                                                        isPointOfInterestApproved = it.isApproved
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
                                        }
                                    Spacer(modifier.height(8.dp))
                                } else {
                                    if (localUserUI.userId != it.userId) {

                                        val listOfClassifications = listOf(
                                            Consts.ONE_STAR_CLASSIFICATION,
                                            Consts.TWO_STAR_CLASSIFICATION,
                                            Consts.THREE_STAR_CLASSIFICATION
                                        )

                                        var mediaClassification by remember {
                                            mutableDoubleStateOf(
                                                pointsOfInterestViewModel.calculateMediaClassification(
                                                    it.id
                                                )
                                            )
                                        }

                                        var classificationFromUser by remember {
                                            mutableLongStateOf(
                                                pointsOfInterestViewModel.findClassificationFromUser(
                                                    it.id
                                                )
                                            )
                                        }

                                        Spacer(modifier.height(8.dp))
                                        Row(
                                            modifier = modifier
                                                .fillMaxWidth()
                                                .padding(end = 32.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(R.string.classify),
                                                fontSize = 12.sp
                                            )
                                            for (i in listOfClassifications) {
                                                Button(
                                                    colors =
                                                    if (classificationFromUser == i) {
                                                        ButtonDefaults.buttonColors(
                                                            containerColor = MaterialTheme.colorScheme.inversePrimary,
                                                            contentColor = Color.White
                                                        )
                                                    } else {
                                                        ButtonDefaults.buttonColors(
                                                            containerColor = MaterialTheme.colorScheme.primary,
                                                            contentColor = Color.White
                                                        )
                                                    },
                                                    onClick = {
                                                        when (i) {

                                                            Consts.ONE_STAR_CLASSIFICATION -> {
                                                                if (classificationFromUser == Consts.ONE_STAR_CLASSIFICATION) {
                                                                    pointsOfInterestViewModel.removeClassificationToPointByUser(
                                                                        it.id
                                                                    )
                                                                } else {
                                                                    pointsOfInterestViewModel.addClassificationToPointByUser(
                                                                        it.id,
                                                                        Consts.ONE_STAR_CLASSIFICATION
                                                                    )
                                                                }
                                                                classificationFromUser =
                                                                    pointsOfInterestViewModel.findClassificationFromUser(
                                                                        it.id
                                                                    )
                                                                mediaClassification =
                                                                    pointsOfInterestViewModel.calculateMediaClassification(
                                                                        it.id
                                                                    )
                                                            }

                                                            Consts.TWO_STAR_CLASSIFICATION -> {
                                                                if (classificationFromUser == Consts.TWO_STAR_CLASSIFICATION) {
                                                                    pointsOfInterestViewModel.removeClassificationToPointByUser(
                                                                        it.id
                                                                    )
                                                                } else {
                                                                    pointsOfInterestViewModel.addClassificationToPointByUser(
                                                                        it.id,
                                                                        Consts.TWO_STAR_CLASSIFICATION
                                                                    )
                                                                }
                                                                classificationFromUser =
                                                                    pointsOfInterestViewModel.findClassificationFromUser(
                                                                        it.id
                                                                    )
                                                                mediaClassification =
                                                                    pointsOfInterestViewModel.calculateMediaClassification(
                                                                        it.id
                                                                    )
                                                            }

                                                            Consts.THREE_STAR_CLASSIFICATION -> {
                                                                if (classificationFromUser == Consts.THREE_STAR_CLASSIFICATION) {
                                                                    pointsOfInterestViewModel.removeClassificationToPointByUser(
                                                                        it.id
                                                                    )
                                                                } else {
                                                                    pointsOfInterestViewModel.addClassificationToPointByUser(
                                                                        it.id,
                                                                        Consts.THREE_STAR_CLASSIFICATION
                                                                    )
                                                                }
                                                                classificationFromUser =
                                                                    pointsOfInterestViewModel.findClassificationFromUser(
                                                                        it.id
                                                                    )
                                                                mediaClassification =
                                                                    pointsOfInterestViewModel.calculateMediaClassification(
                                                                        it.id
                                                                    )
                                                            }

                                                        }
                                                    },
                                                ) {
                                                    Row {
                                                        Text(i.toString())
                                                        Icon(
                                                            Icons.Rounded.Star,
                                                            "Classification",
                                                            modifier = modifier.padding(start = 8.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Text(
                                            text = stringResource(
                                                R.string.classification_media,
                                                mediaClassification
                                            ),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                if (localUserUI.userId == it.userId) {
                                    Spacer(modifier.height(8.dp))
                                    Button(
                                        onClick = { navController?.navigate("EditPointOfInterest?itemName=${it.id}") },
                                    ) {
                                        Row {
                                            Text(stringResource(R.string.edit))
                                            Icon(
                                                Icons.Rounded.Edit,
                                                "Edit",
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