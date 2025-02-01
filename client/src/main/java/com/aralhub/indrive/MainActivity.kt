package com.aralhub.indrive

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.aralhub.indrive.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var navigatorImpl: Navigator
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(com.aralhub.ui.R.style.Theme_InDrive)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowModeLogger.logSoftInputMode(this)

    }

    override fun onResume() {
        super.onResume()
        navigatorImpl.bind(findNavController(R.id.main_nav_host))
    }

    override fun onPause() {
        navigatorImpl.unbind()
        super.onPause()
    }
}