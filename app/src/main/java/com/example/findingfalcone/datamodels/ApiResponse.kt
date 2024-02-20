package com.example.findingfalcone.datamodels

sealed class ApiResponse<out T : Any> {
    data class Loading(val isLoading: Boolean) : ApiResponse<Nothing>()
    data class Success<out T : Any>(val responseData: T?) : ApiResponse<T>()
    data class Error(val code: Int, val message: String?) : ApiResponse<Nothing>()
}
