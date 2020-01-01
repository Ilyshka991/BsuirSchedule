package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class LoadInfo(
        private val employeeRepository: IEmployeeRepository,
        private val groupRepository: IGroupRepository
) : BaseInteractor<Completable, BaseInteractor.NoParams>() {

    override fun execute(params: NoParams) = Single.zip(
            employeeRepository.getAll().subscribeOn(Schedulers.io()).map { it.isNotEmpty() },
            groupRepository.getAll().subscribeOn(Schedulers.io()).map { it.isNotEmpty() },
            BiFunction { t1: Boolean, t2: Boolean -> t1 and t2 })
            .ignoreElement()
}