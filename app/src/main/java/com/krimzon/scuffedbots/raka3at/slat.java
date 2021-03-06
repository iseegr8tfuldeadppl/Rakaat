package com.krimzon.scuffedbots.raka3at;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.krimzon.scuffedbots.raka3at.CustomElements.NoPaddingTextView;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.dialogs.slat_settings;
import java.util.Date;
import java.util.Locale;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.animation.AnimationUtils.*;

public class slat extends AppCompatActivity implements SensorEventListener, slat_settings.BottomSheetListener {
//https://www.youtube.com/watch?v=dfTeS41BbbI

    // settingable²
    private int delay_between_sajadat = 800; // default 600
    private int sajda_length_of_time = 850;
    private int delay_between_raka3at = 7500; // default 5000
    private int delay_during_tahia = 12500; // default 10000
    private int minimum_light = 5; // default
    private double sajda_darkness_percentage = 0.50; // default: percentage of light to accept it as a sajda
    private double percentage_of_light_to_count_as_a_c_bon_rak3a = 0.65; // default
    public static int scheme = 3; // 0:darkest 1:dark 2:white
    public static int scheme_light_mode = 0; // 0:darkest 1:dark 2:white
    public static int delaybeforecounting = 5;

    private TextView raka3at;
    private TextView sajda;

    private ImageView soundsbutton;
    private boolean sounds = true;
    private boolean started = false;
    private boolean start_was_just_clicked = false;
    private String beep = "bloop.mp3";
    private boolean tahia_fading_started = false;
    private boolean not_clicked = true;
    private TextView tahia;
    private SensorManager sensorManager;
    private Sensor light;
    private float full_light;
    private boolean five_second_before_actually_starting_was_finished = false;
    private int num_of_raka3at=0;
    private int num_of_sajadat=0;
    private ImageView blackoutbutton, settings;
    private RelativeLayout blackground;
    private boolean original_light_saved = false;
    private boolean startclicked = false;
    private boolean sajda_done = false;
    private boolean sajda_going_on = false;
    private Button start;
    private TextView slattitle;
    private RelativeLayout full;
    private TextView sajda_pre;
    private Animation fade_in_tahia, fade_out_tahia, fade_in_tahia2, fade_out_tahia2;
    private Button one, two, three, four;
    private int limit = 4; // default
    private LinearLayout coverer;

    private long futuretime = 0;
    private long futuretime2 = 0;
    private boolean between_sajadat_delay_started = false;
    private long futuretime3 = 0;

    private boolean doitposse = true;
    private boolean between_raka3at_delay_started = false;
    private int animation_tracker_of_initial_five_seconds;
    private boolean animation_of_initial_five_seconds_started = false;
    private LinearLayout donecover;
    private Button donebutton;
    private ImageView nightmode;
    private boolean it_is_nightmode_since_lightmode_shines_and_ruins_measurement = true;
    private LinearLayout countdownbackground;
    private NoPaddingTextView countdown;
    private int visibleSystemUiVisibility;
    private String ID;
    private boolean blackout = false;
    private LinearLayout blackoutbuttonbackground, nightmodebuttonbackground, soundsbuttonbackground, settingsbuttonbackground;
    private Animation slide_in_from_right;
    private Animation slide_out_from_right;
    private Animation slide_in_from_right2;
    private Animation slide_out_from_right2;
    private Animation slide_in_from_right3;
    private Animation slide_out_from_right3;
    private Animation slide_in_from_right4;
    private Animation slide_out_from_right4;
    private String language = "ar";
    private String receiveandy = "";
    private String todaycomparable = "";
    private int prayer = 0;
    private String prayed = "00000";
    private String verified = "00000";
    private String athome = "00000";
    private String friday = "";
    private String at_home = "";
    private RelativeLayout backarrowbackground;
    private RelativeLayout howtousebackground;
    private boolean entered_praying_process = false;
    private long futuretime4 = 0;
    private boolean found_prayed_history_in_sql = false;
    private int saved_second;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unmuter();
        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slat);

        // override system locale
        Configuration cfg = new Configuration();
        cfg.locale = new Locale("en");
        this.getResources().updateConfiguration(cfg, null);

        initialize_all_variables();
        resources();

        if (!light_sensor_works)
            exit();

        sql_work();
        countdown.setText(String.valueOf(delaybeforecounting));
        if(language.equals("en"))
            english();
        work_on_text_brightness(); // must be after loading it_is_nightmode_since_lightmode_shines_and_ruins_measurement from SQL
        navigation_bar();
        check_if_sent_from_force();
        /*check_do_not_disturb_permission();*/
    }

    private void muter() {
        try{
            AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            //am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } catch(Exception ignored){
        }
    }


    private void check_do_not_disturb_permission(){
        try{
            if (Build.VERSION.SDK_INT >= 23)
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
        } catch(Exception ignored){
        }
    }
    private void unmuter() {
        try{
            AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } catch(Exception ignored){
        }
    }
    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 6969;
    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            // if user granted access else ask for permission
            if ( notificationManager.isNotificationPolicyAccessGranted()) {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else{
                // Open Setting screen to ask for permisssion
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult( intent, ON_DO_NOT_DISTURB_CALLBACK_CODE );
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
        }
    }


    private void check_if_sent_from_force() {
        receiveandy = getIntent().getStringExtra("sender");
        if(receiveandy==null)
            receiveandy = "main";
        if (receiveandy.equals("force")) {
            Intent sender = getIntent();
            prayer = Integer.parseInt(sender.getStringExtra("prayer"));
            todaycomparable = sender.getStringExtra("todaycomparable");
            prayed = sender.getStringExtra("prayed");
            verified = sender.getStringExtra("verified");
            friday = sender.getStringExtra("friday");
            at_home = sender.getStringExtra("at_home");
            athome = sender.getStringExtra("athome");
            hideNavigationBar();
            forceAndy();
        }
    }


    private void forceAndy() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
        click_on_start();
    }



    private NotificationManager mNotificationManager;
    protected void changeInterruptionFiler(int interruptionFilter){
        /*try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // If notification policy access granted for this package
                if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                    // Set the interruption filter
                    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotificationManager.setInterruptionFilter(interruptionFilter);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }*/
    }

    private void work_on_text_brightness() {
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement)
            update_darkness();
        else
            update_dimmness();
    }


    private void navigation_bar() {
        visibleSystemUiVisibility = this.getWindow().getDecorView().getSystemUiVisibility();
        showNavigationBar();
    }

    private void english(){
        four.setText(getResources().getString(R.string.four));
        three.setText(getResources().getString(R.string.three));
        two.setText(getResources().getString(R.string.two));
        one.setText(getResources().getString(R.string.one));
        slattitle.setText(getResources().getString(R.string.rakaat));
        tahia.setText(getResources().getString(R.string.tahia));
        sajda_pre.setText(getResources().getString(R.string.sajadat));
        start.setText(getResources().getString(R.string.start));
        donebutton.setText(getResources().getString(R.string.back2));
    }


    private void sql_work() {
        sql("slat");

        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToNext();
        String is_it_dark_mode_in_sql = SQLSharing.mycursorslat.getString(1);
        SQLSharing.mycursorslat.moveToNext();
        scheme = Integer.parseInt(SQLSharing.mycursorslat.getString(1));
        SQLSharing.mycursorslat.moveToNext();
        scheme_light_mode = Integer.parseInt(SQLSharing.mycursorslat.getString(1));
        SQLSharing.mycursorslat.moveToNext();
        delaybeforecounting = Integer.parseInt(SQLSharing.mycursorslat.getString(1));
        SQLSharing.mycursorslat.moveToNext();
        int soundstempint = Integer.parseInt(SQLSharing.mycursorslat.getString(1));
        SQLSharing.mycursorslat.moveToNext();
        language = SQLSharing.mycursorslat.getString(1);
        close_sql();

        if(soundstempint ==1)
            sounds = true;
        else if(soundstempint ==0)
            sounds = false;
        if(!sounds) {
                soundsbutton.setImageDrawable(getResources().getDrawable(R.drawable.soundsoff));
        }

        if(is_it_dark_mode_in_sql.equals("no"))
            light_mode();
        else
            it_is_nightmode_since_lightmode_shines_and_ruins_measurement = true;

    }

    private void resources(){
        slide_out_from_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_from_right);
        slide_in_from_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);
        slide_out_from_right2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_from_right);
        slide_in_from_right2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);
        slide_out_from_right3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_from_right);
        slide_in_from_right3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);
        slide_out_from_right4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_from_right);
        slide_in_from_right4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);


    }


    private TextView tampageTahiaFader;
    private ImageView questionmark;
    private void initialize_all_variables() {
        questionmark = findViewById(R.id.questionmark);
        try {
            Glide.with(this).load(R.drawable.questionmark).into(questionmark);
        } catch (Exception ignored) {
            questionmark.setImageDrawable(getResources().getDrawable(R.drawable.questionmark));
        }
        tampageTahiaFader = findViewById(R.id.tampageTahiaFader);
        blackoutbuttonbackground = findViewById(R.id.blackoutbuttonbackground);
        backarrowbackground = findViewById(R.id.backarrowbackground);
        howtousebackground = findViewById(R.id.howtousebackground);
        nightmodebuttonbackground = findViewById(R.id.nightmodebuttonbackground);
        soundsbuttonbackground = findViewById(R.id.soundsbuttonbackground);
        settingsbuttonbackground = findViewById(R.id.settingsbuttonbackground);
        blackground = findViewById(R.id.blackground);
        nightmode = findViewById(R.id.nightmode);
        soundsbutton = findViewById(R.id.soundsbutton);
        blackoutbutton = findViewById(R.id.blackout);
        tahia = findViewById(R.id.tahia);
        settings = findViewById(R.id.settings);
        countdown = findViewById(R.id.countdown);
        countdownbackground = findViewById(R.id.countdownbackground);
        donecover = findViewById(R.id.donecover);
        donebutton = findViewById(R.id.donebutton);
        coverer = findViewById(R.id.coverer);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        fade_in_tahia = loadAnimation(getApplicationContext(), R.anim.fade_in);
        fade_out_tahia = loadAnimation(getApplicationContext(), R.anim.fade_out);
        fade_in_tahia2 = loadAnimation(getApplicationContext(), R.anim.fade_in);
        fade_out_tahia2 = loadAnimation(getApplicationContext(), R.anim.fade_out);
        full = findViewById(R.id.full);
        sajda_pre = findViewById(R.id.sajda_pre);
        slattitle = findViewById(R.id.slattitle);
        start = findViewById(R.id.start);

        Typeface font = Typeface.createFromAsset(getAssets(), "Tajawal-Light.ttf");

        tampageTahiaFader.setTypeface(font);
        slattitle.setTypeface(font);
        start.setTypeface(font);
        sajda_pre.setTypeface(font);
        one.setTypeface(font);
        two.setTypeface(font);
        three.setTypeface(font);
        four.setTypeface(font);
        slattitle.setTypeface(font);
        start.setTypeface(font);
        sajda_pre.setTypeface(font);
        donebutton.setTypeface(font);
        tahia.setTypeface(font);

        //https://www.youtube.com/watch?v=ZL6s8TyHNOc
        //https://www.youtube.com/watch?v=ZL6s8TyHNOc
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sajda = findViewById(R.id.sajda);
        raka3at = findViewById(R.id.raka3at);
        raka3at.setText(String.valueOf(num_of_raka3at));
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        try {
            light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            light_sensor_works = true;
        } catch(Exception ignored){
            light_sensor_works = false;
        }
        if(!light_sensor_works)
            exit();
    }


    private boolean light_sensor_works = true;
    @Override
    public void onBackPressed() {
        try{
            if(receiveandy!=null) {
                if (receiveandy.equals("force"))
                    back_to_force("no");
                else {
                    if(entered_praying_process) {
                        countdownbackground.setVisibility(GONE);
                        countdown.setVisibility(GONE);
                        one.setVisibility(INVISIBLE);
                        two.setVisibility(INVISIBLE);
                        three.setVisibility(INVISIBLE);
                        four.setVisibility(INVISIBLE);
                        reset();
                    } else
                        exit();
                }
            } else {
                if(entered_praying_process) {
                    countdownbackground.setVisibility(GONE);
                    countdown.setVisibility(GONE);
                    one.setVisibility(INVISIBLE);
                    two.setVisibility(INVISIBLE);
                    three.setVisibility(INVISIBLE);
                    four.setVisibility(INVISIBLE);
                    reset();
                } else
                    exit();
            }
        } catch(Exception ignored){
        }
    }

    private void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    );
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    );
        }
    }

    private void showNavigationBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(visibleSystemUiVisibility);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.darkbackgroundcolor));
    }

    private void show_raka3at_selections(){
        entered_praying_process = true;
        if(receiveandy!=null) {
            if (receiveandy.equals("force")) {
                one.setEnabled(false);
                two.setEnabled(false);
                three.setEnabled(false);
                four.setEnabled(false);
            }
        }

        // Step 0: hide back & questionmarkbuttons
        backarrowbackground.setVisibility(View.GONE);
        howtousebackground.setVisibility(View.GONE);

        blackground.setVisibility(GONE);
        prepare_selection_and_countdown_things();
        coverer.setVisibility(VISIBLE);
        settings.setEnabled(false);
        nightmode.setEnabled(false);
        settingsbuttonbackground.setEnabled(false);
        nightmodebuttonbackground.setEnabled(false);
        blackoutbutton.setEnabled(false);
        blackoutbuttonbackground.setEnabled(false);

        one.startAnimation(slide_in_from_right);
        slide_in_from_right.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            one.setVisibility(View.VISIBLE);
            two.startAnimation(slide_in_from_right2);
            slide_in_from_right2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                two.setVisibility(View.VISIBLE);
                three.startAnimation(slide_in_from_right3);
                slide_in_from_right3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    three.setVisibility(View.VISIBLE);
                    four.startAnimation(slide_in_from_right4);
                    slide_in_from_right4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) { }@Override public void onAnimationEnd(Animation animation) {
                        four.setVisibility(View.VISIBLE);
                        if(receiveandy!=null) {
                            if (receiveandy.equals("force")) {
                                if (prayer == 0)
                                    limit = 2;
                                else if (prayer == 1) {
                                    if(friday.equals("true") && at_home.equals("false"))
                                        limit = 2;
                                    else
                                        limit = 4;
                                }
                                else if (prayer == 2)
                                    limit = 4;
                                else if (prayer == 3)
                                    limit = 3;
                                else if (prayer == 4)
                                    limit = 4;

                                hide_raka3at_selections();
                            }
                        }
                    }});
                }});
            }});
        }});
    }

    private void prepare_selection_and_countdown_things() {
        if(delaybeforecounting!=0)
            countdownbackground.setVisibility(VISIBLE);
        else
            five_second_before_actually_starting_was_finished = true;
        if(language.equals("en"))
            start.setText(getResources().getString(R.string.stop));
        else
            start.setText(getResources().getString(R.string.stop_arabe));
        blackoutbutton.setVisibility(VISIBLE);
        sajda_pre.setVisibility(INVISIBLE);
        settings.setVisibility(View.GONE);
        slattitle.setVisibility(GONE);

        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
            start.setBackground(null);
            update_darkness();
        } else {
            update_dimmness();
            start.setBackground(null);
        }
        if(delaybeforecounting==5)
            countdown.setText(String.valueOf(5));
        else if(delaybeforecounting==3)
            countdown.setText(String.valueOf(3));

        coverer.setVisibility(VISIBLE);
    }

    private void hide_raka3at_selections(){
        four.startAnimation(slide_out_from_right);
        slide_out_from_right.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            four.setVisibility(View.INVISIBLE);
            three.startAnimation(slide_out_from_right2);
            slide_out_from_right2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                three.setVisibility(View.INVISIBLE);
                two.startAnimation(slide_out_from_right3);
                slide_out_from_right3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    two.setVisibility(View.INVISIBLE);
                    one.startAnimation(slide_out_from_right4);
                    slide_out_from_right4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) { }@Override public void onAnimationEnd(Animation animation) {
                        if(!it_is_nightmode_since_lightmode_shines_and_ruins_measurement)
                            dimm_start_button_text();
                        else
                            darken_start_button();
                        one.setVisibility(View.INVISIBLE);
                        coverer.setVisibility(GONE);
                        not_clicked = false; // to not allow multiple selection clicks
                        startclicked = true;
                        blackground.setVisibility(VISIBLE);
                        if(delaybeforecounting==0){
                            go_in();
                        } else {
                            nightmode.setVisibility(View.GONE);
                            blackoutbutton.setVisibility(GONE);
                            settings.setVisibility(View.GONE);
                        }
                    }});
                }});
            }});
        }});
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(light_sensor_works)
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        if(five_second_before_actually_starting_was_finished) {
            hideNavigationBar();
        } else
            showNavigationBar();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            if(tam_page_visible)
                warn_tahia2();

            // this quick check below is to limit sensor calculation to only once every 50ms
            // TODO: perform reallife tests on this check pls
            long currenttimetemp = System.currentTimeMillis();
            if(currenttimetemp >= futuretime4) {
                // 50 millisecond delay between checks
                int sensor_slowdown_for_battery_save = 50;
                futuretime4 = currenttimetemp + sensor_slowdown_for_battery_save;

                if (start_was_just_clicked) { start_was_just_clicked = false;
                    if (event.values[0] < minimum_light) {
                        if (receiveandy != null) {
                            if (receiveandy.equals("force"))
                                back_to_force("yes");
                        }
                        if (language.equals("en"))
                            Snackbar.make(full, getString(R.string.low_light), Snackbar.LENGTH_LONG).show();
                        else
                            Snackbar.make(full, getString(R.string.low_light_arabe), Snackbar.LENGTH_LONG).show();
                    } else {
                        started = true;
                        hideNavigationBar();
                        show_raka3at_selections();
                        work2(event);
                    }
                }


                if (started) {
                    if (startclicked) {
                        work2(event);
                    }
                }
            }
        }
    }


    public void startClicked(View view){
        if(light_sensor_works)
            click_on_start();
        else {
            print("Light sensor not found");
            exit();
        }
    }


    private void click_on_start() {
        muter();
        if (!startclicked)
            start_was_just_clicked = true;
        else
            reset();
    }


    private void work2(SensorEvent event){


        // Step 1: a delay of 5 seconds to start working
        if(!five_second_before_actually_starting_was_finished)
            execute_delay_if_asked();
        // only if animation was finished do we start executing things below
        else {
            // Extra step Check for tahia
            if(!tam_page_visible)
                warn_tahia();

            // Step 2: save the original light
            save_original_light(event);

            // Extra step to add betweensajadat delay and betweenraka3at delay
            execute_betweensajadat_and_betweenraka3at_delays();

            // do it pusse
            if(doitposse)
                then_do_it_posse(event);
        }
    }


    private void then_do_it_posse(SensorEvent event) {
        // Step 3: if we got enough darkness start the sajda process
        if (event.values[0] < (full_light * sajda_darkness_percentage)) {

            if (!sajda_done)
                start_sajda();
        } else // Step 6: sajda didn't take over a selected sajda length of time so it gotta be fake baby reset
            sajda_going_on = false;

        increment_if_sajda_is_done(event);
    }


    private void increment_if_sajda_is_done(SensorEvent event) {
        // Step 7: if sajda was declared done, most of the light is back so we good, increment sajda/rak3a and display it.
        if (event.values[0] > (full_light * percentage_of_light_to_count_as_a_c_bon_rak3a) && sajda_done) {
            increment_sajda_and_rak3at_ofc_display();
            doitposse = false;
            sajda_done = false;
        }
    }


    private void start_sajda() {
        // Step 4: start recording the sajda if not sajed already
        if (!sajda_going_on) {
            sajda_going_on = true;
            futuretime = System.currentTimeMillis() + sajda_length_of_time;
        }

        // Step 5: check if we've been sajding for over the specified delay TODO: (make it adjustable by client)
        //   if so then say sajda has been done and pass it onto the if check below to increment after light went above a range
        if (System.currentTimeMillis() >= futuretime) {
            sajda_going_on = false;
            sajda_done = true;
        }
    }


    private void execute_betweensajadat_and_betweenraka3at_delays() {
        if(num_of_sajadat==1){

            // reset between raka3at
            if(between_raka3at_delay_started)
                between_raka3at_delay_started = false;

            // start between sajadat
            if(!between_sajadat_delay_started) {
                between_sajadat_delay_started = true;
                futuretime2 = System.currentTimeMillis();
            }
        }
        else if(num_of_sajadat==0 && num_of_raka3at>=1){
            // reset between sajadat
            if(between_sajadat_delay_started)
                between_sajadat_delay_started = false;

            // start between raka3at
            if(!between_raka3at_delay_started){
                between_raka3at_delay_started = true;
                futuretime3 = System.currentTimeMillis();
            }
        }
        // the big brain check
        if (!(num_of_raka3at == 0 && num_of_sajadat == 0)) {
            if (num_of_sajadat == 1 && between_sajadat_delay_started) {
                if (System.currentTimeMillis() >= delay_between_sajadat + futuretime2)
                    doitposse = true;
            } else if (num_of_sajadat == 0 && num_of_raka3at == 2 && between_raka3at_delay_started) {
                if (System.currentTimeMillis() >= delay_during_tahia + futuretime3)
                    doitposse = true;
            } else if (num_of_sajadat == 0 && num_of_raka3at >= 1 && between_raka3at_delay_started) {
                if (System.currentTimeMillis() >= delay_between_raka3at + futuretime3)
                    doitposse = true;
            }
        }
        if(num_of_raka3at==limit)
            doitposse = false;
    }


    private void save_original_light(SensorEvent event) {
        if (!original_light_saved) {
            full_light = event.values[0];
            original_light_saved = true;
        }
    }


    private void warn_tahia() {
        if(!tahia_fading_started){
            if(((limit == 1 && num_of_raka3at==1 && num_of_sajadat==0)
                    || (num_of_raka3at==2 && num_of_sajadat==0)
                    || (num_of_raka3at==4 && num_of_sajadat==0)) && tahia.getVisibility()==INVISIBLE) {
                tahia_fading_started = true;
                tahia.startAnimation(fade_in_tahia);
                fade_in_tahia.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {

                    if(!startclicked){
                        tahia.setVisibility(INVISIBLE);
                        tahia_fading_started = false;
                    } else {
                        tahia.setVisibility(VISIBLE);
                        tahia_fading_started = false;
                    }
                }});
            } else if(tahia.getVisibility()== VISIBLE) {
                tahia_fading_started = true;
                tahia.startAnimation(fade_out_tahia);
                fade_out_tahia.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) { }@Override public void onAnimationRepeat(Animation animation) { }@Override public void onAnimationEnd(Animation animation) {
                    tahia.setVisibility(View.INVISIBLE);
                    tahia_fading_started = false;
                }});
            }
        }
    }



    private void warn_tahia2() {
        if(tampageTahiaFader.getVisibility()==INVISIBLE) {
            tampageTahiaFader.startAnimation(fade_in_tahia2);
            fade_in_tahia2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                if(!tam_page_visible){
                    tampageTahiaFader.setVisibility(INVISIBLE);
                } else {
                    tampageTahiaFader.setVisibility(VISIBLE);
                }
            }});
        } else if(tampageTahiaFader.getVisibility()== VISIBLE) {
            tampageTahiaFader.startAnimation(fade_out_tahia2);
            fade_out_tahia2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) { }@Override public void onAnimationRepeat(Animation animation) { }@Override public void onAnimationEnd(Animation animation) {
                tampageTahiaFader.setVisibility(View.INVISIBLE);
            }});
        }
    }


    private void execute_delay_if_asked() {
        if(!animation_of_initial_five_seconds_started) {
            Date countdowndate = new Date();

            animation_tracker_of_initial_five_seconds = Integer.parseInt(countdowndate.toString().split(" ")[3].split(":")[2]);
            animation_of_initial_five_seconds_started = true;
            counter = delaybeforecounting;
        }

        initial_few_second_delay();
    }


    private void stopExoPlayer(){
        try{
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        } catch(Exception ignored){}
    }

    private int counter = 5;
    private void initial_few_second_delay() {
        if(sounds&&counter==delaybeforecounting){
            // maximum  music audio to play this bitch
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
            }
        }


        Date countdowndate = new Date();
        int current_second = Integer.parseInt(countdowndate.toString().split(" ")[3].split(":")[2]);

        if(counter==0){
            // TODO add a cooler dingdong after countdown finish it will be right here
            go_in();
        } else {
            if(current_second!=animation_tracker_of_initial_five_seconds){
                animation_tracker_of_initial_five_seconds = current_second;
                countdown.setText(String.valueOf(counter));

                if(sounds) {
                    initExoPlayer(beep);
                }

                counter -= 1;
            }

            if(!sounds) {
                stopExoPlayer();
            }
        }
    }


    private void go_in() {
        five_second_before_actually_starting_was_finished = true;
        countdownbackground.setVisibility(GONE);
        blackground.setVisibility(VISIBLE);
        blackoutbutton.setVisibility(VISIBLE);
        settings.setVisibility(View.GONE);
        nightmode.setEnabled(true);
        nightmodebuttonbackground.setEnabled(true);
        blackoutbutton.setEnabled(true);
        blackoutbuttonbackground.setEnabled(true);
        nightmode.setVisibility(VISIBLE);
        soundsbutton.setEnabled(false);
        soundsbuttonbackground.setEnabled(false);
        soundsbutton.setVisibility(View.GONE);
    }


    private void increment_sajda_and_rak3at_ofc_display() {
        num_of_sajadat++;
        if (num_of_sajadat == 2) {
            num_of_raka3at++;
            if (num_of_raka3at >= 10)
                num_of_raka3at = 0;
            if(num_of_raka3at==limit)
                show_tam_page();
            num_of_sajadat = 0;
        }
        raka3at.setText(String.valueOf(num_of_raka3at));
        sajda.setText(String.valueOf(num_of_sajadat));
    }


    private void reset(){
        unmuter();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);


        settings.setEnabled(true);
        nightmode.setEnabled(true);

        tam_page_visible = false;
        startclicked = false;
        start_was_just_clicked = false;
        between_raka3at_delay_started = false;
        between_sajadat_delay_started = false;

        if(blackout){
            blackground.setOnClickListener(null);
            blackground.setClickable(false);
            blackground.setFocusable(false);
            blackground.setEnabled(false);
            blackground.setBackground(null);
            blackout = false;
        }

        // allow to just close praying selections and praying shit when backpressed, and not just close out of activity
        entered_praying_process = false;
        // helps above
        coverer.setVisibility(GONE);

        // show back & questionmarkbuttons
        backarrowbackground.setVisibility(View.VISIBLE);
        howtousebackground.setVisibility(View.VISIBLE);

        start.setTextColor(Color.WHITE);
        not_clicked = true;
        one.setEnabled(true);
        two.setEnabled(true);
        three.setEnabled(true);
        four.setEnabled(true);
        started = false;
        blackground.setVisibility(VISIBLE);
        soundsbutton.setEnabled(true);
        soundsbuttonbackground.setEnabled(true);
        nightmode.setVisibility(VISIBLE);
        soundsbutton.setVisibility(View.VISIBLE);
        settings.setEnabled(true);
        settingsbuttonbackground.setEnabled(true);
        settings.setVisibility(VISIBLE);
        blackoutbutton.setVisibility(GONE);
        if(blackground.getDrawingCacheBackgroundColor()==getResources().getColor(R.color.black))
            blackground.setBackground(null);
        blackoutbutton.setVisibility(GONE);
        tahia.setVisibility(INVISIBLE);
        showNavigationBar();
        countdown.setText(String.valueOf(5));
        slattitle.setVisibility(VISIBLE);
        sajda_pre.setVisibility(VISIBLE);
        settings.setVisibility(VISIBLE);

        doitposse = true;
        animation_of_initial_five_seconds_started = false;
        five_second_before_actually_starting_was_finished = false;
        if(language.equals("en"))
            start.setText(getResources().getString(R.string.start));
        else
            start.setText(getResources().getString(R.string.start_arabe));
        original_light_saved = false;
        sajda_done = false;
        num_of_raka3at = 0;
        num_of_sajadat = 0;
        raka3at.setText(String.valueOf(num_of_raka3at));
        sajda.setText(String.valueOf(num_of_sajadat));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement)
            start.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));
        else
            start.setBackground(getResources().getDrawable(R.drawable.buttons));



        receiveandy = "main";
    }


    private void show_tam_page(){
        blackground.setVisibility(GONE);
        donecover.setVisibility(VISIBLE);


        StringBuilder strinkbilder = new StringBuilder(prayed);
        strinkbilder.setCharAt(prayer, '1');
        String temper = String.valueOf(strinkbilder);

        StringBuilder verified_stringbuilder = new StringBuilder(verified);
        verified_stringbuilder.setCharAt(prayer, '1');
        verified = String.valueOf(verified_stringbuilder);

        // set if at home or not
        if(at_home.equals("true")){
            StringBuilder athome_stringbuilder = new StringBuilder(athome);
            athome_stringbuilder.setCharAt(prayer, '1');
            athome = String.valueOf(athome_stringbuilder);
        }

        sql(getResources().getString(R.string.justforce3));
        check_if_prayed_exists_in_sql();
        if(found_prayed_history_in_sql)
            SQLSharing.mydbforce3.updatePrayed(todaycomparable, temper, verified, athome);
        else
            SQLSharing.mydbforce3.insertPrayed(todaycomparable, temper, verified, athome);
        close_sql();

        tam_page_visible = true;
        warn_tahia2();
    }

    private boolean tam_page_visible = false;
    private void check_if_prayed_exists_in_sql() {
        if(SQLSharing.mycursorforce3.getCount()<=0)
            found_prayed_history_in_sql = false;
        else {
            while(SQLSharing.mycursorforce3.moveToNext()) {
                if (todaycomparable.equals(SQLSharing.mycursorforce3.getString(1))){
                    found_prayed_history_in_sql = true;
                    break;
                }
            }
        }
    }


    private void sql(String table) {
        SQLSharing.TABLE_NAME_INPUTER = table;
        switch (table) {
            case "slat":
                SQLSharing.mydbslat = SQL.getInstance(this);
                SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
                break;
            case "force":
                SQLSharing.mydbforce = SQL.getInstance(this);
                SQLSharing.mycursorforce = SQLSharing.mydbforce.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mydbforce3 = SQL.getInstance(this);
                SQLSharing.mycursorforce3 = SQLSharing.mydbforce3.getAllDateforce3();
                break;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


    public void backClicked(View view) {
        exit();
    }


    private void exit() {
        Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainActivity);
        finish();
    }


    public void howtouseClicked(View view) {
        Intent tutorial = new Intent(this, Tutorial.class);
        tutorial.putExtra("sender", "slat");
        startActivity(tutorial);
        finish();
    }


    public void nightmodeClicked(View view) {
        if(blackground.getDrawingCacheBackgroundColor()==getResources().getColor(R.color.black))
            blackoutClickeroni();
        else
            switch_view_mode();
    }


    private void switch_view_mode() {
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement)
            light_mode();
        else
            dark_mode();
    }


    private void light_mode(){
        update_dimmness();

        // top buttons backgrounds
        backarrowbackground.setBackground(getResources().getDrawable(R.drawable.lightbackback));
        howtousebackground.setBackground(getResources().getDrawable(R.drawable.lightstatsback));

        it_is_nightmode_since_lightmode_shines_and_ruins_measurement = false;
        slattitle.setTextColor(getResources().getColor(R.color.lightelement));
        tampageTahiaFader.setTextColor(getResources().getColor(R.color.lightelement));
        countdownbackground.setBackground(getResources().getDrawable(R.drawable.simpelbackground));
        full.setBackground(getResources().getDrawable(R.drawable.simpelbackground));
        sajda_pre.setTextColor(getResources().getColor(R.color.lightelement));
        coverer.setBackground(getResources().getDrawable(R.drawable.simpelbackground));

        donecover.setBackground(getResources().getDrawable(R.drawable.simpelbackground));
        donebutton.setBackground(getResources().getDrawable(R.drawable.buttons));

        one.setBackground(getResources().getDrawable(R.drawable.buttons));
        two.setBackground(getResources().getDrawable(R.drawable.buttons));
        three.setBackground(getResources().getDrawable(R.drawable.buttons));
        four.setBackground(getResources().getDrawable(R.drawable.buttons));

        sql("slat");
        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToNext();
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("no", ID);
        close_sql();

        if(five_second_before_actually_starting_was_finished)
            start.setBackground(null);
        else
            start.setBackground(getResources().getDrawable(R.drawable.buttons));
    }


    private void update_dimmness(){
        if (scheme_light_mode == 0) {
            raka3at.setTextColor(getResources().getColor(R.color.dimmest));
            sajda.setTextColor(getResources().getColor(R.color.dimmest));
            countdown.setTextColor(getResources().getColor(R.color.dimmest));
            tahia.setTextColor(getResources().getColor(R.color.dimmest));
            if(startclicked)
                start.setTextColor(getResources().getColor(R.color.dimmest));
        } else if (scheme_light_mode == 1) {
            raka3at.setTextColor(getResources().getColor(R.color.dimmer));
            sajda.setTextColor(getResources().getColor(R.color.dimmer));
            countdown.setTextColor(getResources().getColor(R.color.dimmer));
            tahia.setTextColor(getResources().getColor(R.color.dimmer));
            if(startclicked)
                start.setTextColor(getResources().getColor(R.color.dimmer));
        } else {
            raka3at.setTextColor(getResources().getColor(R.color.dimm));
            sajda.setTextColor(getResources().getColor(R.color.dimm));
            countdown.setTextColor(getResources().getColor(R.color.dimm));
            tahia.setTextColor(getResources().getColor(R.color.dimm));
            if(startclicked)
                start.setTextColor(getResources().getColor(R.color.dimm));
        }
    }

    private void dimm_start_button_text(){
        if (scheme_light_mode == 0)
            start.setTextColor(getResources().getColor(R.color.dimmest));
        else if (scheme_light_mode == 1)
            start.setTextColor(getResources().getColor(R.color.dimmer));
        else
            start.setTextColor(getResources().getColor(R.color.dimm));
    }


    private void dark_mode(){
        update_darkness();

        backarrowbackground.setBackground(getResources().getDrawable(R.drawable.backback));
        howtousebackground.setBackground(getResources().getDrawable(R.drawable.statsback));

        it_is_nightmode_since_lightmode_shines_and_ruins_measurement = true;
        slattitle.setTextColor(getResources().getColor(R.color.white));
        tampageTahiaFader.setTextColor(getResources().getColor(R.color.white));
        full.setBackground(getResources().getDrawable(R.drawable.forcefull));
        countdownbackground.setBackground(getResources().getDrawable(R.drawable.forcefull));
        sajda_pre.setTextColor(getResources().getColor(R.color.white));
        coverer.setBackground(getResources().getDrawable(R.drawable.forcefull));

        donecover.setBackground(getResources().getDrawable(R.drawable.forcefull));
        donebutton.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));

        one.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));
        two.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));
        three.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));
        four.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));

        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(1);
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("yes", ID);
        close_sql();

        if(five_second_before_actually_starting_was_finished)
            start.setBackground(null);
        else
            start.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));
    }


    private void update_darkness() {
        if(scheme==0) {
            sajda.setTextColor(getResources().getColor(R.color.darkest));
            raka3at.setTextColor(getResources().getColor(R.color.darkest));
            countdown.setTextColor(getResources().getColor(R.color.darkest));
            tahia.setTextColor(getResources().getColor(R.color.darkest));
            if(startclicked)
                start.setTextColor(getResources().getColor(R.color.darkest));
        } else if(scheme==1) {
            sajda.setTextColor(getResources().getColor(R.color.dark));
            raka3at.setTextColor(getResources().getColor(R.color.dark));
            countdown.setTextColor(getResources().getColor(R.color.dark));
            tahia.setTextColor(getResources().getColor(R.color.dark));
            if(startclicked)
                start.setTextColor(getResources().getColor(R.color.dark));
        } else {
            sajda.setTextColor(getResources().getColor(R.color.white));
            raka3at.setTextColor(getResources().getColor(R.color.white));
            countdown.setTextColor(getResources().getColor(R.color.white));
            tahia.setTextColor(getResources().getColor(R.color.white));
            if(startclicked)
                start.setTextColor(getResources().getColor(R.color.white));
        }
    }


    private void darken_start_button() {
        if(scheme==0)
            start.setTextColor(getResources().getColor(R.color.darkest));
        else if(scheme==1)
            start.setTextColor(getResources().getColor(R.color.dark));
        else
            start.setTextColor(getResources().getColor(R.color.white));
    }


    public void fourClicked(View view) {
        if(not_clicked) {
            limit = 4;
            hide_raka3at_selections();
        }
    }


    public void threeClicked(View view) {
        if(not_clicked) {
            limit = 3;
            hide_raka3at_selections();
        }
    }


    public void twoClicked(View view) {
        if(not_clicked) {
            limit = 2;
            hide_raka3at_selections();
        }
    }


    public void oneClicked(View view) {
        if(not_clicked) {
            limit = 1;
            hide_raka3at_selections();
        }
    }


    public void tamClicked(View view) {
        tam_page_visible = false;
        if(receiveandy==null)
            receiveandy = "main";
        if (receiveandy.equals("force"))
            back_to_force_with_day_indication();
        else {
            donecover.setVisibility(GONE);
            reset();
        }
    }

    private void back_to_force(String input) {
        Intent forceIntent = new Intent(this, force.class);
        forceIntent.putExtra("light_alert", input); // yes for true
        startActivity(forceIntent);
        finish();
    }

    private void back_to_force_with_day_indication() {
        close_sql();
        Intent forceIntent = new Intent(this, force.class);
        forceIntent.putExtra("todaycomparable", todaycomparable);
        forceIntent.putExtra("light_alert", "no"); // yes for true
        startActivity(forceIntent);
        finish();
    }

    private void close_sql() {
        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();;
    }


    private void print(Object lol) {
        Toast.makeText(getApplicationContext(), String.valueOf(lol), Toast.LENGTH_SHORT).show();
    }


    public void settingsClicked(View view) {
        if(!blackout) {
            slat_settings bottomsheet = new slat_settings();
            bottomsheet.show(getSupportFragmentManager(), "bottomsheeter");
        }
    }


    @Override
    public void onButtonClicked(String text) {}


    private void blackoutClickeroni(){
        if (!blackout) {
            blackground.setClickable(true);
            blackground.setFocusable(true);
            blackground.setEnabled(true);
            blackground.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
                blackoutClickeroni();
            }});
            blackground.setBackgroundColor(getResources().getColor(R.color.black));
            blackout = true;
            nightmode.setVisibility(View.GONE);
        } else {
            blackground.setOnClickListener(null);
            blackground.setClickable(false);
            blackground.setFocusable(false);
            blackground.setEnabled(false);
            blackground.setBackground(null);
            blackout = false;
            nightmode.setVisibility(VISIBLE);
        }
    }


    public void blackoutClicked(View view) {
        blackoutClickeroni();
    }


    private SimpleExoPlayer simpleExoPlayer;
    private void initExoPlayer(String lol) {
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
        String userAgent = Util.getUserAgent(this, "Play Audio");
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse("asset:///" + lol), // file audio ada di folder assets
                new DefaultDataSourceFactory(this, userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }
    /*private void releaseExoPlayer() {
        simpleExoPlayer.release();
    }*/


    public void soundsClicked(View view) {
        if(sounds){
                soundsbutton.setImageDrawable(getResources().getDrawable(R.drawable.soundsoff));
            sounds = false;
            SQLSharing.mydbslat.updateData("0", "6");
        } else {
                soundsbutton.setImageDrawable(getResources().getDrawable(R.drawable.soundson));
            sounds = true;
            SQLSharing.mydbslat.updateData("1", "6");
        }
    }


}
