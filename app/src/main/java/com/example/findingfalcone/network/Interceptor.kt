package com.example.findingfalcone.network

import okhttp3.Interceptor
import okhttp3.Response

class Interceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequestBuilder = chain.request().newBuilder()

        newRequestBuilder.addHeader("Accept", "application/json")

        return chain.proceed(newRequestBuilder.build())
    }
}