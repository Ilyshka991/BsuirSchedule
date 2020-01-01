package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class CheckInfo(
        private val employeeRepository: IEmployeeRepository,
        private val groupRepository: IGroupRepository
) : BaseInteractor<Single<Boolean>, BaseInteractor.NoParams>() {

    override fun execute(params: NoParams) = Single.zip(
            employeeRepository.isStored().subscribeOn(Schedulers.io()),
            groupRepository.isStored().subscribeOn(Schedulers.io()),
            BiFunction { t1: Boolean, t2: Boolean -> t1 and t2 })

}