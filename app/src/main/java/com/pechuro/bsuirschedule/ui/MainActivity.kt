package com.pechuro.bsuirschedule.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleType
import com.pechuro.bsuirschedule.data.ScheduleRepository
import dagger.android.AndroidInjection
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var scheduleRepository: ScheduleRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scheduleRepository.getClasses("750502", ScheduleType.STUDENT_CLASSES)
                .subscribeOn(Schedulers.io())
                .subscribe({ }, { println(it.message) })
    }
}

