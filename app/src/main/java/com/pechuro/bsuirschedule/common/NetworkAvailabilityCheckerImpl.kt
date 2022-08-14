package com.pechuro.bsuirschedule.common

import android.content.Context
import android.net.ConnectivityManager
import com.pechuro.bsuirschedule.remote.common.NetworkAvailabilityChecker

class NetworkAvailabilityCheckerImpl(context: Context) : NetworkAvailabilityChecker {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    //TODO: deprecated
    override fun isNetworkAvailable() = connectivityManager.activeNetworkInfo?.isAvailable ?: false
}