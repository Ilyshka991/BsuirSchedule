package com.pechuro.bsuirschedule.feature.edit

import android.content.Context
import android.content.Intent
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragmentActivity
import com.pechuro.bsuirschedule.feature.edit.editlesson.EditLessonFragment
import kotlinx.android.synthetic.main.activity_container.*

class EditLessonActivity : BaseFragmentActivity() {

    companion object {
        private const val EXTRA_LESSON_ID = "EXTRA_LESSON_ID"

        fun newIntent(context: Context, lessonId: Int) =
                Intent(context, EditLessonActivity::class.java).apply {
                    putExtra(EXTRA_LESSON_ID, lessonId)
                }
    }

    override val layoutId: Int = R.layout.activity_container

    override val containerId: Int by lazy(LazyThreadSafetyMode.NONE) {
        fragmentContainer.id
    }

    override fun getHomeFragment() = EditLessonFragment.newInstance()
}
