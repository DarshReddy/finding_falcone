package com.example.findingfalcone.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.findingfalcone.HomeViewModel
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.utils.ShowApiError

@Composable
fun Vehicles(planets: List<Planet>?, homeViewModel: HomeViewModel) {
    val vehicles by homeViewModel.vehicles.observeAsState()

    LaunchedEffect(Unit) {
        homeViewModel.getVehicles()
    }

    vehicles?.let {
        when (it) {
            is ApiResponse.Error -> {
                ShowApiError(it)
            }

            is ApiResponse.Loading -> {
                Loader(loading = it.isLoading)
            }

            is ApiResponse.Success -> {
                SelectionDropDowns(
                    planets as MutableList,
                    it.responseData as MutableList,
                    homeViewModel
                )
            }
        }
    }
}