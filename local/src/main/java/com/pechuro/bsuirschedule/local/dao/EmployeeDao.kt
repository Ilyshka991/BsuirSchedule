package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(employee: EmployeeCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(employees: List<EmployeeCached>): List<Long>


    @Update
    suspend fun update(employee: EmployeeCached)

    @Update
    suspend fun update(employees: List<EmployeeCached>)


    @Transaction
    suspend fun insertOrUpdate(employees: List<EmployeeCached>) {
        val insertResult = insert(employees)
        val updateList = mutableListOf<EmployeeCached>()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(employees[i])
        }
        if (updateList.isNotEmpty()) update(updateList)
    }


    @Query("SELECT * FROM employee")
    fun getAll(): Flow<List<EmployeeCached>>

    @Query("SELECT * FROM employee WHERE id = :id")
    suspend fun getById(id: Long): EmployeeCached

    @Query("SELECT EXISTS(SELECT 1 FROM employee)")
    suspend fun isNotEmpty(): Boolean
}