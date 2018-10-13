package com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemListClassesDayBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.data.ClassesDayData

class ClassesDayAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val mItemsList = ArrayList<ClassesDayData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val cacheViewBinding = ItemListClassesDayBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(cacheViewBinding)

    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: List<ClassesDayData>) {
        mItemsList.clear()
        mItemsList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val mBinding: ItemListClassesDayBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position]
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }
}