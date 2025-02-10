package com.aralhub.overview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.overview.databinding.FragmentOverviewBinding
import com.aralhub.overview.navigation.FeatureOverviewNavigation
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private val binding by viewBinding(FragmentOverviewBinding::bind)
    @Inject
    lateinit var navigation: FeatureOverviewNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAcceptOrders.setOnClickListener {
            navigation.goToAcceptOrders()
        }
    }
}