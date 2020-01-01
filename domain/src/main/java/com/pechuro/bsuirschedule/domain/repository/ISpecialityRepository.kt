package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import io.reactivex.Completable
import io.reactivex.Single

interface ISpecialityRepository {

    fun getAllFaculties(): Single<List<Faculty>>

    fun getAllDepartments(): Single<List<Department>>

    fun getAllSpecialities(): Single<List<Speciality>>

    fun update(): Completable

    fun isStored(): Single<Boolean>
}