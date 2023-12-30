package com.example.amovtp.ui.screens.editScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.amovtp.ui.composables.AddInfoComposables.NameDescription
import com.example.amovtp.ui.viewmodels.editViewModels.EditCategoryViewModel
import com.example.amovtp.utils.Consts

@Composable
fun EditCategoryScreen(
    editCategoryViewModel: EditCategoryViewModel,
    itemId: String,
    navController: NavHostController?,
    modifier: Modifier = Modifier
){

    val currentCategory = editCategoryViewModel.getCurrentEditingCategory(itemId)
    var name by remember { mutableStateOf(currentCategory?.name ?: "") }
    var description by remember { mutableStateOf(currentCategory?.description ?: "") }

    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackBar by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val unkownError = stringResource(R.string.unknown_error)
    val fillNameError = stringResource(R.string.invalid_name)
    val fillDescriptionError = stringResource(R.string.invalid_description)
    val nameExistsError = stringResource(R.string.error_existing_name)
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

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Consts.CONFIRMATION_COLOR, contentColor = Color.Black),
                    onClick = {
                              val validationResult = editCategoryViewModel.isEditCategoryValid(
                                  name,
                                  description,
                                  fillNameError,
                                  fillDescriptionError
                              ){msg ->
                                  errorMessage=msg
                              }

                        if(validationResult){
                            editCategoryViewModel.editCategory(
                                currentCategory?.id!!,
                                name,
                                description
                            ){resultMessage ->
                                when(resultMessage){
                                    Consts.SUCCESS-> {
                                        navController!!.navigateUp()
                                    }
                                    Consts.ERROR_EXISTING_NAME -> {
                                        showSnackBar = true
                                        errorMessage = nameExistsError
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
                    Text(stringResource(R.string.edit_category))
                }

            }

        }
    }
}