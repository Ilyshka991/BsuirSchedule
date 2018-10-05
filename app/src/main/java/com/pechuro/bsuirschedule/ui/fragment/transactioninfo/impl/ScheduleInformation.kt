package com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.BaseScheduleInformation

class ScheduleInformation(
        name: String, type: Int,
        val day: String,
        val week: Int,
        val subgroup: Int = 0
) : BaseScheduleInformation(name, type) {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(day)
        parcel.writeInt(week)
        parcel.writeInt(subgroup)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ScheduleInformation> {
        override fun createFromParcel(parcel: Parcel) =
                ScheduleInformation(parcel)

        override fun newArray(size: Int): Array<ScheduleInformation?> =
                arrayOfNulls(size)
    }
}