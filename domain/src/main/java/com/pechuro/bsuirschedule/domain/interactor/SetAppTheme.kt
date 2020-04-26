package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.AppTheme
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class SetAppTheme @Inject constructor(
        private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, SetAppTheme.Params>() {

    override suspend fun run(params: Params) {
        sessionRepository.setAppTheme(params.theme)
    }

    data class Params(
            val theme: AppTheme
    )
}