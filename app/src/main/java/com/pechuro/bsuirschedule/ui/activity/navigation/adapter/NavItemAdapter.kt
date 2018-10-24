package com.pechuro.bsuirschedule.ui.activity.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.SCHEDULES
import com.pechuro.bsuirschedule.databinding.ItemListNavBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavMenuBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder

private const val NAV_ITEM_MENU = -1
private const val NAV_ITEM = -2

class NavItemAdapter(private val mContext: Context) : RecyclerView.Adapter<BaseViewHolder>() {
    lateinit var callback: NavCallback
    private val mItemsList = ArrayList<ScheduleInformation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            when (viewType) {
                NAV_ITEM -> {
                    val viewBinding = ItemListNavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    ItemViewHolder(viewBinding)
                }

                NAV_ITEM_MENU -> {
                    val viewBinding = ItemListNavMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    MenuViewHolder(viewBinding)
                }
                else -> throw IllegalStateException()
            }


    override fun getItemViewType(position: Int) =
            if (mItemsList[position].type == NAV_ITEM_MENU) NAV_ITEM_MENU else NAV_ITEM

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: Map<Int, List<ScheduleInformation>>) {
        mItemsList.clear()
        if (data[SCHEDULES]?.isNotEmpty() == true) {
            mItemsList.add(ScheduleInformation(mContext.getString(R.string.schedules), NAV_ITEM_MENU))
            mItemsList.addAll(data[SCHEDULES]!!)
        }

        if (data[EXAMS]?.isNotEmpty() == true) {
            mItemsList.add(ScheduleInformation(mContext.getString(R.string.exams), NAV_ITEM_MENU))
            mItemsList.addAll(data[EXAMS]!!)
        }

        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val mBinding: ItemListNavBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = mItemsList[position]
            mBinding.data = NavItemData(data.name)
            mBinding.executePendingBindings()

            mBinding.scheduleNameButton.apply {
                setOnClickListener {
                    callback.onNavigate(ScheduleInformation(data.name, data.type))
                }

                setOnLongClickListener {

                    true
                }
            }
        }
    }

    inner class MenuViewHolder(private val mBinding: ItemListNavMenuBinding) : BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            val data = mItemsList[position]
            mBinding.data = NavItemData(data.name)
            mBinding.executePendingBindings()
        }
    }

    interface NavCallback {
        fun onNavigate(info: ScheduleInformation)
    }
}