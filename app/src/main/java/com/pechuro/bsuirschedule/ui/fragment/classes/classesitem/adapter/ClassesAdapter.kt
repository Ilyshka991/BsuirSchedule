package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemListClassesDayBinding
import com.pechuro.bsuirschedule.databinding.ItemListClassesWeekBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.StudentClassesDayData
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.StudentClassesWeekData

enum class ViewTypes {
    STUDENT_DAY, STUDENT_WEEK,
    EMPLOYEE_DAY, EMPLOYEE_WEEK,
    UNDEFINED
}

class ClassesAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val mItemsList = ArrayList<BaseData>()
    private var mViewType = ViewTypes.UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (mViewType) {
        ViewTypes.STUDENT_DAY -> {
            val viewBinding = ItemListClassesDayBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentDayViewHolder(viewBinding)
        }
        ViewTypes.STUDENT_WEEK -> {
            val viewBinding = ItemListClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentWeekViewHolder(viewBinding)
        }
        ViewTypes.EMPLOYEE_DAY -> {
            val viewBinding = ItemListClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentWeekViewHolder(viewBinding)
        }
        ViewTypes.EMPLOYEE_WEEK -> {
            val viewBinding = ItemListClassesWeekBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            StudentWeekViewHolder(viewBinding)
        }
        ViewTypes.UNDEFINED -> throw IllegalStateException()
    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: Pair<ViewTypes, List<BaseData>>) {
        mItemsList.clear()
        mItemsList.addAll(data.second)
        mViewType = data.first
        notifyDataSetChanged()
    }

    inner class StudentWeekViewHolder(private val mBinding: ItemListClassesWeekBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position] as StudentClassesWeekData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }

    inner class StudentDayViewHolder(private val mBinding: ItemListClassesDayBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position] as StudentClassesDayData
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }
}