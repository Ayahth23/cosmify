package com.example.cosmify

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.request.transition.Transition
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random.Default.nextInt

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

        val thread = Thread {
            try {
                val picDay = JSONObject(URL("https://cosmify-367717.uk.r.appspot.com/space-picture").readText())
                val imgURL = picDay.getString("imageURL")
                Glide
                    .with(context.applicationContext)
                    .asBitmap()
                    .load(imgURL)
                    .override(480,320)
                    .into(appWidgetTarget)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        thread.start()

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
    val thread = Thread {
        try {
            val rocketLaunch =
                JSONObject(URL("https://cosmify-367717.uk.r.appspot.com/next-launch").readText())
            val nextLaunch = rocketLaunch.getString("win_open")
            val name = rocketLaunch.getJSONObject("provider").getString("name")
            val state = rocketLaunch.getJSONObject("pad").getJSONObject("location").getString("statename")

            val zonedDateTime = ZonedDateTime.parse(nextLaunch)
            val newTime = zonedDateTime.withZoneSameInstant(ZoneId.of(TimeZone.getDefault().id))
            var ret = "Next Rocket Launch:\n"
            ret += "\tName: $name\n"
            ret += "\tDate/Time: " + DateTimeFormatter.ofPattern("MM/dd/yyyy - hh:mm").format(newTime) + "\n"
            ret += "\tState: $state"

            val fact = JSONObject(URL("https://cosmify-367717.uk.r.appspot.com/fact-of-day").readText())

            val views = RemoteViews(context.packageName, R.layout.widget)

            val ran = (0..100).random()
            if (ran < 50) {
                ret = fact.getString("fact")
                views.setFloat(R.id.appwidget_text, "setTextSize", 15F)
            }
            else {
                views.setFloat(R.id.appwidget_text, "setTextSize", 20F)
            }

            // Construct the RemoteViews object
            views.setTextViewText(R.id.appwidget_text, ret)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (exc: Exception) {
            Log.e("Exception", exc.toString())
        }
    }

    thread.start()
}