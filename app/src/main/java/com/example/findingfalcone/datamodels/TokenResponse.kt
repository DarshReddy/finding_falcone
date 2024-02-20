package com.example.findingfalcone.datamodels

import com.squareup.moshi.Json

data class TokenResponse(
    @Json(name = "token")
    val token: String,
)
