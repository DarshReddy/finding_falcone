package com.example.findingfalcone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.FindStatus
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.datamodels.Vehicle
import com.example.findingfalcone.ui.components.Loader
import com.example.findingfalcone.ui.theme.FindingFalconeTheme
import com.example.findingfalcone.utils.ShowApiError
import com.example.findingfalcone.utils.UNKNOWN_ERROR_CODE
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    companion object {
        const val MAX_TEAMS = 4
    }

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindingFalconeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val scrollState = rememberScrollState()
                    var reset by remember { mutableStateOf(false) }

                    Column {
                        GetToolbar {
                            reset = true
                            homeViewModel.resetSelections()
                        }
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .scrollable(state = scrollState, orientation = Orientation.Vertical)
                        ) {
                            GetPlanets(reset, homeViewModel)
                            reset = false
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetToolbar(reset: () -> Unit) = TopAppBar(
    title = {
        Text(
            stringResource(id = R.string.app_name),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    },
    actions = {
        Text(text = stringResource(R.string.reset))
        IconButton(
            onClick = {
                reset()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.reset_icon_label)
            )
        }
    },
    colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
    )
)

@Composable
fun GetPlanets(reset: Boolean, homeViewModel: HomeViewModel) {
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
                GetVehicles(it.responseData, homeViewModel)
            }
        }
    }

}

@Composable
fun GetVehicles(planets: List<Planet>?, homeViewModel: HomeViewModel) {
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
            ShowPlanetAndVehiclePerTeam(i, homeViewModel)
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
            ShowPlanetAndVehiclePerTeam(i, homeViewModel)
        }
    }
}

@Composable
fun ShowPlanetAndVehiclePerTeam(teamNo: Int, homeViewModel: HomeViewModel) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> DropDown(
    label: String,
    items: List<T>,
    getItemText: (T) -> String,
    getItemCount: ((T) -> Int)? = null,
    selectItem: (String) -> Unit,
    shouldReset: Boolean
) {
    var exp by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf("") }
    if (shouldReset) selectedOption = ""

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (exp)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { selectedOption = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text(stringResource(id = R.string.select_label, label)) },
            trailingIcon = {
                Icon(icon, stringResource(id = R.string.drop_down_arrow),
                    Modifier.clickable { exp = !exp }
                )
            }
        )

        if (items.isNotEmpty()) {
            DropdownMenu(
                modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                expanded = exp,
                onDismissRequest = { exp = false },
            ) {
                items.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.testTag(
                            stringResource(
                                id = R.string.drop_down_menu_item_tag,
                                getItemText(option)
                            )
                        ),
                        text = {
                            Text(
                                text = getItemCount?.let {
                                    stringResource(
                                        id = R.string.vehicle_info,
                                        getItemText(option),
                                        it.invoke(option)
                                    )
                                } ?: getItemText(option)
                            )
                        },
                        onClick = {
                            selectedOption = getItemText(option)
                            exp = false
                            selectItem(getItemText(option))
                        }
                    )
                }
            }
        }
    }
}