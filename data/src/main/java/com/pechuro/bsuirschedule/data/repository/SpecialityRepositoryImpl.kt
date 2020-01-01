package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import io.reactivex.Completable
import io.reactivex.Single

class SpecialityRepositoryImpl : ISpecialityRepository {

    override fun getAllFaculties(): Single<List<Faculty>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllDepartments(): Single<List<Department>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllSpecialities(): Single<List<Speciality>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isStored(): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}