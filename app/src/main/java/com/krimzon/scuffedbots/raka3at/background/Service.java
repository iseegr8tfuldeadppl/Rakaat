package com.krimzon.scuffedbots.raka3at.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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
import com.krimzon.scuffedbots.raka3at.force;
import com.krimzon.scuffedbots.raka3at.force_widget;

public class Service extends android.app.Service {
    protected static final int NOTIFICATION_ID = 1337;
    private static Service mCurrentService;
    private CalculationParameters params;
    private int i = 0;
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
    private String language = "en";
    private int current_adding_playing = 0;
    private boolean darkmode = true;
    private boolean once = true;
    private NotificationManager notificationManager;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder;
    private Notification notification;
    private boolean playing = false;
    private List<String> praytimesregularform;

    public Service() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCurrentService = this;

        // check if switch for main notification is on
        sql("slat");
        SQLSharing.mycursor.moveToPosition(1);
        darkmode = SQLSharing.mycursor.getString(1).equals("yes");
        SQLSharing.mycursor.moveToPosition(6);
        language = SQLSharing.mycursor.getString(1);
        SQLSharing.mycursor.moveToPosition(8);
        main_notification_switch = SQLSharing.mycursor.getString(1).equals("yes");
        close_sql();

        sql("force");
        if(SQLSharing.mycursor.getCount()>0)
            startTimer();
        close_sql();

        launch_stop_adan_button_listener();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if(action.equals("com.krimzon.scuffedbots.raka3at.background.stop_adan_finish_listener")){
                try{
                    if(playing)
                        sendBroadcast(new Intent("com.krimzon.scuffedbots.raka3at.background.stop_adan_finish_listener"));
                    simpleExoPlayer.stop();
                    simpleExoPlayer.release();
                    playing = false;
                    once = true;
                } catch(Exception ignored){}
            }
        }
    };

    private void launch_stop_adan_button_listener() {
        // https://stackoverflow.com/questions/9092134/broadcast-receiver-within-a-service
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.krimzon.scuffedbots.raka3at.background.stop_adan_finish_listener"); //further more

        registerReceiver(receiver, filter);
    }

    private void launch_prayer_processing() {
        sql("force");
        if(SQLSharing.mycursor.getCount()>0) {

            params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
            params = CalculationMethod.EGYPTIAN.getParameters();
            params.madhab = Madhab.SHAFI; // SHAFI made 95% accuracy, HANAFI had 1hour different for l'3asr
            params.adjustments.fajr = SQLSharing.params_adjustments_fajr; //2 TODO change this one aswell
            //String pattern = "dd-MMM-yyyy";
            //SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            old_date = new Date();
            location_shit(old_date);

            find_next_adan();

            if(main_notification_switch && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                display_notification(false);
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                display_notification(true);
        }
    }

    private void find_next_adan() {
        try {
            String temptime = String.valueOf(old_date).split(" ")[3];
            //int rightnowcomparable_old = rightnowcomparable;
            rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 60 + Integer.valueOf(temptime.split(":")[1]);

            /*if(rightnowcomparable_old!=rightnowcomparable){
            }*/

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
                        display_notification(true);
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
        unregisterReceiver(receiver);
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

        launch_prayer_processing();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            restartForeground();

        //initialize the TimerTask's job
        initializeTimerTask();


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
                    if(i!=-1) {
                        find_slider(i);
                        calculate_negatifise_and_positifise();
                        check_negatifise();
                        then_check_positifise();
                        if_new_slider_apply_it();
                        cleaning = true;
                        find_slider(i);
                        cleaning = false;
                    }


                    if(!end_of_day) {
                        // Check if we reached the adan, if so, then switch i to the next adan
                        if (prayers.get(i) == rightnowcomparable) {
                            if (!recent_adan) {
                                recent_adan = true;
                                current_adding_playing = i;

                                // Play adan audio
                                if(main_notification_switch && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                                    display_notification(false);
                                else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    display_notification(true);

                                playadan(pullselectedadanforthisprayerfromSQL(i));

                                // set i to the next adan
                                i++;
                                if (i >= 5) i = 0;
                            }
                        } else recent_adan = false;

                    }
                    if(!playing) {
                        if (main_notification_switch && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                            if (once)
                                display_notification(false);
                            notificationManager.notify(NOTIFICATION_ID, notification); // make sure notification is still displayed
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (once)
                                display_notification(true);
                            startForeground(NOTIFICATION_ID, notification);
                        } else
                            notificationManager.cancel(NOTIFICATION_ID);
                    } else {  // if adan is playing then make sure the adan notification is displayed

                        if (main_notification_switch && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                            display_notification(false);
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            display_notification(true);
                        } else
                            notificationManager.cancel(NOTIFICATION_ID);
                    }

                } catch(Exception e){ e.printStackTrace(); }
            }
        };
    }

    private void then_check_positifise() {
        /*if ((temp_positifise != positifise) && !still_scoping_on_previous_adan)*/
            positifise = temp_positifise;
    }

    private int temp_positifise=1, positifise=0;
    private void calculate_negatifise_and_positifise() {
        if (i != 0)
            temp_negatifise = Math.round(Math.abs((rightnowcomparable - prayers.get(i - 1))));
        else
            still_scoping_on_previous_adan = false;

        temp_positifise = Math.round((prayers.get(i) - rightnowcomparable));
    }

    private int minute_limit_to_display_positifise = 100, minute_limit_to_display_negatifise = 20;
    private int temp_negatifise = 1, negatifise = 0;
    private void check_negatifise() {
        if((temp_negatifise != negatifise) && i!=0){
            negatifise = temp_negatifise;
            still_scoping_on_previous_adan = negatifise <= minute_limit_to_display_negatifise;
        }
    }

    int lol = -3;
    private void if_new_slider_apply_it() {
        /*if(still_scoping_on_previous_adan)
            lol = i-1;
        else*/
            lol = i;
        if(lol!=current_displayed_slider){
            switch(lol){
                case 0:
                    widgetViews.setViewVisibility(R.id.widgetsliderfajr, View.INVISIBLE);
                    break;
                case 1:
                    widgetViews.setViewVisibility(R.id.widgetsliderdhuhr, View.INVISIBLE);
                    break;
                case 2:
                    widgetViews.setViewVisibility(R.id.widgetsliderasr, View.INVISIBLE);
                    break;
                case 3:
                    widgetViews.setViewVisibility(R.id.widgetslidermaghreb, View.INVISIBLE);
                    break;
                case 4:
                    widgetViews.setViewVisibility(R.id.widgetsliderisha, View.INVISIBLE);
            }
            if(still_scoping_on_previous_adan){
                widgetViews.setTextViewText(slider, "+ " + String.valueOf(negatifise));
                widgetViews.setViewVisibility(slider, View.VISIBLE);
            }
            else if(positifise<=minute_limit_to_display_positifise){
                widgetViews.setTextViewText(slider, "- " + String.valueOf(positifise));
                widgetViews.setViewVisibility(slider, View.VISIBLE);
            }

            apply_widget_update();
        }
    }

    private boolean still_scoping_on_previous_adan = false;
    private int current_displayed_slider = -1;
    private int slider = 0;
    private boolean cleaning = false;
    private void find_slider(final int next_adaner) {
        int temp;
        if (still_scoping_on_previous_adan)
            temp = next_adaner - 1;
        else
            temp = next_adaner;
        switch (temp) {
            case -1:
                if(cleaning)
                    current_displayed_slider = -1;
                break;
            case 0:
            case 5:
                if(cleaning)
                    current_displayed_slider = 0;
                else
                    slider = R.id.widgetsliderfajr;
                break;
            case 1:
                if(cleaning)
                    current_displayed_slider = 1;
                else
                    slider = R.id.widgetsliderdhuhr;
                break;
            case 2:
                if(cleaning)
                    current_displayed_slider = 2;
                else
                    slider = R.id.widgetsliderasr;
                break;
            case 3:
                if(cleaning)
                    current_displayed_slider = 3;
                else
                    slider = R.id.widgetslidermaghreb;
                break;
            case 4:
                if(cleaning)
                    current_displayed_slider = 4;
                else
                    slider = R.id.widgetsliderisha;
                break;
        }
    }

    private int pullselectedadanforthisprayerfromSQL(int prayedtobepopped) {
        sql("slat");

        SQLSharing.mycursor.moveToPosition(7);
        int selectedadan = Integer.valueOf(SQLSharing.mycursor.getString(1).split(" ")[prayedtobepopped].split(",")[0]) - 1;
        close_sql();
        return selectedadan;
    }

    private void display_notification(boolean is_it_over_android_O) {

        try {
            if (once && !playing) {

                update_widget_ui();
                update_notification_ui();
            }

            if(playing)// TODO add a button that appears in force and all activiteis that says adan is playing and a button to stop it
                update_notification_ui_for_adan();
            else
                slight_update_notification_and_widget_ui();

            notification = builder.build();
            if(is_it_over_android_O)
                startForeground(NOTIFICATION_ID, notification);
            else
                notificationManager.notify(NOTIFICATION_ID, notification);

            apply_widget_update();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private RemoteViews widgetViews;
    private void update_widget_ui() {

        if(darkmode)
            widgetViews = new RemoteViews(getPackageName(), R.layout.force_widget);
        else
            widgetViews = new RemoteViews(getPackageName(), R.layout.force_widget);

        widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
        widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
        widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
        widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
        widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);

        if(language.equals("en")){
            widgetViews.setTextViewText(R.id.widgetfajrtitle, getResources().getString(R.string.fajrtitle));
            widgetViews.setTextViewText(R.id.widgetdhuhrtitle, getResources().getString(R.string.dohrtitle));
            widgetViews.setTextViewText(R.id.widgetasrtitle, getResources().getString(R.string.asrtitle));
            widgetViews.setTextViewText(R.id.widgetmaghribtitle, getResources().getString(R.string.maghrebtitle));
            widgetViews.setTextViewText(R.id.widgetishatitle, getResources().getString(R.string.ishatitle));
        }

        widgetViews.setTextViewText(R.id.widgetfajrtime, praytimesregularform.get(0));
        widgetViews.setTextViewText(R.id.widgetdhuhrtime, praytimesregularform.get(1));
        widgetViews.setTextViewText(R.id.widgetasrtime, praytimesregularform.get(2));
        widgetViews.setTextViewText(R.id.widgetmaghrebtime, praytimesregularform.get(3));
        widgetViews.setTextViewText(R.id.widgetishatime, praytimesregularform.get(4));

    }

    private void apply_widget_update() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(new ComponentName(this.getPackageName(), force_widget.class.getName()), widgetViews);
    }

    private void slight_update_notification_and_widget_ui() {
        // Notification
        switch (i) {
            case 0:
                remoteViews.setImageViewResource(R.id.fajrarrow, R.drawable.greenarrowdown);
                remoteViews.setImageViewResource(R.id.dhuhrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.asrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.maghrebarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.ishaarrow, R.drawable.arrowdown);
                break;
            case 1:
                remoteViews.setImageViewResource(R.id.fajrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.dhuhrarrow, R.drawable.greenarrowdown);
                remoteViews.setImageViewResource(R.id.asrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.maghrebarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.ishaarrow, R.drawable.arrowdown);
                break;
            case 2:
                remoteViews.setImageViewResource(R.id.fajrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.dhuhrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.asrarrow, R.drawable.greenarrowdown);
                remoteViews.setImageViewResource(R.id.maghrebarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.ishaarrow, R.drawable.arrowdown);
                break;
            case 3:
                remoteViews.setImageViewResource(R.id.fajrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.dhuhrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.asrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.maghrebarrow, R.drawable.greenarrowdown);
                remoteViews.setImageViewResource(R.id.ishaarrow, R.drawable.arrowdown);
                break;
            case 4:
                remoteViews.setImageViewResource(R.id.fajrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.dhuhrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.asrarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.maghrebarrow, R.drawable.arrowdown);
                remoteViews.setImageViewResource(R.id.ishaarrow, R.drawable.greenarrowdown);
        }

        // Widget
        switch (i) {
            case 0:
                if(language.equals("en"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.fajrtitle));
                else if(language.equals("ar"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.fajrtitle_arabe));

                widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(0));

                widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.greenarrowdown);
                widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                break;
            case 1:
                if(language.equals("en"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.dohrtitle));
                else if(language.equals("ar"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.dohrtitle_arabe));

                widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(1));

                widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.greenarrowdown);
                widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                break;
            case 2:
                if(language.equals("en"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.asrtitle));
                else if(language.equals("ar"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.asrtitle_arabe));

                widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(2));

                widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.greenarrowdown);
                widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                break;
            case 3:
                if(language.equals("en"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.maghrebtitle));
                else if(language.equals("ar"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.maghrebtitle_arabe));

                widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(3));

                widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.greenarrowdown);
                widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                break;
            case 4:
                if(language.equals("en"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.ishatitle));
                else if(language.equals("ar"))
                    widgetViews.setTextViewText(R.id.nextprayertitle, getResources().getString(R.string.ishatitle_arabe));

                widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(4));

                widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.greenarrowdown);
        }
    }

    private void update_notification_ui_for_adan() {
        if(darkmode)
            remoteViews = new RemoteViews(getPackageName(), R.layout.adanondarkmode);
        else
            remoteViews = new RemoteViews(getPackageName(), R.layout.adanonlightmode);


        String current_adan = "Fajr";
        String stop_it = getResources().getString(R.string.stop_it);
        String time = getResources().getString(R.string.time);
        if(language.equals("ar")) {
            time = getResources().getString(R.string.time_arabe);
            stop_it = getResources().getString(R.string.stop_it_arabe);
            switch (current_adding_playing) {
                case 0:
                    current_adan = getResources().getString(R.string.fajrtitle_arabe);
                    break;
                case 1:
                    current_adan = getResources().getString(R.string.dohrtitle_arabe);
                    break;
                case 2:
                    current_adan = getResources().getString(R.string.asrtitle_arabe);
                    break;
                case 3:
                    current_adan = getResources().getString(R.string.maghrebtitle_arabe);
                    break;
                case 4:
                    current_adan = getResources().getString(R.string.ishatitle_arabe);
            }
            remoteViews.setTextViewText(R.id.adangoingon, time + " " + current_adan);
            remoteViews.setTextViewText(R.id.cancelbutton, stop_it);
        } else if(language.equals("en")){
            switch (current_adding_playing) {
                case 0:
                    current_adan = getResources().getString(R.string.fajrtitle);
                    break;
                case 1:
                    current_adan = getResources().getString(R.string.dohrtitle);
                    break;
                case 2:
                    current_adan = getResources().getString(R.string.asrtitle);
                    break;
                case 3:
                    current_adan = getResources().getString(R.string.maghrebtitle);
                    break;
                case 4:
                    current_adan = getResources().getString(R.string.ishatitle);
            }
            remoteViews.setTextViewText(R.id.adangoingon, current_adan + " " + time);
            remoteViews.setTextViewText(R.id.cancelbutton, stop_it);
        }

        Intent button_intent = new Intent("com.krimzon.scuffedbots.raka3at.background.stop_adan_finish_listener");
        //button_intent.putExtra("id",NOTIFICATION_ID);
        PendingIntent button_pending_event = PendingIntent.getBroadcast(this,NOTIFICATION_ID, button_intent,0);
        remoteViews.setOnClickPendingIntent(R.id.cancelbutton,button_pending_event);
        Intent notification_intent = new Intent(this, force.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification_intent, 0);
        builder.setSmallIcon(R.mipmap.ic_launcher).setOngoing(true).setContentIntent(pendingIntent).setCustomContentView(remoteViews);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
    }

    private void update_notification_ui() {
        Context context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, "channel");

        if(darkmode)
            remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification2);
        else
            remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);

        remoteViews.setImageViewResource(R.id.fajrarrow, R.drawable.arrowdown);
        remoteViews.setImageViewResource(R.id.dhuhrarrow, R.drawable.arrowdown);
        remoteViews.setImageViewResource(R.id.asrarrow, R.drawable.arrowdown);
        remoteViews.setImageViewResource(R.id.maghrebarrow, R.drawable.arrowdown);
        remoteViews.setImageViewResource(R.id.ishaarrow, R.drawable.arrowdown);

        if(language.equals("en")){
            remoteViews.setTextViewText(R.id.fajrtitle, getResources().getString(R.string.fajrtitle));
            remoteViews.setTextViewText(R.id.dhuhrtitle, getResources().getString(R.string.dohrtitle));
            remoteViews.setTextViewText(R.id.asrtitle, getResources().getString(R.string.asrtitle));
            remoteViews.setTextViewText(R.id.maghribtitle, getResources().getString(R.string.maghrebtitle));
            remoteViews.setTextViewText(R.id.ishatitle, getResources().getString(R.string.ishatitle));
        }

        Intent notification_intent = new Intent(context, force.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notification_intent, 0);
        builder.setSmallIcon(R.mipmap.ic_launcher).setOngoing(true).setContentIntent(pendingIntent).setCustomContentView(remoteViews);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        remoteViews.setTextViewText(R.id.fajrtime, praytimesregularform.get(0));
        remoteViews.setTextViewText(R.id.dhuhrtime, praytimesregularform.get(1));
        remoteViews.setTextViewText(R.id.asrtime, praytimesregularform.get(2));
        remoteViews.setTextViewText(R.id.maghrebtime, praytimesregularform.get(3));
        remoteViews.setTextViewText(R.id.ishatime, praytimesregularform.get(4));

        once = false;
    }

    private void print(Object log){
        Toast.makeText(this, String.valueOf(log), Toast.LENGTH_LONG).show();
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
            playing = true;

            simpleExoPlayer.addListener(new Player.EventListener() {

                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_ENDED) {
                        playing = false;
                        once = true;
                        sendBroadcast(new Intent("com.krimzon.scuffedbots.raka3at.background.stop_adan_finish_listener"));
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });
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
        switch (table) {
            case "slat":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateslat();
                break;
            case "force":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateforce3();
                break;
        }
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
