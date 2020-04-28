package com.pechuro.bsuirschedule.feature.lessondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.interactor.GetLessonWeeks
import com.pechuro.bsuirschedule.ext.flowLiveData
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class LessonDetailsViewModel @Inject constructor(
        private val getLessonWeeks: GetLessonWeeks
) : BaseViewModel() {

    lateinit var lesson: Lesson

    val weeks: LiveData<List<WeekNumber>> = liveData {
        val weeks = getLessonWeeks.execute(GetLessonWeeks.Params(lesson)).getOrDefault(emptyList())
        emit(weeks)
    }
}
