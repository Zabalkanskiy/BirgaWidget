package com.example.newfinamwidget.helper

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.newfinamwidget.FinamApplication
import com.example.newfinamwidget.Retrofit.RetrofitApiService
import com.example.newfinamwidget.Retrofit.data.MarketData
import com.example.newfinamwidget.StockWidget
import com.example.newfinamwidget.loadListPaper
import com.example.newfinamwidget.savePriceMap
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class WidgetJobService: JobService() {
    init {
        androidx.work.Configuration.Builder().setJobSchedulerJobIdRange(0, 1000).build()
    }
    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("ON startjob", "On start jop started")
       val paper = loadListPaper(FinamApplication.getAppContext())
        Log.d("Paper", "paper: $paper")
        val map = mutableMapOf<String, Double>()

        if (paper != null) {
            for(name in paper){
                runBlocking {
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        try {
                            //load from internet price paper
                            val marketdata: MarketData =
                                RetrofitApiService.stockService().getStock(name)

                            withContext(Dispatchers.Main) {
                                //need save result
                                map.put(name, marketdata.marketdata.data.first().first())
                                val gson = Gson()
                                val hashMapString = gson.toJson(map)
                                savePriceMap(FinamApplication.getAppContext(), hashMapString)
                                Log.d(
                                    "PUT",
                                    "name: ${name}, price: ${
                                        marketdata.marketdata.data.first().first()
                                    }"
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("Error Service COROUTINE", e.message.toString())
                        }
                    }
                }

            }
            //convert to string using gson
            //convert to string using gson
            //нужно перенести в withContext

           // val gson = Gson()
           // val hashMapString = gson.toJson(map)
         //   savePriceMap(FinamApplication.getAppContext(), hashMapString)
        }

        //unical number job ID
        //delay(20000)
        val sJobId = 1
        val componentName = ComponentName(applicationContext, WidgetJobService::class.java)
        val jobInfo =    JobInfo.Builder(sJobId,componentName)
                    //28 api level need
              //  .setRequiredNetwork(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(60000) //60 second
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