package com.trials.sample001

import android.app.Application
import io.realm.Realm

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}