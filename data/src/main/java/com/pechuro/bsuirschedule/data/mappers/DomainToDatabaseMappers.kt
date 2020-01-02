package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.local.entity.*

fun Department.toDatabaseEntity() = run {
    DepartmentDB(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun AuditoryType.toDatabaseEntity() = run {
    AuditoryTypeDB(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun Building.toDatabaseEntity() = run {
    BuildingDB(
            id = id,
            name = name
    )
}

fun Auditory.toDatabaseEntity(
        building: BuildingDB,
        auditoryType: AuditoryTypeDB,
        department: DepartmentDB?
) = run {
    AuditoryDB(
            id = id,
            name = name,
            note = note,
            capacity = capacity,
            buildingId = building.id,
            auditoryTypeId = auditoryType.id,
            departmentId = department?.id
    )
}

fun Employee.toDatabaseEntity() = run {
    EmployeeDB(
            id = id,
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            abbreviation = abbreviation,
            photoLink = photoLink,
            rank = rank
    )
}

fun EducationForm.toDatabaseEntity() = run {
    EducationFormDB(
            id = id,
            name = name
    )
}

fun Group.toDatabaseEntity() = run {
    GroupDB(
            number = number,
            facultyId = faculty?.id,
            course = course
    )
}

fun Schedule.toDatabaseEntity() = run {
    ScheduleDB(
            id = id,
            name = name,
            type = type,
            lastUpdate = lastUpdate
    )
}

fun Speciality.toDatabaseEntity() = run {
    SpecialityDB(
            id = id,
            name = name,
            facultyId = faculty.id,
            educationFormId = educationForm.id,
            abbreviation = abbreviation,
            code = code
    )
}

fun Faculty.toDatabaseEntity() = run {
    FacultyDB(
            id = id,
            abbreviation = abbreviation,
            name = name
    )
}