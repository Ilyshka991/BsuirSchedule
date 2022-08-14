package com.pechuro.bsuirschedule.data.mappers

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
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.Speciality
import com.pechuro.bsuirschedule.domain.entity.toDate
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.building.AuditoryTypeCached
import com.pechuro.bsuirschedule.local.entity.building.BuildingCached
import com.pechuro.bsuirschedule.local.entity.education.DepartmentCached
import com.pechuro.bsuirschedule.local.entity.education.EducationFormCached
import com.pechuro.bsuirschedule.local.entity.education.FacultyCached
import com.pechuro.bsuirschedule.local.entity.education.SpecialityCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeClassesScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeExamScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemClassesCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupClassesScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupExamScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemClassesCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeExamItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupExamItemComplex
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

internal fun Department.toDatabaseEntity() = run {
    DepartmentCached(
        id = id,
        name = name,
        abbreviation = abbreviation
    )
}

internal fun AuditoryType.toDatabaseEntity() = run {
    AuditoryTypeCached(
        id = id,
        name = name,
        abbreviation = abbreviation
    )
}

internal fun Building.toDatabaseEntity() = run {
    BuildingCached(
        id = id,
        name = name
    )
}

internal fun Auditory.toDatabaseEntity(
    building: BuildingCached,
    auditoryType: AuditoryTypeCached,
    department: DepartmentCached?
) = run {
    AuditoryCached(
        id = id,
        name = name,
        note = note,
        capacity = capacity,
        buildingId = building.id,
        auditoryTypeId = auditoryType.id,
        departmentId = department?.id
    )
}

internal fun List<Auditory>.toDatabaseEntity() = map {
    val auditoryTypeCached = it.auditoryType.toDatabaseEntity()
    val departmentCached = it.department?.toDatabaseEntity()
    val buildingCached = it.building.toDatabaseEntity()
    it.toDatabaseEntity(
        auditoryType = auditoryTypeCached,
        building = buildingCached,
        department = departmentCached
    )
}

internal fun Employee.toDatabaseEntity() = run {
    EmployeeCached(
        id = id,
        urlId = urlId,
        firstName = firstName,
        middleName = middleName,
        lastName = lastName,
        abbreviation = abbreviation,
        photoLink = photoLink,
        rank = rank,
        departmentId = department?.id
    )
}

internal fun EducationForm.toDatabaseEntity() = run {
    EducationFormCached(
        id = id,
        name = name
    )
}

internal fun Group.toDatabaseEntity() = run {
    GroupCached(
        id = id,
        number = number,
        course = course,
        specialityId = speciality.id
    )
}

internal fun Speciality.toDatabaseEntity() = run {
    SpecialityCached(
        id = id,
        name = name,
        facultyId = faculty?.id,
        educationFormId = educationForm.id,
        abbreviation = abbreviation,
        code = code
    )
}

internal fun Faculty.toDatabaseEntity() = run {
    FacultyCached(
        id = id,
        abbreviation = abbreviation,
        name = name
    )
}

internal fun Schedule.GroupClasses.toDatabaseEntity() = run {
    GroupClassesScheduleCached(
        name = name,
        lastUpdate = lastUpdatedDate,
        groupId = group.id,
        notRemindForUpdates = notRemindForUpdates
    )
}

internal fun Schedule.GroupExams.toDatabaseEntity() = run {
    GroupExamScheduleCached(
        name = name,
        lastUpdate = lastUpdatedDate,
        groupId = group.id,
        notRemindForUpdates = notRemindForUpdates
    )
}

internal fun Lesson.GroupLesson.toDatabaseEntity(schedule: GroupClassesScheduleCached) =
    toDatabaseEntity(schedule.name)

internal fun Lesson.GroupLesson.toDatabaseEntity(scheduleName: String) = run {
    val scheduleItem = GroupItemClassesCached(
        id = id,
        scheduleName = scheduleName,
        subject = subject,
        subgroupNumber = subgroupNumber.value,
        lessonType = lessonType,
        note = note,
        startTime = startTime.toDate(),
        endTime = endTime.toDate(),
        weekDay = weekDay.index,
        weekNumber = weekNumber.index,
        priority = priority.value,
        isAddedByUser = isAddedByUser
    )
    val employees = employees.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    GroupClassesItemComplex(
        scheduleItem = scheduleItem,
        employees = employees,
        auditories = auditories
    )
}

internal fun Exam.GroupExam.toDatabaseEntity(schedule: GroupExamScheduleCached) =
    toDatabaseEntity(schedule.name)

internal fun Exam.GroupExam.toDatabaseEntity(scheduleName: String) = run {
    val scheduleItem = GroupItemExamCached(
        id = id,
        scheduleName = scheduleName,
        subject = subject,
        subgroupNumber = subgroupNumber.value,
        lessonType = lessonType,
        note = note,
        startTime = startTime.toDate(),
        endTime = endTime.toDate(),
        date = date.toDate(),
        isAddedByUser = isAddedByUser
    )
    val employees = employees.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    GroupExamItemComplex(
        scheduleItem = scheduleItem,
        employees = employees,
        auditories = auditories
    )
}

internal fun Schedule.EmployeeClasses.toDatabaseEntity() = run {
    EmployeeClassesScheduleCached(
        name = name,
        employeeId = employee.id,
        lastUpdate = lastUpdatedDate,
        notRemindForUpdates = notRemindForUpdates
    )
}

internal fun Schedule.EmployeeExams.toDatabaseEntity() = run {
    EmployeeExamScheduleCached(
        name = name,
        employeeId = employee.id,
        lastUpdate = lastUpdatedDate,
        notRemindForUpdates = notRemindForUpdates
    )
}

internal fun Lesson.EmployeeLesson.toDatabaseEntity(schedule: EmployeeClassesScheduleCached) =
    toDatabaseEntity(schedule.name)

internal fun Lesson.EmployeeLesson.toDatabaseEntity(scheduleName: String) = run {
    val scheduleItem = EmployeeItemClassesCached(
        id = id,
        scheduleName = scheduleName,
        subject = subject,
        weekNumber = weekNumber.index,
        subgroupNumber = subgroupNumber.value,
        lessonType = lessonType,
        note = note,
        startTime = startTime.toDate(),
        endTime = endTime.toDate(),
        weekDay = weekDay.index,
        priority = priority.value,
        isAddedByUser = isAddedByUser
    )
    val groups = studentGroups.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    EmployeeClassesItemComplex(
        scheduleItem = scheduleItem,
        groups = groups,
        auditories = auditories
    )
}

internal fun Exam.EmployeeExam.toDatabaseEntity(schedule: EmployeeExamScheduleCached) =
    toDatabaseEntity(schedule.name)

internal fun Exam.EmployeeExam.toDatabaseEntity(scheduleName: String) = run {
    val scheduleItem = EmployeeItemExamCached(
        id = id,
        scheduleName = scheduleName,
        subject = subject,
        subgroupNumber = subgroupNumber.value,
        lessonType = lessonType,
        note = note,
        startTime = startTime.toDate(),
        endTime = endTime.toDate(),
        date = date.toDate(),
        isAddedByUser = isAddedByUser
    )
    val groups = studentGroups.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    EmployeeExamItemComplex(
        scheduleItem = scheduleItem,
        groups = groups,
        auditories = auditories
    )
}