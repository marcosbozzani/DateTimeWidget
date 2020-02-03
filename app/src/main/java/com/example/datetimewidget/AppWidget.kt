package com.example.datetimewidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
//        for (appWidgetId in appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId)
//        }
        performUpdate(context)
        context.scheduleNextWidgetUpdate()
    }

    override fun onEnabled(context: Context) {
        context.scheduleNextWidgetUpdate()
    }

    override fun onDisabled(context: Context) {

    }

    private fun performUpdate(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.getAppWidgetIds(getComponentName(context)).forEach {
            RemoteViews(context.packageName, getProperLayout(context)).apply {
                updateTexts(context, this)
                appWidgetManager.updateAppWidget(it, this)
            }
        }
    }

    private fun updateTexts(context: Context, views: RemoteViews) {
        val dateTimeText = SimpleDateFormat("dd/MM/yyyy").format(Date())
        val dayOfWeekText = SimpleDateFormat("EEEE").format(Date())
        views.apply {
            setTextViewText(R.id.appwidget_text, dateTimeText)
            setTextViewText(R.id.appwidget_text2, dayOfWeekText)
        }
    }

    private fun getComponentName(context: Context) = ComponentName(context, this::class.java)

    private fun getProperLayout(context: Context) = R.layout.app_widget
}
