package com.example.findingfalcone.network

import com.example.findingfalcone.datamodels.FindRequestBody
import com.example.findingfalcone.datamodels.FindResponse
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.datamodels.TokenResponse
import com.example.findingfalcone.datamodels.Vehicle
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET(ApiConstants.PLANETS_ENDPOINT)
    suspend fun getPlanets(): Response<List<Planet>>

    @GET(ApiConstants.VEHICLES_ENDPOINT)
    suspend fun getVehicles(): Response<List<Vehicle>>

    @POST(ApiConstants.TOKEN_ENDPOINT)
    suspend fun getToken(): Response<TokenResponse>

    @POST(ApiConstants.FIND_FALCONE_ENDPOINT)
    suspend fun findFalcone(@Body findRequestBody: FindRequestBody): Response<FindResponse>
}
