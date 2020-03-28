package com.pechuro.bsuirschedule.data.mappers

import android.annotation.SuppressLint
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.exception.DataSourceException
import com.pechuro.bsuirschedule.domain.ext.parseOrDefault
import com.pechuro.bsuirschedule.remote.dto.*
import java.text.DateFormat
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
            capacity = capacity ?: -1,
            building = building,
            auditoryType = auditoryType,
            department = department
    )
}

internal fun EmployeeDTO.toDomainEntity(department: Department) = run {
    Employee(
            id = id,
            firstName = firstName,
            middleName = middleName ?: "",
            lastName = lastName,
            abbreviation = abbreviation,
            photoLink = photoLink ?: "",
            rank = rank ?: "",
            department = department
    )
}

internal fun GroupDTO.toDomainEntity(faculty: Faculty, speciality: Speciality) = run {
    Group(
            id = id,
            number = number,
            faculty = faculty,
            course = course ?: -1,
            speciality = speciality
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
    dateFormatter.parseOrDefault(lastUpdateDate, Date(0))
}

internal fun List<ScheduleItemDTO>.toGroupLessons(
        auditories: List<Auditory>,
        departments: List<Department>
): List<ScheduleItem.GroupLesson> {
    val resultList = mutableListOf<ScheduleItem.GroupLesson>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonEmployees = lesson.employees?.map { employeeDto ->
                val department = departments.find {
                    it.abbreviation == employeeDto.departmentAbbreviation.firstOrNull()
                } ?: throw DataSourceException.InvalidData
                employeeDto.toDomainEntity(department)
            }
            getResultWeekNumbers(lesson.weekNumber).forEach { weekNumber ->
                val lessonType = lesson.lessonType?.toUpperCase(Locale.getDefault()) ?: ""
                val mappedScheduleItem = ScheduleItem.GroupLesson(
                        //This ID will be generated later
                        id = 0,
                        subject = lesson.subject ?: "",
                        subgroupNumber = lesson.subgroupNumber,
                        lessonType = lesson.lessonType ?: "",
                        auditories = lessonAuditories ?: emptyList(),
                        note = lesson.note ?: "",
                        startTime = lesson.startTime ?: "",
                        endTime = lesson.endTime ?: "",
                        employees = lessonEmployees ?: emptyList(),
                        weekDay = getWeekDayFor(scheduleItem.weekDay),
                        weekNumber = weekNumber,
                        priority = LessonPriority.getDefaultForLessonType(lessonType)
                )
                resultList.add(mappedScheduleItem)
            }
        }
    }
    return resultList
}

internal fun List<ScheduleItemDTO>.toGroupExams(
        auditories: List<Auditory>,
        departments: List<Department>
): List<ScheduleItem.GroupExam> {
    val resultList = mutableListOf<ScheduleItem.GroupExam>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonEmployees = lesson.employees?.map { employeeDto ->
                val department = departments.find {
                    it.abbreviation == employeeDto.departmentAbbreviation.firstOrNull()
                } ?: throw DataSourceException.InvalidData
                employeeDto.toDomainEntity(department)
            }
            val mappedScheduleItem = ScheduleItem.GroupExam(
                    //This ID will be generated later
                    id = 0,
                    subject = lesson.subject ?: "",
                    subgroupNumber = lesson.subgroupNumber,
                    lessonType = lesson.lessonType ?: "",
                    auditories = lessonAuditories ?: emptyList(),
                    note = lesson.note ?: "",
                    startTime = lesson.startTime ?: "",
                    endTime = lesson.endTime ?: "",
                    employees = lessonEmployees ?: emptyList(),
                    date = dateFormatter.parseOrDefault(scheduleItem.weekDay, Date())
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}

internal fun List<ScheduleItemDTO>.toEmployeeLessons(
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
            getResultWeekNumbers(lesson.weekNumber).forEach { weekNumber ->
                val lessonType = lesson.lessonType?.toUpperCase(Locale.getDefault()) ?: ""
                val mappedScheduleItem = ScheduleItem.EmployeeLesson(
                        //This ID will be generated later
                        id = 0,
                        subject = lesson.subject ?: "",
                        weekNumber = weekNumber,
                        subgroupNumber = lesson.subgroupNumber,
                        lessonType = lessonType,
                        auditories = lessonAuditories ?: emptyList(),
                        note = lesson.note ?: "",
                        startTime = lesson.startTime ?: "",
                        endTime = lesson.endTime ?: "",
                        weekDay = getWeekDayFor(scheduleItem.weekDay),
                        studentGroups = lessonGroups ?: emptyList(),
                        priority = LessonPriority.getDefaultForLessonType(lessonType)
                )
                resultList.add(mappedScheduleItem)
            }
        }
    }
    return resultList
}

internal fun List<ScheduleItemDTO>.toEmployeeExams(
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
                    subject = lesson.subject ?: "",
                    subgroupNumber = lesson.subgroupNumber,
                    lessonType = lesson.lessonType ?: "",
                    auditories = lessonAuditories ?: emptyList(),
                    note = lesson.note ?: "",
                    startTime = lesson.startTime ?: "",
                    endTime = lesson.endTime ?: "",
                    studentGroups = lessonGroups ?: emptyList(),
                    date = dateFormatter.parseOrDefault(scheduleItem.weekDay, Date())
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

private val dateFormatter: DateFormat
    get() = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
        isLenient = false
    }

private fun getResultWeekNumbers(source: List<Int>) = if (source.contains(0)) {
    WeekNumber.values().toList()
} else {
    source.map { WeekNumber.getForIndex(it - 1) }
}