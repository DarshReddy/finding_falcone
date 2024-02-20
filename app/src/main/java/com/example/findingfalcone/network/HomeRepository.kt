package com.example.findingfalcone.network

import androidx.lifecycle.MutableLiveData
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.FindRequestBody
import com.example.findingfalcone.datamodels.FindResponse
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.datamodels.TokenResponse
import com.example.findingfalcone.datamodels.Vehicle
import com.example.findingfalcone.utils.invokeApiCall
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getPlanets(planets: MutableLiveData<ApiResponse<List<Planet>>>) =
        invokeApiCall(planets) { apiService.getPlanets() }

    suspend fun getVehicles(vehicles: MutableLiveData<ApiResponse<List<Vehicle>>>) =
        invokeApiCall(vehicles) { apiService.getVehicles() }

    suspend fun getToken() =
        invokeApiCall<TokenResponse>(null) { apiService.getToken() }

    suspend fun findFalcone(
        findRequestBody: FindRequestBody,
        findResponse: MutableLiveData<ApiResponse<FindResponse>>
    ) = invokeApiCall(findResponse) { apiService.findFalcone(findRequestBody) }
}
