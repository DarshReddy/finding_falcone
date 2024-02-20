package com.example.findingfalcone.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.findingfalcone.HomeViewModel
import com.example.findingfalcone.R

@Composable
fun PlanetsAndVehiclesPerTeam(teamNo: Int, homeViewModel: HomeViewModel) {
    val planetAndVehicleNames = homeViewModel.getPlanetAndVehiclePerTeam(teamNo)
    Text(
        text = stringResource(
            id = R.string.selected_planet,
            teamNo + 1,
            planetAndVehicleNames.first
        )
    )
    Text(
        text = stringResource(
            id = R.string.selected_vehicle,
            teamNo + 1,
            planetAndVehicleNames.second
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
}