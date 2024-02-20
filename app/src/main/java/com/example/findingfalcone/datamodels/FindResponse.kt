package com.example.findingfalcone.datamodels

import com.squareup.moshi.Json

data class FindResponse(
    @Json(name = "planet_name")
    val planetName: String?,
    @Json(name = "status")
    val status: String?
)
