package com.pechuro.bsuirschedule.feature.main.classes.data.classesinformation

import android.os.Parcel
import android.os.Parcelable

abstract class ClassesBaseInformation(var name: String,
                                      val type: Int) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(type)
    }

    override fun describeContents() = 0
}