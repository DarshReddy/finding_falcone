package com.example.findingfalcone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.findingfalcone.ui.components.Planets
import com.example.findingfalcone.ui.components.Toolbar
import com.example.findingfalcone.ui.theme.FindingFalconeTheme
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
                        Toolbar {
                            reset = true
                            homeViewModel.resetSelections()
                        }
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .scrollable(state = scrollState, orientation = Orientation.Vertical)
                        ) {
                            Planets(reset, homeViewModel)
                            reset = false
                        }
                    }
                }
            }
        }
    }
}
