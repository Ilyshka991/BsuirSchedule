package com.pechuro.bsuirschedule.ui.activity.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleTypes.SCHEDULES
import com.pechuro.bsuirschedule.databinding.ItemListNavBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavMenuBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.INavigator
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder


class NavItemAdapter(private val mContext: Context) : RecyclerView.Adapter<BaseViewHolder>() {
    companion object {
        private const val NAV_ITEM_MENU = -1
        private const val NAV_ITEM = -2
    }

    lateinit var navigator: INavigator
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


    override fun getItemViewType(position: Int): Int {
        return if (mItemsList[position].type == NAV_ITEM_MENU) NAV_ITEM_MENU else NAV_ITEM
    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: Map<Int, List<ScheduleInformation>>) {
        mItemsList.clear()
        if (data.containsKey(SCHEDULES)) {

            mItemsList.add(ScheduleInformation(mContext.getString(R.string.schedules), NAV_ITEM_MENU))
            mItemsList.addAll(data[SCHEDULES]!!)
        }

        if (data.containsKey(EXAMS)) {
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
                    navigator.onNavigate(ScheduleInformation(data.name, data.type))
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
}