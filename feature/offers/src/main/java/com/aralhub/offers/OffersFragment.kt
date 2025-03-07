package com.aralhub.offers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.araltaxi.client.offers.R
import com.aralhub.araltaxi.client.offers.databinding.FragmentOffersBinding
import com.aralhub.offers.navigation.FeatureOffersNavigation
import com.aralhub.offers.navigation.sheet.SheetNavigator
import com.aralhub.ui.adapter.OfferItemAdapter
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OffersFragment : Fragment(R.layout.fragment_offers) {
    private val binding by viewBinding(FragmentOffersBinding::bind)
    @Inject lateinit var navigator: SheetNavigator
    @Inject lateinit var featureOffersNavigation: FeatureOffersNavigation
    private val offerItemAdapter by lazy { OfferItemAdapter() }
    private val viewModel by viewModels<OffersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapView()
        setUpRecyclerView()
        initBottomNavController()
        viewModel.getOffers()
        initObservers()
    }

    private fun initObservers() {
       observeState(viewModel.offersUiState){ offersUiState ->
           when(offersUiState){
               is OffersUiState.Error -> Log.e("OffersFragment", "Error: ${offersUiState.message}")
               OffersUiState.Loading -> {}
               is OffersUiState.Success -> {
                   Log.i("OffersFragment", "Success: ${offersUiState.offers}")
                   offerItemAdapter.submitList(offersUiState.offers)
               }
           }
       }
    }

    private fun setUpRecyclerView() {
        binding.rvOffers.adapter = offerItemAdapter
        offerItemAdapter.setOnItemAcceptClickListener {
            featureOffersNavigation.goToRideFragment()
        }

    }

    private fun initBottomNavController() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.offers_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let {
            navigator.bind(navController)
        }
    }

    private fun setUpMapView() {
        binding.mapView.onStart()
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                Point(42.4619, 59.6166),
                /* zoom = */ 17.0f,
                /* azimuth = */ 150.0f,
                /* tilt = */ 30.0f
            )
        )
    }

    override fun onDestroy() {
        navigator.unbind()
        super.onDestroy()
    }

}