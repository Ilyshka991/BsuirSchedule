package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.local.entity.*

internal fun FacultyCached.toDomainEntity() = run {
    Faculty(
            id = id,
            abbreviation = abbreviation,
            name = name
    )
}

internal fun EmployeeCached.toDomainEntity() = run {
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

internal fun GroupCached.toDomainEntity(faculty: Faculty?) = run {
    Group(
            id = id,
            number = number,
            faculty = faculty,
            course = course
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