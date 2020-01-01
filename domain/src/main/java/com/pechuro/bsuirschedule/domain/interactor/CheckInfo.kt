package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import io.reactivex.Single
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers

class CheckInfo(
        private val employeeRepository: IEmployeeRepository,
        private val groupRepository: IGroupRepository,
        private val specialityRepository: ISpecialityRepository,
        private val buildingRepository: IBuildingRepository
) : BaseInteractor<Single<Boolean>, BaseInteractor.NoParams>() {

    override fun execute(params: NoParams) = Single.zip(
            employeeRepository.isStored().subscribeOn(Schedulers.io()),
            groupRepository.isStored().subscribeOn(Schedulers.io()),
            specialityRepository.isStored().subscribeOn(Schedulers.io()),
            buildingRepository.isStored().subscribeOn(Schedulers.io()),
            Function4 { t1: Boolean, t2: Boolean, t3: Boolean, t4: Boolean ->
                t1 and t2 and t3 and t4
            }
    )
}