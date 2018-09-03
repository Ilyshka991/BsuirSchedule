package com.pechuro.bsuirschedule

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.pechuro.bsuirschedule.view.ListFragment
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ListFragment()).commit()
        }
    }
}
