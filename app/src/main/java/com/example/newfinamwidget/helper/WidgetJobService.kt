package com.example.newfinamwidget.helper

import android.app.job.JobParameters
import android.app.job.JobService
import com.example.newfinamwidget.FinamApplication
import com.example.newfinamwidget.Retrofit.RetrofitApiService
import com.example.newfinamwidget.loadListPaper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WidgetJobService: JobService() {
    init {
        androidx.work.Configuration.Builder().setJobSchedulerJobIdRange(0, 1000).build()
    }
    override fun onStartJob(p0: JobParameters?): Boolean {
       val paper = loadListPaper(FinamApplication.getAppContext())
        for(name in paper){
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitApiService.stockService().getStock(name)
            }
        }

        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }
}