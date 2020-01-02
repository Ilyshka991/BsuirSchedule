package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.local.entity.EmployeeDB
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg employee: EmployeeDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employees: List<EmployeeDB>)

    @Query("DELETE FROM employee")
    suspend fun deleteAll()

    @Query("SELECT * FROM employee")
    fun getAll(): Flow<List<EmployeeDB>>

    @Query("SELECT * FROM employee WHERE id = :id")
    suspend fun getById(id: Long): EmployeeDB

    @Query("SELECT abbreviation FROM employee")
    suspend fun getAllNames(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM employee)")
    suspend fun isNotEmpty(): Boolean
}