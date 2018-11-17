package com.pechuro.bsuirschedule.ui.activity.editlesson

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivityContainerBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.fragment.edit.EditLessonFragment

class EditLessonActivity :
        BaseActivity<ActivityContainerBinding, EditLessonActivityViewModel>() {

    companion object {
        private const val INTENT_SCHEDULE_INFO = "schedule_info"
        private const val INTENT_LESSON_ID = "lesson_id"

        fun newIntent(context: Context, info: ScheduleInformation): Intent {
            val intent = Intent(context, EditLessonActivity::class.java)
            intent.putExtra(INTENT_SCHEDULE_INFO, info)
            return intent
        }

        fun newIntent(context: Context, lessonId: Int): Intent {
            val intent = Intent(context, EditLessonActivity::class.java)
            intent.putExtra(INTENT_LESSON_ID, lessonId)
            return intent
        }
    }

    override val viewModel: EditLessonActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(EditLessonActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_container
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, viewModel))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = EditLessonFragment.newInstance()
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, fragment)
        }
    }

}
