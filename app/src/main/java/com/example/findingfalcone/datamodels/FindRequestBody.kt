package com.example.findingfalcone.datamodels

import com.squareup.moshi.Json

data class FindRequestBody(
    @Json(name = "token")
    val token: String,
    @Json(name = "planet_names")
    val planetNames: List<String>,
    @Json(name = "vehicle_names")
    val vehicleNames: List<String>,
)
