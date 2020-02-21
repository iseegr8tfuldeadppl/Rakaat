package com.krimzon.scuffedbots.raka3at.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.format.DateFormat;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.background.utilities.Notification;

public class Service extends android.app.Service {
    protected static final int NOTIFICATION_ID = 1337;
    private static Service mCurrentService;
    private CalculationParameters params;
    private int i = 0;
    private List<String> lol;
    private int rightnowcomparable;
    private Date old_date, new_date;
    private boolean recent_adan = false;
    private Coordinates coordinates;
    private DateComponents date;
    private List<Integer> prayers;
    private String fajr;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;
    private static Timer timer;
    private static TimerTask timerTask;
    private boolean end_of_day = false;
    private boolean main_notification_switch = true;
    private SimpleExoPlayer simpleExoPlayer;

    public Service() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCurrentService = this;

        // check if switch for main notification is on
        sql("slat");
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        main_notification_switch = SQLSharing.mycursor.getString(1).equals("yes");

        close_sql();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            restartForeground();

        sql("force");
        if(SQLSharing.mycursor.getCount()>0)
            startTimer();
        close_sql();
    }

    private String nextadan = "";
    private String at = "";
    private void launch_prayer_processing() {
        sql("force");
        if(SQLSharing.mycursor.getCount()>0) {

            params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
            params = CalculationMethod.EGYPTIAN.getParameters();
            params.madhab = Madhab.SHAFI; // SHAFI made 95% accuracy, HANAFI had 1hour different for l'3asr
            params.adjustments.fajr = 2; //2
            //String pattern = "dd-MMM-yyyy";
            //SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            sql("slat");
            SQLSharing.mycursor.moveToPosition(6);
            String language = SQLSharing.mycursor.getString(1);
            close_sql();

            lol = new ArrayList<>();
            if(language.equals("en")){
                nextadan = getResources().getString(R.string.nextadan);
                at = getResources().getString(R.string.at);

                lol.add(getResources().getString(R.string.fajrtitle));
                lol.add(getResources().getString(R.string.dohrtitle));
                lol.add(getResources().getString(R.string.asrtitle));
                lol.add(getResources().getString(R.string.maghrebtitle));
                lol.add(getResources().getString(R.string.ishatitle));
            } else if(language.equals("ar")){
                nextadan = getResources().getString(R.string.nextadan_arabe);
                at = getResources().getString(R.string.at_arabe);

                lol.add(getResources().getString(R.string.fajrtitle_arabe));
                lol.add(getResources().getString(R.string.dohrtitle_arabe));
                lol.add(getResources().getString(R.string.asrtitle_arabe));
                lol.add(getResources().getString(R.string.maghrebtitle_arabe));
                lol.add(getResources().getString(R.string.ishatitle_arabe));
            }

            old_date = new Date();
            location_shit(old_date);

            find_next_adan();
        }
    }

    private void find_next_adan() {
        try {
            String temptime = String.valueOf(old_date).split(" ")[3];
            int rightnowcomparable_old = rightnowcomparable;
            rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 60 + Integer.valueOf(temptime.split(":")[1]);

            //if(rightnowcomparable_old!=rightnowcomparable)
                // displayed rightnowcomparable
            for (int j = 0; j < 5; j++) {
                if(rightnowcomparable<prayers.get(0)) {
                    i = 0;
                    break;
                }
                if (rightnowcomparable > prayers.get(j)){
                    i = j + 1;
                }
            }
            if(i>=5){
                i = 0;
                end_of_day = true;
            } else
                end_of_day = false;
        } catch(Exception ignored){}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // check if switch for main notification is on
        sql("slat");
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        main_notification_switch = SQLSharing.mycursor.getString(1).equals("yes");
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

        // it has been killed by Android and now it is restarted. We must make sure to have reinitialised everything
        if (intent == null) {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(this);
        }

        // make sure you call the startForeground on onStartCommand because otherwise
        // when we hide the notification on onScreen it will nto restart in Android 6 and 7
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            restartForeground();

        sql("force");
        if(SQLSharing.mycursor.getCount()>0)
            startTimer();
        close_sql();

        // return start sticky so if it is killed by android, it will be restarted with Intent null
        return START_STICKY;
    }

    private void close_sql() {
        if(SQLSharing.mycursor==null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb==null)
            SQLSharing.mydb.close();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                sql("force");
                if(SQLSharing.mycursor.getCount()>0) {
                    if(main_notification_switch) {
                        display_notification("Raka'at", "*Loading Next Adan*");
                    }
                }
            } catch (Exception ignored) {}
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

                    // Check if we are still in the same day, if not then calculate new day's prayertimes
                    new_date = new Date();
                    if(!String.valueOf(old_date).split(" ")[2].equals(String.valueOf(new_date).split(" ")[2]))
                        location_shit(new_date);
                    old_date = new_date;
                    find_next_adan();

                    if(!end_of_day) {
                        // Check if we reached the adan, if so, then switch i to the next adan
                        if (prayers.get(i) == rightnowcomparable) {
                            if (!recent_adan) {
                                recent_adan = true;

                                // Play adan audio
                                if(main_notification_switch && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                                    display_notification("New adan: " + lol.get(i) + " " + fajr, "");
                                else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    display_notification("New adan: " + lol.get(i) + " " + fajr, "");

                                playadan(pullselectedadanforthisprayerfromSQL(i));

                                // set i to the next adan
                                i++;
                                if (i >= 5) i = 0;
                            }
                        } else recent_adan = false;

                    }
                        if(main_notification_switch && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                            display_notification(nextadan + " " + lol.get(i) + " " + at + " " + praytimesregularform.get(i), "");
                        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            display_notification(nextadan + " " + lol.get(i) + " " + praytimesregularform.get(i), "");

                } catch(Exception e){e.printStackTrace();}
            }
        };
    }

    private int twentyfourhourtimeofnextadan = 0;
    private int pullselectedadanforthisprayerfromSQL(int prayedtobepopped) {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        int selectedadan = Integer.valueOf(SQLSharing.mycursor.getString(1).split(" ")[prayedtobepopped].split(",")[0]) - 1;
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();
        return selectedadan;
    }

    private void display_notification(String title, String description) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new Notification();
            startForeground(NOTIFICATION_ID, notification.setNotification(this, title, description, R.drawable.ic_launcher_background));
        } else {
            Intent emptyIntent = new Intent();
            int NOT_USED = 1338;
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), NOT_USED, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(title)
                            .setContentText(description)
                            .setContentIntent(pendingIntent); //Required on Gingerbread and below

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            int mId = 5565;
            notificationManager.notify(mId, mBuilder.build());
        }
    }

    private void location_shit(Date date) {
        if_theres_previous_info_load_it_n_display(date);
    }

    private void if_theres_previous_info_load_it_n_display(Date date) {
        sql("force");
        SQLSharing.mycursor.moveToFirst();
        double longitude = Double.valueOf(SQLSharing.mycursor.getString(1));
        double latitude = Double.valueOf(SQLSharing.mycursor.getString(2));
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

    private void playadan(int adantag) {
        try{
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        } catch(Exception ignored){}
        String adan = "";
        switch(adantag){
            case 0:
                adan = "amine.mp3";
                break;
            case 1:
                adan = "altazi.mp3";
                break;
            case 2:
                adan = "karim.mp3";
                break;
            case 3:
                adan = "ismail.mp3";
                break;
            case 4:
                adan = "afasi.mp3";
                break;
            case 5:
                adan = "madani.mp3";
        }
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(
                this,
                null,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        );
        TrackSelector trackSelector = new DefaultTrackSelector();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                renderersFactory,
                trackSelector
        );
        String userAgent = Util.getUserAgent(this, getResources().getString(R.string.adanner));
        try {
            ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(getResources().getString(R.string.idek) + adan), // file audio ada di folder assets
                    new DefaultDataSourceFactory(this, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null
            );
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        } catch(Exception ignored){}
    }

    private void convert_prayertimes_into_milliseconds() {
        int fajrtemp = Integer.valueOf(fajr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(fajr.split(" ")[0].split(":")[1]);
        if(fajr.split(" ")[1].equals("PM"))
            fajrtemp += 720; //12*60
        //Integer risetemp = Integer.valueOf(rise.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(rise.split(" ")[0].split(":")[1])*60;
        int dhuhrtemp = Integer.valueOf(dhuhr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(dhuhr.split(" ")[0].split(":")[1]);
        if(dhuhr.split(" ")[1].equals("PM") && !dhuhr.split(":")[0].equals("12"))
            dhuhrtemp += 720; //12*60
        int asrtemp = Integer.valueOf(asr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(asr.split(" ")[0].split(":")[1]);
        if(asr.split(" ")[1].equals("PM"))
            asrtemp += 720; //12*60
        int maghribtemp = Integer.valueOf(maghrib.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(maghrib.split(" ")[0].split(":")[1]);
        if(maghrib.split(" ")[1].equals("PM"))
            maghribtemp += 720; //12*60
        int ishatemp = Integer.valueOf(isha.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(isha.split(" ")[0].split(":")[1]);
        if(isha.split(" ")[1].equals("PM"))
            ishatemp += 720; //12*60



        /*// TODO:  for testing purposes
        temptime = String.valueOf(old_date).split(" ")[3];
        rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 3600 + Integer.valueOf(temptime.split(":")[1]) * 60 + Integer.valueOf(temptime.split(":")[2]);
        fajrtemp = rightnowcomparable + 10;*/


        prayers.add(fajrtemp);
        prayers.add(dhuhrtemp);
        prayers.add(asrtemp);
        prayers.add(maghribtemp);
        prayers.add(ishatemp);



        praytimesregularform = new ArrayList<>();

        if(fajr.split(" ")[1].equals("PM")) {
            if(Integer.valueOf(fajr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + fajr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(fajr.split(" ")[0].split(":")[0]) + 12) + ":" + fajr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(fajr.split(" ")[0]);

        if(dhuhr.split(" ")[1].equals("PM")){
            if(Integer.valueOf(dhuhr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + dhuhr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(dhuhr.split(" ")[0].split(":")[0])+12) + ":" + dhuhr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(dhuhr.split(" ")[0]);

        if(asr.split(" ")[1].equals("PM")){
            if(Integer.valueOf(asr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + asr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(asr.split(" ")[0].split(":")[0])+12) + ":" + asr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(asr.split(" ")[0]);

        if(maghrib.split(" ")[1].equals("PM")){
            if(Integer.valueOf(maghrib.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + asr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(maghrib.split(" ")[0].split(":")[0])+12) + ":" + maghrib.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(maghrib.split(" ")[0]);

        if(isha.split(" ")[1].equals("PM")){
            if(Integer.valueOf(maghrib.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + isha.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(isha.split(" ")[0].split(":")[0])+12) + ":" + isha.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(isha.split(" ")[0]);

    }

    private List<String> praytimesregularform;
    private void pull_prayer_times_and_shape_them() {
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, date, params);
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
