package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.*
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.ClassesItemEvent
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseClassesData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl.*
import com.pechuro.bsuirschedule.ui.utils.EventBus

class ClassesAdapter(private val diffCallback: ClassesDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _classesList = ArrayList<BaseClassesData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        EMPTY -> {
            val viewBinding = ItemClassesEmptyBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyViewHolder(viewBinding)
        }
        STUDENT_DAY -> {
            val viewBinding = ItemStudentClassesDayBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentDayViewHolder(viewBinding)
        }
        STUDENT_WEEK -> {
            val viewBinding = ItemStudentClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentWeekViewHolder(viewBinding)
        }
        EMPLOYEE_DAY -> {
            val viewBinding = ItemEmployeeClassesDayBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeDayViewHolder(viewBinding)
        }
        EMPLOYEE_WEEK -> {
            val viewBinding = ItemEmployeeClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeWeekViewHolder(viewBinding)
        }
        else -> throw IllegalArgumentException("Illegal view type")
    }

    override fun getItemViewType(position: Int) = when (_classesList[position]) {
        is EmptyClassesData -> EMPTY
        is StudentClassesDayData -> STUDENT_DAY
        is StudentClassesWeekData -> STUDENT_WEEK
        is EmployeeClassesDayData -> EMPLOYEE_DAY
        is EmployeeClassesWeekData -> EMPLOYEE_WEEK
        else -> throw IllegalArgumentException("Illegal view type")
    }

    override fun getItemCount(): Int = _classesList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (position < _classesList.size) {
            holder.onBind(position)
        }
    }

    fun setItems(data: List<BaseClassesData>) {
        val result = mutableListOf<BaseClassesData>()
        result += if (data.isEmpty()) listOf(EmptyClassesData()) else data
        if (_classesList.isEmpty()) {
            _classesList += result
            notifyDataSetChanged()
        } else {
            diffCallback.setData(_classesList, result)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            _classesList.clear()
            _classesList += result
            diffResult.dispatchUpdatesTo(this)
        }
    }

    inner class StudentDayViewHolder(private val binding: ItemStudentClassesDayBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val data = _classesList[position] as StudentClassesDayData
            binding.data = data
            binding.executePendingBindings()

            binding.container.apply {
                setOnLongClickListener {
                    EventBus.publish(ClassesItemEvent.OnItemLongClick(data.itemId))
                    true
                }
            }
        }
    }

    inner class StudentWeekViewHolder(private val binding: ItemStudentClassesWeekBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val data = _classesList[position] as StudentClassesWeekData
            binding.data = data
            binding.executePendingBindings()

            binding.container.apply {
                setOnLongClickListener {
                    EventBus.publish(ClassesItemEvent.OnItemLongClick(data.itemId))
                    true
                }
            }
        }
    }

    inner class EmployeeDayViewHolder(private val binding: ItemEmployeeClassesDayBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val data = _classesList[position] as EmployeeClassesDayData
            binding.data = data
            binding.executePendingBindings()

            binding.container.apply {
                setOnLongClickListener {
                    EventBus.publish(ClassesItemEvent.OnItemLongClick(data.itemId))
                    true
                }
            }
        }
    }

    inner class EmployeeWeekViewHolder(private val binding: ItemEmployeeClassesWeekBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {

            val data = _classesList[position] as EmployeeClassesWeekData
            binding.data = data
            binding.executePendingBindings()

            binding.container.apply {
                setOnLongClickListener {
                    EventBus.publish(ClassesItemEvent.OnItemLongClick(data.itemId))
                    true
                }
            }
        }
    }

    inner class EmptyViewHolder(mBinding: ItemClassesEmptyBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {}
    }

    companion object ClassesViewTypes {
        const val STUDENT_DAY = 1
        const val STUDENT_WEEK = 2
        const val EMPLOYEE_DAY = 3
        const val EMPLOYEE_WEEK = 4
        const val EMPTY = 0
    }
}