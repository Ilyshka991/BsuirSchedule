package com.pechuro.bsuirschedule.ui.fragment.classes

import android.os.Parcel
import android.os.Parcelable

data class DayScheduleInformation(
        val group: String,
        val day: String,
        val week: Int,
        val subgroup: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(group)
        parcel.writeString(day)
        parcel.writeInt(week)
        parcel.writeInt(subgroup)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DayScheduleInformation> {
        override fun createFromParcel(parcel: Parcel): DayScheduleInformation {
            return DayScheduleInformation(parcel)
        }

        override fun newArray(size: Int): Array<DayScheduleInformation?> {
            return arrayOfNulls(size)
        }
    }
}