package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.local.entity.DepartmentCached
import com.pechuro.bsuirschedule.local.entity.EducationFormCached
import com.pechuro.bsuirschedule.local.entity.FacultyCached
import com.pechuro.bsuirschedule.local.entity.SpecialityCached
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecialityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg department: DepartmentCached)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg speciality: SpecialityCached)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg faculty: FacultyCached)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg educationForm: EducationFormCached)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDepartments(departments: List<DepartmentCached>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpecialities(specialities: List<SpecialityCached>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFaculties(faculties: List<FacultyCached>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEducationForms(educationForms: List<EducationFormCached>)

    @Query("DELETE FROM speciality")
    suspend fun deleteAllSpecialities()

    @Query("DELETE FROM faculty")
    suspend fun deleteAllFaculties()

    @Query("DELETE FROM department")
    suspend fun deleteAllDepartments()

    @Query("SELECT * FROM department")
    fun getAllDepartments(): Flow<List<DepartmentCached>>

    @Query("SELECT * FROM speciality")
    fun getAllSpecialities(): Flow<List<SpecialityCached>>

    @Query("SELECT * FROM faculty")
    fun getAllFaculties(): Flow<List<FacultyCached>>

    @Query("SELECT * FROM education_form")
    fun getAllEducationForms(): Flow<List<EducationFormCached>>

    @Query("SELECT * FROM department WHERE id = :id")
    suspend fun getDepartmentById(id: Long): DepartmentCached

    @Query("SELECT * FROM faculty WHERE id = :id")
    suspend fun getFacultyById(id: Long): FacultyCached?

    @Query("SELECT * FROM speciality WHERE id = :id")
    suspend fun getSpecialityById(id: Long): SpecialityCached

    @Query("SELECT * FROM education_form WHERE id = :id")
    suspend fun getEducationFormById(id: Long): EducationFormCached

    @Query("SELECT EXISTS(SELECT 1 FROM speciality)")
    suspend fun isSpecialitiesNotEmpty(): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM department)")
    suspend fun isDepartmentsNotEmpty(): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM faculty)")
    suspend fun isFacultiesNotEmpty(): Boolean
}