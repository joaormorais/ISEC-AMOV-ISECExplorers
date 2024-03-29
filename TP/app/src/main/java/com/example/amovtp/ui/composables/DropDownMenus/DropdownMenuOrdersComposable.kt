package com.example.amovtp.ui.composables.DropDownMenus

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amovtp.R
import com.example.amovtp.utils.Consts

@Composable
fun DropdownMenuOrders(
    orderFor: (String),
    itemPicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val orderNameString = stringResource(R.string.ordered_by_name)
    val orderDistanceString = stringResource(R.string.ordered_by_distance)
    val orderCategoryNameString = stringResource(R.string.order_by_category_name)
    val items = listOf(orderNameString, orderCategoryNameString, orderDistanceString)
    var isExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Button(
        onClick = {
            isExpanded = true
        },
        modifier = modifier
            .wrapContentWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = modifier
                .wrapContentWidth()
        ) {
            Text(items[selectedIndex])
            Icon(Icons.Rounded.KeyboardArrowDown, "down")
        }
    }

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false },
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

        items.forEachIndexed { index, s ->
            if (orderFor == Consts.ORDER_FOR_LOCATIONS && s == orderCategoryNameString)
                return@forEachIndexed

            DropdownMenuItem(
                text = { Text(text = s) },
                onClick = {
                    selectedIndex = index
                    isExpanded = false

                    when (s) {
                        orderNameString -> {
                            itemPicked(Consts.ORDER_BY_NAME)
                        }

                        orderCategoryNameString -> {
                            itemPicked(Consts.ORDER_BY_CATEGORY)
                        }

                        orderDistanceString -> {
                            itemPicked(Consts.ORDER_BY_DISTANCE)
                        }
                    }
                }
            )
        }
    }
}