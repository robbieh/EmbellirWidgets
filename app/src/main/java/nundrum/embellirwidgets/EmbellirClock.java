package nundrum.embellirwidgets;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class EmbellirClock extends AppWidgetProvider {

    private static final String TAG = "EmbellirClockAWP";
    private PendingIntent service = null;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.v(TAG, "updateAppWidget called");
        // Construct the RemoteViews object
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.embellir_clock);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        //appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "onUpdate called");

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent clockIntent = new Intent(context,  EmbellirClockUpdateService.class);
        final Calendar now = Calendar.getInstance();
        for (int appWidgetId : appWidgetIds) {
            getWidgetSizes(context, appWidgetManager, appWidgetId);
        }

        if (service  == null) {
            service = PendingIntent.getService(context, 0, clockIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC, now.getTime().getTime(), 1000*60, service);

        // There may be multiple widgets active, so update all of them

        //String hour = String.valueOf(now.get(Calendar.HOUR));
        //String minute = String.valueOf(now.get(Calendar.MINUTE));
        //String second = String.valueOf(now.get(Calendar.SECOND));
        //String widgetText = hour.concat(":").concat(minute).concat(" ").concat(second);

        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.embellir_clock);
        //views.setTextViewText(R.id.appwidget_text, widgetText);


        /*for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
            updateAppWidget(context, appWidgetManager, appWidgetId);


        }*/
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        getWidgetSizes(context, appWidgetManager, appWidgetId);
    }

    private void getWidgetSizes(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        int mWidgetLandWidth;
        int mWidgetPortHeight;
        int mWidgetPortWidth;
        int mWidgetLandHeight;
        boolean mIsKeyguard;
        //context.getResources().getBoolean(R.bool.isPort);


           AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(
                   context).getAppWidgetInfo(appWidgetId);
           mWidgetLandWidth = providerInfo.minWidth;
           mWidgetPortHeight = providerInfo.minHeight;
           mWidgetPortWidth = providerInfo.minWidth;
           mWidgetLandHeight = providerInfo.minHeight;
           Bundle mAppWidgetOptions = null;
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
               mAppWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
            if (mAppWidgetOptions != null
                    && mAppWidgetOptions
                    .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) > 0) {
                    Log.d(TAG,"appWidgetOptions not null, getting widget sizes...");
                // Reduce width by a margin of 8dp (automatically added by
                // Android, can vary with third party launchers)

           /* Actually Min and Max is a bit irritating,
              because it depends on the homescreen orientation
              whether Min or Max should be used: */

                mWidgetPortWidth = mAppWidgetOptions
                        .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
                mWidgetLandWidth = mAppWidgetOptions
                        .getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
                mWidgetLandHeight = mAppWidgetOptions
                        .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
                mWidgetPortHeight = mAppWidgetOptions
                        .getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

                // Get the value of OPTION_APPWIDGET_HOST_CATEGORY
                int category = mAppWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY, -1);

                // If the value is WIDGET_CATEGORY_KEYGUARD, it's a lockscreen
                // widget (dumped with Android-L preview :-( ).
                mIsKeyguard = category == AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD;

            } else {
               Log.d(TAG,"No AppWidgetOptions for this widget, using minimal dimensions from provider info!");
                // For some reason I had to set this again here, may be obsolete
                mWidgetLandWidth = providerInfo.minWidth;
                mWidgetPortHeight = providerInfo.minHeight;
                mWidgetPortWidth = providerInfo.minWidth;
                mWidgetLandHeight = providerInfo.minHeight;
            }
            Log.v(TAG,String.valueOf(mWidgetPortWidth));
            Log.v(TAG,String.valueOf(mWidgetLandWidth));
            Log.v(TAG,String.valueOf(mWidgetPortHeight));
            Log.v(TAG,String.valueOf(mWidgetLandHeight));


    }

    @Override
    public void onEnabled(Context context) {
        Log.v(TAG, "onEnabled called");
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        Log.v(TAG, "onDisabled called");
        // Enter relevant functionality for when the last widget is disabled
    }
}

