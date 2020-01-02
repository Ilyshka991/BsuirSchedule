package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.local.entity.*

fun FacultyCached.toDomainEntity() = run {
    Faculty(
            id = id,
            abbreviation = abbreviation,
            name = name
    )
}

fun EmployeeCached.toDomainEntity() = run {
    Employee(
            id = id,
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            abbreviation = abbreviation,
            photoLink = photoLink,
            rank = rank
    )
}

fun DepartmentCached.toDomainEntity() = run {
    Department(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun AuditoryTypeCached.toDomainEntity() = run {
    AuditoryType(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun AuditoryCached.toDomainEntity(
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

fun BuildingCached.toDomainEntity() = run {
    Building(
            id = id,
            name = name
    )
}

fun GroupCached.toDomainEntity(faculty: Faculty?) = run {
    Group(
            id = id,
            number = number,
            faculty = faculty,
            course = course
    )
}

fun ScheduleCached.toDomainEntity() = run {
    Schedule(
            id = id,
            name = name,
            type = ScheduleType.getOrException(type),
            lastUpdated = lastUpdate
    )
}

fun EducationFormCached.toDomainEntity() = run {
    EducationForm(
            id = id,
            name = name
    )
}

fun SpecialityCached.toDomainEntity(
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