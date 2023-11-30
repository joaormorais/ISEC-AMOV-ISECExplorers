package com.example.amovtp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.amovtp.R
import com.example.amovtp.ui.viewmodels.LocationsViewModel

//TODO: ordenar por ordem alfabética + ordenar de acordo com a distância da nossa localização atual

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    navController: NavHostController?,
    viewModel: LocationsViewModel,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit
) {

    var isExpanded by remember { mutableStateOf(false) }
    val items = listOf("Tudo", "Ordem alfabética", "Distância")
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                items[selectedIndex],
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable(onClick = { isExpanded = true })
            )

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .wrapContentWidth()
            ) {

                items.forEachIndexed { index, s ->
                    DropdownMenuItem(
                        text = { Text(text = s) },
                        onClick = {
                            selectedIndex = index
                            isExpanded = false
                        })
                }

            }
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(viewModel.locations, key = { it.id }) {

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.Red
                    ),
                    onClick = {
                        onSelected(it.id)
                        navController?.navigate(Screens.POINTS_OF_INTEREST.route + "?locationId=${it.id}")
                    }
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {

                        //TODO: caso esta informação pertença a um utilizador, deve aparecer o botão de editar
                        Text(
                            text = stringResource(R.string.locattion, it.name),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier.height(16.dp))
                        Text(text = stringResource(R.string.description, it.description), fontSize = 12.sp)
                    }

                }

            }
        }

    }

}

@Preview
@Composable
fun LocationsScreenPreview(navController: NavHostController = rememberNavController()) {
    var viewModel: LocationsViewModel? = LocationsViewModel()
    LocationsScreen(navController, viewModel!!) {}
}