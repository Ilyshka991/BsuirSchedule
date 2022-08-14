package com.pechuro.bsuirschedule.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import androidx.core.view.children
import com.google.android.material.R
import com.google.android.material.chip.ChipGroup

class MultipleSelectionChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.chipGroupStyle
) : ChipGroup(context, attrs, defStyleAttr) {

    private val checkedChangeInternalListener =
        CompoundButton.OnCheckedChangeListener { view, isChecked ->
            if (!isChecked && !isAnyChipChecked()) {
                view.isChecked = true
            }
        }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        val chip = child as? CompoundButton ?: return
        chip.setOnCheckedChangeListener(checkedChangeInternalListener)
    }

    private fun isAnyChipChecked() = children.map { view ->
        val chip = view as? CompoundButton ?: return@map false
        chip.isChecked
    }.fold(false) { acc, isChecked ->
        acc || isChecked
    }
}