package com.example.amovtp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.amovtp.R
import com.example.amovtp.ui.viewmodels.PointsOfInterest
import com.example.amovtp.ui.viewmodels.PointsOfInterestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointsOfInterestScreen(
    navController: NavController,
    viewModel: PointsOfInterestViewModel,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit
) {

    val argID = navController.currentBackStackEntry?.arguments?.getString("locationId")?.toInt()
    Log.d("PointsOfInterestScreen", "aquiiiiiiiiiii argID: $argID")

    val pointsOfInterestFiltered: MutableList<PointsOfInterest> = mutableListOf()
    for (i in viewModel.pointsOfInterest)
        if (i.location.id == argID)
            pointsOfInterestFiltered.add(i)

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(pointsOfInterestFiltered, key = { it.id }) {

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
                            text = stringResource(R.string.point_of_interest, it.name),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier.height(16.dp))
                        Text(text = stringResource(R.string.description, it.description), fontSize = 12.sp)
                        Spacer(modifier.height(16.dp))
                        Text(text = stringResource(R.string.locattion, it.location.name), fontSize = 12.sp)
                        Spacer(modifier.height(16.dp))
                        Text(text = stringResource(R.string.latitude, it.lat), fontSize = 12.sp)
                        Spacer(modifier.height(16.dp))
                        Text(text = stringResource(R.string.longitude, it.long), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}