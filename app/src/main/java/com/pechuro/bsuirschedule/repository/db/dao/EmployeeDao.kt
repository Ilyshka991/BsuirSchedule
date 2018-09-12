package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.pechuro.bsuirschedule.repository.entity.Employee
import io.reactivex.Single

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg employee: Employee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employees: List<Employee>)

    @Query("SELECT EXISTS(SELECT 1 FROM all_employees)")
    fun isNotEmpty(): Boolean

    @Query("DELETE FROM all_employees")
    fun delete()

    @Query("SELECT * FROM all_employees")
    fun get(): Single<List<Employee>>

    @Query("SELECT * FROM all_employees WHERE fio = :fio")
    fun get(fio: String): Single<Employee>

    @Query("SELECT fio FROM all_employees")
    fun getNames(): Single<List<String>>

    @Query("Select employee_id FROM all_employees WHERE fio = :fio")
    fun getId(fio: String): Single<String>
}