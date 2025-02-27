package com.aralhub.araltaxi.profile.driver.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.profile.driver.R
import com.aralhub.araltaxi.profile.driver.databinding.FragmentProfileBinding
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.core.data.model.driver.DriverProfileWithVehicle
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()
    private val pickMedia: ActivityResultLauncher<PickVisualMediaRequest?> = registerForActivityResult<PickVisualMediaRequest, Uri>(
        PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val file = getFileFromUri(requireContext(), uri)
            file?.let {
                viewModel.uploadImage(it)
            }
        } else {
            Log.i("PhotoPicker", "No media selected")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.getDriverProfile()
        viewModel.profileUiState.onEach {
            when(it){
                is ProfileUiState.Error -> Log.i("RequestFragment", "profileUiState: error ${it.message}")
                ProfileUiState.Loading -> Log.i("RequestFragment", "profileUiState: loading")
                is ProfileUiState.Success -> displayProfile(it.driverProfile)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.getDriverCard()
        viewModel.cardUiState.onEach {
            when(it){
                is CardUiState.Error -> Log.i("RequestFragment", "cardUiState: error ${it.message}")
                CardUiState.Loading -> Log.i("RequestFragment", "cardUiState: loading")
                is CardUiState.Success -> displayCard(it.card, it.cardHolder)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getDriverProfileWithVehicle()
        viewModel.profileWithVehicleUiState.onEach {
            when(it){
                is ProfileWithVehicleUiState.Error -> Log.i("RequestFragment", "profileWithVehicleUiState: error ${it.message}")
                ProfileWithVehicleUiState.Loading -> Log.i("RequestFragment", "profileWithVehicleUiState: loading")
                is ProfileWithVehicleUiState.Success -> displayProfileWithVehicle(it.driverProfileWithVehicle)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.uploadImageUiState.onEach {
            when(it){
                is UploadImageUiState.Error -> Log.i("ProfileFragment: uploadImage", "error: $it" )
                UploadImageUiState.Loading ->  Log.i("ProfileFragment: uploadImage", "loading: $it" )
                UploadImageUiState.Success -> viewModel.getDriverProfile()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun displayProfileWithVehicle(driverProfileWithVehicle: DriverProfileWithVehicle) {
        binding.tvCar.text = driverProfileWithVehicle.vehicleType
        binding.tvCarNumber.text = driverProfileWithVehicle.plateNumber
        binding.tvCarColor.text = driverProfileWithVehicle.color
    }

    private fun displayCard(cardNumber: String, nameOnCard: String) {
        binding.tvCard.text = cardNumber
        binding.tvCardLabel.text = nameOnCard
    }

    private fun displayProfile(driverProfile: DriverProfile) {
        Glide.with(requireContext())
            .load("https://araltaxi.aralhub.uz/${driverProfile.photoUrl}")
            .centerCrop()
            .placeholder(com.aralhub.ui.R.drawable.ic_user)
            .signature(ObjectKey(System.currentTimeMillis()))
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivAvatar)

        binding.tvName.text = driverProfile.fullName
        binding.tvPhone.text = driverProfile.phoneNumber
    }

    private fun initListeners() {
        binding.tbProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest.Builder()
                .setMediaType(PickVisualMedia.ImageOnly)
                .build())
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output -> input.copyTo(output) }
        }
        return tempFile
    }
}