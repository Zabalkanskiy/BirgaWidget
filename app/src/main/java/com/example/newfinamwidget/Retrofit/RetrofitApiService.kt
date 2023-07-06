package com.example.newfinamwidget.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val STOCK_URL = "https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/"
object RetrofitApiService {
    private val stockRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(STOCK_URL)
        .build()

    fun stockService() : StockApi {
        return stockRetrofit.create(StockApi::class.java)
    }
}