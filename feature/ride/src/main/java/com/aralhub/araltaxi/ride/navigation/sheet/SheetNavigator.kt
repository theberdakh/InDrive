package com.aralhub.araltaxi.ride.navigation.sheet

import androidx.navigation.NavController

interface SheetNavigator {
    fun bind(navController: NavController)
    fun unbind()
}