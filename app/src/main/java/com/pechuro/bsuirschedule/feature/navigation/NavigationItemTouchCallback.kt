package com.pechuro.bsuirschedule.feature.navigation

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewHolder
import com.pechuro.bsuirschedule.ext.colorFromAttr
import com.pechuro.bsuirschedule.ext.dimenPx
import com.pechuro.bsuirschedule.ext.drawable
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class NavigationItemTouchCallback(
    private val onDelete: (position: Int) -> Unit,
    private val onUpdate: (position: Int) -> Unit,
    private val onActionReadyToPerform: () -> Unit
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private var isActionReadyToPerformHandled = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.LEFT -> onDelete(position)
            ItemTouchHelper.RIGHT -> onUpdate(position)
        }
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val baseViewHolder = viewHolder as? BaseViewHolder<*> ?: return 0
        return if (baseViewHolder.isSwipeAllowed) super.getMovementFlags(
            recyclerView,
            viewHolder
        ) else 0
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val context = recyclerView.context
        val itemView = viewHolder.itemView

        val backgroundColorDrawable: Drawable = ColorDrawable(
            context.colorFromAttr(R.attr.backgroundNavigationSwipeActionColor)
        )
        val iconDrawable: Drawable?
        val drawingDx: Float
        when {
            dX >= 0 -> {
                iconDrawable = context.drawable(R.drawable.ic_update)
                drawingDx = dX / 4
            }
            dX <= 0 -> {
                iconDrawable = context.drawable(R.drawable.ic_delete_sweep)
                drawingDx = dX
            }
            else -> throw IllegalArgumentException()
        }

        val iconHeight = iconDrawable?.intrinsicHeight ?: 0
        val iconWidth = iconDrawable?.intrinsicWidth ?: 0
        val iconMargin = context.dimenPx(R.dimen.navigation_sheet_item_action_icon_margin)

        val iconTop = itemView.top + ((itemView.height - iconHeight) / 2)
        val iconBottom = iconTop + iconHeight
        val iconLeft: Int
        val iconRight: Int

        val backgroundWidth = dX.roundToInt() * 2
        val backgroundTop = itemView.top
        val backgroundBottom = itemView.bottom
        val backgroundLeft: Int
        val backgroundRight: Int

        when {
            dX > 0 -> {
                iconLeft = itemView.left + iconMargin
                iconRight = iconLeft + iconWidth
                backgroundLeft = itemView.left
                backgroundRight = itemView.left + backgroundWidth
            }
            dX < 0 -> {
                iconRight = itemView.right - iconMargin
                iconLeft = iconRight - iconWidth
                backgroundLeft = itemView.right + backgroundWidth
                backgroundRight = itemView.right
            }
            else -> {
                iconLeft = 0
                iconRight = 0
                backgroundLeft = 0
                backgroundRight = 0
            }
        }

        iconDrawable?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        backgroundColorDrawable.setBounds(
            backgroundLeft,
            backgroundTop,
            backgroundRight,
            backgroundBottom
        )

        backgroundColorDrawable.draw(canvas)
        iconDrawable?.draw(canvas)

        if (!isActionReadyToPerformHandled && backgroundWidth.absoluteValue >= itemView.width) {
            isActionReadyToPerformHandled = true
            onActionReadyToPerform()
        }
        if (isActionReadyToPerformHandled && backgroundWidth.absoluteValue < itemView.width) {
            isActionReadyToPerformHandled = false
        }

        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            drawingDx,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}