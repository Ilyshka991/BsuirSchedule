package com.pechuro.bsuirschedule.common.provider

import android.net.Uri
import com.pechuro.bsuirschedule.domain.entity.LatLng

interface AppUriProvider {

    val privacyPoliceUri: Uri

    val playStoreWebUri: Uri

    val playStoreAppUri: Uri

    val emailFeedbackUri: Uri

    fun provideGeoUri(latLng: LatLng): Uri
}