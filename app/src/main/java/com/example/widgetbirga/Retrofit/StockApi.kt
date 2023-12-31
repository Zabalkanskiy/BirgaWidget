package com.example.widgetbirga.Retrofit

import com.example.widgetbirga.Retrofit.data.MarketData
import retrofit2.http.GET
import retrofit2.http.Path

interface StockApi {
    @GET("{stock}.json?iss.meta=off&iss.only=marketdata&marketdata.columns=LAST")
    suspend fun getStock(@Path("stock") name : String) : MarketData
}