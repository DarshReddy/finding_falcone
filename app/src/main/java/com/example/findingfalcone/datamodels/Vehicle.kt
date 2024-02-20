package com.example.findingfalcone.datamodels

import com.squareup.moshi.Json

data class Vehicle(
    @Json(name = "name")
    val name: String,
    @Json(name = "total_no")
    var totalNo: Int,
    @Json(name = "max_distance")
    val maxDistance: Int,
    @Json(name = "speed")
    val speed: Int,
)
