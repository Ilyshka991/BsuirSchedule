package com.pechuro.bsuirschedule.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.data.entity.Employee
import io.reactivex.Single

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg employee: Employee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employees: List<Employee>)

    @Query("DELETE FROM all_employees")
    fun delete()

    @Query("SELECT EXISTS(SELECT 1 FROM all_employees)")
    fun isNotEmpty(): Single<Boolean>

    @Query("SELECT * FROM all_employees")
    fun get(): Single<List<Employee>>

    @Query("SELECT * FROM all_employees WHERE fio = :fio")
    fun get(fio: String): Single<Employee>

    @Query("SELECT fio FROM all_employees")
    fun getNames(): Single<List<String>>

    @Query("Select employee_id FROM all_employees WHERE fio = :fio")
    fun getId(fio: String): Single<String>
}