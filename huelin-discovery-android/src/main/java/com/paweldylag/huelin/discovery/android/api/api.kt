package com.paweldylag.huelin.discovery.android.api

import android.content.Context
import android.net.nsd.NsdManager
import com.paweldylag.huelin.api.PhillipsHueBridgeAddress
import com.paweldylag.huelin.discovery.android.internal.MdnsHueBridgeDiscovery
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit


/**
 * Created by Pawel Dylag
 */
sealed class HuelinDiscoveryException : Exception() {
    data class MdnsHuelinDiscoveryException(val errorCode: Int) : HuelinDiscoveryException()
}

fun discoverPhillipsHueBridges(context: Context,
                               bridgesLimit: Long = 16,
                               timeoutValue: Long = 5,
                               timeoutUnit: TimeUnit = TimeUnit.SECONDS,
                               onSuccess: (Set<PhillipsHueBridgeAddress>) -> Unit,
                               onError: (Throwable) -> Unit) {
    discoverPhillipsHueBridges(context, bridgesLimit, timeoutValue, timeoutUnit)
            .subscribe({
                onSuccess(it)
            }, {
                onError(it)
            })
}

fun discoverPhillipsHueBridges(context: Context,
                               bridgesLimit: Long = 16,
                               timeoutValue: Long = 5,
                               timeoutUnit: TimeUnit = TimeUnit.SECONDS): Single<Set<PhillipsHueBridgeAddress>> =
        MdnsHueBridgeDiscovery(context.getSystemService(Context.NSD_SERVICE) as NsdManager)
                .discoverBridges()
                .take(bridgesLimit)
                .take(timeoutValue, timeoutUnit)
                .toList()
                .map { it.toSet() }


interface HueBridgeDiscovery {
    fun discoverBridges(): Observable<PhillipsHueBridgeAddress>
}
