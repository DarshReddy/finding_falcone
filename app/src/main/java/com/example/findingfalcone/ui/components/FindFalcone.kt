package com.example.findingfalcone.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.findingfalcone.HomeViewModel
import com.example.findingfalcone.R
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.FindStatus
import com.example.findingfalcone.utils.ShowApiError
import com.example.findingfalcone.utils.UNKNOWN_ERROR_CODE

@Composable
fun FindFalcone(timeTaken: Int, homeViewModel: HomeViewModel) {
    val findResponse by homeViewModel.findResponse.observeAsState()

    LaunchedEffect(Unit) {
        homeViewModel.findFalcone()
    }
    findResponse?.let {
        when (it) {
            is ApiResponse.Error -> {
                ShowApiError(it)
            }

            is ApiResponse.Loading -> {
                Loader(loading = it.isLoading)
            }

            is ApiResponse.Success -> {
                when (FindStatus.fromStatusKey(it.responseData?.status)) {
                    FindStatus.SUCCESS -> {
                        Text(text = stringResource(R.string.find_falcone_success_message))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(
                                id = R.string.find_falcone_success_info,
                                timeTaken,
                                it.responseData?.planetName ?: ""
                            )
                        )
                    }

                    FindStatus.FAILURE -> {
                        Text(text = stringResource(id = R.string.find_falcone_failed_message))
                    }

                    else -> {
                        ShowApiError(
                            errorResponse = ApiResponse.Error(UNKNOWN_ERROR_CODE, null)
                        )
                    }
                }
            }
        }
    }
}