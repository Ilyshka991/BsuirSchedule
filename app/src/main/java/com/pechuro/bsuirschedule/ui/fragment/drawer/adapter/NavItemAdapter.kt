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
import com.pechuro.bsuirschedule.databinding.ItemListNavDividerBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavEmptyBinding
import com.pechuro.bsuirschedule.databinding.ItemListNavTitleBinding
import com.pechuro.bsuirschedule.ui.base.BaseViewHolder
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.drawer.DrawerEvent
import com.pechuro.bsuirschedule.ui.utils.EventBus
import org.jetbrains.anko.doAsyncResult

enum class ViewType(val type: Int) {
    TITLE(-1),
    ITEM(-2),
    EMPTY(-3),
    DIVIDER(-4)
}

class NavItemAdapter(private val context: Context,
                     private val diffCallback: NavItemsDiffCallback) : RecyclerView.Adapter<BaseViewHolder>() {

    private val _itemsList = mutableListOf<ScheduleInformation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewType.ITEM.type -> {
            val viewBinding = ItemListNavBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(viewBinding)
        }

        ViewType.TITLE.type -> {
            val viewBinding = ItemListNavTitleBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            TitleViewHolder(viewBinding)
        }

        ViewType.EMPTY.type -> {
            val viewBinding = ItemListNavEmptyBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyViewHolder(viewBinding)
        }

        ViewType.DIVIDER.type -> {
            val viewBinding = ItemListNavDividerBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            DividerViewHolder(viewBinding)
        }

        else -> throw IllegalStateException()
    }

    override fun getItemViewType(position: Int) = when {
        _itemsList[position].type == ViewType.EMPTY.type -> ViewType.EMPTY.type
        _itemsList[position].type == ViewType.DIVIDER.type -> ViewType.DIVIDER.type
        _itemsList[position].type == ViewType.TITLE.type -> ViewType.TITLE.type
        else -> ViewType.ITEM.type
    }

    override fun getItemCount() = _itemsList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    fun setItems(data: Map<Int, List<ScheduleInformation>>) {
        val result = mutableListOf<ScheduleInformation>()

        val diffResult = doAsyncResult {
            if (data[SCHEDULES].isNullOrEmpty() && data[EXAMS].isNullOrEmpty()) {
                result += ScheduleInformation(-1, "", ViewType.EMPTY.type)
            } else {
                if (data[SCHEDULES]?.isNotEmpty() == true) {
                    result += ScheduleInformation(-1, context.getString(R.string.nav_drawer_title_schedules), ViewType.TITLE.type)
                    result += data[SCHEDULES]!!
                    result += ScheduleInformation(-2, "", ViewType.DIVIDER.type)
                }
                if (data[EXAMS]?.isNotEmpty() == true) {
                    result += ScheduleInformation(-1, context.getString(R.string.nav_drawer_title_exams), ViewType.TITLE.type)
                    result += data[EXAMS]!!
                    result += ScheduleInformation(-2, "", ViewType.DIVIDER.type)
                }
            }
            diffCallback.setData(_itemsList, result)
            DiffUtil.calculateDiff(diffCallback)
        }.get()

        _itemsList.clear()
        _itemsList += result


        diffResult.dispatchUpdatesTo(this)
    }

    inner class ItemViewHolder(private val binding: ItemListNavBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val data = _itemsList[position]
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

    inner class TitleViewHolder(private val binding: ItemListNavTitleBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val data = _itemsList[position]
            binding.data = NavItemData(data.name)
            binding.executePendingBindings()
        }
    }

    inner class EmptyViewHolder(binding: ItemListNavEmptyBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {}
    }

    inner class DividerViewHolder(binding: ItemListNavDividerBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {}
    }
}