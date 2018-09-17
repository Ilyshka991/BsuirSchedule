package com.pechuro.bsuirschedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pechuro.bsuirschedule.data.ScheduleRepository
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var scheduleRepository: ScheduleRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).appComponent.inject(this)

        scheduleRepository.getClasses("850502", 0)
                .subscribeOn(Schedulers.io())
                .subscribe({ println(it.schedule.toString()) }, { println(it.message) })
    }
}

