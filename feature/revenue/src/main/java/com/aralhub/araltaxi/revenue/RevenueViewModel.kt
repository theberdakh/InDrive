package com.aralhub.araltaxi.revenue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aralhub.araltaxi.core.domain.driver.GetDriverBalanceInfoUseCase
import com.aralhub.indrive.core.data.model.driver.DriverBalanceInfo
import com.aralhub.indrive.core.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RevenueViewModel @Inject constructor(private val getDriverBalanceInfoUseCase: GetDriverBalanceInfoUseCase) :
    ViewModel() {

    private val _revenueUiState = MutableStateFlow<RevenueUiState>(RevenueUiState.Loading)
    val revenueUiState = _revenueUiState.asStateFlow()

    fun getDriverBalanceInfo() = viewModelScope.launch {
        getDriverBalanceInfoUseCase().let { result ->
            _revenueUiState.value = when (result) {
                is Result.Success -> RevenueUiState.Success(result.data)
                is Result.Error -> RevenueUiState.Error(message = result.message)
            }
        }
    }
}

sealed interface RevenueUiState {
    data object Loading : RevenueUiState
    data class Success(val driverBalanceInfo: DriverBalanceInfo) : RevenueUiState
    data class Error(val message: String) : RevenueUiState
}