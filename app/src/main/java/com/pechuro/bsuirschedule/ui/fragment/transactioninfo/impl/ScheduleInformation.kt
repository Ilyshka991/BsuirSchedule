package com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.BaseInformation

class ScheduleInformation(name: String, type: Int) : BaseInformation(name, type) {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readInt())

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScheduleInformation> {
        override fun createFromParcel(parcel: Parcel): ScheduleInformation {
            return ScheduleInformation(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleInformation?> {
            return arrayOfNulls(size)
        }
    }
}