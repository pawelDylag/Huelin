package com.paweldylag.huelin.discovery.android.internal

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.paweldylag.huelin.api.PhillipsHueBridgeAddress
import com.paweldylag.huelin.discovery.android.api.HueBridgeDiscovery
import com.paweldylag.huelin.discovery.android.api.HuelinDiscoveryException
import com.wafel.skald.api.createLogger
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/**
 * Created by Pawel Dylag
 */
internal class MdnsHueBridgeDiscovery(private val nsdManager: NsdManager) : HueBridgeDiscovery {

    private val logger = createLogger(this.javaClass)
    private val MDNS_PHILLIPS_HUE_SERVICE_NAME = "_hue._tcp"

    override fun discoverBridges(): Observable<PhillipsHueBridgeAddress> =
            Observable.create<PhillipsHueBridgeAddress> { emitter ->
                val discoveryListener = object : NsdManager.DiscoveryListener {
                    override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
                        logger.debug("onServiceFound: $serviceInfo")
                        resolveService(serviceInfo, emitter)
                    }

                    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
                        logger.debug("onStopDiscoveryFailed: $serviceType, error code: $errorCode")
                    }

                    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
                        logger.debug("onStartDiscoveryFailed: $serviceType, error code: $errorCode")
                        emitter.onError(HuelinDiscoveryException.MdnsHuelinDiscoveryException(errorCode))
                    }

                    override fun onDiscoveryStarted(serviceType: String?) {
                        logger.debug("onDiscoveryStarted: $serviceType")
                    }

                    override fun onDiscoveryStopped(serviceType: String?) {
                        logger.debug("onDiscoveryStopped: $serviceType")
                    }

                    override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
                        logger.debug("onServiceLost: $serviceInfo ")
                    }
                }
                nsdManager.discoverServices(MDNS_PHILLIPS_HUE_SERVICE_NAME, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
                emitter.setCancellable {
                    nsdManager.stopServiceDiscovery(discoveryListener)
                }
            }


    private fun resolveService(serviceInfo: NsdServiceInfo?,
                               emitter: ObservableEmitter<PhillipsHueBridgeAddress>) {
        nsdManager.resolveService(serviceInfo, object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                logger.debug("onResolveFailed: $serviceInfo, error code: $errorCode")
                emitter.onError(HuelinDiscoveryException.MdnsHuelinDiscoveryException(errorCode))
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
                logger.debug("onServiceResolved: $serviceInfo" + "Service found " + serviceInfo?.serviceName + "@" + serviceInfo?.host?.hostAddress)
                serviceInfo?.apply { emitter.onNext(PhillipsHueBridgeAddress(serviceInfo.host)) }
            }
        })
    }

}