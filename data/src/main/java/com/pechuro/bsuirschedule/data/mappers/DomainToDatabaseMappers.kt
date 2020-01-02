package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.local.entity.*

fun Department.toDatabaseEntity() = run {
    DepartmentCached(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun AuditoryType.toDatabaseEntity() = run {
    AuditoryTypeCached(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun Building.toDatabaseEntity() = run {
    BuildingCached(
            id = id,
            name = name
    )
}

fun Auditory.toDatabaseEntity(
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

fun Employee.toDatabaseEntity() = run {
    EmployeeCached(
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
    EducationFormCached(
            id = id,
            name = name
    )
}

fun Group.toDatabaseEntity() = run {
    GroupCached(
            id = id,
            number = number,
            facultyId = faculty?.id,
            course = course
    )
}

fun Schedule.toDatabaseEntity() = run {
    ScheduleCached(
            id = id,
            name = name,
            type = type.value,
            lastUpdate = lastUpdated
    )
}

fun Speciality.toDatabaseEntity() = run {
    SpecialityCached(
            id = id,
            name = name,
            facultyId = faculty?.id,
            educationFormId = educationForm.id,
            abbreviation = abbreviation,
            code = code
    )
}

fun Faculty.toDatabaseEntity() = run {
    FacultyCached(
            id = id,
            abbreviation = abbreviation,
            name = name
    )
}