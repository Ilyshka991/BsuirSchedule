package com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo

import android.os.Parcel
import android.os.Parcelable

class ScheduleInformation(val name: String, val type: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(type)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ScheduleInformation> {
        override fun createFromParcel(parcel: Parcel): ScheduleInformation {
            return ScheduleInformation(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleInformation?> {
            return arrayOfNulls(size)
        }
    }
}