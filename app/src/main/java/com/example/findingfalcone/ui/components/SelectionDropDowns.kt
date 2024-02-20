package com.example.findingfalcone.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.findingfalcone.HomeActivity
import com.example.findingfalcone.HomeViewModel
import com.example.findingfalcone.R
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.datamodels.Vehicle

@Composable
fun SelectionDropDowns(
    planets: MutableList<Planet>,
    vehicles: MutableList<Vehicle>,
    homeViewModel: HomeViewModel
) {
    var findFalcone by remember { mutableStateOf(false) }

    var count by remember { mutableStateOf(0) }

    var selectedVehicleName by remember { mutableStateOf("") }

    var selectedPlanetName by remember { mutableStateOf("") }

    if (count == HomeActivity.MAX_TEAMS) {
        var timeTaken = 0
        for (i in 0 until count) {
            PlanetsAndVehiclesPerTeam(i, homeViewModel)
            timeTaken += homeViewModel.timeTakenPerTeam(i)
        }
        if (findFalcone) {
            FindFalcone(timeTaken = timeTaken, homeViewModel)
            return
        }
        Text(text = stringResource(id = R.string.total_time_taken, timeTaken))
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { findFalcone = true }) {
            Text(text = stringResource(id = R.string.find_falcone))
        }
        return
    }

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
    {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.select_for_team_message, count + 1)
        )
    }
    DropDown(
        label = stringResource(id = R.string.label_planet),
        items = planets,
        getItemText = { planet -> planet.name },
        selectItem = {
            selectedPlanetName = it
        },
        shouldReset = selectedPlanetName.isEmpty()
    )
    Spacer(modifier = Modifier.height(12.dp))
    if (selectedPlanetName.isNotEmpty()) {
        DropDown(
            label = stringResource(id = R.string.label_vehicle),
            items = homeViewModel.filterVehicles(vehicles),
            getItemText = { vehicle -> vehicle.name },
            getItemCount = { vehicle -> vehicle.totalNo },
            selectItem = {
                selectedVehicleName = it
            },
            shouldReset = selectedVehicleName.isEmpty()
        )
    }
    if (selectedPlanetName.isNotEmpty() && selectedVehicleName.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    planets.firstOrNull { planet -> planet.name == selectedPlanetName }
                        ?.let { planet ->
                            homeViewModel.selectPlanet(planet)
                            planets.remove(planet)
                        }
                    vehicles.indexOfFirst { vehicle -> vehicle.name == selectedVehicleName }
                        .let { vehicleIndex ->
                            if (vehicleIndex > -1) {
                                vehicles[vehicleIndex].totalNo--
                                homeViewModel.selectVehicle(vehicles[vehicleIndex])
                            }
                        }
                    count++
                    selectedPlanetName = ""
                    selectedVehicleName = ""
                },
            ) {
                Text(text = stringResource(id = R.string.proceed))
            }
        }
    }
    if (!findFalcone) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(id = R.string.current_selected_teams, count))
        Spacer(modifier = Modifier.height(12.dp))
        for (i in 0 until count) {
            PlanetsAndVehiclesPerTeam(i, homeViewModel)
        }
    }
}