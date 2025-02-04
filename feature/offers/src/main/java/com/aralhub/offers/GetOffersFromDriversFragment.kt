package com.aralhub.offers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.offers.adapter.OfferItemAdapter
import com.aralhub.offers.databinding.FragmentGetOffersFromDriversBinding
import com.aralhub.offers.model.OfferItem
import com.aralhub.offers.model.OfferItemDriver
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

class GetOffersFromDriversFragment : Fragment(R.layout.fragment_get_offers_from_drivers) {
    private val binding by viewBinding(FragmentGetOffersFromDriversBinding::bind)
    private val offerItemAdapter by lazy { OfferItemAdapter() }
    private val offers = listOf(
        OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),  OfferItem(
            1,
            OfferItemDriver("Atabek", "Cobalt"),
            "20,000"
        ),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapView()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvOffers.adapter = offerItemAdapter
        offerItemAdapter.submitList(offers)
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

}