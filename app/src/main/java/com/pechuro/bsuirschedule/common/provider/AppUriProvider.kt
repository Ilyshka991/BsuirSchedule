package com.pechuro.bsuirschedule.common.provider

import android.net.Uri

interface AppUriProvider {

    val privacyPoliceUri: Uri

    val playStoreWebUri: Uri

    val playStoreAppUri: Uri

    val emailFeedbackUri: Uri
}