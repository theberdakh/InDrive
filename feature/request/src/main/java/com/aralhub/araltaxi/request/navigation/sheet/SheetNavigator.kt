package com.aralhub.araltaxi.request.navigation.sheet

import androidx.navigation.NavController

interface SheetNavigator {
    fun bind(navController: NavController)
    fun unbind()
}