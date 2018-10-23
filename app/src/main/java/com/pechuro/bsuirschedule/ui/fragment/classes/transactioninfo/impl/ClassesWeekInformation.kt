package com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.impl

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.ClassesBaseInformation

class ClassesWeekInformation(
        name: String, type: Int,
        val day: String,
        val subgroupNumber: Int) : ClassesBaseInformation(name, type) {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(day)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ClassesWeekInformation> {
        override fun createFromParcel(parcel: Parcel) =
                ClassesWeekInformation(parcel)

        override fun newArray(size: Int): Array<ClassesWeekInformation?> =
                arrayOfNulls(size)
    }
}