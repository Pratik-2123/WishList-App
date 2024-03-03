package com.example.wishlistapp

import android.annotation.SuppressLint
import android.text.Layout.Alignment
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.ui.theme.darkpurple
import com.example.wishlistapp.ui.theme.lightpurple
import com.example.wishlistapp.ui.theme.verylightpurple

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel
) {
    val context = LocalContext.current
        Scaffold(
            topBar = { AppBarView(title = "Make Your Wish", {
                Toast.makeText(context,"Button CLicked", Toast.LENGTH_LONG).show()
            }) },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(20.dp)
                        .clip(CircleShape),
                    contentColor = darkpurple,
                    containerColor = Color.White,
                    onClick = {
                        navController.navigate(Screen.AddScreen.routes + "/0L")
                    }
                    ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription =null)
                }
            }
        ) {
            val wishlist = viewModel.getAllWishes.collectAsState(initial = listOf())
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(verylightpurple)
            ) {
                items(wishlist.value, key = { wish -> wish.id }) {
                    wish ->

                    val dismissState = rememberDismissState(
                        confirmStateChange = {dismissvalue->
                            if(dismissvalue == DismissValue.DismissedToEnd || dismissvalue == DismissValue.DismissedToStart) {
                                viewModel.deleteWish(wish)
                            }
                            true
                        }
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        background = {
                                     val color by animateColorAsState(
                                         targetValue = verylightpurple,
                                         label = ""
                                         )
                            val alignment = androidx.compose.ui.Alignment.CenterStart
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(20.dp),
                                contentAlignment = alignment
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.White)
                            }
                        },
                        directions = setOf(DismissDirection.StartToEnd),
                        dismissThresholds = {FractionalThreshold(0.25f)},
                        dismissContent = {
                            WishItem(wish = wish) {
                                val id = wish.id
                                navController.navigate(Screen.AddScreen.routes + "/$id")
                            }
                        }
                    )
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishItem(wish: Wish, onClick: () -> Unit){
    androidx.compose.material.Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .clickable {
                onClick()
            },
        elevation = 10.dp,
        backgroundColor = Color.White
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Text(text = wish.title, fontWeight = FontWeight.ExtraBold, style = TextStyle(color = Color.Black))
            Text(text = wish.description,style = TextStyle(color = Color.Black))
        }
    }
}