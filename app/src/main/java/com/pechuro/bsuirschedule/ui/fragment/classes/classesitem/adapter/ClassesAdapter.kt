package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.*
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseClassesData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.*
import org.jetbrains.anko.doAsyncResult
import kotlin.IllegalArgumentException

class ClassesAdapter(private val diffCallback: ClassesDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _classesList = ArrayList<BaseClassesData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewTypes.STUDENT_DAY.type -> {
            val viewBinding = ItemStudentsClassesDayBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentDayViewHolder(viewBinding)
        }
        ViewTypes.STUDENT_WEEK.type -> {
            val viewBinding = ItemStudentClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentWeekViewHolder(viewBinding)
        }
        ViewTypes.EMPLOYEE_DAY.type -> {
            val viewBinding = ItemEmployeeClassesDayBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeDayViewHolder(viewBinding)
        }
        ViewTypes.EMPLOYEE_WEEK.type -> {
            val viewBinding = ItemEmployeeClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeWeekViewHolder(viewBinding)
        }
        ViewTypes.EMPTY.type -> {
            val viewBinding = ItemClassesEmptyBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyViewHolder(viewBinding)
        }
        else -> throw IllegalArgumentException()
    }

    override fun getItemViewType(position: Int) = when (_classesList[position]) {
        is StudentClassesDayData -> ViewTypes.STUDENT_DAY.type
        is StudentClassesWeekData -> ViewTypes.STUDENT_WEEK.type
        is EmployeeClassesDayData -> ViewTypes.EMPLOYEE_DAY.type
        is EmployeeClassesWeekData -> ViewTypes.EMPLOYEE_WEEK.type
        is EmptyClassesData -> ViewTypes.EMPTY.type
        else -> throw IllegalArgumentException()
    }

    override fun getItemCount(): Int = _classesList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: List<BaseClassesData>) {
        val result = mutableListOf<BaseClassesData>()

        val diffResult = doAsyncResult {
            result += if (data.isEmpty()) listOf(EmptyClassesData()) else data
            diffCallback.setData(_classesList, result)
            DiffUtil.calculateDiff(diffCallback)
        }.get()

        _classesList.clear()
        _classesList += result

        diffResult.dispatchUpdatesTo(this)
    }

    inner class StudentDayViewHolder(private val mBinding: ItemStudentsClassesDayBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = _classesList[position] as StudentClassesDayData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class StudentWeekViewHolder(private val mBinding: ItemStudentClassesWeekBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = _classesList[position] as StudentClassesWeekData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class EmployeeDayViewHolder(private val mBinding: ItemEmployeeClassesDayBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = _classesList[position] as EmployeeClassesDayData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class EmployeeWeekViewHolder(private val mBinding: ItemEmployeeClassesWeekBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = _classesList[position] as EmployeeClassesWeekData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class EmptyViewHolder(mBinding: ItemClassesEmptyBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {}
    }
}

private enum class ViewTypes(val type: Int) {
    STUDENT_DAY(1), STUDENT_WEEK(2),
    EMPLOYEE_DAY(3), EMPLOYEE_WEEK(4),
    EMPTY(0)
}