package com.pechuro.bsuirschedule.ui.fragment.exam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemListExamBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.exam.data.ExamData

class ExamAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val mItemsList = ArrayList<ExamData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val cacheViewBinding = ItemListExamBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(cacheViewBinding)

    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: List<ExamData>) {
        mItemsList.clear()
        mItemsList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val mBinding: ItemListExamBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position]
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }
}