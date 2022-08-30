package ua.com.foxminded.newsfeed.models.network.listener

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class DefaultConnectivityStatusListener(appCtx: Context) : ConnectivityStatusListener {

    private val statusFlow = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    private val connectivityManager =
        appCtx.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            statusFlow.tryEmit(ConnectivityStatusListener.STATUS_AVAILABLE)
        }

        override fun onLost(network: Network) {
            statusFlow.tryEmit(ConnectivityStatusListener.STATUS_UNAVAILABLE)
        }
    }

    init {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    override fun checkIfNetworkAvailable() {
        statusFlow.tryEmit(getNetworkStatus())
    }

    private fun getNetworkStatus(): Int {
        val network = connectivityManager.activeNetwork
            ?: return ConnectivityStatusListener.STATUS_UNAVAILABLE
        val networkCaps = connectivityManager.getNetworkCapabilities(network)
            ?: return ConnectivityStatusListener.STATUS_UNAVAILABLE
        return when {
            networkCaps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> ConnectivityStatusListener.STATUS_AVAILABLE
            else -> ConnectivityStatusListener.STATUS_UNAVAILABLE
        }
    }

    override fun getStatusFlow(): Flow<Int> = statusFlow

    override fun releaseRequest() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}