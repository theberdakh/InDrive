package com.aralhub.araltaxi.savedplaces.saveaddress

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.saved_places.R
import com.aralhub.araltaxi.saved_places.databinding.FragmentSaveAddressBinding
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveAddressFragment: Fragment(R.layout.fragment_save_address) {
    private var name = ARG_DEFAULT_STRING
    private var address = ARG_DEFAULT_STRING
    private var latitude = ARG_DEFAULT_DOUBLE
    private var longitude = ARG_DEFAULT_DOUBLE
    private val binding by viewBinding(FragmentSaveAddressBinding::bind)
    private val viewModel by viewModels<SaveAddressViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initObservers()
        initListeners()
    }

    private fun initArgs() {
        requireArguments().getString(ARG_KEY_NAME)?.let {
            name = it
            binding.itemAddress.tvTitle.text = it
        }
        requireArguments().getString(ARG_KEY_ADDRESS)?.let {
            address = it
            binding.itemAddress.tvSubtitle.text = it
        }
        requireArguments().getDouble(ARG_KEY_LATITUDE).let {
            latitude = it
        }
        requireArguments().getDouble(ARG_KEY_LONGITUDE).let {
            longitude = it
        }
        if (latitude != ARG_DEFAULT_DOUBLE && longitude != ARG_DEFAULT_DOUBLE){
            setWebView(latitude, longitude)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView(latitude: Double, longitude: Double) {
        binding.wvMap.apply {
            settings.javaScriptEnabled = true
            settings.apply {
                domStorageEnabled = true
                loadsImagesAutomatically = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                useWideViewPort = true
                loadWithOverviewMode = true
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(request?.url.toString())
                    return true
                }
            }
            loadUrl("https://unique-banoffee-29f6df.netlify.app/?z=15&loc=$latitude%2C$longitude&hl=ru&alt=0")
        }
    }

    private fun initObservers() {
        observeState(viewModel.createAddressUiState){ createAddressUiState ->
            when(createAddressUiState){
                is CreateAddressUiState.Error -> {}
                CreateAddressUiState.Loading -> {}
                is CreateAddressUiState.Success -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun initListeners() {
        binding.flBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSave.setOnClickListener {
            val customName = binding.etPlaceName.text.toString()
            viewModel.createAddress(CreateAddressRequest(
                customName,
                address,
                latitude,
                longitude
            ))
        }
    }

    companion object {
        private const val ARG_KEY_NAME = "arg_name"
        private const val ARG_KEY_ADDRESS = "arg_address"
        private const val ARG_KEY_LATITUDE = "arg_latitude"
        private const val ARG_KEY_LONGITUDE = "arg_longitude"
        private const val ARG_DEFAULT_STRING= "arg"
        private const val ARG_DEFAULT_DOUBLE= 0.0
        fun args(name: String, address: String, latitude: Double, longitude: Double) = Bundle().apply {
            putString(ARG_KEY_NAME, name)
            putString(ARG_KEY_ADDRESS,address)
            putDouble(ARG_KEY_LATITUDE, latitude)
            putDouble(ARG_KEY_LONGITUDE, longitude)
        }
    }
}