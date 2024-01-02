package com.example.amovtp.ui.screens.editScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.amovtp.R
import com.example.amovtp.ui.composables.AddInfoComposables.GeoDescription
import com.example.amovtp.ui.composables.AddInfoComposables.NameDescription
import com.example.amovtp.ui.viewmodels.editViewModels.EditLocationViewModel
import com.example.amovtp.utils.Consts

@Composable
fun EditLocationScreen(
    editLocationViewModel: EditLocationViewModel,
    itemId: String,
    navController: NavHostController?,
    modifier: Modifier = Modifier
){

    val currentLocation = editLocationViewModel.getCurrentEditingLocation(itemId)
    var name by remember { mutableStateOf(currentLocation?.name ?: "") }
    var description by remember { mutableStateOf(currentLocation?.description ?: "") }
    var lat by remember { mutableStateOf(currentLocation?.lat) }
    var long by remember { mutableStateOf(currentLocation?.long) }
    var isManual by remember { mutableStateOf(currentLocation?.isManualCoords) }

    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackBar by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val unkownError = stringResource(R.string.unknown_error)
    val fillNameError = stringResource(R.string.invalid_name)
    val fillDescriptionError = stringResource(R.string.invalid_description)
    val fillCoordinatesError = stringResource(R.string.invalid_coordinates)
    val nameExistsError = stringResource(R.string.error_existing_name)
    val locationExistsError = stringResource(R.string.error_existing_location)
    val loginNeededError = stringResource(R.string.error_you_have_to_login)

    LaunchedEffect(showSnackBar) {
        if (showSnackBar) {
            snackbarHostState.showSnackbar(errorMessage ?: unkownError)
            showSnackBar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier=modifier.padding(top = 32.dp)
    ){innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item{

                NameDescription(
                    name,
                    description,
                    nameChanged = { newName ->
                        name = newName
                    },
                    descriptionChanged = { newDescription ->
                        description = newDescription
                    }
                )
                Spacer(modifier = modifier.height(8.dp))
                GeoDescription(
                    lat.toString(),
                    long.toString(),
                    isManual!!,
                    latChanged = { newLat ->
                        lat = newLat
                    },
                    longChanged = { newLong ->
                        long = newLong
                    },
                    manualChanged = { newManual ->
                        isManual = newManual
                        if (!newManual) {
                            val tempLocation = editLocationViewModel.getCurrentEditingLocation(itemId)
                            lat = tempLocation?.lat
                            long = tempLocation?.long
                        }
                    }
                )

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Consts.CONFIRMATION_COLOR, contentColor = Color.Black),
                    onClick = {
                        val validationResult = editLocationViewModel.isEditLocationValid(
                            name,
                            description,
                            lat,
                            long,
                            fillNameError,
                            fillDescriptionError,
                            fillCoordinatesError
                        ) { msg ->
                            errorMessage=msg
                        }

                        if(validationResult){
                            editLocationViewModel.editLocation(
                                currentLocation?.id!!,
                                name,
                                description,
                                lat!!,
                                long!!,
                                isManual!!,
                            ){resultMessage ->
                                when(resultMessage){
                                    Consts.SUCCESS-> {
                                        navController!!.navigateUp()
                                    }
                                    Consts.ERROR_EXISTING_NAME -> {
                                        showSnackBar = true
                                        errorMessage = nameExistsError
                                    }
                                    Consts.ERROR_EXISTING_LOCATION -> {
                                        showSnackBar = true
                                        errorMessage = locationExistsError
                                    }
                                    Consts.ERROR_NEED_LOGIN ->{
                                        showSnackBar=true
                                        errorMessage=loginNeededError
                                    }
                                    else -> {
                                        showSnackBar = true
                                        errorMessage = resultMessage
                                    }
                                }
                            }
                        }

                    },
                    modifier = modifier.padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.edit_location))
                }

            }

        }
    }

}