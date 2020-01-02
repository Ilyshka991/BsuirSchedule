package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.remote.dto.*

fun BuildingDTO.toDomainEntity() = run {
    Building(
            id = id,
            name = name
    )
}

fun AuditoryTypeDTO.toDomainEntity() = run {
    AuditoryType(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun DepartmentDTO.toDomainEntity() = run {
    Department(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun AuditoryDTO.toDomainEntity() = run {
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

fun EmployeeDTO.toDomainEntity() = run {
    Employee(
            id = employeeId,
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            abbreviation = abbreviation,
            photoLink = photoLink,
            rank = rank
    )
}

fun GroupDTO.toDomainEntity(faculty: Faculty?) = run {
    Group(
            number = number,
            faculty = faculty,
            course = course
    )
}

fun AnnouncementDTO.toDomainEntity() = run {
    Announcement(
            date = date,
            content = content,
            employeeName = employeeName
    )
}

fun EducationFormDTO.toDomainEntity() = run {
    EducationForm(
            id = id,
            name = name
    )
}

fun FacultyDTO.toDomainEntity() = run {
    Faculty(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

fun SpecialityDTO.toDomainEntity(faculty: Faculty) = run {
    Speciality(
            id = id,
            name = name,
            abbreviation = abbreviation,
            faculty = faculty,
            educationForm = educationForm.toDomainEntity(),
            code = code
    )
}
