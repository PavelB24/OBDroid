package ru.barinov.obdroid

import android.net.LinkProperties
import android.net.Network

sealed class WifiConnectionState {

    data class Connected(val network: Network) : WifiConnectionState()

    object UnAvailable : WifiConnectionState()

    data class LinkPropertiesChanged(
        val network: Network,
        val linkProperties: LinkProperties
    ) : WifiConnectionState()

    data class Lost(val network: Network) : WifiConnectionState()


}
