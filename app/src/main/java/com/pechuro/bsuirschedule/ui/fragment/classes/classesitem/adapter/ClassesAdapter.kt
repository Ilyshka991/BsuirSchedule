package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemEmployeeClassesDayBinding
import com.pechuro.bsuirschedule.databinding.ItemEmployeeClassesWeekBinding
import com.pechuro.bsuirschedule.databinding.ItemStudentClassesWeekBinding
import com.pechuro.bsuirschedule.databinding.ItemStudentsClassesDayBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseClassesData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.EmployeeClassesDayData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.EmployeeClassesWeekData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.StudentClassesDayData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.StudentClassesWeekData

enum class ViewTypes {
    STUDENT_DAY, STUDENT_WEEK,
    EMPLOYEE_DAY, EMPLOYEE_WEEK,
    UNDEFINED
}

class ClassesAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val mItemsList = ArrayList<BaseClassesData>()
    private var mViewType = ViewTypes.UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (mViewType) {
        ViewTypes.STUDENT_DAY -> {
            val viewBinding = ItemStudentsClassesDayBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentDayViewHolder(viewBinding)
        }
        ViewTypes.STUDENT_WEEK -> {
            val viewBinding = ItemStudentClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentWeekViewHolder(viewBinding)
        }
        ViewTypes.EMPLOYEE_DAY -> {
            val viewBinding = ItemEmployeeClassesDayBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeDayViewHolder(viewBinding)
        }
        ViewTypes.EMPLOYEE_WEEK -> {
            val viewBinding = ItemEmployeeClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeWeekViewHolder(viewBinding)
        }
        ViewTypes.UNDEFINED -> throw IllegalStateException()
    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: Pair<ViewTypes, List<BaseClassesData>>) {
        mItemsList.clear()
        mItemsList.addAll(data.second)
        mViewType = data.first
        notifyDataSetChanged()
    }

    inner class StudentDayViewHolder(private val mBinding: ItemStudentsClassesDayBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position] as StudentClassesDayData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class StudentWeekViewHolder(private val mBinding: ItemStudentClassesWeekBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position] as StudentClassesWeekData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class EmployeeDayViewHolder(private val mBinding: ItemEmployeeClassesDayBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position] as EmployeeClassesDayData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class EmployeeWeekViewHolder(private val mBinding: ItemEmployeeClassesWeekBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position] as EmployeeClassesWeekData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }
}