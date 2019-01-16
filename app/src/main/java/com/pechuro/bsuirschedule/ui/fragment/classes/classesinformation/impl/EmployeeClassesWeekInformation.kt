package com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.impl

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.ClassesBaseInformation

class EmployeeClassesWeekInformation(
        name: String, type: Int, val day: String) : ClassesBaseInformation(name, type) {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(day)
    }

    companion object CREATOR : Parcelable.Creator<EmployeeClassesWeekInformation> {
        override fun createFromParcel(parcel: Parcel) =
                EmployeeClassesWeekInformation(parcel)

        override fun newArray(size: Int): Array<EmployeeClassesWeekInformation?> =
                arrayOfNulls(size)
    }
}