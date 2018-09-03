package com.pechuro.bsuirschedule.repository.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(
        @PrimaryKey
        val name: String)