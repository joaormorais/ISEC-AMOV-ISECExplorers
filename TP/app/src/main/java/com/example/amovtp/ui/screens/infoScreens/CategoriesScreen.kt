package com.example.amovtp.ui.screens.infoScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.amovtp.R
import com.example.amovtp.ui.viewmodels.infoViewModels.CategoriesViewModel
import com.example.amovtp.utils.Consts

@Composable
fun CategoriesScreen(
    categoriesViewModel: CategoriesViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    // info for the card
    val categories by remember { categoriesViewModel.getCategories() }

    // info for the buttons in the card
    val localUserDM = categoriesViewModel.getLocalUser()
    var currentUserId by remember { mutableStateOf("") }
    LaunchedEffect(key1 = localUserDM.value, block = {
        currentUserId = localUserDM.value.userId
    })

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(categories, key = { it.name }) {

                var isDetailExpanded by remember { mutableStateOf(false) }
                var isVotedByUser by remember {
                    mutableStateOf(
                        categoriesViewModel.findVoteForApprovedCategoryByUser(it.name)
                    )
                }
                var isCategoryApproved by remember { mutableStateOf(it.isApproved) }

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 4.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    colors =
                    if (!isCategoryApproved)
                        CardDefaults.cardColors(
                            containerColor = Consts.NOT_APPROVED_COLOR,
                            contentColor = Color.White
                        )
                    else
                        CardDefaults.cardColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
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
                            AsyncImage(
                                model = it.img,
                                modifier = modifier
                                    .wrapContentWidth()
                                    .height(200.dp)
                                    .background(Color.White)
                                    .padding(bottom = 3.dp, top = 3.dp),
                                contentDescription = null
                            )
                            Column(
                                modifier = modifier
                                    .padding(start = 8.dp)
                            ) {
                                Spacer(modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.description, it.description),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier.height(8.dp))
                                if (!isCategoryApproved) {
                                    //TODO: quem criou a categoria não deve poder aprovar a mesma
                                    Divider(color = Color.DarkGray, thickness = 1.dp)
                                    Spacer(modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.not_approved_category),
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

                                    if (currentUserId != "")
                                        if (currentUserId != it.userId) {
                                            if (!isVotedByUser) {
                                                Spacer(modifier.height(8.dp))
                                                Button(
                                                    onClick = {
                                                        isVotedByUser = true
                                                        categoriesViewModel.voteForApprovalCategoryByUser(
                                                            it.name
                                                        )
                                                        isCategoryApproved = it.isApproved
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
                                                        isVotedByUser = false
                                                        categoriesViewModel.removeVoteForApprovalCategoryByUser(
                                                            it.name
                                                        )
                                                        isCategoryApproved = it.isApproved
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
                                        }else {
                                            Spacer(modifier.height(8.dp))
                                            Button(
                                                onClick = {navController?.navigate("EditCategory?itemName=${it.name}") },
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
}