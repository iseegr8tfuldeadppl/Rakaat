package com.krimzon.scuffedbots.raka3at;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.krimzon.scuffedbots.raka3at.dialogs.SlatCustomDialogClass;
import com.krimzon.scuffedbots.raka3at.dialogs.slat_settings;
import java.util.Locale;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.animation.AnimationUtils.*;

public class slat extends AppCompatActivity implements SensorEventListener, slat_settings.BottomSheetListener {
//https://www.youtube.com/watch?v=dfTeS41BbbI

    @SuppressLint("StaticFieldLeak")
    public static TextView raka3at;
    @SuppressLint("StaticFieldLeak")
    public static TextView sajda;

    private ImageView soundsbutton;
    private boolean sounds = true;
    private boolean started = false;
    private boolean start_was_just_clicked = false;
    protected int soundstempint = 1;
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
    protected double sajda_darkness_percentage = 0.50; // default: percentage of light to accept it as a sajda
    protected double percentage_of_light_to_count_as_a_c_bon_rak3a = 0.65; // default
    private long futuretime = 0;
    private SimpleExoPlayer simpleExoPlayer;
    protected int sajda_length_of_time = 850; // default : ask user for twice this time damn takes twice to be done problem here is it takes longer than the actual default cuz of it depending on sensor to be reached?
    private long futuretime2 = 0;
    protected int delay_between_sajadat = 600; // default 600
    private boolean between_sajadat_delay_started = false;
    private long futuretime3 = 0;
    protected int delay_between_raka3at = 5000; // default 5000
    protected int minimum_light = 6; // default 6
    private boolean doitposse = true;
    private boolean between_raka3at_delay_started = false;
    private long animation_tracker_of_initial_five_seconds;
    private boolean animation_of_initial_five_seconds_started = false;
    private LinearLayout donecover;
    private Button donebutton;
    private TextView donetitle;
    private ImageView nightmode;
    public static boolean it_is_nightmode_since_lightmode_shines_and_ruins_measurement = true;
    private LinearLayout countdownbackground;
    private NoPaddingTextView countdown;
    private int visibleSystemUiVisibility;
    protected String ID;
    public static int scheme = 3; // 0:darkest 1:dark 2:white
    public static int scheme_light_mode = 0; // 0:darkest 1:dark 2:white
    protected Resources resources;
    private int darkbackgroundcolor;
    public static int white;
    public static int dark;
    public static int darkest;
    private Drawable darkbuttons;
    private int black;
    private Drawable darkbuttons2;
    private Drawable darkbuttons4, darkbuttons5, darkbuttons6, darkbuttons7;
    private Drawable buttons4, buttons5, buttons6, buttons7;
    private Drawable buttons;
    private Drawable buttons2;
    public static Drawable simpelbackground;
    public static Drawable darksimpelbackground;
    private int dimm;
    private int dimmer;
    private int dimmest;
    public static Typeface arabic_font;
    public static Typeface english_font;
    protected String is_it_dark_mode_in_sql;
    protected slat_settings bottomsheet;
    private int typicallightbuttoncolors;
    private boolean blackout = false;
    public static int delaybeforecounting = 5;
    private boolean five_secs = true, four_secs = true, three_secs = true, two_secs = true;
    private Drawable soundson, soundsoff;
    private LinearLayout blackoutbuttonbackground, nightmodebuttonbackground, soundsbuttonbackground, settingsbuttonbackground;
    private Animation slide_in_from_right;
    private Animation slide_out_from_right;
    private Animation slide_in_from_right2;
    private Animation slide_out_from_right2;
    private Animation slide_in_from_right3;
    private Animation slide_out_from_right3;
    private Animation slide_in_from_right4;
    private Animation slide_out_from_right4;

    private String rakaat, tahiah, sajadat, starth;
    private String back, done, back2;
    private String oneh, twoh, threeh, fourh, stop;

    private String start_arabe, stop_arabe;

    public static String language = "ar";

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

        if (light == null)
            exit();

        sql_work();
        work_on_language();
        work_on_text_brightness(); // must be after loading it_is_nightmode_since_lightmode_shines_and_ruins_measurement from SQL
        navigation_bar();
        check_if_sent_from_force();
    }

    protected String receiveandy = "";
    protected String todaycomparable = "";
    protected int prayer = 0;
    protected String prayed = "00000";
    protected Intent sender;
    private void check_if_sent_from_force() {
        receiveandy = getIntent().getStringExtra("sender");
        if(receiveandy!=null) {
            if (receiveandy.equals("force")) {
                sender = getIntent();
                prayer = Integer.valueOf(sender.getStringExtra("prayer"));
                todaycomparable = sender.getStringExtra("todaycomparable");
                prayed = sender.getStringExtra("prayed");
                forceAndy();
            }
        }
    }


    private void forceAndy() {
        click_on_start();
    }


    private void work_on_language() {
        getStrings();
        if(language.equals("en"))
            english();
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


    private void getStrings(){
         if(language.equals("en")) {
            rakaat = getString(R.string.rakaat);
            tahiah = getString(R.string.tahia);
            sajadat = getString(R.string.sajadat);
            starth = getString(R.string.start);
            done = getString(R.string.done);
            back2 = getString(R.string.back2);
            oneh = getString(R.string.one);
            twoh = getString(R.string.two);
            threeh = getString(R.string.three);
            fourh = getString(R.string.four);
        }
         start_arabe = getString(R.string.start_arabe);
         stop_arabe = getString(R.string.stop_arabe);
         stop = getString(R.string.stop);
    }


    private void english(){
        four.setText(fourh);
        three.setText(threeh);
        two.setText(twoh);
        one.setText(oneh);
        slattitle.setText(rakaat);
        tahia.setText(tahiah);
        sajda_pre.setText(sajadat);
        start.setText(starth);
        donetitle.setText(done);
        donebutton.setText(back2);
    }


    private void sql_work() {
        sql("slat");

        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        is_it_dark_mode_in_sql = SQLSharing.mycursor.getString(1);
        SQLSharing.mycursor.moveToNext();
        scheme = Integer.parseInt(SQLSharing.mycursor.getString(1));
        SQLSharing.mycursor.moveToNext();
        scheme_light_mode = Integer.parseInt(SQLSharing.mycursor.getString(1));
        SQLSharing.mycursor.moveToNext();
        delaybeforecounting = Integer.parseInt(SQLSharing.mycursor.getString(1));
        SQLSharing.mycursor.moveToNext();
        soundstempint = Integer.parseInt(SQLSharing.mycursor.getString(1));
        SQLSharing.mycursor.moveToNext();
        language = SQLSharing.mycursor.getString(1);

        if(soundstempint==1)
            sounds = true;
        else if(soundstempint==0)
            sounds = false;
        if(!sounds)
            soundsbutton.setImageDrawable(soundsoff);

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

        darkbackgroundcolor = resources.getColor(R.color.darkbackgroundcolor);
        typicallightbuttoncolors = resources.getColor(R.color.typicallightbuttoncolors);
        white = resources.getColor(R.color.white);
        dark = resources.getColor(R.color.dark);
        darkest = resources.getColor(R.color.darkest);
        black = resources.getColor(R.color.black);
        darkbuttons = resources.getDrawable(R.drawable.darkbuttons2);
        darkbuttons2 = resources.getDrawable(R.drawable.darkbuttons2);
        darkbuttons4 = resources.getDrawable(R.drawable.darkbuttons2);
        darkbuttons5 = resources.getDrawable(R.drawable.darkbuttons2);
        darkbuttons6 = resources.getDrawable(R.drawable.darkbuttons2);
        darkbuttons7 = resources.getDrawable(R.drawable.darkbuttons2);
        buttons4 = resources.getDrawable(R.drawable.buttons);
        buttons5 = resources.getDrawable(R.drawable.buttons);
        buttons6 = resources.getDrawable(R.drawable.buttons);
        buttons7 = resources.getDrawable(R.drawable.buttons);
        buttons = resources.getDrawable(R.drawable.buttons);
        buttons2 = resources.getDrawable(R.drawable.buttons);
        soundsoff = resources.getDrawable(R.drawable.soundsoff);
        soundson = resources.getDrawable(R.drawable.soundson);
        simpelbackground = resources.getDrawable(R.drawable.simpelbackground);
        darksimpelbackground = resources.getDrawable(R.drawable.forcefull);
        dimm = resources.getColor(R.color.dimm);
        dimmer = resources.getColor(R.color.dimmer);
        dimmest = resources.getColor(R.color.dimmest);

        soundson = resources.getDrawable(R.drawable.soundson);
    }


    RelativeLayout backarrowbackground;
    RelativeLayout howtousebackground;
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
        donetitle = findViewById(R.id.donetitle);
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

        arabic_font = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");
        english_font = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");

        if(language.equals("ar")) {
            slattitle.setTypeface(arabic_font);
            start.setTypeface(arabic_font);
            sajda_pre.setTypeface(arabic_font);
        } else if(language.equals("en")) {
            one.setTypeface(english_font);
            two.setTypeface(english_font);
            three.setTypeface(english_font);
            four.setTypeface(english_font);
            slattitle.setTypeface(english_font);
            start.setTypeface(english_font);
            sajda_pre.setTypeface(english_font);
        }

        //https://www.youtube.com/watch?v=ZL6s8TyHNOc
        //https://www.youtube.com/watch?v=ZL6s8TyHNOc
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sajda = findViewById(R.id.sajda);
        raka3at = findViewById(R.id.raka3at);
        raka3at.setText(String.valueOf(num_of_raka3at));
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }


    @Override
    public void onBackPressed() {
        if(receiveandy!=null) {
            if (receiveandy.equals("force"))
                back_to_force("no");
            else {
                if(entered_praying_process)
                    reset();
                else
                    exit();
            }
        } else {
            if(entered_praying_process)
                reset();
            else
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
            this.getWindow().setStatusBarColor(darkbackgroundcolor);
    }


    boolean entered_praying_process = false;
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
        backarrowbackground.setVisibility(View.INVISIBLE);
        howtousebackground.setVisibility(View.INVISIBLE);

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
                                else if (prayer == 1)
                                    limit = 4;
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
            start.setText(stop);
        else
            start.setText(stop_arabe);
        blackoutbutton.setVisibility(VISIBLE);
        sajda_pre.setVisibility(View.INVISIBLE);
        settings.setVisibility(View.INVISIBLE);
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
                            nightmode.setVisibility(View.INVISIBLE);
                            blackoutbutton.setVisibility(GONE);
                            settings.setVisibility(View.INVISIBLE);
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
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        if(five_second_before_actually_starting_was_finished) {
            hideNavigationBar();
        } else
            showNavigationBar();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            /*TextView lmao = findViewById(R.id.lmao);
            lmao.setText(String.valueOf(event.values[0]));*/

            if (start_was_just_clicked) { start_was_just_clicked = false;
                if (event.values[0] < minimum_light) {
                    if(receiveandy!=null) {
                        if (receiveandy.equals("force"))
                            back_to_force("yes");
                    }
                    if(language.equals("en"))
                        Snackbar.make(full, getString(R.string.low_light), BaseTransientBottomBar.LENGTH_LONG).show();
                    else
                        Snackbar.make(full, getString(R.string.low_light_arabe), BaseTransientBottomBar.LENGTH_LONG).show();
                } else {
                    started = true;
                    hideNavigationBar();
                    show_raka3at_selections();
                    if (startclicked) work2(event);
                }
            }

            if(started)
                if (startclicked) work2(event);

        }
    }


    public void startClicked(View view){ click_on_start(); }


    private void click_on_start() {
        if(receiveandy!=null) {
            if (!receiveandy.equals("force2")) {
                if (!startclicked)
                    start_was_just_clicked = true;
                else
                    reset();
            } else
                back_to_force("no");
        } else
            back_to_force("no");
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
            } else if (num_of_sajadat == 0 && num_of_raka3at >= 1 && between_raka3at_delay_started) {
                if (System.currentTimeMillis() >= delay_between_raka3at + futuretime3)
                    doitposse = true;
            }
        }
    }


    private void save_original_light(SensorEvent event) {
        if (!original_light_saved) {
            full_light = event.values[0];
            original_light_saved = true;
            return;
        }
    }


    private void warn_tahia() {
        if(!tahia_fading_started){
            if(( (num_of_raka3at==2 && num_of_sajadat==0)
                    || (num_of_raka3at==4 && num_of_sajadat==0)
                    || (limit==3 && num_of_raka3at==3 && num_of_sajadat==0))
                    && tahia.getVisibility()== View.INVISIBLE){
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
                    tahia.setVisibility(View.INVISIBLE);
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
            }
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 1000) {
            countdown.setText(String.valueOf(2));
            soundsbutton.setVisibility(VISIBLE);
            if(sounds && three_secs) {
                initExoPlayer(beep);
                three_secs = false;
            }
        }
    }


    private void five_second_delay() {
        if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 5000){
            go_in();
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 4000) {
            countdown.setText(String.valueOf(1));
            if(sounds && two_secs) {
                initExoPlayer(beep);
                two_secs = false;
            }
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 3000) {
            countdown.setText(String.valueOf(2));
            if(sounds && three_secs) {
                initExoPlayer(beep);
                three_secs = false;
            }
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 2000) {
            countdown.setText(String.valueOf(3));
            if(sounds && four_secs) {
                initExoPlayer(beep);
                four_secs = false;
            }
        } else if(System.currentTimeMillis() > animation_tracker_of_initial_five_seconds + 1000) {
            countdown.setText(String.valueOf(4));
            soundsbutton.setVisibility(VISIBLE);
            if(sounds && five_secs) {
                initExoPlayer(beep);
                five_secs = false;
            }
        }
    }


    private void go_in() {
        five_second_before_actually_starting_was_finished = true;
        countdownbackground.setVisibility(GONE);
        blackground.setVisibility(VISIBLE);
        blackoutbutton.setVisibility(VISIBLE);
        settings.setVisibility(View.INVISIBLE);
        nightmode.setEnabled(true);
        nightmodebuttonbackground.setEnabled(true);
        blackoutbutton.setEnabled(true);
        blackoutbuttonbackground.setEnabled(true);
        nightmode.setVisibility(VISIBLE);
        soundsbutton.setEnabled(false);
        soundsbuttonbackground.setEnabled(false);
        soundsbutton.setVisibility(View.INVISIBLE);
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
        if(blackground.getDrawingCacheBackgroundColor()==black)
            blackground.setBackground(null);
        blackoutbutton.setVisibility(GONE);
        tahia.setVisibility(GONE);
        showNavigationBar();
        countdown.setText(String.valueOf(5));
        slattitle.setVisibility(VISIBLE);
        sajda_pre.setVisibility(VISIBLE);
        settings.setVisibility(VISIBLE);

        animation_of_initial_five_seconds_started = false;
        five_second_before_actually_starting_was_finished = false;
        if(language.equals("en"))
            start.setText(starth);
        else
            start.setText(start_arabe);
        startclicked = false;
        original_light_saved = false;
        sajda_done = false;
        num_of_raka3at = 0;
        num_of_sajadat = 0;
        raka3at.setText(String.valueOf(num_of_raka3at));
        sajda.setText(String.valueOf(num_of_sajadat));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement)
            start.setBackground(darkbuttons2);
        else
            start.setBackground(buttons2);
    }


    protected String temper = "00000";
    protected StringBuilder strinkbilder;
    private void show_tam_page(){
        blackground.setVisibility(GONE);
        donecover.setVisibility(VISIBLE);

        sql("force2");
        strinkbilder = new StringBuilder(prayed);
        strinkbilder.setCharAt(prayer, '1');
        temper = String.valueOf(strinkbilder);
        check_if_prayed_exists_in_sql();
        if(found_prayed_history_in_sql)
            SQLSharing.mydb.updatePrayed(todaycomparable,temper);
        else
            SQLSharing.mydb.insertPrayed(todaycomparable, temper);

    }

    protected boolean found_prayed_history_in_sql = false;
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
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
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
        SlatCustomDialogClass cdddd=new SlatCustomDialogClass(this, false);
        cdddd.show();
    }


    public void nightmodeClicked(View view) {
        if(blackground.getDrawingCacheBackgroundColor()==black)
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


    boolean onlyonce2 = true;
    Drawable lightbackback, lightstatsback;
    private void light_mode(){
        update_dimmness();


        // top buttons backgrounds
        if(onlyonce2) {
            onlyonce2 = false;
            lightbackback = resources.getDrawable(R.drawable.lightbackback);
            lightstatsback = resources.getDrawable(R.drawable.lightstatsback);
        }
        backarrowbackground.setBackground(lightbackback);
        howtousebackground.setBackground(lightstatsback);

        it_is_nightmode_since_lightmode_shines_and_ruins_measurement = false;
        slattitle.setTextColor(typicallightbuttoncolors);
        countdownbackground.setBackground(simpelbackground);
        full.setBackground(simpelbackground);
        sajda_pre.setTextColor(typicallightbuttoncolors);
        coverer.setBackground(simpelbackground);

        donecover.setBackground(simpelbackground);
        donebutton.setBackground(buttons);

        one.setBackground(buttons4);
        two.setBackground(buttons5);
        three.setBackground(buttons6);
        four.setBackground(buttons7);

        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("no", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

        if(five_second_before_actually_starting_was_finished)
            start.setBackground(null);
        else
            start.setBackground(buttons2);
    }


    private void update_dimmness(){
        if (scheme_light_mode == 0) {
            raka3at.setTextColor(dimmest);
            sajda.setTextColor(dimmest);
            donetitle.setTextColor(dimmest);
            countdown.setTextColor(dimmest);
            tahia.setTextColor(dimmest);
        } else if (scheme_light_mode == 1) {
            raka3at.setTextColor(dimmer);
            sajda.setTextColor(dimmer);
            donetitle.setTextColor(dimmer);
            countdown.setTextColor(dimmer);
            tahia.setTextColor(dimmer);
        } else {
            raka3at.setTextColor(dimm);
            sajda.setTextColor(dimm);
            donetitle.setTextColor(dimm);
            countdown.setTextColor(dimm);
            tahia.setTextColor(dimm);
        }
    }

    private void dimm_start_button_text(){
        if (scheme_light_mode == 0)
            start.setTextColor(dimmest);
        else if (scheme_light_mode == 1)
            start.setTextColor(dimmer);
        else
            start.setTextColor(dimm);
    }


    boolean onlyonce = true;
    Drawable statsback;
    Drawable backback;
    private void dark_mode(){
        update_darkness();


        // top buttons backgrounds
        if(onlyonce) {
            onlyonce = false;
            backback = resources.getDrawable(R.drawable.backback);
            statsback = resources.getDrawable(R.drawable.statsback);
        }
        backarrowbackground.setBackground(backback);
        howtousebackground.setBackground(statsback);

        it_is_nightmode_since_lightmode_shines_and_ruins_measurement = true;
        slattitle.setTextColor(white);
        full.setBackground(darksimpelbackground);
        countdownbackground.setBackground(darksimpelbackground);
        sajda_pre.setTextColor(white);
        coverer.setBackground(darksimpelbackground);

        donecover.setBackground(darksimpelbackground);
        donebutton.setBackground(darkbuttons);

        one.setBackground(darkbuttons4);
        two.setBackground(darkbuttons5);
        three.setBackground(darkbuttons6);
        four.setBackground(darkbuttons7);

        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("yes", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

        if(five_second_before_actually_starting_was_finished)
            start.setBackground(null);
        else
            start.setBackground(darkbuttons2);
    }


    private void update_darkness() {
        if(scheme==0) {
            sajda.setTextColor(darkest);
            raka3at.setTextColor(darkest);
            countdown.setTextColor(darkest);
            donetitle.setTextColor(darkest);
            tahia.setTextColor(darkest);
        } else if(scheme==1) {
            sajda.setTextColor(dark);
            raka3at.setTextColor(dark);
            countdown.setTextColor(dark);
            donetitle.setTextColor(dark);
            tahia.setTextColor(dark);
        } else {
            sajda.setTextColor(white);
            raka3at.setTextColor(white);
            countdown.setTextColor(white);
            donetitle.setTextColor(white);
            tahia.setTextColor(white);
        }
    }


    private void darken_start_button() {
        if(scheme==0)
            start.setTextColor(darkest);
        else if(scheme==1)
            start.setTextColor(dark);
        else
            start.setTextColor(white);
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


    protected Intent forceIntent;
    public void tamClicked(View view) {
        if(receiveandy!=null) {
            if (receiveandy.equals("force"))
                back_to_force("no");
            else {
                donecover.setVisibility(GONE);
                reset();
            }
        } else {
            donecover.setVisibility(GONE);
            reset();
        }
    }

    private void back_to_force(String input) {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        forceIntent = new Intent(this, force.class);
        forceIntent.putExtra("light_alert", input); // yes for true
        startActivity(forceIntent);
        finish();
    }


    private void print(Object lol) {
        Toast.makeText(getApplicationContext(), String.valueOf(lol), Toast.LENGTH_SHORT).show();
    }


    public void settingsClicked(View view) {
        if(!blackout) {
            bottomsheet = new slat_settings();
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
            blackground.setBackgroundColor(black);
            blackout = true;
            nightmode.setVisibility(View.INVISIBLE);
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
            soundsbutton.setImageDrawable(soundsoff);
            sounds = false;
            SQLSharing.mydb.updateData("0", "6");
        } else {
            soundsbutton.setImageDrawable(soundson);
            sounds = true;
            SQLSharing.mydb.updateData("1", "6");
        }
    }


}
