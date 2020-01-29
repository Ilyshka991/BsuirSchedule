package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.local.entity.EmployeeCached
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg employee: EmployeeCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employees: List<EmployeeCached>)

    @Query("DELETE FROM employee")
    suspend fun deleteAll()

    @Query("SELECT * FROM employee")
    fun getAll(): Flow<List<EmployeeCached>>

    @Query("SELECT * FROM employee WHERE id = :id")
    suspend fun getById(id: Long): EmployeeCached

    @Query("SELECT id FROM employee WHERE abbreviation = :name")
    suspend fun getIdByName(name: String): Long

    @Query("SELECT abbreviation FROM employee")
    fun getAllNames(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM employee)")
    suspend fun isNotEmpty(): Boolean
}