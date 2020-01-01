package com.pechuro.bsuirschedule.domain.repository

import io.reactivex.Single

interface ISpecialityRepository {

    fun isStored(): Single<Boolean>
}