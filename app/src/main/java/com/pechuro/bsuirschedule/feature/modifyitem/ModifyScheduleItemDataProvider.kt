package com.pechuro.bsuirschedule.feature.modifyitem

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.ext.*
import java.text.SimpleDateFormat
import java.util.*

class ModifyScheduleItemDataProvider(private val context: Context) {

    val subjectData = MutableLiveData<String>()
    val lessonTypeData = MutableLiveData<String>()
    val subgroupNumberData = MutableLiveData<SubgroupNumber>()
    val noteData = MutableLiveData<String>()
    val startTimeData = MutableLiveData<String>()
    val endTimeData = MutableLiveData<String>()
    val priorityData = MutableLiveData<LessonPriority>()
    val weekDayData = MutableLiveData<WeekDay>()
    val weekNumberData = MutableLiveData<List<WeekNumber>>()
    val dateData = MutableLiveData<Date>()
    val auditoriesData = MutableLiveData<List<Auditory>>()
    val employeesData = MutableLiveData<List<Employee>>()
    val studentGroupsData = MutableLiveData<List<Group>>()

    private val timeFormatter = SimpleDateFormat(SCHEDULE_ITEM_TIME_FORMAT_PATTERN, Locale.getDefault())

    lateinit var schedule: Schedule
        private set
    var scheduleItems: Array<ScheduleItem> = emptyArray()
        private set

    fun init(schedule: Schedule, scheduleItems: Array<ScheduleItem>) {
        this.schedule = schedule
        this.scheduleItems = scheduleItems
        val scheduleItem = scheduleItems.firstOrNull()

        subjectData.value = scheduleItem?.subject ?: ""

        val lessonType = scheduleItem?.lessonType ?: getDefaultLessonType(schedule)
        lessonTypeData.value = lessonType

        val subgroupNumber = scheduleItem?.subgroupNumber ?: SubgroupNumber.ALL
        subgroupNumberData.value = subgroupNumber

        noteData.value = scheduleItem?.note ?: ""

        val defaultTime = timeFormatter.format(Date())
        startTimeData.value = scheduleItem?.startTime ?: defaultTime
        endTimeData.value = scheduleItem?.endTime ?: defaultTime

        var priority = LessonPriority.getDefaultForLessonType(lessonType)
        var weekDay = Calendar.getInstance().getWeekDay()
        var weekNumber = listOf(WeekNumber.calculateCurrentWeekNumber())
        var date = Date()
        when (scheduleItem) {
            is Lesson -> {
                priority = scheduleItem.priority
                weekDay = scheduleItem.weekDay
                val resultWeekNumbers = scheduleItems.filterIsInstance<Lesson>().map { it.weekNumber }
                if (resultWeekNumbers.isNotEmpty()) {
                    weekNumber = resultWeekNumbers
                }
            }
            is Exam -> {
                date = scheduleItem.date
            }
        }
        priorityData.value = priority
        weekDayData.value = weekDay
        weekNumberData.value = weekNumber
        dateData.value = date

        auditoriesData.value = scheduleItem?.auditories ?: emptyList()

        val employees: List<Employee> = emptyList()
        employeesData.value = employees

        val studentGroups: List<Group> = emptyList()
        studentGroupsData.value = studentGroups
    }

    fun getResultScheduleItem(): List<ScheduleItem> {
        val subject = subjectData.requireValue
        val subgroupNumber = subgroupNumberData.requireValue
        val lessonType = lessonTypeData.requireValue
        val note = noteData.requireValue
        val startTime = startTimeData.requireValue
        val endTime = endTimeData.requireValue
        val auditories = auditoriesData.requireValue
        val employees = employeesData.requireValue
        val studentGroups = studentGroupsData.requireValue
        val isAddedByUser = scheduleItems.isEmpty()
        val weekNumbers = weekNumberData.requireValue.addIfEmpty { WeekNumber.calculateCurrentWeekNumber() }
        return when (schedule) {
            is Schedule.GroupClasses -> weekNumbers.map { weekNumber ->
                Lesson.GroupLesson(
                        id = 0,
                        subject = subject,
                        subgroupNumber = subgroupNumber,
                        lessonType = lessonType,
                        note = note,
                        startTime = startTime,
                        endTime = endTime,
                        auditories = auditories,
                        isAddedByUser = isAddedByUser,
                        priority = priorityData.requireValue,
                        weekDay = weekDayData.requireValue,
                        weekNumber = weekNumber,
                        employees = employees
                )
            }
            is Schedule.EmployeeClasses -> weekNumbers.map { weekNumber ->
                Lesson.EmployeeLesson(
                        id = 0,
                        subject = subject,
                        subgroupNumber = subgroupNumber,
                        lessonType = lessonType,
                        note = note,
                        startTime = startTime,
                        endTime = endTime,
                        auditories = auditories,
                        isAddedByUser = isAddedByUser,
                        priority = priorityData.requireValue,
                        weekDay = weekDayData.requireValue,
                        weekNumber = weekNumber,
                        studentGroups = studentGroups
                )
            }
            is Schedule.GroupExams -> listOf(Exam.GroupExam(
                    id = 0,
                    subject = subject,
                    subgroupNumber = subgroupNumber,
                    lessonType = lessonType,
                    note = note,
                    startTime = startTime,
                    endTime = endTime,
                    auditories = auditories,
                    isAddedByUser = isAddedByUser,
                    date = dateData.requireValue,
                    employees = employees
            )
            )
            is Schedule.EmployeeExams -> listOf(Exam.EmployeeExam(
                    id = 0,
                    subject = subject,
                    subgroupNumber = subgroupNumber,
                    lessonType = lessonType,
                    note = note,
                    startTime = startTime,
                    endTime = endTime,
                    auditories = auditories,
                    isAddedByUser = isAddedByUser,
                    date = dateData.requireValue,
                    studentGroups = studentGroups
            ))
        }
    }

    private fun getDefaultLessonType(schedule: Schedule): String {
        val isClassesSchedule = schedule is Schedule.GroupClasses || schedule is Schedule.EmployeeClasses
        val arrayResId = if (isClassesSchedule) R.array.lesson_types else R.array.exam_types
        return context.resources.getStringArray(arrayResId).first()
    }
}