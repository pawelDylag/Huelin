package com.paweldylag.huelin.internal

/**
 * Created by Pawel Dylag
 */

data class Light(val state: State)


data class State(val on: Boolean,
                 val bri: Int,
                 val hue: Int,
                 val sat: Int)