package com.pechuro.bsuirschedule.feature.lessondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.interactor.GetLessonWeeks
import javax.inject.Inject

class LessonDetailsViewModel @Inject constructor(
        private val getLessonWeeks: GetLessonWeeks
) : BaseViewModel() {

    lateinit var lesson: Lesson

    val detailsItems: LiveData<List<DetailsItem>> = liveData {
        val weeks = getLessonWeeks.execute(GetLessonWeeks.Params(lesson)).getOrDefault(emptyList())
        emit(getDetailsItems(weeks))
    }

    private fun getDetailsItems(weeks: List<WeekNumber>): List<DetailsItem> {
        val list = mutableListOf<DetailsItem>(DetailsItem.LessonHeader(lesson, weeks))
        list += lesson.auditories.map { DetailsItem.LocationItem(it) }
        return list
    }

}
