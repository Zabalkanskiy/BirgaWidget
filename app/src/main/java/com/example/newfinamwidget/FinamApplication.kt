package com.example.newfinamwidget

import android.app.Application
import android.content.Context

class FinamApplication: Application() {
    init {
        app = this
    }

    companion object {
        private lateinit var app : FinamApplication
        fun getAppContext(): Context = app.applicationContext
    }
}