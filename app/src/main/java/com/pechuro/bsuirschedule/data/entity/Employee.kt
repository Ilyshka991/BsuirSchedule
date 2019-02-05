package com.pechuro.bsuirschedule.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "all_employees")
data class Employee(

        @PrimaryKey()
        @ColumnInfo(name = "employee_id")
        @SerializedName("id")
        var employeeId: String,

        @ColumnInfo(name = "first_name")
        var firstName: String?,

        @ColumnInfo(name = "last_name")
        var lastName: String,

        @ColumnInfo(name = "middle_name")
        var middleName: String?,

        var fio: String,

        @ColumnInfo(name = "photo_link")
        var photoLink: String?,

        @ColumnInfo(name = "rank")
        var rank: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(employeeId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(middleName)
        parcel.writeString(fio)
        parcel.writeString(photoLink)
        parcel.writeString(rank)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Employee> {
        override fun createFromParcel(parcel: Parcel) = Employee(parcel)

        override fun newArray(size: Int): Array<Employee?> {
            return arrayOfNulls(size)
        }
    }
}
