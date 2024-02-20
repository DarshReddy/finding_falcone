package com.example.findingfalcone.datamodels

import com.squareup.moshi.Json

data class Planet(
    @Json(name = "name")
    val name: String,
    @Json(name = "distance")
    val distance: Int,
)
