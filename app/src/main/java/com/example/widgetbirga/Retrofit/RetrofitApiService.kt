package com.example.widgetbirga.Retrofit

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.util.Log
import com.example.widgetbirga.WidgetApplication
import com.example.widgetbirga.Retrofit.data.MarketData
import com.example.widgetbirga.StockWidget
import com.example.widgetbirga.savePriceMap
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

private const val STOCK_URL = "https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/"
object RetrofitApiService {
    private val stockRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(STOCK_URL)
        .build()

    fun stockService() : StockApi {
        return stockRetrofit.create(StockApi::class.java)
    }

    fun loadinginternetData( papers: Set<String>? ) {
        if (papers != null) {
            val map = mutableMapOf<String, Double>()
            CoroutineScope(Dispatchers.Main).launch {
                val job = CoroutineScope(Dispatchers.IO).launch {

                    for (paper in papers) {
                        try {
                            //load from internet price paper
                            val marketdata: MarketData =
                                RetrofitApiService.stockService().getStock(paper)
                            map.put(paper, marketdata.marketdata.data.first().first())
                            val gson = Gson()
                            val hashMapString = gson.toJson(map)
                            savePriceMap(WidgetApplication.getAppContext(), hashMapString)
                            Log.d(
                                "PUT",
                                "name: ${paper}, price: ${
                                    marketdata.marketdata.data.first().first()
                                }"
                            )
                        } catch (e: Exception) {

                            Log.e("Error Service COROUTINE", e.message.toString())
                        }
                    }

                }
                job.join()
                val forceWidgetUpdate =
                    Intent(WidgetApplication.getAppContext(), StockWidget::class.java)
                forceWidgetUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                WidgetApplication.getAppContext().sendBroadcast(forceWidgetUpdate)
            }

        }
    }

}