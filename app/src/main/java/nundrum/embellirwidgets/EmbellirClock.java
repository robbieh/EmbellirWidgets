package nundrum.embellirwidgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

            //Bitmap bitmap = Bitmap.createBitmap()
            //Canvas canvas = new Canvas();
        }*/
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

