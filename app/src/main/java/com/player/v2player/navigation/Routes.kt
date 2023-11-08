package com.player.v2player.navigation

sealed class Routes(val route : String){
    object Home : Routes("Home")
    object VideoScreen : Routes("VideoScreen")
    object FileScreen : Routes("FileScreen")
}
