package com.pechuro.bsuirschedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test("sd")
    }

    fun test(group: String) = App.injectRepository()
            .getNotAddedGroups(0).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe({
                println("SDFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFf")
                println(it.indexOf("750502"))
            }, {})

}

