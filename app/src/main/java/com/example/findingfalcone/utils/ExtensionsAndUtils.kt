package com.example.findingfalcone.utils

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import com.example.findingfalcone.R
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.ErrorResponse
import com.squareup.moshi.Moshi
import retrofit2.Response
import java.net.UnknownHostException

const val NETWORK_ERROR_CODE = 100
const val UNKNOWN_ERROR_CODE = -1

fun Context.getErrorMessage(statusCode: Int?): String {
    return when (statusCode) {
        in 100..199 -> getString(R.string.text_no_internet_connection)
        in 400..401 -> getString(R.string.text_authentication_error)
        in 402..499 -> getString(R.string.text_something_went_wrong)
        in 500..599 -> getString(R.string.text_server_not_responding)
        else -> getString(R.string.text_something_went_wrong)
    }
}

fun <T : Any> MutableLiveData<ApiResponse<T>>.setLoading(loading: Boolean) {
    this.value = ApiResponse.Loading(isLoading = loading)
}

inline fun <reified T : Any> invokeApiCall(
    mutableLiveData: MutableLiveData<ApiResponse<T>>?,
    call: () -> Response<*>
): ApiResponse<T> {
    var code: Int? = null
    try {
        mutableLiveData?.setLoading(loading = true)
        call.invoke().let { response ->
            code = response.code()
            return if (response.isSuccessful) {
                mutableLiveData?.setLoading(loading = false)
                ApiResponse.Success(responseData = response.body() as T)
            } else {
                mutableLiveData?.setLoading(loading = false)
                ApiResponse.Error(
                    code = response.code(),
                    message = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                        .fromJson(response.errorBody()?.string().toString())?.error
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return if (e is UnknownHostException)
            ApiResponse.Error(code = NETWORK_ERROR_CODE, null)
        else {
            ApiResponse.Error(code = code ?: UNKNOWN_ERROR_CODE, null)
        }
    }
}

@Composable
fun ShowApiError(errorResponse: ApiResponse.Error) {
    Text(text = errorResponse.message ?: LocalContext.current.getErrorMessage(errorResponse.code))
}