package com.pechuro.bsuirschedule.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleType
import com.pechuro.bsuirschedule.data.ScheduleRepository
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var scheduleRepository: ScheduleRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.test)
        scheduleRepository.getClasses("750502", ScheduleType.STUDENT_CLASSES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ text.text = it.schedule.toString() }, { println(it.message) })
    }
}

