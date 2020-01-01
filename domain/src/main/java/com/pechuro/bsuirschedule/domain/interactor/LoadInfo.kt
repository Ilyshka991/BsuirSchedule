package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class LoadInfo(
        private val employeeRepository: IEmployeeRepository,
        private val groupRepository: IGroupRepository,
        private val specialityRepository: ISpecialityRepository,
        private val buildingRepository: IBuildingRepository
) : BaseInteractor<Completable, BaseInteractor.NoParams>() {

    override fun execute(params: NoParams) = Single.zip(
            employeeRepository.getAll().subscribeOn(Schedulers.io()).take(1).singleOrError().map { it.isNotEmpty() },
            groupRepository.getAll().subscribeOn(Schedulers.io()).take(1).singleOrError().map { it.isNotEmpty() },
            BiFunction { t1: Boolean, t2: Boolean -> t1 and t2 })
            .ignoreElement()
}