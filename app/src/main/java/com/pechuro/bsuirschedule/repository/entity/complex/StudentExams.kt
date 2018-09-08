package com.pechuro.bsuirschedule.repository.entity.complex

import android.arch.persistence.room.Relation
import com.pechuro.bsuirschedule.repository.entity.Schedule
import com.pechuro.bsuirschedule.repository.entity.item.StudentClassItem
import com.pechuro.bsuirschedule.repository.entity.item.StudentExamItem


class StudentExams(name: String, type: Int, lastUpdate: String) :
        Schedule(name, type, lastUpdate) {
    @Relation(parentColumn = "_id", entityColumn = "schedule_id",
            entity = StudentClassItem::class)
    lateinit var exams: List<StudentExamItem>
}