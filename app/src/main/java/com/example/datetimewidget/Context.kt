package com.example.datetimewidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.*

const val UPDATE_WIDGET_INTENT_ID = 9997

fun Context.updateWidgets() {
    val widgetsCnt = AppWidgetManager.getInstance(applicationContext)
        .getAppWidgetIds(ComponentName(applicationContext, AppWidget::class.java))
    if (widgetsCnt.isNotEmpty()) {
        val ids = intArrayOf(R.xml.app_widget_info)
        Intent(applicationContext, AppWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            sendBroadcast(this)
        }
    }
    scheduleNextWidgetUpdate()
}

fun Context.scheduleNextWidgetUpdate() {
    val widgetsCnt = AppWidgetManager.getInstance(applicationContext)
        .getAppWidgetIds(ComponentName(applicationContext, AppWidget::class.java))
    if (widgetsCnt.isEmpty()) {
        return
    }

    val intent = Intent(this, DateTimeWidgetUpdateReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        this,
        UPDATE_WIDGET_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerAtMillis = System.currentTimeMillis() + getMSTillNextMinute()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        alarmManager.setExact(AlarmManager.RTC, triggerAtMillis, pendingIntent)
    } else {
        alarmManager.set(AlarmManager.RTC, triggerAtMillis, pendingIntent)
    }
}

fun getMSTillNextMinute(): Long {
    val calendar = Calendar.getInstance()
    return 60000L - calendar.get(Calendar.MILLISECOND) - calendar.get(Calendar.SECOND) * 1000
}
