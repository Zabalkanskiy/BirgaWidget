package com.example.newfinamwidget.helper;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RmViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewStockFactory(getApplicationContext(),intent);
    }
}
