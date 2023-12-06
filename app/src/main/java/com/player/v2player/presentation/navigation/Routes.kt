package com.player.v2player.presentation.navigation

sealed class Routes(val route : String){
    object Home : Routes("Home")
    object VideoScreen : Routes("VideoScreen")
    object FileScreen : Routes("FileScreen")
}
