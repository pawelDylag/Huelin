package com.paweldylag.huelin.api

import java.net.InetAddress

/**
 * Created by Pawel Dylag
 */
data class PhillipsHueBridgeAddress(val inetAddress: InetAddress)

sealed class HuelinException : Exception()