package com.example.findingfalcone

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.Planet
import com.example.findingfalcone.datamodels.Vehicle
import com.example.findingfalcone.network.HomeRepository
import com.example.findingfalcone.utils.MainDispatcherRule
import com.example.findingfalcone.utils.getOrAwaitValue
import com.example.findingfalcone.utils.mockErrorApiResponse
import com.example.findingfalcone.utils.mockFindFalconeSuccessApiResponse
import com.example.findingfalcone.utils.mockPlanetsSuccessApiResponse
import com.example.findingfalcone.utils.mockTokenSuccessApiResponse
import com.example.findingfalcone.utils.mockVehiclesSuccessApiResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

class HomeViewModelTest {

    @Mock
    private lateinit var homeRepository: HomeRepository

    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun test_getPlanets_success() {
        runBlocking {
            whenever(homeRepository.getPlanets(any())).doReturn(
                mockPlanetsSuccessApiResponse()
            )
            homeViewModel.getPlanets()
            val response = homeViewModel.planets.getOrAwaitValue()
            assertThat((response as? ApiResponse.Success)?.responseData)
                .isEqualTo(mockPlanetsSuccessApiResponse().responseData)
        }
    }

    @Test
    fun test_getPlanets_failure() {
        runBlocking {
            whenever(homeRepository.getPlanets(any())).doReturn(
                mockErrorApiResponse()
            )
            homeViewModel.getPlanets()
            val response = homeViewModel.planets.getOrAwaitValue()
            assertThat((response as? ApiResponse.Error))
                .isEqualTo(mockErrorApiResponse())
        }
    }

    @Test
    fun test_getVehicles_success() {
        runBlocking {
            whenever(homeRepository.getVehicles(any())).doReturn(
                mockVehiclesSuccessApiResponse()
            )
            homeViewModel.getVehicles()
            val response = homeViewModel.vehicles.getOrAwaitValue()
            assertThat((response as? ApiResponse.Success)?.responseData)
                .isEqualTo(mockVehiclesSuccessApiResponse().responseData)
        }
    }

    @Test
    fun test_getVehicles_failure() {
        runBlocking {
            whenever(homeRepository.getVehicles(any())).doReturn(
                mockErrorApiResponse()
            )
            homeViewModel.getVehicles()
            val response = homeViewModel.vehicles.getOrAwaitValue()
            assertThat((response as? ApiResponse.Error))
                .isEqualTo(mockErrorApiResponse())
        }
    }

    @Test
    fun test_findFalcone_success() {
        runBlocking {
            whenever(homeRepository.getToken()).doReturn(
                mockTokenSuccessApiResponse()
            )
            whenever(homeRepository.findFalcone(any(), any())).doReturn(
                mockFindFalconeSuccessApiResponse()
            )
            homeViewModel.findFalcone()
            val response = homeViewModel.findResponse.getOrAwaitValue()
            assertThat((response as? ApiResponse.Success)?.responseData)
                .isEqualTo(mockFindFalconeSuccessApiResponse().responseData)
        }
    }

    @Test
    fun test_findFalcone_tokenFailure() {
        runBlocking {
            whenever(homeRepository.getToken()).doReturn(
                mockErrorApiResponse()
            )
            homeViewModel.findFalcone()
            val response = homeViewModel.findResponse.getOrAwaitValue()
            assertThat((response as? ApiResponse.Error)).isEqualTo(mockErrorApiResponse())
        }
    }

    @Test
    fun test_findFalcone_failure() {
        runBlocking {
            whenever(homeRepository.getToken()).doReturn(
                mockTokenSuccessApiResponse()
            )
            whenever(homeRepository.findFalcone(any(), any())).doReturn(
                mockErrorApiResponse()
            )

            homeViewModel.findFalcone()
            val response = homeViewModel.findResponse.getOrAwaitValue()
            assertThat((response as? ApiResponse.Error)).isEqualTo(mockErrorApiResponse())
        }
    }

    @Test
    fun test_filterVehicles() {
        val vehicles = mutableListOf(
            Vehicle("Vehicle1", 5, 100, 50),
            Vehicle("Vehicle2", 0, 200, 40),
            Vehicle("Vehicle3", 3, 150, 60)
        )
        homeViewModel.selectPlanet(Planet("Planet1", 100))
        homeViewModel.selectPlanet(Planet("Planet2", 150))

        val filteredVehicles = homeViewModel.filterVehicles(vehicles)

        assertThat(filteredVehicles).hasSize(1)
        assertThat(filteredVehicles).doesNotContain(vehicles[0])
        assertThat(filteredVehicles).doesNotContain(vehicles[1])
        assertThat(filteredVehicles).contains(vehicles[2])
    }

    @Test
    fun test_timeTakenPerTeam() {
        homeViewModel.selectPlanet(Planet("Planet1", 200))
        homeViewModel.selectVehicle(
            Vehicle("Vehicle1", 5, 400, 50)
        )

        val timeTaken = homeViewModel.timeTakenPerTeam(0)

        assertThat(timeTaken).isEqualTo(4) // Distance / Speed = 200 / 50 = 4
    }

    @Test
    fun test_timeTakenPerTeam_whenIndexOutOfBounds() {
        homeViewModel.selectPlanet(Planet("Planet1", 200))
        homeViewModel.selectVehicle(
            Vehicle("Vehicle1", 5, 400, 50)
        )

        val timeTaken = homeViewModel.timeTakenPerTeam(1)

        assertThat(timeTaken).isEqualTo(0)
    }

    @Test
    fun test_getPlanetAndVehiclePerTeam() {
        homeViewModel.selectPlanet(Planet("Planet1", 200))
        homeViewModel.selectVehicle(Vehicle("Vehicle1", 5, 400, 50))

        val result = homeViewModel.getPlanetAndVehiclePerTeam(0)

        assertThat(result).isEqualTo(Pair("Planet1", "Vehicle1"))
    }
}