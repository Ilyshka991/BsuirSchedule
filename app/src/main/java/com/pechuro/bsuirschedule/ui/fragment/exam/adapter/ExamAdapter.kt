package com.pechuro.bsuirschedule.ui.fragment.exam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemEmployeeExamBinding
import com.pechuro.bsuirschedule.databinding.ItemEmptyExamBinding
import com.pechuro.bsuirschedule.databinding.ItemStudentExamBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.base.BaseViewHolderData
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
            ExamAdapterViewHolders.StudentViewHolder(viewBinding)
        }
        EMPLOYEE -> {
            val viewBinding = ItemEmployeeExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            ExamAdapterViewHolders.EmployeeViewHolder(viewBinding)
        }
        EMPTY -> {
            val viewBinding = ItemEmptyExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            ExamAdapterViewHolders.EmptyViewHolder(viewBinding)
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

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(_examsList[position])

    fun setItems(data: List<BaseExamData>) {
        val result = mutableListOf<BaseExamData>()
        result += if (data.isEmpty()) listOf(EmptyExamData()) else data

        diffCallback.setData(_examsList, result)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        _examsList.clear()
        _examsList += result

        diffResult.dispatchUpdatesTo(this)
    }

    private companion object ExamsViewTypes {
        const val STUDENT = 1
        const val EMPLOYEE = 2
        const val EMPTY = 0
    }
}

sealed class ExamAdapterViewHolders {

    class StudentViewHolder(private val binding: ItemStudentExamBinding) : BaseViewHolder(binding.root) {

        override fun onBind(data: BaseViewHolderData) {
            binding.data = data as StudentExamData
            binding.executePendingBindings()
        }
    }

    class EmployeeViewHolder(private val binding: ItemEmployeeExamBinding) : BaseViewHolder(binding.root) {

        override fun onBind(data: BaseViewHolderData) {
            binding.data = data as EmployeeExamData
            binding.executePendingBindings()
        }
    }

    class EmptyViewHolder(binding: ItemEmptyExamBinding) : BaseViewHolder(binding.root) {

        override fun onBind(data: BaseViewHolderData) {}
    }
}