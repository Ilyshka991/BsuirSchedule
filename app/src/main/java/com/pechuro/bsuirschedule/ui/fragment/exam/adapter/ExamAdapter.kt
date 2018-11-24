package com.pechuro.bsuirschedule.ui.fragment.exam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemEmployeeExamBinding
import com.pechuro.bsuirschedule.databinding.ItemEmptyExamBinding
import com.pechuro.bsuirschedule.databinding.ItemStudentExamBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.exam.data.BaseExamData
import com.pechuro.bsuirschedule.ui.fragment.exam.data.impl.EmployeeExamData
import com.pechuro.bsuirschedule.ui.fragment.exam.data.impl.EmptyExamData
import com.pechuro.bsuirschedule.ui.fragment.exam.data.impl.StudentExamData
import org.jetbrains.anko.doAsyncResult


class ExamAdapter(private val diffCallback: ExamsDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _examsList = ArrayList<BaseExamData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewTypes.STUDENT.type -> {
            val viewBinding = ItemStudentExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentViewHolder(viewBinding)
        }
        ViewTypes.EMPLOYEE.type -> {
            val viewBinding = ItemEmployeeExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeViewHolder(viewBinding)
        }
        ViewTypes.EMPTY.type -> {
            val viewBinding = ItemEmptyExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyViewHolder(viewBinding)
        }
        else -> throw IllegalArgumentException()
    }

    override fun getItemViewType(position: Int) = when (_examsList[position]) {
        is StudentExamData -> ViewTypes.STUDENT.type
        is EmployeeExamData -> ViewTypes.EMPLOYEE.type
        is EmptyExamData -> ViewTypes.EMPTY.type
        else -> throw IllegalArgumentException()
    }

    override fun getItemCount(): Int = _examsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: List<BaseExamData>) {
        val result = mutableListOf<BaseExamData>()

        val diffResult = doAsyncResult {
            result += if (data.isEmpty()) listOf(EmptyExamData()) else data
            diffCallback.setData(_examsList, result)
            DiffUtil.calculateDiff(diffCallback)
        }.get()

        _examsList.clear()
        _examsList += result

        diffResult.dispatchUpdatesTo(this)
    }

    inner class StudentViewHolder(private val binding: ItemStudentExamBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val data = _examsList[position] as StudentExamData
            binding.data = data
            binding.executePendingBindings()
        }
    }

    inner class EmployeeViewHolder(private val binding: ItemEmployeeExamBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val data = _examsList[position] as EmployeeExamData
            binding.data = data
            binding.executePendingBindings()
        }
    }

    inner class EmptyViewHolder(binding: ItemEmptyExamBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {}
    }
}

private enum class ViewTypes(val type: Int) {
    STUDENT(1), EMPLOYEE(2), EMPTY(0)
}
