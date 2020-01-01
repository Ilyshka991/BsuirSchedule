package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.AuditoryType
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import io.reactivex.Completable
import io.reactivex.Single

class BuildingRepositoryImpl : IBuildingRepository {

    override fun getAllAuditories(): Single<List<Auditory>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllAuditoryTypes(): Single<List<AuditoryType>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isStored(): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}