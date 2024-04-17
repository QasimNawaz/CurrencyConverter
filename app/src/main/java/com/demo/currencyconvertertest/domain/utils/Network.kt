package com.demo.currencyconvertertest.domain.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Network(private val context: Context?) : NetworkConnectivity {

    override fun isConnected(): Boolean {
        if (context != null) {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            return result
        } else {
            return true
        }

    }
}

interface NetworkConnectivity {
    fun isConnected(): Boolean
}