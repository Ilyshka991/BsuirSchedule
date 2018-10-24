package com.pechuro.bsuirschedule.ui.fragment.exam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemEmployeeExamBinding
import com.pechuro.bsuirschedule.databinding.ItemStudentExamBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.exam.data.BaseExamData
import com.pechuro.bsuirschedule.ui.fragment.exam.data.impl.EmployeeExamData
import com.pechuro.bsuirschedule.ui.fragment.exam.data.impl.StudentExamData

enum class ExamViewTypes {
    STUDENT, EMPLOYEE, UNDEFINED
}

class ExamAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val mItemsList = ArrayList<BaseExamData>()
    private var mViewType = ExamViewTypes.UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (mViewType) {
        ExamViewTypes.STUDENT -> {
            val viewBinding = ItemStudentExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentViewHolder(viewBinding)
        }
        ExamViewTypes.EMPLOYEE -> {
            val viewBinding = ItemEmployeeExamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmployeeViewHolder(viewBinding)
        }
        ExamViewTypes.UNDEFINED -> throw IllegalStateException()

    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: Pair<ExamViewTypes, List<BaseExamData>>) {
        mItemsList.clear()
        mViewType = data.first
        mItemsList.addAll(data.second)
        notifyDataSetChanged()
    }

    inner class StudentViewHolder(private val mBinding: ItemStudentExamBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position] as StudentExamData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class EmployeeViewHolder(private val mBinding: ItemEmployeeExamBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position] as EmployeeExamData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }
}