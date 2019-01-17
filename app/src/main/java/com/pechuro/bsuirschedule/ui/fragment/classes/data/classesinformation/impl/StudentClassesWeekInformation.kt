package com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.impl

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.ClassesBaseInformation

class StudentClassesWeekInformation(
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

    companion object CREATOR : Parcelable.Creator<StudentClassesWeekInformation> {
        override fun createFromParcel(parcel: Parcel) =
                StudentClassesWeekInformation(parcel)

        override fun newArray(size: Int): Array<StudentClassesWeekInformation?> =
                arrayOfNulls(size)
    }
}