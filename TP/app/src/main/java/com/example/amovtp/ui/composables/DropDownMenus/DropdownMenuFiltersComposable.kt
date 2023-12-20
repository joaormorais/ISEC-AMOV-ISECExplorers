package com.example.amovtp.ui.composables.DropDownMenus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.amovtp.R
import com.example.amovtp.data.Category
import com.example.amovtp.data.GeoData
import com.example.amovtp.data.Location
import com.example.amovtp.ui.screens.Screens
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import com.example.amovtp.utils.Consts
import org.osmdroid.util.GeoPoint

@Composable
fun DropdownMenuFilters(
    pointsOfInterestViewModel: PointsOfInterestViewModel,
    itemNameForLocation: String?,
    itemsLocations: List<Location>?,
    itemPicked: (String) -> Unit,
    newGeoPoint: (GeoPoint) -> Unit,
    modifier: Modifier = Modifier
) {

    var isExpanded by remember { mutableStateOf(false) } // boolean for the dropdown menu

    if (itemsLocations != null) {

        val allLocationsString = stringResource(R.string.all_locations)
        var selectedItem by remember {
            mutableStateOf(
                if (itemNameForLocation != Consts.DEFAULT_VALUE)
                    itemNameForLocation!!
                else
                    allLocationsString
            )
        }

        Button(
            onClick = {
                isExpanded = true
            },
            modifier = modifier
                .wrapContentWidth()
        ) {
            Row {
                Text(selectedItem)
                Icon(Icons.Rounded.KeyboardArrowDown, "Dropdown")
            }
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = modifier
                .wrapContentWidth()
                .heightIn(max = 200.dp)
                .wrapContentHeight(Alignment.Top)
        ) {

            DropdownMenuItem(
                text = { Text(text = allLocationsString) },
                onClick = {
                    selectedItem = allLocationsString
                    isExpanded = false
                    itemPicked(Consts.ALL_LOCATIONS)
                    val currentLocation = pointsOfInterestViewModel.getCurrentLocation()
                    newGeoPoint(
                        GeoPoint(
                            currentLocation.value!!.latitude,
                            currentLocation.value!!.longitude
                        )
                    )
                }
            )

            itemsLocations.sortedBy { it.name }.forEachIndexed { index, location ->
                DropdownMenuItem(
                    text = { Text(text = location.name) },
                    onClick = {
                        selectedItem = location.name
                        isExpanded = false
                        itemPicked(location.name)
                        newGeoPoint(GeoPoint(location.lat, location.long))
                    }
                )
            }

        }


    } else {

        val allCategoriesString = stringResource(R.string.all_categories)
        val categories by remember { mutableStateOf(pointsOfInterestViewModel.getCategories()) } // every category
        var selectedItem by remember { mutableStateOf(allCategoriesString) }

        Button(
            onClick = {
                isExpanded = true
            },
            modifier = modifier
                .wrapContentWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selectedItem)
                Icon(Icons.Rounded.KeyboardArrowDown, "down")
            }
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = modifier
                .wrapContentWidth()
                .heightIn(max = 200.dp)
                .wrapContentHeight(Alignment.Top)
        ) {

            DropdownMenuItem(
                text = { Text(text = allCategoriesString) },
                onClick = {
                    selectedItem = allCategoriesString
                    isExpanded = false
                    itemPicked(Consts.ALL_CATEGORIES)
                }
            )

            categories.sortedBy { it.name }.forEachIndexed { index, category ->

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = category.img,
                        modifier = Modifier.size(30.dp),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(text = category.name)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = category.description, fontSize = 6.sp)
                            }
                        },
                        onClick = {
                            selectedItem = category.name
                            isExpanded = false
                            itemPicked(category.name)
                        }
                    )
                }
            }

        }

    }

}