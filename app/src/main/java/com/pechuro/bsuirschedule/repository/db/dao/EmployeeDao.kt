package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.pechuro.bsuirschedule.repository.entity.Employee
import io.reactivex.Single

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM all_employees")
    fun getEmployees(): Single<List<Employee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg employee: Employee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employees: List<Employee>)
}