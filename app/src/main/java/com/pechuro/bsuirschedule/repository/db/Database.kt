package com.pechuro.bsuirschedule.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.pechuro.bsuirschedule.repository.entity.Group


@Database(entities = [Group::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
}