package com.paweldylag.hueviking

import android.app.Application
import com.wafel.skald.api.LogLevel
import com.wafel.skald.api.skald
import com.wafel.skald.plugins.logcat.toLogcat

/**
 * Created by Pawel Dylag
 */
class HueVikingApp: Application() {

    override fun onCreate() {
        super.onCreate()
        skald {
            writeSaga {
                toLogcat {
                    withPath { "com.paweldylag" }
                    withLevel { LogLevel.TRACE }
                    withTag { "HUELIN" }
                }
            }
        }
    }
}