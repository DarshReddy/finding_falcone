package com.example.findingfalcone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.FindRequestBody
import com.example.findingfalcone.datamodels.FindResponse
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.datamodels.Vehicle
import com.example.findingfalcone.network.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _planets = MutableLiveData<ApiResponse<List<Planet>>>()
    val planets: LiveData<ApiResponse<List<Planet>>> = _planets
    private val _vehicles = MutableLiveData<ApiResponse<List<Vehicle>>>()
    val vehicles: LiveData<ApiResponse<List<Vehicle>>> = _vehicles
    private val _findResponse = MutableLiveData<ApiResponse<FindResponse>>()
    val findResponse: LiveData<ApiResponse<FindResponse>> = _findResponse
    private val selectedPlanets = mutableListOf<Planet>()
    private val selectedVehicles = mutableListOf<Vehicle>()

    fun getPlanets() {
        viewModelScope.launch {
            _planets.postValue(
                homeRepository.getPlanets(_planets)
            )
        }
    }

    fun getVehicles() {
        viewModelScope.launch {
            _vehicles.postValue(
                homeRepository.getVehicles(_vehicles)
            )
        }
    }

    fun findFalcone() {
        viewModelScope.launch {
            when (val tokenResponse = homeRepository.getToken()) {
                is ApiResponse.Success -> {
                    tokenResponse.responseData?.token?.let { token ->
                        val findRequestBody = FindRequestBody(
                            token = token,
                            planetNames = selectedPlanets.map { it.name },
                            vehicleNames = selectedVehicles.map { it.name }
                        )
                        _findResponse.postValue(
                            homeRepository.findFalcone(findRequestBody, _findResponse)
                        )
                    }
                }

                is ApiResponse.Error -> {
                    _findResponse.postValue(tokenResponse)
                }

                else -> {}
            }
        }
    }

    fun filterVehicles(vehicles: MutableList<Vehicle>) = vehicles.filter {
        it.totalNo > 0 && it.maxDistance >= (selectedPlanets.lastOrNull()?.distance ?: 0)
    }

    fun timeTakenPerTeam(teamNo: Int) =
        try {
            selectedPlanets[teamNo].distance / selectedVehicles[teamNo].speed
        } catch (e: IndexOutOfBoundsException) {
            0
        }

    fun getPlanetAndVehiclePerTeam(teamNo: Int) =
        Pair(selectedPlanets[teamNo].name, selectedVehicles[teamNo].name)

    fun selectPlanet(planet: Planet) {
        selectedPlanets.add(planet)
    }

    fun selectVehicle(vehicle: Vehicle) {
        selectedVehicles.add(vehicle)
    }

    fun resetSelections() {
        selectedPlanets.clear()
        selectedVehicles.clear()
    }
}
