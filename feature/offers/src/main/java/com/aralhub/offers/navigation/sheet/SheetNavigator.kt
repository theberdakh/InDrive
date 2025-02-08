package com.aralhub.offers.navigation.sheet

import androidx.navigation.NavController

interface SheetNavigator {
    fun bind(navController: NavController)
    fun unbind()
}