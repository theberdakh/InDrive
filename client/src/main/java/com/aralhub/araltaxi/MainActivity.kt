package com.aralhub.araltaxi

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.araltaxi.client.R
import com.aralhub.araltaxi.navigation.Navigator
import com.aralhub.network.utils.LocalStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var navigator: Navigator
    @Inject lateinit var localStorage: LocalStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(com.aralhub.ui.R.style.Theme_InDrive)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPadding()

        if (localStorage.isLogin){
            setStartDestination(R.id.requestFragment)
        } else {
            setStartDestination(R.id.logoFragment)
        }

    }

    private fun setStartDestination(fragment: Int) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(fragment)
        navController.graph = graph
    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_nav_host)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        navigator.bind(findNavController(R.id.main_nav_host))
    }

    override fun onPause() {
        navigator.unbind()
        super.onPause()
    }
}