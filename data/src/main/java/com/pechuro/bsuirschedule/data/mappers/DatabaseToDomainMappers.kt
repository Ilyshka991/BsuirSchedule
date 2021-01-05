package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.building.AuditoryTypeCached
import com.pechuro.bsuirschedule.local.entity.building.BuildingCached
import com.pechuro.bsuirschedule.local.entity.education.DepartmentCached
import com.pechuro.bsuirschedule.local.entity.education.EducationFormCached
import com.pechuro.bsuirschedule.local.entity.education.FacultyCached
import com.pechuro.bsuirschedule.local.entity.education.SpecialityCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeClassesScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeExamScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupClassesScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupExamScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeExamItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupExamItemComplex
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

internal fun FacultyCached.toDomainEntity() = run {
    Faculty(
            id = id,
            abbreviation = abbreviation,
            name = name
    )
}

internal fun EmployeeCached.toDomainEntity(
        department: Department?
) = run {
    Employee(
            id = id,
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            abbreviation = abbreviation,
            photoLink = photoLink,
            rank = rank,
            department = department
    )
}

internal fun DepartmentCached.toDomainEntity() = run {
    Department(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun AuditoryTypeCached.toDomainEntity() = run {
    AuditoryType(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun AuditoryCached.toDomainEntity(
        building: Building,
        auditoryType: AuditoryType,
        department: Department?
) = run {
    Auditory(
            id = id,
            name = name,
            note = note,
            capacity = capacity,
            building = building,
            auditoryType = auditoryType,
            department = department
    )
}

internal fun BuildingCached.toDomainEntity() = run {
    Building(
            id = id,
            name = name
    )
}

internal fun GroupCached.toDomainEntity(
        speciality: Speciality
) = run {
    Group(
            id = id,
            number = number,
            course = course,
            speciality = speciality
    )
}

internal fun EducationFormCached.toDomainEntity() = run {
    EducationForm(
            id = id,
            name = name
    )
}

internal fun SpecialityCached.toDomainEntity(
        faculty: Faculty?,
        educationForm: EducationForm
) = run {
    Speciality(
            id = id,
            name = name,
            faculty = faculty,
            educationForm = educationForm,
            abbreviation = abbreviation,
            code = code
    )
}

internal fun GroupClassesScheduleCached.toDomainEntity(group: Group) = run {
    Schedule.GroupClasses(
            name = name,
            lastUpdatedDate = lastUpdate,
            group = group,
            notRemindForUpdates = notRemindForUpdates
    )
}

internal fun GroupExamScheduleCached.toDomainEntity(group: Group) = run {
    Schedule.GroupExams(
            name = name,
            lastUpdatedDate = lastUpdate,
            group = group,
            notRemindForUpdates = notRemindForUpdates
    )
}

internal fun EmployeeClassesScheduleCached.toDomainEntity(employee: Employee) = run {
    Schedule.EmployeeClasses(
            name = name,
            employee = employee
    )
}

internal fun EmployeeExamScheduleCached.toDomainEntity(employee: Employee) = run {
    Schedule.EmployeeExams(
            name = name,
            employee = employee
    )
}

internal fun GroupClassesItemComplex.toDomainEntity(
        auditories: List<Auditory>,
        employees: List<Employee>
) = run {
    Lesson.GroupLesson(
            id = scheduleItem.id,
            subject = scheduleItem.subject,
            subgroupNumber = SubgroupNumber.getForValue(scheduleItem.subgroupNumber),
            lessonType = scheduleItem.lessonType,
            note = scheduleItem.note,
            startTime = scheduleItem.startTime.getLocalTime(),
            endTime = scheduleItem.endTime.getLocalTime(),
            weekDay = WeekDay.getForIndex(scheduleItem.weekDay),
            weekNumber = WeekNumber.getForIndex(scheduleItem.weekNumber),
            auditories = auditories,
            employees = employees,
            priority = LessonPriority.getForValue(scheduleItem.priority),
            isAddedByUser = scheduleItem.isAddedByUser
    )
}

internal fun GroupExamItemComplex.toDomainEntity(
        auditories: List<Auditory>,
        employees: List<Employee>
) = run {
    Exam.GroupExam(
            id = scheduleItem.id,
            subject = scheduleItem.subject,
            subgroupNumber = SubgroupNumber.getForValue(scheduleItem.subgroupNumber),
            lessonType = scheduleItem.lessonType,
            note = scheduleItem.note,
            startTime = scheduleItem.startTime.getLocalTime(),
            endTime = scheduleItem.endTime.getLocalTime(),
            date = scheduleItem.date.getLocalDate(),
            auditories = auditories,
            employees = employees,
            isAddedByUser = scheduleItem.isAddedByUser
    )
}


internal fun EmployeeClassesItemComplex.toDomainEntity(
        auditories: List<Auditory>,
        groups: List<Group>
) = run {
    Lesson.EmployeeLesson(
            id = scheduleItem.id,
            subject = scheduleItem.subject,
            subgroupNumber = SubgroupNumber.getForValue(scheduleItem.subgroupNumber),
            lessonType = scheduleItem.lessonType,
            note = scheduleItem.note,
            startTime = scheduleItem.startTime.getLocalTime(),
            endTime = scheduleItem.endTime.getLocalTime(),
            weekDay = WeekDay.getForIndex(scheduleItem.weekDay),
            weekNumber = WeekNumber.getForIndex(scheduleItem.weekNumber),
            auditories = auditories,
            studentGroups = groups,
            priority = LessonPriority.getForValue(scheduleItem.priority),
            isAddedByUser = scheduleItem.isAddedByUser
    )
}

internal fun EmployeeExamItemComplex.toDomainEntity(
        auditories: List<Auditory>,
        groups: List<Group>
) = run {
    Exam.EmployeeExam(
            id = scheduleItem.id,
            subject = scheduleItem.subject,
            subgroupNumber = SubgroupNumber.getForValue(scheduleItem.subgroupNumber),
            lessonType = scheduleItem.lessonType,
            note = scheduleItem.note,
            startTime = scheduleItem.startTime.getLocalTime(),
            endTime = scheduleItem.endTime.getLocalTime(),
            date = scheduleItem.date.getLocalDate(),
            auditories = auditories,
            studentGroups = groups,
            isAddedByUser = scheduleItem.isAddedByUser
    )
}