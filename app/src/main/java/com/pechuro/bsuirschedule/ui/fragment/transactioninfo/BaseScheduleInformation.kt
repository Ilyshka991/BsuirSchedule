package com.pechuro.bsuirschedule.ui.fragment.transactioninfo

import android.os.Parcel
import android.os.Parcelable

abstract class BaseScheduleInformation(var name: String,
                                       val type: Int) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(type)
    }

    override fun describeContents() = 0
}