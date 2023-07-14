package com.example.newfinamwidget.helper

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.newfinamwidget.FinamApplication
import com.example.newfinamwidget.R
import com.example.newfinamwidget.R.layout.item
import com.example.newfinamwidget.loadPriceMap

class RemoteViewStockFactory(val applicationContext: Context?, val intent: Intent?) : RemoteViewsService.RemoteViewsFactory {
  var priceMap : HashMap<String, Double>? = null
    override fun onCreate() {
        priceMap = loadPriceMap(FinamApplication.getAppContext())
    }

    override fun onDataSetChanged() {
     priceMap =  loadPriceMap(FinamApplication.getAppContext())
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        val count = priceMap?.size ?: 0
        return count
    }

    override fun getViewAt(p0: Int): RemoteViews {
        //val priceMap = loadPriceMap(FinamApplication.getAppContext())
        val rView = RemoteViews(FinamApplication.getAppContext().packageName, item)
       val key  =priceMap?.keys?.elementAt(p0)
        rView.setTextViewText(R.id.list_name_stock, key)
        rView.setTextViewText(R.id.list_price_stock, priceMap?.get(key).toString())
        return rView
    }

    override fun getLoadingView(): RemoteViews {
        val rView = RemoteViews(FinamApplication.getAppContext().packageName, item)
        rView.setTextViewText(R.id.list_name_stock, "Not Loaded")
        rView.setTextViewText(R.id.list_price_stock, "0.00")
        return rView
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

}
