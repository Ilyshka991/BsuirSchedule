package com.pechuro.bsuirschedule.common.provider

import android.content.Context
import android.net.Uri
import com.pechuro.bsuirschedule.domain.entity.LatLng

class AppUriProviderImpl(context: Context) : AppUriProvider {

    override val privacyPoliceUri: Uri = Uri.parse(
        "https://ilyshka991.github.io/BsuirSchedule/docs/privacy_policy.html"
    )

    override val playStoreWebUri: Uri = Uri.parse(
        "http://play.google.com/store/apps/details?id=${context.packageName}"
    )

    override val playStoreAppUri: Uri = Uri.parse(
        "market://details?id=${context.packageName}"
    )

    override val emailFeedbackUri: Uri = Uri.parse("mailto:")

    override fun provideGeoUri(latLng: LatLng): Uri = Uri.parse(
        "geo:${latLng.latitude},${latLng.longitude}?z=17.5"
    )
}