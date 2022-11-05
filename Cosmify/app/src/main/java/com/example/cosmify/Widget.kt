package com.example.cosmify

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.request.transition.Transition


/**
 * Implementation of App Widget functionality.
 */
class Widget : AppWidgetProvider() {
    fun pushWidgetUpdate(context: Context, remoteViews: RemoteViews?) {
        val myWidget = ComponentName(context, AppWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(context)
        manager.updateAppWidget(myWidget, remoteViews)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {


//        pushWidgetUpdate(context, remoteViews)
        val remoteViews = RemoteViews(context.packageName, R.layout.widget)

        val appWidgetTarget =
            object : AppWidgetTarget(context, R.id.appwidget_img, remoteViews, *appWidgetIds) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                    super.onResourceReady(resource!!, transition)
                }
            }

        Glide
            .with(context.applicationContext)
            .asBitmap()
            .load("https://apod.nasa.gov/apod/image/2211/Lunar-Eclipse-South-Pole.jpg")
            .override(480,320)
            .into(appWidgetTarget)

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}