package nundrum.embellirwidgets;

import android.app.AlarmManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Date;

public class EmbellirClockUpdateService extends Service {
    private static final String TAG = "EmbellirClockUpdateServ";

    //public EmbellirClockUpdateService() {
    //}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStart called");

        /*AppWidgetManager appWidgetManager =
            AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.embellir_clock);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }*/
        String lastUpdated = DateFormat.format("hh:mm a", new Date()).toString();

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.embellir_clock);
        view.setTextViewText(R.id.appwidget_text, lastUpdated);

        ComponentName thisWidget = new ComponentName(this, EmbellirClock.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, view);

        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
