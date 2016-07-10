package nundrum.embellirwidgets;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;

public class EmbellirClockUpdateService extends Service {
    private static final String TAG = "EmbellirClockUpdateServ";

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int color_main = Color.rgb(50, 170, 70);
    private final int color_hilight = Color.rgb(50, 140, 50);

    private final float degPer60 = 360.0f / 60.0f;
    private final float degPer12 = 360.0f / 12.0f;
    private final float degPer24 = 360.0f / 24.0f;


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

        drawClock();

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.embellir_clock);
        view.setTextViewText(R.id.clock_text, lastUpdated);
        view.setImageViewBitmap(R.id.clock_image, bitmap);

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

    private void drawClock(){
        if (null == bitmap) {

            bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        }
        canvas = new Canvas(bitmap);

        canvas.save();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        paint.setStrokeWidth(5);

        float w = canvas.getWidth();
        float h = canvas.getHeight();
        float size = Math.min(w, h);
        float radius = 1.0f * size;
        float halfRadius = 0.5f * radius;

        // get time/date info
        Calendar now = Calendar.getInstance();
        //Calendar nowGMT = Calendar.getInstance(TZ, locale);
        int hour = now.get(Calendar.HOUR);
        int hour24 = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        float riseDeg = 0.0f;
        float setDeg = 0.0f;



        float minuteDeg =  minute * degPer60;
        float hourDeg = hour * degPer12;
        float hour24Deg = hour24 * degPer24 + minute / degPer60 / degPer24;

        float ringmax = (radius * 0.2f); //divide into 10 rings
        float hp = (ringmax * 4f);  //hour
        float mp = (ringmax * 3f);  //minute
        float ssp = (ringmax * 5f); //sunrise/sunset/24h
        float cp = (ringmax * 1f) - (ringmax * 0.5f);  //calendar
        float dp = (ringmax * 1f) + (ringmax * 0.5f);  //day

        canvas.translate(0.5f * w, 0.5f * h );
        canvas.rotate(-90);

        paint.setStyle(Paint.Style.STROKE);

         //24h marker
        //mPaint.setColor(mCSHl);
        paint.setStrokeWidth(ringmax - (ringmax * 0.5f));
        //drawArc(canvas, 0.0f + ssp - radius, 0.0f + ssp - radius, radius - ssp, radius - ssp, hour24Deg - 3f , 6f ,false, paint);
        //hour ring
        //mPaint.setColor(mCPHlm00);
        paint.setColor(color_hilight);
        paint.setStrokeWidth(ringmax / 1.5f);
        drawArc(canvas, 0.0f + hp - radius, 0.0f + hp - radius, radius - hp, radius - hp, 0.0f, hourDeg, false, paint);
        //minute ring
        //mPaint.setColor(mCPMain);
        paint.setColor(color_main);
        paint.setStrokeWidth(ringmax / 2.5f);
        drawArc(canvas, 0.0f + mp - radius, 0.0f + mp - radius, radius - mp, radius - mp, 0.0f, minuteDeg,false, paint);


        canvas.restore();
    }

    private void drawArc(Canvas canvas, float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, Paint paint) {
        //float radius = (right - left) / 2.0f ;
        final RectF oval = new RectF();
        oval.set(left, top, right, bottom);
        Path myPath = new Path();
        myPath.arcTo(oval, startAngle, sweepAngle, true);
        canvas.drawPath(myPath, paint);
    }
}
