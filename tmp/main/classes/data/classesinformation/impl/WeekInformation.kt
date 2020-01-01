package com.pechuro.bsuirschedule.feature.main.classes.data.classesinformation.impl

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.ClassesBaseInformation

class WeekInformation(
        name: String, type: Int,
        val day: String) : ClassesBaseInformation(name, type) {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(day)
    }

    companion object CREATOR : Parcelable.Creator<WeekInformation> {
        override fun createFromParcel(parcel: Parcel) =
                WeekInformation(parcel)

        override fun newArray(size: Int): Array<WeekInformation?> =
                arrayOfNulls(size)
    }
}