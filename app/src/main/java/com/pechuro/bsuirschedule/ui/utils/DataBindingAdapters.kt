package com.pechuro.bsuirschedule.ui.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.data.entity.Employee

@BindingAdapter("visibility")
fun setVisibilityGone(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibility_invisible")
fun setVisibilityInvisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("lesson_type_image")
fun setLessonTypeImage(view: ImageView, type: String?) {
    val id = when (type) {
        "ЛК" -> R.mipmap.ic_type_lk
        "ПЗ" -> R.mipmap.ic_type_pz
        "ЛР" -> R.mipmap.ic_type_lr
        else -> R.mipmap.ic_type_unknown
    }
    view.setImageResource(id)
}

@BindingAdapter("text_note")
fun setNote(view: TextView, note: String?) =
        if (note.isNullOrEmpty()) view.visibility = View.GONE else view.text = note

@BindingAdapter("text_auditory")
fun setAuditories(view: TextView, auditories: List<String>?) {
    var text = ""
    auditories?.forEachIndexed { index, auditory ->
        text += auditory + (if (index != auditories.lastIndex) "\n" else "")
    }
    view.text = text
}

@BindingAdapter("text_employee")
fun setEmployees(view: TextView, employees: List<Employee>?) {
    var text = ""
    employees?.forEachIndexed { index, employee ->
        text += "${employee.lastName} "
        if (employee.lastName.length < 15) {
            text += (if (!employee.firstName.isNullOrEmpty()) employee.firstName!![0] + "." else "") +
                    (if (!employee.middleName.isNullOrEmpty()) employee.middleName!![0] + "." else "")
        }
        text += (if (index != employees.lastIndex) "\n" else "")
    }
    view.text = text
}

@BindingAdapter("text_subgroup")
fun setSubgroup(view: TextView, number: Int?) =
        if (number != 0) {
            view.text = view.context.getString(R.string.item_classes_text_subgroup, number)
        } else {
            view.visibility = View.GONE
        }


@BindingAdapter("text_week_number")
fun setWeekNumber(view: TextView, weekNumbers: String?) {
    val result = if (weekNumbers != "0 1 2 3 4 ") weekNumbers?.trim() else view.context.getString(R.string.msg_all)
    view.text = view.context.getString(R.string.item_classes_text_week_number, result)
}