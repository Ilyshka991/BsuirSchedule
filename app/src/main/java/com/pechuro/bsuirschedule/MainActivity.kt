package com.pechuro.bsuirschedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pechuro.bsuirschedule.repository.EmployeeRepository
import com.pechuro.bsuirschedule.repository.entity.complex.StudentClasses
import com.pechuro.bsuirschedule.repository.entity.item.StudentClassItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.injectRepository().loadClasses("750502").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { println(it.classes) }
    }
}
