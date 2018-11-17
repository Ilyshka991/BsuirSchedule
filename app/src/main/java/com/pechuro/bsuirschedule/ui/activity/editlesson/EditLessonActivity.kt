package com.pechuro.bsuirschedule.ui.activity.editlesson

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivityContainerBinding
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.fragment.edit.EditLessonFragment

class EditLessonActivity :
        BaseActivity<ActivityContainerBinding, EditLessonActivityViewModel>() {

    companion object {
        private const val INTENT_SCHEDULE_TYPE = "schedule_type"
        private const val INTENT_SCHEDULE_NAME = "schedule_name"
        private const val INTENT_LESSON_ID = "lesson_id"

        fun newIntent(context: Context, name: String, type: Int): Intent {
            val intent = Intent(context, EditLessonActivity::class.java)
            intent.putExtra(INTENT_SCHEDULE_NAME, name)
            intent.putExtra(INTENT_SCHEDULE_TYPE, type)
            return intent
        }

        fun newIntent(context: Context, lessonId: Int): Intent {
            val intent = Intent(context, EditLessonActivity::class.java)
            intent.putExtra(INTENT_LESSON_ID, lessonId)
            return intent
        }
    }

    override val mViewModel: EditLessonActivityViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(EditLessonActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_container
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, mViewModel))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = EditLessonFragment.newInstance()
        supportFragmentManager.transaction {
            replace(mViewDataBinding.container.id, fragment)
        }
    }

}
