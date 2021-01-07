package com.pechuro.bsuirschedule.remote.common

interface NetworkAvailabilityChecker {

    fun isNetworkAvailable(): Boolean
}