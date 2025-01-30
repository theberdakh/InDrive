package com.aralhub.indrive

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        showLayoutOnTopOfSoftKeyboard()
        setPadding()

    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_nav_host)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    /**
     * For some reason, solely changing  android:windowSoftInputMode="adjustResize" does not work.*/
    private fun showLayoutOnTopOfSoftKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
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