package com.example.wishlistapp

sealed class Screen(val routes: String) {
    object HomeScreen: Screen("home_screen")
    object AddScreen: Screen("add_screen")
}