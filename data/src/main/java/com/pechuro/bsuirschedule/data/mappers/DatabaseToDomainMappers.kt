package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.local.entity.*

fun FacultyDB.toDomainEntity() = run {
    Faculty(
            id = id,
            abbreviation = abbreviation,
            name = name
    )
}

fun EmployeeDB.toDomainEntity() = run {
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

fun DepartmentDB.toDomainEntity() = run {
    Department(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun AuditoryTypeDB.toDomainEntity() = run {
    AuditoryType(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun AuditoryDB.toDomainEntity(
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

fun BuildingDB.toDomainEntity() = run {
    Building(
            id = id,
            name = name
    )
}

fun GroupDB.toDomainEntity(faculty: Faculty?) = run {
    Group(
            id = id,
            number = number,
            faculty = faculty,
            course = course
    )
}

fun ScheduleDB.toDomainEntity() = run {
    Schedule(
            id = id,
            name = name,
            type = type,
            lastUpdate = lastUpdate
    )
}

fun EducationFormDB.toDomainEntity() = run {
    EducationForm(
            id = id,
            name = name
    )
}

fun SpecialityDB.toDomainEntity(
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