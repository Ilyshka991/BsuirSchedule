package com.pechuro.bsuirschedule.remote.interceptor

import com.pechuro.bsuirschedule.remote.common.NetworkAvailabilityChecker
import com.pechuro.bsuirschedule.remote.common.NetworkUnavailableException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkAvailabilityInterceptor(
        private val networkAvailabilityChecker: NetworkAvailabilityChecker
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkAvailabilityChecker.isNetworkAvailable()) throw NetworkUnavailableException
        return chain.proceed(chain.request())
    }
}