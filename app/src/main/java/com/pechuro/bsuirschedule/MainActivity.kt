package com.pechuro.bsuirschedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test()
    }

    private fun test() {
        App.injectScheduleRepository()
                .getClasses("750502", 0)
                .subscribeOn(Schedulers.io())
                .subscribe({ println(it.schedule) }, { println(it.printStackTrace()) })
    }
}

