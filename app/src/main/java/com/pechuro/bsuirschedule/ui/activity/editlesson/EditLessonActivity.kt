package com.pechuro.bsuirschedule.ui.activity.editlesson

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivityContainerBinding
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.edit.EditLessonFragment
import com.pechuro.bsuirschedule.ui.utils.transaction

class EditLessonActivity :
        BaseActivity<ActivityContainerBinding, EditLessonActivityViewModel>() {
    override val viewModel: EditLessonActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(EditLessonActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) homeFragment()
    }

    private fun homeFragment() {
        val fragment = EditLessonFragment.newInstance()
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, fragment)
        }
    }

    companion object {
        private const val INTENT_SCHEDULE_INFO = "schedule_info"
        private const val INTENT_LESSON_ID = "lesson_id"

        fun newIntent(context: Context, info: ScheduleInformation) =
                Intent(context, EditLessonActivity::class.java).apply {
                    putExtra(INTENT_SCHEDULE_INFO, info)
                }

        fun newIntent(context: Context, lessonId: Int) =
                Intent(context, EditLessonActivity::class.java).apply {
                    putExtra(INTENT_LESSON_ID, lessonId)
                }

    }
}
