package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.remote.dto.*

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
            note = note,
            capacity = capacity,
            building = building,
            auditoryType = auditoryType,
            department = department
    )
}

internal fun EmployeeDTO.toDomainEntity() = run {
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

internal fun GroupDTO.toDomainEntity(faculty: Faculty?) = run {
    Group(
            id = id,
            number = number,
            faculty = faculty,
            course = course
    )
}

internal fun AnnouncementDTO.toDomainEntity() = run {
    Announcement(
            date = date,
            content = content,
            employeeName = employeeName
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
