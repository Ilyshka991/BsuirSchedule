package com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.impl

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.ClassesBaseInformation

class EmployeeClassesDayInformation(
        name: String, type: Int,
        val day: String,
        val week: Int,
        val dateTag: String) : ClassesBaseInformation(name, type) {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(day)
        parcel.writeInt(week)
        parcel.writeString(dateTag)
    }

    companion object CREATOR : Parcelable.Creator<EmployeeClassesDayInformation> {
        override fun createFromParcel(parcel: Parcel) =
                EmployeeClassesDayInformation(parcel)

        override fun newArray(size: Int): Array<EmployeeClassesDayInformation?> =
                arrayOfNulls(size)
    }
}