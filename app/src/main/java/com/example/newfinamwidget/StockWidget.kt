package com.example.newfinamwidget

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.newfinamwidget.helper.RemoteViewService
import com.example.newfinamwidget.helper.RemoteViewStockFactory
import com.example.newfinamwidget.helper.RmViewService
import com.example.newfinamwidget.helper.WidgetJobService
import java.util.concurrent.TimeUnit

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [StockWidgetConfigureActivity]
 */
class StockWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE){
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val newComponentName = ComponentName(context as Context, StockWidget::class.java)
            val appWidgetId = appWidgetManager.getAppWidgetIds(newComponentName)
            onUpdate(context, appWidgetManager, appWidgetId)
        }
        super.onReceive(context, intent)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val views = RemoteViews(context.packageName, R.layout.stock_widget)
    var adapter = Intent(context,RmViewService::class.java)
    adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    views.setRemoteAdapter(R.id.position_list, adapter)
   // views.setTextViewText(R.id.position_list, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.position_list)
}