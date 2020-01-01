package com.pechuro.bsuirschedule.feature.main

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.bsuirschedule.common.BaseViewHolderData

data class ScheduleInformation(val id: Int, val name: String, val type: Int) : BaseViewHolderData(), Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readInt())

    constructor() : this(-1, "", -1)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(type)
    }

    override fun describeContents() = 0

    fun isEmpty() = id == -1 && name.isEmpty() && type == -1

    companion object CREATOR : Parcelable.Creator<ScheduleInformation> {
        override fun createFromParcel(parcel: Parcel): ScheduleInformation {
            return ScheduleInformation(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleInformation?> {
            return arrayOfNulls(size)
        }
    }
}