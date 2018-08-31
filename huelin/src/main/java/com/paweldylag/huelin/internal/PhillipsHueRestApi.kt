package com.paweldylag.huelin.internal

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Pawel Dylag
 */
internal interface PhillipsHueRestApi {

    @GET("/lights")
    fun getAllLights(): Call<List<Light>>

}