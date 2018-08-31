package com.paweldylag.hueviking

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.paweldylag.huelin.discovery.android.api.discoverPhillipsHueBridges
import com.wafel.skald.api.createLogger

class MainActivity : AppCompatActivity() {

    private val logger = createLogger(this.javaClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        discoverPhillipsHueBridges(this).subscribe ({ bridges ->
            logger.debug("HUE BRIDGES FOUND: ${bridges.joinToString { it.inetAddress.hostAddress }}")
        }, {
            logger.error("UNABLE TO DISCOVER BRIDGES: ${it.message}")
        })
    }
}
