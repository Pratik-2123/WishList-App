package com.example.wishlistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wishlistapp.data.Wish
import kotlinx.coroutines.launch

@Composable
fun AddEditDetailView(
    id:Long,
    viewModel: WishViewModel,
    navController: NavController
) {

    val snackMessage = remember{
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if(id != 0L) {
        val wish = viewModel.getWishByID(id).collectAsState(initial = Wish(0L,"",""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    } else {
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }

    Scaffold(
        topBar = {
            AppBarView(
                title =
                if (id != 0L) {
                    stringResource(id = R.string.update_wish)
                } else {
                    stringResource(id = R.string.add_wish)
                }, {navController.navigateUp()}
            )
        }, scaffoldState = scaffoldState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(it)
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            WishTextField(
                label = "Title",
                value = viewModel.wishTitleState,
                onValueChange = {
                    viewModel.onWishTitleChanged(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            WishTextField(
                label = "Description",
                value = viewModel.wishDescriptionState,
                onValueChange = {
                    viewModel.onWishDescriptionChanged(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if(viewModel.wishTitleState.isNotEmpty() && viewModel.wishDescriptionState.isNotEmpty()) {
                    if(id != 0L ) {
                        // Update Wish
                        viewModel.updateWish(
                            Wish(
                                id = id,
                                title = viewModel.wishTitleState,
                                description = viewModel.wishDescriptionState
                            )
                        )
                    } else {
                        //add wish
                        viewModel.addWish(
                            Wish(
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                        snackMessage.value = "Wish has been Created"
                    }
                } else {
                    snackMessage.value = "Enter fields to create a Wish"
                }
                scope.launch {
                    navController.navigateUp()
                }
            }) {
                Text(
                    text = if (id != 0L) stringResource(id = R.string.update_wish)
                    else stringResource(id = R.string.add_wish)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishTextField(
    label : String,
    value : String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = colorResource(id = R.color.darkpurple), fontWeight = FontWeight.Medium)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            focusedBorderColor = colorResource(id = R.color.lightpurple),
            unfocusedBorderColor = colorResource(id = R.color.lightpurple),
            cursorColor = colorResource(id = R.color.lightpurple),
            focusedLabelColor = colorResource(id = R.color.lightpurple),
            unfocusedLabelColor = colorResource(id = R.color.lightpurple)
        )
    )
}