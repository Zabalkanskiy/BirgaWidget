package com.example.widgetbirga

import android.app.Application
import android.content.Context

class WidgetApplication: Application() {
    init {
        app = this
    }

    companion object {
        private lateinit var app : WidgetApplication
        fun getAppContext(): Context = app.applicationContext
    }
}