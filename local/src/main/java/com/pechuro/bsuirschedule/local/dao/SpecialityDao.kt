package com.pechuro.bsuirschedule.local.dao

import androidx.room.*
import com.pechuro.bsuirschedule.local.entity.education.DepartmentCached
import com.pechuro.bsuirschedule.local.entity.education.EducationFormCached
import com.pechuro.bsuirschedule.local.entity.education.FacultyCached
import com.pechuro.bsuirschedule.local.entity.education.SpecialityCached
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecialityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(department: DepartmentCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(educationForm: EducationFormCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDepartments(departments: List<DepartmentCached>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpecialities(specialities: List<SpecialityCached>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFaculties(faculties: List<FacultyCached>): List<Long>


    @Update
    suspend fun update(department: DepartmentCached)

    @Update
    suspend fun update(educationForm: EducationFormCached)

    @Update
    suspend fun updateDepartments(departments: List<DepartmentCached>)

    @Update
    suspend fun updateSpecialities(specialities: List<SpecialityCached>)

    @Update
    suspend fun updateFaculties(faculties: List<FacultyCached>)


    @Transaction
    suspend fun insertOrUpdate(department: DepartmentCached) {
        val id = insert(department)
        if (id == -1L) update(department)
    }

    @Transaction
    suspend fun insertOrUpdate(educationForm: EducationFormCached) {
        val id = insert(educationForm)
        if (id == -1L) update(educationForm)
    }

    @Transaction
    suspend fun insertOrUpdateDepartments(departments: List<DepartmentCached>) {
        val insertResult = insertDepartments(departments)
        val updateList = mutableListOf<DepartmentCached>()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(departments[i])
        }
        if (updateList.isNotEmpty()) updateDepartments(updateList)
    }

    @Transaction
    suspend fun insertOrUpdateSpecialities(specialities: List<SpecialityCached>) {
        val insertResult = insertSpecialities(specialities)
        val updateList = mutableListOf<SpecialityCached>()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(specialities[i])
        }
        if (updateList.isNotEmpty()) updateSpecialities(updateList)
    }

    @Transaction
    suspend fun insertOrUpdateFaculties(faculties: List<FacultyCached>) {
        val insertResult = insertFaculties(faculties)
        val updateList = mutableListOf<FacultyCached>()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(faculties[i])
        }
        if (updateList.isNotEmpty()) updateFaculties(updateList)
    }


    @Query("SELECT * FROM department")
    fun getAllDepartments(): Flow<List<DepartmentCached>>

    @Query("SELECT * FROM speciality")
    fun getAllSpecialities(): Flow<List<SpecialityCached>>

    @Query("SELECT * FROM faculty")
    fun getAllFaculties(): Flow<List<FacultyCached>>

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