package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Building(
        val id: Long,
        val name: String
) : Parcelable

val Building.coordinates: LatLng?
    get() = when (name) {
        "1" -> LatLng(53.917750, 27.595138)
        "2" -> LatLng(53.918812, 27.593705)
        "3" -> LatLng(53.916677, 27.596191)
        "4" -> LatLng(53.912343, 27.594539)
        "5" -> LatLng(53.911641, 27.595961)
        "7" -> LatLng(53.902895, 27.598172)
        "8" -> LatLng(53.903092, 27.598119)
        else -> null
    }