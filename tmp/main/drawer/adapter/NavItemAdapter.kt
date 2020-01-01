package com.pechuro.bsuirschedule.feature.main.drawer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes.EXAMS
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes.SCHEDULES
import com.pechuro.bsuirschedule.databinding.ItemListNavBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavDividerBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavEmptyBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavTitleBinding
import com.pechuro.bsuirschedule.common.BaseViewHolder
import com.pechuro.bsuirschedule.common.BaseViewHolderData
import com.pechuro.bsuirschedule.feature.main.ScheduleInformation
import com.pechuro.bsuirschedule.feature.main.drawer.DrawerEvent
import com.pechuro.bsuirschedule.common.EventBus

class NavItemAdapter(private val context: Context,
                     private val diffCallback: NavItemsDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _itemsList = mutableListOf<ScheduleInformation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ITEM -> {
            val viewBinding = ItemListNavBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            NavAdapterViewHolders.ItemViewHolder(viewBinding)
        }

        TITLE -> {
            val viewBinding = ItemListNavTitleBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            NavAdapterViewHolders.TitleViewHolder(viewBinding)
        }

        EMPTY -> {
            val viewBinding = ItemListNavEmptyBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            NavAdapterViewHolders.EmptyViewHolder(viewBinding)
        }

        DIVIDER -> {
            val viewBinding = ItemListNavDividerBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            NavAdapterViewHolders.DividerViewHolder(viewBinding)
        }

        else -> throw IllegalStateException()
    }

    override fun getItemViewType(position: Int) = when {
        _itemsList[position].type == EMPTY -> EMPTY
        _itemsList[position].type == DIVIDER -> DIVIDER
        _itemsList[position].type == TITLE -> TITLE
        else -> ITEM
    }

    override fun getItemCount() = _itemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(_itemsList[position])

    fun setItems(data: Map<Int, List<ScheduleInformation>>) {
        val result = mutableListOf<ScheduleInformation>()

        if (data[SCHEDULES].isNullOrEmpty() && data[EXAMS].isNullOrEmpty()) {
            result += ScheduleInformation(EMPTY, "", EMPTY)
        } else {
            if (data[SCHEDULES]?.isNotEmpty() == true) {
                result += ScheduleInformation(TITLE, context.getString(R.string.nav_drawer_title_schedules), TITLE)
                result += data[SCHEDULES]!!
                result += ScheduleInformation(DIVIDER, "", DIVIDER)
            }
            if (data[EXAMS]?.isNotEmpty() == true) {
                result += ScheduleInformation(TITLE, context.getString(R.string.nav_drawer_title_exams), TITLE)
                result += data[EXAMS]!!
                result += ScheduleInformation(DIVIDER, "", DIVIDER)
            }
        }

        diffCallback.setData(_itemsList, result)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        _itemsList.clear()
        _itemsList += result

        diffResult.dispatchUpdatesTo(this)
    }

    private companion object ItemViewTypes {
        const val EMPTY = -1
        const val TITLE = -2
        const val ITEM = -3
        const val DIVIDER = -4
    }
}

sealed class NavAdapterViewHolders {

    class ItemViewHolder(private val binding: ItemListNavBinding) : BaseViewHolder(binding.root) {

        override fun onBind(data: BaseViewHolderData) {
            if (data !is ScheduleInformation) {
                return
            }

            binding.data = NavItemData(data.name)
            binding.executePendingBindings()

            binding.buttonScheduleName.apply {
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

    class TitleViewHolder(private val binding: ItemListNavTitleBinding) : BaseViewHolder(binding.root) {
        override fun onBind(data: BaseViewHolderData) {
            if (data !is ScheduleInformation) {
                return
            }

            binding.data = NavItemData(data.name)
            binding.executePendingBindings()
        }
    }

    class EmptyViewHolder(binding: ItemListNavEmptyBinding) : BaseViewHolder(binding.root) {
        override fun onBind(data: BaseViewHolderData) {}
    }

    class DividerViewHolder(binding: ItemListNavDividerBinding) : BaseViewHolder(binding.root) {
        override fun onBind(data: BaseViewHolderData) {}
    }
}