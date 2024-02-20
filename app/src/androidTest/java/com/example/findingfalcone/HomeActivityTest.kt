package com.example.findingfalcone

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.datamodels.Vehicle
import com.example.findingfalcone.ui.theme.FindingFalconeTheme
import io.mockk.mockkClass
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        homeViewModel = mockkClass(HomeViewModel::class)
    }

    @Test
    fun test_GetToolbar() {
        // Start the app
        composeTestRule.setContent {
            FindingFalconeTheme {
                GetToolbar { }
            }
        }
        composeTestRule.onNodeWithText("Finding Falcone").assertIsDisplayed()
    }

    @Test
    fun test_SelectionDropDowns() {
        // Start the app
        composeTestRule.setContent {
            FindingFalconeTheme {
                Column {
                    SelectionDropDowns(
                        planets = mutableListOf(
                            Planet("Earth", 400),
                            Planet("Jupiter", 700)
                        ),
                        vehicles = mutableListOf(
                            Vehicle("Rover", 4, 800, 50)
                        ),
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
        composeTestRule.onNodeWithContentDescription("Drop down arrow").performClick()
        composeTestRule.onNodeWithTag("Dropdown Menu Item Earth", useUnmergedTree = true)
            .onChildAt(0).assertTextEquals("Earth")
        composeTestRule.onNodeWithTag("Dropdown Menu Item Jupiter", useUnmergedTree = true)
            .onChildAt(0).assertTextEquals("Jupiter")
    }
}