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
import com.pechuro.bsuirschedule.databinding.ItemListNavEmptyBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavTitleBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.drawer.DrawerEvent
import com.pechuro.bsuirschedule.ui.utils.EventBus

enum class ViewType(val type: Int) {
    VIEW_TITLE(-1),
    VIEW_ITEM(-2),
    VIEW_EMPTY(-3)
}

class NavItemAdapter(private val context: Context,
                     private val diffCallback: NavItemsDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _itemsList = mutableListOf<ScheduleInformation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewType.VIEW_ITEM.type -> {
            val viewBinding = ItemListNavBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(viewBinding)
        }

        ViewType.VIEW_TITLE.type -> {
            val viewBinding = ItemListNavTitleBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            TitleViewHolder(viewBinding)
        }

        ViewType.VIEW_EMPTY.type -> {
            val viewBinding = ItemListNavEmptyBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyViewHolder(viewBinding)
        }

        else -> throw IllegalStateException()
    }

    override fun getItemViewType(position: Int) = when {
        _itemsList[position].type == ViewType.VIEW_EMPTY.type -> ViewType.VIEW_EMPTY.type
        _itemsList[position].type == ViewType.VIEW_TITLE.type -> ViewType.VIEW_TITLE.type
        else -> ViewType.VIEW_ITEM.type
    }

    override fun getItemCount() = _itemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: Map<Int, List<ScheduleInformation>>) {
        val result = mutableListOf<ScheduleInformation>()

        if (data[SCHEDULES]?.isNotEmpty() == true) {
            result += ScheduleInformation(-1, context.getString(R.string.nav_drawer_title_schedules), ViewType.VIEW_TITLE.type)
            result += data[SCHEDULES]!!
        }

        if (data[EXAMS]?.isNotEmpty() == true) {
            result += ScheduleInformation(-1, context.getString(R.string.nav_drawer_title_exams), ViewType.VIEW_TITLE.type)
            result += data[EXAMS]!!
        }

        if (data[SCHEDULES].isNullOrEmpty() && data[EXAMS].isNullOrEmpty()) {
            result += ScheduleInformation(-1, "DSF", ViewType.VIEW_EMPTY.type)
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

    inner class TitleViewHolder(private val mBinding: ItemListNavTitleBinding) : BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            val data = _itemsList[position]
            mBinding.data = NavItemData(data.name)
            mBinding.executePendingBindings()
        }
    }

    inner class EmptyViewHolder(private val mBinding: ItemListNavEmptyBinding) : BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {}
    }
}