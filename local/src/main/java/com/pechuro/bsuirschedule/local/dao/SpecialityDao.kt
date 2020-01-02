package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.local.entity.DepartmentDB
import com.pechuro.bsuirschedule.local.entity.EducationFormDB
import com.pechuro.bsuirschedule.local.entity.FacultyDB
import com.pechuro.bsuirschedule.local.entity.SpecialityDB
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecialityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg departmentDB: DepartmentDB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg specialityDB: SpecialityDB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg facultyDB: FacultyDB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg educationFormDB: EducationFormDB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDepartmentList(departmentDBList: List<DepartmentDB>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpecialityList(specialityDBList: List<SpecialityDB>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFacultyList(facultyDBList: List<FacultyDB>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEducationFormList(educationFormDBList: List<EducationFormDB>)

    @Query("DELETE FROM speciality")
    suspend fun deleteAllSpecialities()

    @Query("DELETE FROM faculty")
    suspend fun deleteAllFaculties()

    @Query("DELETE FROM department")
    suspend fun deleteAllDepartments()

    @Query("SELECT * FROM department")
    fun getAllDepartments(): Flow<List<DepartmentDB>>

    @Query("SELECT * FROM speciality")
    fun getAllSpecialities(): Flow<List<SpecialityDB>>

    @Query("SELECT * FROM faculty")
    fun getAllFaculties(): Flow<List<FacultyDB>>

    @Query("SELECT * FROM education_form")
    fun getAllEducationForms(): Flow<List<EducationFormDB>>

    @Query("SELECT * FROM department WHERE id = :id")
    suspend fun getDepartmentById(id: Long): DepartmentDB

    @Query("SELECT * FROM faculty WHERE id = :id")
    suspend fun getFacultyById(id: Long): FacultyDB?

    @Query("SELECT * FROM speciality WHERE id = :id")
    suspend fun getSpecialityById(id: Long): SpecialityDB

    @Query("SELECT * FROM education_form WHERE id = :id")
    suspend fun getEducationFormById(id: Long): EducationFormDB

    @Query("SELECT EXISTS(SELECT 1 FROM speciality)")
    suspend fun isSpecialitiesNotEmpty(): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM department)")
    suspend fun isDepartmentsNotEmpty(): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM faculty)")
    suspend fun isFacultiesNotEmpty(): Boolean
}