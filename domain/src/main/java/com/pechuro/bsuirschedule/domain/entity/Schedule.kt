package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

sealed class Schedule(
        open val name: String
) : Parcelable {

    @Parcelize
    data class GroupClasses(
            override val name: String,
            val lastUpdatedDate: Date,
            val group: Group,
            val notRemindForUpdates: Boolean
    ) : Schedule(
            name = name
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as GroupClasses

            if (name != other.name) return false
            if (lastUpdatedDate != other.lastUpdatedDate) return false
            if (group != other.group) return false
            if (notRemindForUpdates != other.notRemindForUpdates) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + lastUpdatedDate.hashCode()
            result = 31 * result + group.hashCode()
            result = 31 * result + notRemindForUpdates.hashCode()
            return result - 1
        }
    }

    @Parcelize
    data class GroupExams(
            override val name: String,
            val lastUpdatedDate: Date,
            val group: Group,
            val notRemindForUpdates: Boolean
    ) : Schedule(
            name = name
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as GroupExams

            if (name != other.name) return false
            if (lastUpdatedDate != other.lastUpdatedDate) return false
            if (group != other.group) return false
            if (notRemindForUpdates != other.notRemindForUpdates) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + lastUpdatedDate.hashCode()
            result = 31 * result + group.hashCode()
            result = 31 * result + notRemindForUpdates.hashCode()
            return result - 2
        }
    }

    @Parcelize
    data class EmployeeClasses(
            override val name: String,
            val employee: Employee
    ) : Schedule(
            name = name
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as EmployeeClasses

            if (name != other.name) return false
            if (employee != other.employee) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + employee.hashCode()
            return result - 1
        }
    }

    @Parcelize
    data class EmployeeExams(
            override val name: String,
            val employee: Employee
    ) : Schedule(
            name = name
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as EmployeeExams

            if (name != other.name) return false
            if (employee != other.employee) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + employee.hashCode()
            return result - 2
        }
    }
}