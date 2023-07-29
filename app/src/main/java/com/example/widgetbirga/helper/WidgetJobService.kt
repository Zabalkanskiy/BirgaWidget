package com.example.widgetbirga.helper

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.example.widgetbirga.WidgetApplication
import com.example.widgetbirga.Retrofit.RetrofitApiService
import com.example.widgetbirga.loadListPaper
import com.example.widgetbirga.loadSecondUpdater


class WidgetJobService: JobService() {
    init {
        androidx.work.Configuration.Builder().setJobSchedulerJobIdRange(0, 1000).build()
    }
    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("ON startjob", "On start jop started")
       val paper = loadListPaper(WidgetApplication.getAppContext())
        Log.d("Paper", "paper: $paper")
        val map = mutableMapOf<String, Double>()
        //load seconds for update widget
        val seconds = loadSecondUpdater(WidgetApplication.getAppContext()) * 1000 // value in millisecons

        //Internet call
        RetrofitApiService.loadinginternetData(paper)



        //unical number job ID
        //delay(20000)
        val sJobId = 1
        val componentName = ComponentName(applicationContext, WidgetJobService::class.java)
        val jobInfo =    JobInfo.Builder(sJobId,componentName)
                    //28 api level need
              //  .setRequiredNetwork(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(seconds)
                .setPersisted(true)
                .build()

        //создаем новый jobScheduler для того чтобы он обновил данные через минуту
        val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)

        //отправляем broadcast для обновления данных


       //     val forceWidgetUpdate = Intent(applicationContext, StockWidget::class.java)
       //     forceWidgetUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
       //     sendBroadcast(forceWidgetUpdate)
       //     jobFinished(p0, false)

        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }
}