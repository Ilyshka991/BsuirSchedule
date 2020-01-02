package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.local.entity.DepartmentDB

@Dao
interface SpecialityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg departmentDB: DepartmentDB)

    @Query("SELECT * FROM department")
    suspend fun getAllDepartments(): List<DepartmentDB>

    @Query("SELECT * FROM department WHERE id = :id")
    suspend fun getDepartmentById(id: Long): DepartmentDB
}