package com.aralhub.araltaxi.request.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.aralhub.araltaxi.request.adapter.LocationItemAdapter
import com.aralhub.araltaxi.request.model.LocationItem
import com.aralhub.araltaxi.request.navigation.sheet.FeatureRequestBottomSheetNavigation
import com.aralhub.indrive.request.R
import com.aralhub.indrive.request.databinding.BottomSheetRequestTaxiBinding
import com.aralhub.ui.components.EndTextEditText
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class RequestTaxiBottomSheet : Fragment(R.layout.bottom_sheet_request_taxi) {
    private val binding by viewBinding(BottomSheetRequestTaxiBinding::bind)
    private val locationItemAdapter = LocationItemAdapter()
    private val locationItems = listOf(
        LocationItem(
            id = 1,
            title = "Home",
            subtitle = "123 Main St"
        ),
        LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        ), LocationItem(
            id = 1,
            title = "Home",
            subtitle = "123 Main St"
        ),
        LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        ), LocationItem(
            id = 1,
            title = "Home",
            subtitle = "123 Main St"
        ),
        LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        ), LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        ), LocationItem(
            id = 1,
            title = "Home",
            subtitle = "123 Main St"
        ),
        LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        )
    )
    @Inject
    lateinit var navigator: FeatureRequestBottomSheetNavigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLocations.adapter = locationItemAdapter
        setEditTexts(binding.etFromLocation, binding.etToLocation)
        binding.etFromLocation.setEndTextClickListener {
            navigator.goToSelectLocationFromRequestTaxi()
        }
        binding.etToLocation.setEndTextClickListener {
             navigator.goToSendOrderFromRequestTaxi()
        }
    }

    private fun setEditTexts(vararg endTexts: EndTextEditText) {
        for (endText in endTexts) {
            endText.setOnActivatedListener { isActivated ->
                endText.setEndTextVisible(isActivated)
            }
            endText.setOnTextChangedListener {
                if (it.isNotEmpty()) {
                    binding.rvLocations.isVisible = true
                    locationItemAdapter.submitList(locationItems)
                } else {
                    locationItemAdapter.submitList(emptyList())
                }
            }
        }
    }
}