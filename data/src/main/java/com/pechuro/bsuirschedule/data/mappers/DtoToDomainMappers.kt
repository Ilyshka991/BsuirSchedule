package com.pechuro.bsuirschedule.data.mappers

import android.annotation.SuppressLint
import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.AuditoryType
import com.pechuro.bsuirschedule.domain.entity.Building
import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.EducationForm
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Exam
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.LessonPriority
import com.pechuro.bsuirschedule.domain.entity.LocalTime
import com.pechuro.bsuirschedule.domain.entity.Speciality
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.domain.entity.WeekDay
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.entity.getLocalDate
import com.pechuro.bsuirschedule.domain.entity.getLocalTime
import com.pechuro.bsuirschedule.domain.ext.parseOrDefault
import com.pechuro.bsuirschedule.remote.dto.AuditoryDTO
import com.pechuro.bsuirschedule.remote.dto.AuditoryTypeDTO
import com.pechuro.bsuirschedule.remote.dto.BuildingDTO
import com.pechuro.bsuirschedule.remote.dto.DepartmentDTO
import com.pechuro.bsuirschedule.remote.dto.EducationFormDTO
import com.pechuro.bsuirschedule.remote.dto.EmployeeDTO
import com.pechuro.bsuirschedule.remote.dto.FacultyDTO
import com.pechuro.bsuirschedule.remote.dto.GroupDTO
import com.pechuro.bsuirschedule.remote.dto.LastUpdateDTO
import com.pechuro.bsuirschedule.remote.dto.ScheduleItemDTO
import com.pechuro.bsuirschedule.remote.dto.SpecialityDTO
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

internal fun EmployeeDTO.toDomainEntity(department: Department?) = run {
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

internal fun GroupDTO.toDomainEntity(speciality: Speciality) = run {
    Group(
        id = id,
        number = number,
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
        name = name ?: "",
        abbreviation = abbreviation ?: ""
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
): List<Lesson.GroupLesson> {
    val resultList = mutableListOf<Lesson.GroupLesson>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonEmployees = lesson.employees?.map { employeeDto ->
                val department = departments.find {
                    it.abbreviation == employeeDto.departmentAbbreviation.firstOrNull()
                }
                employeeDto.toDomainEntity(department)
            }
            getResultWeekNumbers(lesson.weekNumber).forEach { weekNumber ->
                val lessonType = lesson.lessonType?.uppercase(Locale.getDefault()) ?: ""
                val mappedScheduleItem = Lesson.GroupLesson(
                    //This ID will be generated later
                    id = 0,
                    subject = lesson.subject ?: "",
                    subgroupNumber = SubgroupNumber.getForValue(lesson.subgroupNumber),
                    lessonType = lesson.lessonType ?: "",
                    auditories = lessonAuditories ?: emptyList(),
                    note = lesson.note ?: "",
                    startTime = lesson.startTime?.run { timeFormatter.parse(this) }?.getLocalTime()
                        ?: LocalTime.of(0, 0),
                    endTime = lesson.endTime?.run { timeFormatter.parse(this) }?.getLocalTime()
                        ?: LocalTime.of(0, 0),
                    employees = lessonEmployees ?: emptyList(),
                    weekDay = getWeekDayFor(scheduleItem.weekDay),
                    weekNumber = weekNumber,
                    priority = LessonPriority.getDefaultForLessonType(lessonType),
                    isAddedByUser = false
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
): List<Exam.GroupExam> {
    val resultList = mutableListOf<Exam.GroupExam>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonEmployees = lesson.employees?.map { employeeDto ->
                val department = departments.find {
                    it.abbreviation == employeeDto.departmentAbbreviation.firstOrNull()
                }
                employeeDto.toDomainEntity(department)
            }
            val mappedScheduleItem = Exam.GroupExam(
                //This ID will be generated later
                id = 0,
                subject = lesson.subject ?: "",
                subgroupNumber = SubgroupNumber.getForValue(lesson.subgroupNumber),
                lessonType = lesson.lessonType ?: "",
                auditories = lessonAuditories ?: emptyList(),
                note = lesson.note ?: "",
                startTime = lesson.startTime?.run { timeFormatter.parse(this) }?.getLocalTime()
                    ?: LocalTime.of(0, 0),
                endTime = lesson.endTime?.run { timeFormatter.parse(this) }?.getLocalTime()
                    ?: LocalTime.of(0, 0),
                employees = lessonEmployees ?: emptyList(),
                date = dateFormatter.parseOrDefault(scheduleItem.weekDay, Date(0)).getLocalDate(),
                isAddedByUser = false
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}

internal fun List<ScheduleItemDTO>.toEmployeeLessons(
    groups: List<Group>,
    auditories: List<Auditory>
): List<Lesson.EmployeeLesson> {
    val resultList = mutableListOf<Lesson.EmployeeLesson>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonGroups = lesson.studentGroups?.let { lessonGroups ->
                groups.filter { it.number in lessonGroups }
            }
            getResultWeekNumbers(lesson.weekNumber).forEach { weekNumber ->
                val lessonType = lesson.lessonType?.uppercase(Locale.getDefault()) ?: ""
                val mappedScheduleItem = Lesson.EmployeeLesson(
                    //This ID will be generated later
                    id = 0,
                    subject = lesson.subject ?: "",
                    weekNumber = weekNumber,
                    subgroupNumber = SubgroupNumber.getForValue(lesson.subgroupNumber),
                    lessonType = lessonType,
                    auditories = lessonAuditories ?: emptyList(),
                    note = lesson.note ?: "",
                    startTime = lesson.startTime?.run { timeFormatter.parse(this) }?.getLocalTime()
                        ?: LocalTime.of(0, 0),
                    endTime = lesson.endTime?.run { timeFormatter.parse(this) }?.getLocalTime()
                        ?: LocalTime.of(0, 0),
                    weekDay = getWeekDayFor(scheduleItem.weekDay),
                    studentGroups = lessonGroups ?: emptyList(),
                    priority = LessonPriority.getDefaultForLessonType(lessonType),
                    isAddedByUser = false
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
): List<Exam.EmployeeExam> {
    val resultList = mutableListOf<Exam.EmployeeExam>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonGroups = lesson.studentGroups?.let { lessonGroups ->
                groups.filter { it.number in lessonGroups }
            }
            val mappedScheduleItem = Exam.EmployeeExam(
                //This ID will be generated later
                id = 0,
                subject = lesson.subject ?: "",
                subgroupNumber = SubgroupNumber.getForValue(lesson.subgroupNumber),
                lessonType = lesson.lessonType ?: "",
                auditories = lessonAuditories ?: emptyList(),
                note = lesson.note ?: "",
                startTime = lesson.startTime?.run { timeFormatter.parse(this) }?.getLocalTime()
                    ?: LocalTime.of(0, 0),
                endTime = lesson.endTime?.run { timeFormatter.parse(this) }?.getLocalTime()
                    ?: LocalTime.of(0, 0),
                studentGroups = lessonGroups ?: emptyList(),
                date = dateFormatter.parseOrDefault(scheduleItem.weekDay, Date(0)).getLocalDate(),
                isAddedByUser = false
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}


@SuppressLint("DefaultLocale")
private fun getWeekDayFor(value: String) = when (value.lowercase(Locale.getDefault())) {
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

private val timeFormatter: DateFormat
    get() = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
        isLenient = false
    }

private fun getResultWeekNumbers(source: List<Int>) = if (source.contains(0)) {
    WeekNumber.values().toList()
} else {
    source.map { WeekNumber.getForIndex(it - 1) }
}