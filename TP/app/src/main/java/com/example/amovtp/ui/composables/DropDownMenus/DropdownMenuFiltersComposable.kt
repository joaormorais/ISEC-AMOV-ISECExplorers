package com.example.amovtp.ui.composables.DropDownMenus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amovtp.R
import com.example.amovtp.data.Category
import com.example.amovtp.data.Location
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import com.example.amovtp.utils.codes.Codes
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
                if (itemNameForLocation != Codes.DEFAULT_VALUE.toString())
                    itemNameForLocation!!
                else
                    allLocationsString
            )
        }

        Text(
            selectedItem,
            modifier = modifier
                .wrapContentWidth()
                .clickable(onClick = { isExpanded = true })
        )
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
                    itemPicked(Codes.ALL_LOCATIONS.toString())
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

        Text(
            selectedItem,
            modifier = modifier
                .wrapContentWidth()
                .clickable(onClick = { isExpanded = true })
        )
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
                    itemPicked(Codes.ALL_CATEGORIES.toString())
                }
            )

            categories.sortedBy { it.name }.forEachIndexed { index, category ->

                //TODO FAZER UMA ROW DENTRO DA ROW TEM UMA ASYNC IMAGE E DEPOIS DO ASYNC IMAGE TEM O DROPDOWN MENU ITEM
                DropdownMenuItem(
                    text = { Text(text = category.name) },
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