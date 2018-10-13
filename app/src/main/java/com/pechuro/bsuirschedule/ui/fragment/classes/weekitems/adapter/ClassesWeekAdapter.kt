package com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemListClassesWeekBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.data.ClassesWeekData

class ClassesWeekAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val mItemsList = ArrayList<ClassesWeekData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val cacheViewBinding = ItemListClassesWeekBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(cacheViewBinding)

    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: List<ClassesWeekData>) {
        mItemsList.clear()
        mItemsList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val mBinding: ItemListClassesWeekBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position]
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }
}