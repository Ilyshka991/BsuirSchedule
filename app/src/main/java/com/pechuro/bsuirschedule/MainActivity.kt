package com.pechuro.bsuirschedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

        scheduleRepository.getClasses("850502", 0)
                .subscribeOn(Schedulers.io())
                .subscribe({ println(it.schedule.toString()) }, { println(it.message) })
    }
}

