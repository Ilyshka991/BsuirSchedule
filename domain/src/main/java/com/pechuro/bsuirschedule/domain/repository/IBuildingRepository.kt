package com.pechuro.bsuirschedule.domain.repository

import io.reactivex.Single

interface IBuildingRepository {

    fun isStored(): Single<Boolean>
}