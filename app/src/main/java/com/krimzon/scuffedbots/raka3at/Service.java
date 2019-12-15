package com.krimzon.scuffedbots.raka3at;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;

import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.krimzon.scuffedbots.raka3at.utilities.Notification;

public class Service extends android.app.Service {
    protected static final int NOTIFICATION_ID = 1337;
    private static Service mCurrentService;
    private int counter = 0;
    private CalculationParameters params;
    private int NOT_USED = 1338;
    private int mId = 5565;
    private int i = 0;
    private List<String> lol;
    private String temptime;
    private int rightnowcomparable;
    private Date old_date, new_date;
    private Intent emptyIntent;
    private boolean recent_adan = false;
    private Coordinates coordinates;
    private DateComponents date;
    private List<Integer> prayers;
    private double longitude, latitude;
    private String fajr;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;
    private PrayerTimes prayerTimes;
    private static Timer timer;
    private static TimerTask timerTask;
    /*long oldTime = 0;
    private static String TAG = "Service";*/

    public Service() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCurrentService = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        } else {
            launch_prayer_processing();
        }
    }

    private void launch_prayer_processing() {
        params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        params = CalculationMethod.EGYPTIAN.getParameters();
        params.madhab = Madhab.SHAFI; // SHAFI made 95% accuracy, HANAFI had 1hour different for l'3asr
        params.adjustments.fajr = 2; //2
        String pattern = "dd-MMM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        lol = new ArrayList<>();
        lol.add("fajr");
        lol.add("dhuhr");
        lol.add("asr");
        lol.add("maghrib");
        lol.add("isha");

        sql("slat");
        if (SQLSharing.mycursor.getCount() <= 0) {
            SQLSharing.mydb.insertMawa9it(String.valueOf(3.1875071999999998), String.valueOf(36.728832));
        }

        old_date = new Date();
        location_shit(old_date);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        counter = 0;

        // it has been killed by Android and now it is restarted. We must make sure to have reinitialised everything
        if (intent == null) {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(this);
        }

        // make sure you call the startForeground on onStartCommand because otherwise
        // when we hide the notification on onScreen it will nto restart in Android 6 and 7
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        startTimer();

        // return start sticky so if it is killed by android, it will be restarted with Intent null
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Notification notification = new Notification();
                startForeground(NOTIFICATION_ID, notification.setNotification(this, "Service notification", "This is the service's notification", R.drawable.ic_launcher_background));
                startTimer();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
    }

    public void startTimer() {

        //set a new Timer - if one is already running, cancel it to avoid two running at the same time
        stoptimertask();
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        launch_prayer_processing();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                try {
                    display_notification();

                    /*// Check if we are still in the same day, if not then calculate new day's prayertimes
                    new_date = new Date();
                    if(!String.valueOf(old_date).split(" ")[2].equals(String.valueOf(new_date).split(" ")[2]))
                        location_shit(new_date);
                    old_date = new_date;
                    temptime = String.valueOf(old_date).split(" ")[3];
                    rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 3600 + Integer.valueOf(temptime.split(":")[1]) * 60 + Integer.valueOf(temptime.split(":")[2]);

                    // Check if we reached the adan, if so, then switch i to the next adan
                    if(prayers.get(i) <= rightnowcomparable && rightnowcomparable <= prayers.get(i) + 60 && !recent_adan){
                        recent_adan = true;

                        // Play adan audio
                        display_notification();
                        initExoPlayer();

                        // set i to the next adan
                        i++; if(i>=5) i = 0;
                    } else recent_adan = false;*/


                } catch(Exception ignored){}
            }
        };
    }

    private void display_notification() {
        emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), NOT_USED, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("New adan: " + lol.get(i))
                        .setContentText("")
                        .setContentIntent(pendingIntent); //Required on Gingerbread and below

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(mId, mBuilder.build());
    }

    private void location_shit(Date date) {
        sql("force");
        if(SQLSharing.mycursor.getCount()>0)
            if_theres_previous_info_load_it_n_display(date);
    }

    private void if_theres_previous_info_load_it_n_display(Date date) {
        SQLSharing.mycursor.moveToFirst();
        longitude = Double.valueOf(SQLSharing.mycursor.getString(1));
        latitude = Double.valueOf(SQLSharing.mycursor.getString(2));
        use(longitude, latitude, date);
    }

    private void pull_date_and_shape_it(double longitude, double latitude, Date today) {
        coordinates = new Coordinates(latitude, longitude);
        date = DateComponents.from(today);
    }

    private void use(double longitude, double latitude, Date today) {
        prayers = new ArrayList<>();

        /*String[] temptoday = today.toString().split(" ");
        String todaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5].charAt(2) + temptoday[5].charAt(3);
        */old_date = new Date();

        pull_date_and_shape_it(longitude, latitude, today);
        pull_prayer_times_and_shape_them();
        convert_prayertimes_into_milliseconds();
    }


    private void initExoPlayer() {
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(
                this,
                null,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        );
        TrackSelector trackSelector = new DefaultTrackSelector();
        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                renderersFactory,
                trackSelector
        );
        String userAgent = Util.getUserAgent(this, "Play Audio");
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse("asset:///" + "bloop.mp3"), // file audio ada di folder assets
                new DefaultDataSourceFactory(this, userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }

    private void convert_prayertimes_into_milliseconds() {
        int fajrtemp = Integer.valueOf(fajr.split(" ")[0].split(":")[0]) * 3600 + Integer.valueOf(fajr.split(" ")[0].split(":")[1]) * 60;
        if(fajr.split(" ")[1].equals("PM"))
            fajrtemp += 43200; //12*3600
        //Integer risetemp = Integer.valueOf(rise.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(rise.split(" ")[0].split(":")[1])*60;
        int dhuhrtemp = Integer.valueOf(dhuhr.split(" ")[0].split(":")[0]) * 3600 + Integer.valueOf(dhuhr.split(" ")[0].split(":")[1]) * 60;
        if(dhuhr.split(" ")[1].equals("PM") && !dhuhr.split(":")[0].equals("12"))
            dhuhrtemp += 43200; //12*3600
        int asrtemp = Integer.valueOf(asr.split(" ")[0].split(":")[0]) * 3600 + Integer.valueOf(asr.split(" ")[0].split(":")[1]) * 60;
        if(asr.split(" ")[1].equals("PM"))
            asrtemp += 43200; //12*3600
        int maghribtemp = Integer.valueOf(maghrib.split(" ")[0].split(":")[0]) * 3600 + Integer.valueOf(maghrib.split(" ")[0].split(":")[1]) * 60;
        if(maghrib.split(" ")[1].equals("PM"))
            maghribtemp += 43200; //12*3600
        int ishatemp = Integer.valueOf(isha.split(" ")[0].split(":")[0]) * 3600 + Integer.valueOf(isha.split(" ")[0].split(":")[1]) * 60;
        if(isha.split(" ")[1].equals("PM"))
            ishatemp += 43200; //12*3600



        /*// TODO:  for testing purposes
        temptime = String.valueOf(old_date).split(" ")[3];
        rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 3600 + Integer.valueOf(temptime.split(":")[1]) * 60 + Integer.valueOf(temptime.split(":")[2]);
        fajrtemp = rightnowcomparable + 10;*/


        prayers.add(fajrtemp);
        prayers.add(dhuhrtemp);
        prayers.add(asrtemp);
        prayers.add(maghribtemp);
        prayers.add(ishatemp);
    }

    private void pull_prayer_times_and_shape_them() {
        prayerTimes = new PrayerTimes(coordinates, date, params);
        try {
            String timeshape = "hh:mm a";
            fajr = DateFormat.format(timeshape, new Date(prayerTimes.fajr.getTime())).toString();
            dhuhr = DateFormat.format(timeshape, new Date(prayerTimes.dhuhr.getTime())).toString();
            asr = DateFormat.format(timeshape, new Date(prayerTimes.asr.getTime())).toString();
            maghrib = DateFormat.format(timeshape, new Date(prayerTimes.maghrib.getTime())).toString();
            isha = DateFormat.format(timeshape, new Date(prayerTimes.isha.getTime())).toString();
        } catch(Exception ignored){}

    }

    private void sql(String table) {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = table;
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static Service getmCurrentService() {
        return mCurrentService;
    }

    public static void setmCurrentService(Service mCurrentService) {
        Service.mCurrentService = mCurrentService;
    }

    public static void isServiceRunning(){

    }

}
