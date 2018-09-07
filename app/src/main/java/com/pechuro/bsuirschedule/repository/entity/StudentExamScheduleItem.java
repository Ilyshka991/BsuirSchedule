package com.pechuro.bsuirschedule.repository.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "student_exam_schedule")
public class StudentExamScheduleItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer _id;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "week_day")
    private String weekDay;

    @ColumnInfo(name = "number_subgroup")
    private Integer numSubGroup;

    @ColumnInfo(name = "employee")
    private String employee;

    @ColumnInfo(name = "employeeRank")
    private String employeeRank;

    @ColumnInfo(name = "lesson_type")
    private String lessonType;

    @ColumnInfo(name = "auditory")
    private String auditory;

    @ColumnInfo(name = "lesson_time")
    private String lessonTime;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "studentGroup")
    private String studentGroup;

    @ColumnInfo(name = "employeeFirstName")
    private String employeeFirstName;

    @ColumnInfo(name = "employeeLastName")
    private String employeeLastName;

    @ColumnInfo(name = "employeeMiddleName")
    private String employeeMiddleName;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getNumSubGroup() {
        return numSubGroup;
    }

    public void setNumSubGroup(Integer numSubGroup) {
        this.numSubGroup = numSubGroup;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getAuditory() {
        return auditory;
    }

    public void setAuditory(String auditory) {
        this.auditory = auditory;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(String lessonTime) {
        this.lessonTime = lessonTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeRank() {
        return employeeRank;
    }

    public void setEmployeeRank(String employeeRank) {
        this.employeeRank = employeeRank;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeMiddleName() {
        return employeeMiddleName;
    }

    public void setEmployeeMiddleName(String employeeMiddleName) {
        this.employeeMiddleName = employeeMiddleName;
    }
}
