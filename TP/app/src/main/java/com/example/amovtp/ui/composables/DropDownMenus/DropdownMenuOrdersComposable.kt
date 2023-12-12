package com.example.amovtp.ui.composables.DropDownMenus

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.amovtp.R
import com.example.amovtp.utils.codes.Codes

@Composable
fun DropdownMenuOrders(
    itemPicked: (Codes) -> Unit,
    modifier: Modifier = Modifier
) {
    val orderVotesString = stringResource(R.string.ordered_by_votes)
    val orderNameString = stringResource(R.string.ordered_by_name)
    val orderDistanceString = stringResource(R.string.ordered_by_distance)
    val items = listOf(orderVotesString, orderNameString, orderDistanceString)
    var isExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Text(
        items[selectedIndex],
        modifier = modifier
            .wrapContentWidth()
            .clickable(onClick = { isExpanded = true })
    )

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false },
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

        items.forEachIndexed { index, s ->
            DropdownMenuItem(
                text = { Text(text = s) },
                onClick = {
                    selectedIndex = index
                    isExpanded = false

                    when (s) {
                        orderVotesString -> {
                            itemPicked(Codes.ORDER_BY_VOTES)
                        }

                        orderNameString -> {
                            itemPicked(Codes.ORDER_BY_NAME)
                        }

                        orderDistanceString -> {
                            itemPicked(Codes.ORDER_BY_DISTANCE)
                        }
                    }
                }
            )
        }
    }
}