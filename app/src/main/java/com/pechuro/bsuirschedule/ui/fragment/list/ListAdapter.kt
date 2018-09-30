package com.pechuro.bsuirschedule.ui.fragment.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.databinding.ItemListViewBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder

class ListAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val mItemsList = ArrayList<ListItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val cacheViewBinding = ItemListViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return CacheViewHolder(cacheViewBinding)

    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun addItems(data: List<ListItemData>) {
        mItemsList.addAll(data)
        notifyDataSetChanged()
    }

    fun clearItems() = mItemsList.clear()

    inner class CacheViewHolder(private val mBinding: ItemListViewBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val mCacheItemViewModel = mItemsList[position]
            mBinding.data = mCacheItemViewModel
            mBinding.executePendingBindings()
        }
    }
}