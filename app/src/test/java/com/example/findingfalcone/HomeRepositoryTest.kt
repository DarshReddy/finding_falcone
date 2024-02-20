package com.example.findingfalcone

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.findingfalcone.datamodels.ApiResponse
import com.example.findingfalcone.datamodels.FindRequestBody
import com.example.findingfalcone.network.ApiService
import com.example.findingfalcone.network.HomeRepository
import com.example.findingfalcone.utils.MainDispatcherRule
import com.example.findingfalcone.utils.mockErrorApiResponse
import com.example.findingfalcone.utils.mockErrorResponse
import com.example.findingfalcone.utils.mockFindFalconeSuccessApiResponse
import com.example.findingfalcone.utils.mockFindFalconeSuccessResponse
import com.example.findingfalcone.utils.mockPlanetsSuccessApiResponse
import com.example.findingfalcone.utils.mockPlanetsSuccessResponse
import com.example.findingfalcone.utils.mockTokenSuccessApiResponse
import com.example.findingfalcone.utils.mockTokenSuccessResponse
import com.example.findingfalcone.utils.mockVehiclesSuccessApiResponse
import com.example.findingfalcone.utils.mockVehiclesSuccessResponse
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

class HomeRepositoryTest {

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var homeRepository: HomeRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        homeRepository = HomeRepository(apiService)
    }

    @Test
    fun test_getPlanets_success() {
        runBlocking {
            whenever(apiService.getPlanets()).doReturn(
                mockPlanetsSuccessResponse()
            )

            val response = homeRepository.getPlanets(MutableLiveData())
            Truth.assertThat((response as? ApiResponse.Success)?.responseData)
                .isEqualTo(mockPlanetsSuccessApiResponse().responseData)
        }
    }

    @Test
    fun test_getPlanets_failure() {
        runBlocking {
            whenever(apiService.getPlanets()).doReturn(
                mockErrorResponse()
            )
            val response = homeRepository.getPlanets(MutableLiveData())
            Truth.assertThat((response as? ApiResponse.Error)).isEqualTo(mockErrorApiResponse())
        }
    }

    @Test
    fun test_getVehicles_success() {
        runBlocking {
            whenever(apiService.getVehicles()).doReturn(
                mockVehiclesSuccessResponse()
            )
            val response = homeRepository.getVehicles(MutableLiveData())
            Truth.assertThat((response as? ApiResponse.Success)?.responseData)
                .isEqualTo(mockVehiclesSuccessApiResponse().responseData)
        }
    }

    @Test
    fun test_getVehicles_failure() {
        runBlocking {
            whenever(apiService.getVehicles()).doReturn(
                mockErrorResponse()
            )
            val response = homeRepository.getVehicles(MutableLiveData())
            Truth.assertThat((response as? ApiResponse.Error)).isEqualTo(mockErrorApiResponse())
        }
    }

    @Test
    fun test_findFalcone_success() {
        runBlocking {
            whenever(apiService.findFalcone(any())).doReturn(
                mockFindFalconeSuccessResponse()
            )
            val response = homeRepository.findFalcone(
                FindRequestBody(
                    "",
                    listOf(),
                    listOf()
                ),
                MutableLiveData()
            )
            Truth.assertThat((response as? ApiResponse.Success)?.responseData)
                .isEqualTo(mockFindFalconeSuccessApiResponse().responseData)
        }
    }

    @Test
    fun test_findFalcone_failure() {
        runBlocking {
            whenever(apiService.findFalcone(any())).doReturn(
                mockErrorResponse()
            )

            val response = homeRepository.findFalcone(
                FindRequestBody(
                    "",
                    listOf(),
                    listOf()
                ),
                MutableLiveData()
            )

            Truth.assertThat((response as? ApiResponse.Error)).isEqualTo(mockErrorApiResponse())
        }
    }

    @Test
    fun test_getToken_success() {
        runBlocking {
            whenever(apiService.getToken()).doReturn(
                mockTokenSuccessResponse()
            )
            val response = homeRepository.getToken()
            Truth.assertThat((response as? ApiResponse.Success)).isEqualTo(
                mockTokenSuccessApiResponse()
            )
        }
    }

    @Test
    fun test_getToken_failure() {
        runBlocking {
            whenever(apiService.getToken()).doReturn(
                mockErrorResponse()
            )
            val response = homeRepository.getToken()
            Truth.assertThat((response as? ApiResponse.Error)).isEqualTo(mockErrorApiResponse())
        }
    }
}