package com.example.findingfalcone.utils

import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.FindResponse
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.datamodels.TokenResponse
import com.example.findingfalcone.datamodels.Vehicle
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

fun mockPlanetsSuccessApiResponse() =
    ApiResponse.Success(responseData = listOf(Planet("earth", 330)))

fun mockVehiclesSuccessApiResponse() =
    ApiResponse.Success(responseData = listOf(Vehicle("rover", 4, 44, 55)))

fun mockTokenSuccessApiResponse() =
    ApiResponse.Success(responseData = TokenResponse("token"))

fun mockFindFalconeSuccessApiResponse() =
    ApiResponse.Success(responseData = FindResponse("earth", "found"))

fun mockErrorApiResponse() =
    ApiResponse.Error(code = 404, message = "Not found")

fun mockPlanetsSuccessResponse(): Response<List<Planet>> =
    Response.success(listOf(Planet("earth", 330)))

fun mockVehiclesSuccessResponse(): Response<List<Vehicle>> =
    Response.success(listOf(Vehicle("rover", 4, 44, 55)))

fun mockTokenSuccessResponse(): Response<TokenResponse> =
    Response.success(TokenResponse("token"))

fun mockFindFalconeSuccessResponse(): Response<FindResponse> =
    Response.success(FindResponse("earth", "found"))

fun <T> mockErrorResponse(): Response<T> =
    Response.error(404, "{ \"error\": \"Not found\" }".toResponseBody())

