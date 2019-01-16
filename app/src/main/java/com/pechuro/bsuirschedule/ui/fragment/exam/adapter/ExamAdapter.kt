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

class ExamAdapter(private val diffCallback: ExamsDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _examsList = ArrayList<BaseExamData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        STUDENT -> {
            val viewBinding = ItemStudentExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentViewHolder(viewBinding)
        }
        EMPLOYEE -> {
            val viewBinding = ItemEmployeeExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeViewHolder(viewBinding)
        }
        EMPTY -> {
            val viewBinding = ItemEmptyExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyViewHolder(viewBinding)
        }
        else -> throw IllegalArgumentException()
    }

    override fun getItemViewType(position: Int) = when (_examsList[position]) {
        is StudentExamData -> STUDENT
        is EmployeeExamData -> EMPLOYEE
        is EmptyExamData -> EMPTY
        else -> throw IllegalArgumentException()
    }

    override fun getItemCount(): Int = _examsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: List<BaseExamData>) {
        val result = mutableListOf<BaseExamData>()
        result += if (data.isEmpty()) listOf(EmptyExamData()) else data

        diffCallback.setData(_examsList, result)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

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

    companion object ExamsViewTypes {
        const val STUDENT = 1
        const val EMPLOYEE = 2
        const val EMPTY = 0
    }
}