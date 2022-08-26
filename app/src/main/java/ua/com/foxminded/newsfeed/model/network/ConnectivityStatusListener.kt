package ua.com.foxminded.newsfeed.model.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityStatusListener {
    fun getStatusFlow(): Flow<Int>
    fun checkIfNetworkAvailable()
    fun releaseRequest()

    companion object {
        const val STATUS_AVAILABLE = 1
        const val STATUS_UNAVAILABLE = 0
    }
}