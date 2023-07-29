package com.example.widgetbirga.helper

import android.content.Intent
import android.widget.RemoteViewsService

class RemoteViewService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewStockFactory {
        return RemoteViewStockFactory(applicationContext, intent )
    }


}