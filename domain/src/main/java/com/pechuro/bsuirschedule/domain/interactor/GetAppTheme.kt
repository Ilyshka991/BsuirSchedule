package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.AppTheme
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class GetAppTheme @Inject constructor(
    private val sessionRepository: ISessionRepository
) : BaseInteractor<AppTheme, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams): AppTheme = sessionRepository.getAppTheme()
}