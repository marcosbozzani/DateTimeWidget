package com.example.datetimewidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class UpdateWidgetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.updateWidgets()
    }
}
