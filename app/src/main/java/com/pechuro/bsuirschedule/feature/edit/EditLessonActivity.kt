package com.pechuro.bsuirschedule.feature.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseActivity
import com.pechuro.bsuirschedule.ext.transaction
import com.pechuro.bsuirschedule.feature.edit.editlesson.EditLessonFragment
import kotlinx.android.synthetic.main.activity_container.*

class EditLessonActivity : BaseActivity() {

    companion object {
        private const val EXTRA_LESSON_ID = "EXTRA_LESSON_ID"

        fun newIntent(context: Context, lessonId: Int) =
                Intent(context, EditLessonActivity::class.java).apply {
                    putExtra(EXTRA_LESSON_ID, lessonId)
                }
    }

    override val layoutId: Int
        get() = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) homeFragment()
    }

    private fun homeFragment() {
        val fragment = EditLessonFragment.newInstance()
        supportFragmentManager.transaction {
            replace(fragmentContainer.id, fragment)
        }
    }
}
