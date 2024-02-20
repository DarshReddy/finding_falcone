package com.example.findingfalcone.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.findingfalcone.HomeViewModel
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.utils.ShowApiError

@Composable
fun Planets(reset: Boolean, homeViewModel: HomeViewModel) {
    val planets by homeViewModel.planets.observeAsState()

    if (reset) {
        homeViewModel.getPlanets()
    }

    LaunchedEffect(Unit) {
        homeViewModel.getPlanets()
    }
    planets?.let {
        when (it) {
            is ApiResponse.Error -> {
                ShowApiError(it)
            }

            is ApiResponse.Loading -> {
                Loader(loading = it.isLoading)
            }

            is ApiResponse.Success -> {
                Vehicles(it.responseData, homeViewModel)
            }
        }
    }

}