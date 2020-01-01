package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ISpecialityRepository {

    fun getAllFaculties(): Observable<List<Faculty>>

    fun getAllDepartments(): Observable<List<Department>>

    fun getAllSpecialities(): Observable<List<Speciality>>

    fun update(): Completable

    fun isStored(): Single<Boolean>
}