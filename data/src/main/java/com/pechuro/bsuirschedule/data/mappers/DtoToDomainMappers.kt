package com.pechuro.bsuirschedule.data.mappers

import android.annotation.SuppressLint
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.ext.parseDateOrDefault
import com.pechuro.bsuirschedule.remote.dto.*
import java.text.SimpleDateFormat
import java.util.*

internal fun BuildingDTO.toDomainEntity() = run {
    Building(
            id = id,
            name = name
    )
}

internal fun AuditoryTypeDTO.toDomainEntity() = run {
    AuditoryType(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun DepartmentDTO.toDomainEntity() = run {
    Department(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun AuditoryDTO.toDomainEntity() = run {
    val building = buildingNumber.toDomainEntity()
    val auditoryType = auditoryType.toDomainEntity()
    val department = department?.toDomainEntity()
    Auditory(
            id = id,
            name = name,
            note = note ?: "",
            capacity = capacity ?: 0,
            building = building,
            auditoryType = auditoryType,
            department = department
    )
}

internal fun EmployeeDTO.toDomainEntity() = run {
    Employee(
            id = id,
            firstName = firstName,
            middleName = middleName ?: "",
            lastName = lastName,
            abbreviation = abbreviation,
            photoLink = photoLink ?: "",
            rank = rank ?: ""
    )
}

internal fun GroupDTO.toDomainEntity(faculty: Faculty?) = run {
    Group(
            id = id,
            number = number,
            faculty = faculty,
            course = course ?: 1
    )
}

internal fun EducationFormDTO.toDomainEntity() = run {
    EducationForm(
            id = id,
            name = name
    )
}

internal fun FacultyDTO.toDomainEntity() = run {
    Faculty(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun SpecialityDTO.toDomainEntity(faculty: Faculty?) = run {
    Speciality(
            id = id,
            name = name,
            abbreviation = abbreviation,
            faculty = faculty,
            educationForm = educationForm.toDomainEntity(),
            code = code
    )
}

internal fun LastUpdateDTO.toDomainEntity() = run {
    dateFormatter.parseDateOrDefault(lastUpdateDate, Date(0))
}

internal fun List<ScheduleItemDTO>.toGroupLessons(
        schedule: Schedule.GroupClasses,
        auditories: List<Auditory>
): List<ScheduleItem.GroupLesson> {
    val resultList = mutableListOf<ScheduleItem.GroupLesson>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonEmployees = lesson.employees?.map { it.toDomainEntity() }
            val mappedScheduleItem = ScheduleItem.GroupLesson(
                    //This ID will be generated later
                    id = 0,
                    schedule = schedule,
                    subject = lesson.subject ?: "",
                    subgroupNumber = lesson.subgroupNumber,
                    lessonType = lesson.lessonType ?: "",
                    auditories = lessonAuditories ?: emptyList(),
                    note = lesson.note ?: "",
                    startTime = lesson.startTime ?: "",
                    endTime = lesson.endTime ?: "",
                    employees = lessonEmployees ?: emptyList(),
                    weekDay = getWeekDayFor(scheduleItem.weekDay),
                    weekNumbers = lesson.weekNumber
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}

internal fun List<ScheduleItemDTO>.toGroupExams(
        schedule: Schedule.GroupExams,
        auditories: List<Auditory>
): List<ScheduleItem.GroupExam> {
    val resultList = mutableListOf<ScheduleItem.GroupExam>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonEmployees = lesson.employees?.map { it.toDomainEntity() }
            val mappedScheduleItem = ScheduleItem.GroupExam(
                    //This ID will be generated later
                    id = 0,
                    schedule = schedule,
                    subject = lesson.subject ?: "",
                    subgroupNumber = lesson.subgroupNumber,
                    lessonType = lesson.lessonType ?: "",
                    auditories = lessonAuditories ?: emptyList(),
                    note = lesson.note ?: "",
                    startTime = lesson.startTime ?: "",
                    endTime = lesson.endTime ?: "",
                    employees = lessonEmployees ?: emptyList(),
                    date = dateFormatter.parseDateOrDefault(scheduleItem.weekDay, Date())
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}

internal fun List<ScheduleItemDTO>.toEmployeeLessons(
        schedule: Schedule.EmployeeClasses,
        groups: List<Group>,
        auditories: List<Auditory>
): List<ScheduleItem.EmployeeLesson> {
    val resultList = mutableListOf<ScheduleItem.EmployeeLesson>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonGroups = lesson.studentGroups?.let { lessonGroups ->
                groups.filter { it.number in lessonGroups }
            }
            val mappedScheduleItem = ScheduleItem.EmployeeLesson(
                    //This ID will be generated later
                    id = 0,
                    schedule = schedule,
                    subject = lesson.subject ?: "",
                    weekNumbers = lesson.weekNumber,
                    subgroupNumber = lesson.subgroupNumber,
                    lessonType = lesson.lessonType ?: "",
                    auditories = lessonAuditories ?: emptyList(),
                    note = lesson.note ?: "",
                    startTime = lesson.startTime ?: "",
                    endTime = lesson.endTime ?: "",
                    weekDay = getWeekDayFor(scheduleItem.weekDay),
                    studentGroups = lessonGroups ?: emptyList()
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}

internal fun List<ScheduleItemDTO>.toEmployeeExams(
        schedule: Schedule.EmployeeExams,
        groups: List<Group>,
        auditories: List<Auditory>
): List<ScheduleItem.EmployeeExam> {
    val resultList = mutableListOf<ScheduleItem.EmployeeExam>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonGroups = lesson.studentGroups?.let { lessonGroups ->
                groups.filter { it.number in lessonGroups }
            }
            val mappedScheduleItem = ScheduleItem.EmployeeExam(
                    //This ID will be generated later
                    id = 0,
                    schedule = schedule,
                    subject = lesson.subject ?: "",
                    subgroupNumber = lesson.subgroupNumber,
                    lessonType = lesson.lessonType ?: "",
                    auditories = lessonAuditories ?: emptyList(),
                    note = lesson.note ?: "",
                    startTime = lesson.startTime ?: "",
                    endTime = lesson.endTime ?: "",
                    studentGroups = lessonGroups ?: emptyList(),
                    date = dateFormatter.parseDateOrDefault(scheduleItem.weekDay, Date())
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}


@SuppressLint("DefaultLocale")
private fun getWeekDayFor(value: String) = when (value.toLowerCase()) {
    "понедельник" -> WeekDay.MONDAY
    "вторник" -> WeekDay.TUESDAY
    "среда" -> WeekDay.WEDNESDAY
    "четверг" -> WeekDay.THURSDAY
    "пятница" -> WeekDay.FRIDAY
    "суббота" -> WeekDay.SATURDAY
    "воскресение" -> WeekDay.SUNDAY
    else -> WeekDay.SUNDAY
}

@SuppressLint("SimpleDateFormat")
private val dateFormatter = SimpleDateFormat("dd.MM.yyyy")
