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
import com.krimzon.scuffedbots.raka3at.dialogs.SlatCustomDialogClass;
import com.krimzon.scuffedbots.raka3at.dialogs.slat_settings;
import java.util.Locale;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.animation.AnimationUtils.*;

public class slat extends AppCompatActivity implements SensorEventListener, slat_settings.BottomSheetListener {
//https://www.youtube.com/watch?v=dfTeS41BbbI

    // settingable
    private int delay_between_sajadat = 800; // default 600
    private int sajda_length_of_time = 850;
    private int delay_between_raka3at = 7500; // default 5000
    private int delay_during_tahia = 10000; // default 10000
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
    private Animation fade_in_tahia, fade_out_tahia;
    private Button one, two, three, four;
    private int limit = 4; // default
    private LinearLayout coverer;

    private long futuretime = 0;
    private long futuretime2 = 0;
    private boolean between_sajadat_delay_started = false;
    private long futuretime3 = 0;

    private boolean doitposse = true;
    private boolean between_raka3at_delay_started = false;
    private long animation_tracker_of_initial_five_seconds;
    private boolean animation_of_initial_five_seconds_started = false;
    private LinearLayout donecover;
    private Button donebutton;
    private ImageView nightmode;
    private boolean it_is_nightmode_since_lightmode_shines_and_ruins_measurement = true;
    private LinearLayout countdownbackground;
    private NoPaddingTextView countdown;
    private int visibleSystemUiVisibility;
    private String ID;
    private Resources resources;
    private boolean blackout = false;
    private boolean five_secs = true, four_secs = true, three_secs = true, two_secs = true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unmuter();
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
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void check_do_not_disturb_permission(){
        try{
            if (Build.VERSION.SDK_INT >= 23)
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private void unmuter() {
        try{
            AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } catch(Exception e){
            e.printStackTrace();
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
            prayer = Integer.valueOf(sender.getStringExtra("prayer"));
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
        muter();
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
        four.setText(resources.getString(R.string.four));
        three.setText(resources.getString(R.string.three));
        two.setText(resources.getString(R.string.two));
        one.setText(resources.getString(R.string.one));
        slattitle.setText(resources.getString(R.string.rakaat));
        tahia.setText(resources.getString(R.string.tahia));
        sajda_pre.setText(resources.getString(R.string.sajadat));
        start.setText(resources.getString(R.string.start));
        donebutton.setText(resources.getString(R.string.back2));
    }


    private void sql_work() {
        sql("slat");

        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        String is_it_dark_mode_in_sql = SQLSharing.mycursor.getString(1);
        SQLSharing.mycursor.moveToNext();
        scheme = Integer.parseInt(SQLSharing.mycursor.getString(1));
        SQLSharing.mycursor.moveToNext();
        scheme_light_mode = Integer.parseInt(SQLSharing.mycursor.getString(1));
        SQLSharing.mycursor.moveToNext();
        delaybeforecounting = Integer.parseInt(SQLSharing.mycursor.getString(1));
        SQLSharing.mycursor.moveToNext();
        int soundstempint = Integer.parseInt(SQLSharing.mycursor.getString(1));
        SQLSharing.mycursor.moveToNext();
        language = SQLSharing.mycursor.getString(1);

        if(soundstempint ==1)
            sounds = true;
        else if(soundstempint ==0)
            sounds = false;
        if(!sounds) {
            try {
                Glide.with(this).load(R.drawable.soundsoff).into(soundsbutton);
            } catch (Exception ignored) {
                soundsbutton.setImageDrawable(resources.getDrawable(R.drawable.soundsoff));
            }
        }

        if(is_it_dark_mode_in_sql.equals("no"))
            light_mode();
        else
            it_is_nightmode_since_lightmode_shines_and_ruins_measurement = true;

        assert SQLSharing.mycursor != null;
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();
    }

    private void resources(){
        resources = getResources();
        slide_out_from_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_from_right);
        slide_in_from_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);
        slide_out_from_right2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_from_right);
        slide_in_from_right2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);
        slide_out_from_right3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_from_right);
        slide_in_from_right3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);
        slide_out_from_right4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_from_right);
        slide_in_from_right4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);


    }


    private void initialize_all_variables() {
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
        full = findViewById(R.id.full);
        sajda_pre = findViewById(R.id.sajda_pre);
        slattitle = findViewById(R.id.slattitle);
        start = findViewById(R.id.start);

        Typeface font = Typeface.createFromAsset(getAssets(), "Tajawal-Light.ttf");

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
        } catch(Exception e){
            e.printStackTrace();
            light_sensor_works = false;
        }
        if(!light_sensor_works)
            exit();
    }


    private boolean light_sensor_works = true;
    @Override
    public void onBackPressed() {
        if(receiveandy!=null) {
            if (receiveandy.equals("force"))
                back_to_force("no");
            else {
                if(entered_praying_process) {
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
            this.getWindow().setStatusBarColor(resources.getColor(R.color.darkbackgroundcolor));
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
        settingsbuttonbackground.setEnabled(false);
        nightmode.setEnabled(false);
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
            start.setText(resources.getString(R.string.stop));
        else
            start.setText(resources.getString(R.string.stop_arabe));
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
                        if(sounds && delaybeforecounting!=0)
                            initExoPlayer(beep);
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
        Log.i("HH", "num_of_raka3at " + num_of_raka3at);
        Log.i("HH", "num_of_sajadat " + num_of_sajadat);
        Log.i("HH", "tahia_fading_started " + tahia_fading_started);
        if(!tahia_fading_started){
            if(((limit==1 && num_of_raka3at==1 && num_of_sajadat==0) || (num_of_raka3at==2 && num_of_sajadat==0)
                    || (num_of_raka3at==4 && num_of_sajadat==0)
                    || (limit==3 && num_of_raka3at==3 && num_of_sajadat==0)) && tahia.getVisibility()==GONE) {
                tahia_fading_started = true;
                tahia.startAnimation(fade_in_tahia);
                fade_in_tahia.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    tahia.setVisibility(VISIBLE);
                    tahia_fading_started = false;
                }});
            } else if(tahia.getVisibility()== VISIBLE) {
                tahia_fading_started = true;
                tahia.startAnimation(fade_out_tahia);
                fade_out_tahia.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) { }@Override public void onAnimationRepeat(Animation animation) { }@Override public void onAnimationEnd(Animation animation) {
                    tahia.setVisibility(View.GONE);
                    tahia_fading_started = false;
                }});
            }
        }
    }


    private void execute_delay_if_asked() {
        if(!animation_of_initial_five_seconds_started) {
            animation_tracker_of_initial_five_seconds = System.currentTimeMillis();
            animation_of_initial_five_seconds_started = true;
        }

        if(delaybeforecounting==5)
            five_second_delay();
        else if(delaybeforecounting==3)
            three_second_delay();
    }


    private void three_second_delay() {
        if (System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 3000){
            go_in();
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 2000) {
            countdown.setText(String.valueOf(1));
            if(sounds && two_secs) {
                initExoPlayer(beep);
                two_secs = false;
            } else {
                stopExoPlayer();
            }
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 1000) {
            countdown.setText(String.valueOf(2));
            soundsbutton.setVisibility(VISIBLE);
            if(sounds && three_secs) {
                initExoPlayer(beep);
                three_secs = false;
            } else {
                stopExoPlayer();
            }
        }
    }

    private void stopExoPlayer(){
        try{
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        } catch(Exception ignored){}
    }

    private void five_second_delay() {
        if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 5000){
            go_in();
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 4000) {
            countdown.setText(String.valueOf(1));
            if(sounds && two_secs) {
                initExoPlayer(beep);
                two_secs = false;
            } else {
                stopExoPlayer();
            }
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 3000) {
            countdown.setText(String.valueOf(2));
            if(sounds && three_secs) {
                initExoPlayer(beep);
                three_secs = false;
            } else {
                stopExoPlayer();
            }
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 2000) {
            countdown.setText(String.valueOf(3));
            if(sounds && four_secs) {
                initExoPlayer(beep);
                four_secs = false;
            } else {
                stopExoPlayer();
            }
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 1000) {
            countdown.setText(String.valueOf(4));
            soundsbutton.setVisibility(VISIBLE);
            if(sounds && five_secs) {
                initExoPlayer(beep);
                five_secs = false;
            } else {
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
            nightmode.setVisibility(VISIBLE);
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
        tahia.setVisibility(GONE);
        one.setEnabled(true);
        two.setEnabled(true);
        three.setEnabled(true);
        four.setEnabled(true);
        started = false;
        blackground.setVisibility(VISIBLE);
        soundsbutton.setEnabled(true);
        soundsbuttonbackground.setEnabled(true);
        soundsbutton.setVisibility(View.VISIBLE);
        settings.setEnabled(true);
        settingsbuttonbackground.setEnabled(true);
        settings.setVisibility(VISIBLE);
        blackoutbutton.setVisibility(GONE);
        if(!two_secs) {
            five_secs = true;
            four_secs = true;
            three_secs = true;
            two_secs = true;
        }
        if(blackground.getDrawingCacheBackgroundColor()==resources.getColor(R.color.black))
            blackground.setBackground(null);
        blackoutbutton.setVisibility(GONE);
        tahia.setVisibility(GONE);
        showNavigationBar();
        countdown.setText(String.valueOf(5));
        slattitle.setVisibility(VISIBLE);
        sajda_pre.setVisibility(VISIBLE);
        settings.setVisibility(VISIBLE);

        doitposse = true;
        animation_of_initial_five_seconds_started = false;
        five_second_before_actually_starting_was_finished = false;
        if(language.equals("en"))
            start.setText(resources.getString(R.string.start));
        else
            start.setText(resources.getString(R.string.start_arabe));
        original_light_saved = false;
        sajda_done = false;
        num_of_raka3at = 0;
        num_of_sajadat = 0;
        raka3at.setText(String.valueOf(num_of_raka3at));
        sajda.setText(String.valueOf(num_of_sajadat));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement)
            start.setBackground(resources.getDrawable(R.drawable.darkbuttons2));
        else
            start.setBackground(resources.getDrawable(R.drawable.buttons));

        receiveandy = "main";
    }


    private void show_tam_page(){
        blackground.setVisibility(GONE);
        donecover.setVisibility(VISIBLE);

        sql("force3");

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

        check_if_prayed_exists_in_sql();
        if(found_prayed_history_in_sql)
            SQLSharing.mydb.updatePrayed(todaycomparable, temper, verified, athome);
        else
            SQLSharing.mydb.insertPrayed(todaycomparable, temper, verified, athome);

    }

    private void check_if_prayed_exists_in_sql() {
        if(SQLSharing.mycursor.getCount()<=0)
            found_prayed_history_in_sql = false;
        else {
            while(SQLSharing.mycursor.moveToNext()) {
                if (todaycomparable.equals(SQLSharing.mycursor.getString(1))){
                    found_prayed_history_in_sql = true;
                    break;
                }
            }
        }
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
        SlatCustomDialogClass cdddd=new SlatCustomDialogClass(this, false, language);
        cdddd.show();
    }


    public void nightmodeClicked(View view) {
        if(blackground.getDrawingCacheBackgroundColor()==resources.getColor(R.color.black))
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
        backarrowbackground.setBackground(resources.getDrawable(R.drawable.lightbackback));
        howtousebackground.setBackground(resources.getDrawable(R.drawable.lightstatsback));

        it_is_nightmode_since_lightmode_shines_and_ruins_measurement = false;
        slattitle.setTextColor(resources.getColor(R.color.typicallightbuttoncolors));
        countdownbackground.setBackground(resources.getDrawable(R.drawable.simpelbackground));
        full.setBackground(resources.getDrawable(R.drawable.simpelbackground));
        sajda_pre.setTextColor(resources.getColor(R.color.typicallightbuttoncolors));
        coverer.setBackground(resources.getDrawable(R.drawable.simpelbackground));

        donecover.setBackground(resources.getDrawable(R.drawable.simpelbackground));
        donebutton.setBackground(resources.getDrawable(R.drawable.buttons));

        one.setBackground(resources.getDrawable(R.drawable.buttons));
        two.setBackground(resources.getDrawable(R.drawable.buttons));
        three.setBackground(resources.getDrawable(R.drawable.buttons));
        four.setBackground(resources.getDrawable(R.drawable.buttons));

        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDateslat();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("no", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

        if(five_second_before_actually_starting_was_finished)
            start.setBackground(null);
        else
            start.setBackground(resources.getDrawable(R.drawable.buttons));
    }


    private void update_dimmness(){
        if (scheme_light_mode == 0) {
            raka3at.setTextColor(resources.getColor(R.color.dimmest));
            sajda.setTextColor(resources.getColor(R.color.dimmest));
            countdown.setTextColor(resources.getColor(R.color.dimmest));
            tahia.setTextColor(resources.getColor(R.color.dimmest));
            if(startclicked)
                start.setTextColor(resources.getColor(R.color.dimmest));
        } else if (scheme_light_mode == 1) {
            raka3at.setTextColor(resources.getColor(R.color.dimmer));
            sajda.setTextColor(resources.getColor(R.color.dimmer));
            countdown.setTextColor(resources.getColor(R.color.dimmer));
            tahia.setTextColor(resources.getColor(R.color.dimmer));
            if(startclicked)
                start.setTextColor(resources.getColor(R.color.dimmer));
        } else {
            raka3at.setTextColor(resources.getColor(R.color.dimm));
            sajda.setTextColor(resources.getColor(R.color.dimm));
            countdown.setTextColor(resources.getColor(R.color.dimm));
            tahia.setTextColor(resources.getColor(R.color.dimm));
            if(startclicked)
                start.setTextColor(resources.getColor(R.color.dimm));
        }
    }

    private void dimm_start_button_text(){
        if (scheme_light_mode == 0)
            start.setTextColor(resources.getColor(R.color.dimmest));
        else if (scheme_light_mode == 1)
            start.setTextColor(resources.getColor(R.color.dimmer));
        else
            start.setTextColor(resources.getColor(R.color.dimm));
    }


    private void dark_mode(){
        update_darkness();

        backarrowbackground.setBackground(resources.getDrawable(R.drawable.backback));
        howtousebackground.setBackground(resources.getDrawable(R.drawable.statsback));

        it_is_nightmode_since_lightmode_shines_and_ruins_measurement = true;
        slattitle.setTextColor(resources.getColor(R.color.white));
        full.setBackground(resources.getDrawable(R.drawable.forcefull));
        countdownbackground.setBackground(resources.getDrawable(R.drawable.forcefull));
        sajda_pre.setTextColor(resources.getColor(R.color.white));
        coverer.setBackground(resources.getDrawable(R.drawable.forcefull));

        donecover.setBackground(resources.getDrawable(R.drawable.forcefull));
        donebutton.setBackground(resources.getDrawable(R.drawable.darkbuttons2));

        one.setBackground(resources.getDrawable(R.drawable.darkbuttons2));
        two.setBackground(resources.getDrawable(R.drawable.darkbuttons2));
        three.setBackground(resources.getDrawable(R.drawable.darkbuttons2));
        four.setBackground(resources.getDrawable(R.drawable.darkbuttons2));

        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDateslat();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("yes", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

        if(five_second_before_actually_starting_was_finished)
            start.setBackground(null);
        else
            start.setBackground(resources.getDrawable(R.drawable.darkbuttons2));
    }


    private void update_darkness() {
        if(scheme==0) {
            sajda.setTextColor(resources.getColor(R.color.darkest));
            raka3at.setTextColor(resources.getColor(R.color.darkest));
            countdown.setTextColor(resources.getColor(R.color.darkest));
            tahia.setTextColor(resources.getColor(R.color.darkest));
            if(startclicked)
                start.setTextColor(resources.getColor(R.color.darkest));
        } else if(scheme==1) {
            sajda.setTextColor(resources.getColor(R.color.dark));
            raka3at.setTextColor(resources.getColor(R.color.dark));
            countdown.setTextColor(resources.getColor(R.color.dark));
            tahia.setTextColor(resources.getColor(R.color.dark));
            if(startclicked)
                start.setTextColor(resources.getColor(R.color.dark));
        } else {
            sajda.setTextColor(resources.getColor(R.color.white));
            raka3at.setTextColor(resources.getColor(R.color.white));
            countdown.setTextColor(resources.getColor(R.color.white));
            tahia.setTextColor(resources.getColor(R.color.white));
            if(startclicked)
                start.setTextColor(resources.getColor(R.color.white));
        }
    }


    private void darken_start_button() {
        if(scheme==0)
            start.setTextColor(resources.getColor(R.color.darkest));
        else if(scheme==1)
            start.setTextColor(resources.getColor(R.color.dark));
        else
            start.setTextColor(resources.getColor(R.color.white));
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
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
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
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
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
            blackground.setBackgroundColor(resources.getColor(R.color.black));
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
            try {
                Glide.with(this).load(R.drawable.soundsoff).into(soundsbutton);
            } catch (Exception ignored) {
                soundsbutton.setImageDrawable(resources.getDrawable(R.drawable.soundsoff));
            }
            sounds = false;
            SQLSharing.mydb.updateData("0", "6");
        } else {
            try {
                Glide.with(this).load(R.drawable.soundson).into(soundsbutton);
            } catch (Exception ignored) {
                soundsbutton.setImageDrawable(resources.getDrawable(R.drawable.soundson));
            }
            sounds = true;
            SQLSharing.mydb.updateData("1", "6");
        }
    }


}
