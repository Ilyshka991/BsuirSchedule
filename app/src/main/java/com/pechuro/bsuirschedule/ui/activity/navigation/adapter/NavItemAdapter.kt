package com.pechuro.bsuirschedule.ui.activity.navigation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemListNavBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder

class NavItemAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val mItemsList = ArrayList<NavItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val viewBinding = ItemListNavBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: List<NavItemData>) {
        mItemsList.clear()
        mItemsList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val mBinding: ItemListNavBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position]
            mBinding.data = data
            mBinding.executePendingBindings()
        }
    }
}