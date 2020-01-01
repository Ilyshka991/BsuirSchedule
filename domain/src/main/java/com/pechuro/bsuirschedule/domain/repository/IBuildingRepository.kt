package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.AuditoryType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IBuildingRepository {

    fun getAllAuditories(): Observable<List<Auditory>>

    fun getAllAuditoryTypes(): Observable<List<AuditoryType>>

    fun update(): Completable

    fun isStored(): Single<Boolean>
}