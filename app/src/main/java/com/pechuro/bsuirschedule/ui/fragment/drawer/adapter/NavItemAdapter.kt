package com.pechuro.bsuirschedule.ui.fragment.drawer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.SCHEDULES
import com.pechuro.bsuirschedule.databinding.ItemListNavBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavMenuBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.drawer.DrawerEvent
import com.pechuro.bsuirschedule.ui.utils.EventBus

private const val NAV_ITEM_MENU = -1
private const val NAV_ITEM = -2

class NavItemAdapter(private val context: Context,
                     private val diffCallback: NavItemsDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _itemsList = mutableListOf<ScheduleInformation>()

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
            if (_itemsList[position].type == NAV_ITEM_MENU) NAV_ITEM_MENU else NAV_ITEM

    override fun getItemCount(): Int = _itemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: Map<Int, List<ScheduleInformation>>) {
        val result = mutableListOf<ScheduleInformation>()

        if (data[SCHEDULES]?.isNotEmpty() == true) {
            result.add(ScheduleInformation(-1, context.getString(R.string.schedules), NAV_ITEM_MENU))
            result.addAll(data[SCHEDULES]!!)
        }

        if (data[EXAMS]?.isNotEmpty() == true) {
            result.add(ScheduleInformation(-1, context.getString(R.string.exams), NAV_ITEM_MENU))
            result.addAll(data[EXAMS]!!)
        }

        diffCallback.setData(_itemsList, result)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        _itemsList.clear()
        _itemsList += result

        diffResult.dispatchUpdatesTo(this)
    }

    inner class ItemViewHolder(private val mBinding: ItemListNavBinding) : BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val data = _itemsList[position]
            mBinding.data = NavItemData(data.name)
            mBinding.executePendingBindings()

            mBinding.scheduleNameButton.apply {
                setOnClickListener {
                    EventBus.publish(DrawerEvent.OnNavigate(ScheduleInformation(data.id, data.name, data.type)))
                }

                setOnLongClickListener {
                    EventBus.publish(DrawerEvent.OnItemLongClick(ScheduleInformation(data.id, data.name, data.type)))
                    true
                }
            }
        }
    }

    inner class MenuViewHolder(private val mBinding: ItemListNavMenuBinding) : BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            val data = _itemsList[position]
            mBinding.data = NavItemData(data.name)
            mBinding.executePendingBindings()
        }
    }
}