package com.aralhub.indrive.request

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.indrive.request.navigation.BottomSheetNavigator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*Another solution to host bottom sheet, however it shows on top of all fragment by making top buttons non-clickable even they are transparent.*/
@AndroidEntryPoint
class BottomSheetHostFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_host) {
    @Inject
    lateinit var bottomSheetNavigator: BottomSheetNavigator
    private var navController: NavController? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        initDialog(dialog)
        return dialog
    }
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    @SuppressLint("ClickableViewAccessibility")
    private fun initDialog(dialog: BottomSheetDialog) {
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnDismissListener {
            Log.i("Host", "dismissListener $it")
        }
        dialog.setOnCancelListener {
            Log.i("Host", "cancelListener $it")
        }
        dialog.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                onBackPressed()
                return@setOnKeyListener true
            }
            false
        }
        dialog.setOnShowListener {
            Log.i("Host", "Show")
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("BottomSheetHost", "OnCreated")
        view.setBackgroundResource(android.R.color.transparent)
        initBottomNavController()
        initBottomSheetBehavior()
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isHideable = true
            isDraggable = false
        }
    }

    fun hideBottomSheet() {
        bottomSheetBehavior?.let {
            it.isHideable = true
            it.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    fun showBottomSheet() {
        bottomSheetBehavior?.let {
            it.isHideable = false
            it.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    private fun initBottomNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.bottom_sheet_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        navController?.let {
            bottomSheetNavigator.bind(navController!!)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun onBackPressed() {
        navController?.let {
            if (navController!!.currentDestination?.id == R.id.bottom_sheet_nav_host) {
                // There are no more fragments in the back stack, so dismiss the BottomSheetHostFragment
                dismiss()
            } else {
                // Navigate back within the fragments in the NavHostFragment
                navController!!.navigateUp()
            }
        }


    }

    override fun onDestroy() {
        bottomSheetNavigator.unbind()
        super.onDestroy()

    }
}