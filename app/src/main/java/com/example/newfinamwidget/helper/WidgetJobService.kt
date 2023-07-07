package com.example.newfinamwidget.helper

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.icu.util.TimeUnit
import android.util.Log
import com.example.newfinamwidget.FinamApplication
import com.example.newfinamwidget.Retrofit.RetrofitApiService
import com.example.newfinamwidget.Retrofit.data.MarketData
import com.example.newfinamwidget.StockWidget
import com.example.newfinamwidget.loadListPaper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class WidgetJobService: JobService() {
    init {
        androidx.work.Configuration.Builder().setJobSchedulerJobIdRange(0, 1000).build()
    }
    override fun onStartJob(p0: JobParameters?): Boolean {
       val paper = loadListPaper(FinamApplication.getAppContext())
        val map = mutableMapOf<String, Double>()
        for(name in paper){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    //load from internet price paper
                    val marketdata: MarketData = RetrofitApiService.stockService().getStock(name)

                    withContext(Dispatchers.Main){
                        //need save result
                       map.put(name, marketdata.marketdata.data.first().first())
                        Log.d("PUT", "name: ${name}, price: ${ marketdata.marketdata.data.first().first()}")
                    }
                } catch (e: Exception){
                    Log.e("Error Service COROUTINE", e.message.toString())
                }
            }
        }

        //unical number job ID
        val sJobId = 1
        val componentName = ComponentName(applicationContext, WidgetJobService::class.java)
        val jobInfo = JobInfo.Builder(sJobId,componentName)
                //28 api level need
           // .setRequiredNetwork(JobInfo.NETWORK_TYPE_ANY)
            .setOverrideDeadline(java.util.concurrent.TimeUnit.MINUTES.toMillis(1))
            .setPersisted(true)
            .build()

        //создаем новый jobScheduler для того чтобы он обновил данные через минуту
        val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)

        //отправляем broadcast для обновления данных
        val forceWidgetUpdate = Intent(applicationContext, StockWidget::class.java)
        forceWidgetUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        sendBroadcast(forceWidgetUpdate)
        jobFinished(p0, false)

        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }
}