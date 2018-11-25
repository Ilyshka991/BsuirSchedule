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
import org.jetbrains.anko.doAsyncResult

class ClassesAdapter(private val diffCallback: ClassesDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _classesList = ArrayList<BaseClassesData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewTypes.STUDENT_DAY.type -> {
            val viewBinding = ItemStudentClassesDayBinding
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
}

private enum class ViewTypes(val type: Int) {
    STUDENT_DAY(1), STUDENT_WEEK(2),
    EMPLOYEE_DAY(3), EMPLOYEE_WEEK(4),
    EMPTY(0)
}