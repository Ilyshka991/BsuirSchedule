package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao

@Dao
interface EmployeeDao {

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg employee: EmployeeDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employees: List<EmployeeDB>)

    @Query("DELETE FROM all_employees")
    fun delete()

    @Query("SELECT EXISTS(SELECT 1 FROM all_employees)")
    fun isAuditoriesNotEmpty(): Single<Boolean>

    @Query("SELECT * FROM all_employees")
    fun getAll(): Single<List<EmployeeDB>>

    @Query("SELECT * FROM all_employees WHERE fio = :fio")
    fun getAll(fio: String): Single<EmployeeDB>

    @Query("SELECT fio FROM all_employees")
    fun getAllNames(): Single<List<String>>

    @Query("Select employee_id FROM all_employees WHERE fio = :fio")
    fun getId(fio: String): Single<String>*/
}