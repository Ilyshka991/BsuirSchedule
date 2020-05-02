package com.pechuro.bsuirschedule.feature.lessondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.interactor.GetLessonWeeks
import com.pechuro.bsuirschedule.domain.interactor.UpdateScheduleItem
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LessonDetailsViewModel @Inject constructor(
        private val getLessonWeeks: GetLessonWeeks,
        private val updateScheduleItem: UpdateScheduleItem
) : BaseViewModel() {

    companion object {
        private const val NOTE_CHANGED_DEBOUNCE = 500L
    }

    private val noteChangedChannel = BroadcastChannel<String>(1).also {
        it.asFlow()
                .debounce(NOTE_CHANGED_DEBOUNCE)
                .onEach(::updateNote)
                .launchIn(viewModelScope)
    }

    lateinit var lesson: Lesson

    val detailsItems: LiveData<List<DetailsItem>> = liveData {
        val weeks = getLessonWeeks.execute(GetLessonWeeks.Params(lesson)).getOrDefault(emptyList())
        emit(getDetailsItems(lesson, weeks))
    }

    private fun getDetailsItems(lesson: Lesson, weeks: List<WeekNumber>): List<DetailsItem> {
        val list = mutableListOf(
                DetailsItem.Time(lesson.startTime, lesson.endTime),
                getInfoItem(lesson),
                DetailsItem.Weeks(weeks)
        )
        if (lesson.auditories.isNotEmpty()) {
            list += DetailsItem.LocationHeader
        }
        list += lesson.auditories.map { DetailsItem.LocationItem(it) }
        list += DetailsItem.LessonFooter(lesson.note) { onNoteChanged(it) }
        return list
    }

    private fun onNoteChanged(note: String) {
        if (note == lesson.note) {
            return
        }
        noteChangedChannel.offer(note)
    }

    private fun getInfoItem(lesson: Lesson): DetailsItem = when (lesson) {
        is Lesson.GroupLesson -> DetailsItem.EmployeeInfo(lesson.employees)
        is Lesson.EmployeeLesson -> TODO()
    }

    private suspend fun updateNote(note: String) {
        val newLesson = lesson.copy(note = note)
        updateScheduleItem.execute(UpdateScheduleItem.Params(newLesson))
    }
}
