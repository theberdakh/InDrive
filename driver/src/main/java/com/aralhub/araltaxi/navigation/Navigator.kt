package com.aralhub.araltaxi.navigation

import androidx.navigation.NavController

interface Navigator {
    fun bind(navController: NavController)
    fun unbind()
}