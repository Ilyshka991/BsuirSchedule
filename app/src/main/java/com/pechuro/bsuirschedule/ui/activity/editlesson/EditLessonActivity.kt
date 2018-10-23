package com.pechuro.bsuirschedule.ui.activity.editlesson

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivityLessonEditBinding
import com.pechuro.bsuirschedule.ui.base.BaseActivity

class EditLessonActivity :
        BaseActivity<ActivityLessonEditBinding, EditLessonActivityViewModel>() {

    companion object {
        const val INTENT_SCHEDULE_TYPE = "dsf"
        const val INTENT_SCHEDULE_NAME = "dfssdf"
        const val INTENT_LESSON_ID = "DSFSDF"

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
        get() = R.layout.activity_lesson_edit
    override val bindingVariable: Int
        get() = BR.viewModel

}
