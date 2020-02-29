package com.krimzon.scuffedbots.raka3at;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.bumptech.glide.Glide;
import com.github.mrengineer13.snackbar.SnackBar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.background.ProcessMainClass;
import com.krimzon.scuffedbots.raka3at.background.restarter.RestartServiceBroadcastReceiver;
import com.krimzon.scuffedbots.raka3at.dialogs.HomeOrMosque;
import com.krimzon.scuffedbots.raka3at.dialogs.Statistictictictictic;
import com.krimzon.scuffedbots.raka3at.dialogs.protected_apps_request;
import net.time4j.PlainDate;
import net.time4j.calendar.HijriCalendar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.animation.AnimationUtils.loadAnimation;

public class force extends AppCompatActivity  {

    private static final int REQUEST_CODE = 1000;
    private Coordinates coordinates;
    private boolean new_coordinates = false;
    private double longitude;
    private double latitude;
    private CalculationParameters params;
    private boolean an_alert_to_turn_location_on_was_displayed = false;
    private String todaycomparable;
    private DateComponents date;
    private int hijri_month = 0, hijri_year = 0, hijri_day = 0;
    private int miladi_month = 0;
    private TextView fajrtitle, risetitle, dohrtitle, asrtitle, maghrebtitle, ishatitle;
    private TextView fajrtime, risetime, dhuhrtime, asrtime, maghribtime, ishatime;
    private List<TextView> prayerdisplayviews, prayerdisplayviews2;
    private String fajr, rise, dhuhr, asr, maghrib, isha;
    private List<Integer> prayers;
    private FusedLocationProviderClient mFusedLocationClient;
    private RelativeLayout full;
    private TextView title, citydisplay, daterr;
    private String language = "";
    private String hijri = "";
    private Resources resources;
    private boolean darkmode = true;
    private TextView slider;
    private RelativeLayout doublearrowsbackground;
    private ImageView doublearrows;
    private List <ImageView> checkmarks;
    private List<String> prayernames, prayernames_arabe;
    private LinearLayout fajrbackground, risebackground, dhuhrbackground, asrbackground, maghribbackground, ishabackground;
    private RelativeLayout yesterdayarrowbackground, tommorowarrowbackground;
    private LinearLayout rightsideelementsbackground;
    private ImageView lmfaoimage;
    private boolean onlyonceu = true, onlyonce = true;
    private String ID;
    private boolean going_left = false, going_right = false, changing_day = false;
    private GregorianCalendar gc;
    private Date CurrentDisplayedDay;
    private int day, year, month = 0;
    private String[] todaysplittemparray;
    private boolean all_white = false, fill_all = false;
    private String athome = "00000";
    private int next_adan = -1, temp_next_adan = -1;
    private boolean end_of_day = false, it_is_today = true, new_adan = false;
    private String verified = "";
    private List<TextView> praybuttons;
    private boolean allow_pray = false;
    private boolean friday = false;
    private String prayed = "";
    private boolean one_of_previous_is_zero = false;
    private String datin = "";
    private String tfajr, trise, tdhuhr, tasr, tmaghrib, tisha;
    private String[] temptoday;
    private boolean only_once = true, only_once2 = true;
    private Animation to_right11, to_right12, to_right1, to_right2, to_right3, to_right4, to_right5, to_right6, to_right7, to_right8, to_right9, to_right10;
    private Animation from_left11, from_left12, from_left1, from_left2, from_left3, from_left4, from_left5, from_left6, from_left7, from_left8, from_left9, from_left10;
    private Animation toleft1, toleft2, toleft3, toleft4, toleft5, toleft6, toleft7, toleft8, toleft9, toleft10, toleft11, toleft12;
    private Animation fromright1, fromright2, fromright3, fromright4, fromright5, fromright6, fromright7, fromright8, fromright9, fromright10, fromright11, fromright12;
    private int temp_negatifise = 1000, temp_positifise = 1000, negatifise = 1000, positifise = 1000;
    private Thread mythread;
    private int current_displayed_next_adan = -1, rightnowcomparable = 0;
    private boolean running = true, initialdelayoncebrk = true, still_scoping_on_previous_adan = false, can_find_in = true;
    private LinearLayout leftsideelementsbackground;


    private Handler displaycity = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            citydisplay.setText(city);
            return true;}});

    private Handler handler3 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { if(slider!=null) { if(positifise!=0) slider.setText("- " + positifise);else begonethot(); }return true;}});
    private Handler calluse = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { use(longitude, latitude, new_coordinates, new Date()); return true;}});
    private Handler handler7 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { find_slider(next_adan, false);return true; }});
    private Handler handler6 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { for(int i = 0; i<5; i++) { find_slider(i, true);slider.setVisibility(View.GONE); }find_slider(next_adan, false); return true;}});
    private Handler handler5 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { switch_to_next_adan();return true; }
    });
    private Handler handler4 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { if(slider!=null){ if(negatifise!=0) slider.setText("+ " + negatifise);begonethot();  } return true;}});
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { if(it_is_today) animatenextadan(); return true; }});
    private Handler color_pray_buttonshandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { color_pray_buttons(); return true; }});

    private Handler checkonfajr = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { if(positifise < SQLSharing.minute_limit_to_display_positifise){

                for(int i=0; i<5; i++) {
                    find_slider(i, true);
                    slider.setVisibility(View.GONE);
                }

                find_slider(next_adan, false);
                if(still_scoping_on_previous_adan)
                    handler4.sendEmptyMessage(0);
                else
                    handler3.sendEmptyMessage(0);
                if(can_find_in)
                    fade_slider_in();

                clean_titles_and_times();
                if(next_adan!=-1) {
                    prayerdisplayviews.get(next_adan).setTextColor(Color.GREEN);
                    prayerdisplayviews2.get(next_adan).setTextColor(Color.GREEN);
                }

                color_pray_buttons();

            }return true; }});


    private boolean praybuttonsdone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force);

        // override system locale
        Configuration cfg = new Configuration();
        cfg.locale = new Locale(getResources().getString(R.string.en));
        this.getResources().updateConfiguration(cfg, null);

        params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        params = CalculationMethod.EGYPTIAN.getParameters();
        params.madhab = Madhab.SHAFI; // SHAFI made 95% accuracy, HANAFI had 1hour different for l'3asr
        params.adjustments.fajr = SQLSharing.params_adjustments_fajr; //2
        params.fajrAngle = SQLSharing.fajrangle;
        params.ishaAngle = SQLSharing.ishaangle;
        //params.adjustments.isha = SQLSharing.params_adjustments_isha; //2
        /*String pattern = "dd-MMM-yyyy";*/
        /*SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);*/

        CurrentDisplayedDay = new Date();



        /*preparing_background_handler();*/

        variables_setup();

        fontAndy();

        sql(resources.getString(R.string.slat));

        load_data_from_slat_sql();

        if(request_protected_menu)
            protected_apps_request();

        languageshet();

        sql(resources.getString(R.string.justforce));

        location_shit(CurrentDisplayedDay);

        // TODO; check if it show up correctly and if it doesn't show up when quickpraying after it said it before ofc, kinda keeps that value
        low_light_alert();
        if_sent_from_slat_after_prayer_check_which_day_we_were_praying_and_display_that();

        //longitude = 30;latitude = 30;use(longitude, latitude, true, new Date());


        IntentFilter filter = new IntentFilter();
        filter.addAction("com.krimzon.scuffedbots.raka3at.background.iprayeditmate"); //further more
        registerReceiver(receiver, filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if(action.equals("com.krimzon.scuffedbots.raka3at.background.iprayeditmate")){
                Intent restart = new Intent(getApplicationContext(), slat.class);
                startActivity(restart);
            }
        }
    };

    private void if_sent_from_slat_after_prayer_check_which_day_we_were_praying_and_display_that() {
        try {
            String gtodaycomparable = getIntent().getStringExtra("todaycomparable");
            assert gtodaycomparable != null;
            String[] todaycomparablesplit = gtodaycomparable.split(" ");
            if(todaycomparablesplit.length==3)
                gotoday(Integer.valueOf(todaycomparablesplit[1]), get_month(todaycomparablesplit[1]), Integer.valueOf(todaycomparablesplit[2]));
        } catch(Exception e){e.printStackTrace();}
    }
    private void protected_apps_request() {
        try {
            if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                protected_apps_request request = new protected_apps_request(this, darkmode, language);
                request.show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void live_updates() {
        Runnable r=new Runnable() {@Override public void run() { try {
            while(running) {

                wait_1_second();
                if (it_is_today) {

                    calculate_rightnowcomparable();
                    if(!praybuttonsdone)
                        color_pray_buttonshandler.sendEmptyMessage(0);


                    temp_positifise = Math.round((prayers.get(next_adan) - rightnowcomparable));

                    check_next_adan();
                    // this check is to fix the glitch of changing time from 3AM to 11PM instantly, gets stuck on fajr
                    // TODO: might remove idk
                    if(temp_positifise<0) {
                        handler5.sendEmptyMessage(0);
                        temp_positifise = Math.abs(temp_positifise);
                    }

                    if (next_adan != 0)
                        temp_negatifise = Math.round(Math.abs((rightnowcomparable - prayers.get(next_adan - 1))));
                    else
                        still_scoping_on_previous_adan = false;

                    /*handlos.sendEmptyMessage(0);*/

                    // move to next adan if available
                    if (next_adan!=current_displayed_next_adan || end_of_day)
                        handler5.sendEmptyMessage(0);

                    if(next_adan==0 && current_displayed_next_adan==0)
                        checkonfajr.sendEmptyMessage(0);

                    display_neg_if_possible();


                    if ((temp_positifise != positifise || changing_day) && !still_scoping_on_previous_adan) {
                        positifise = temp_positifise;
                        handler3.sendEmptyMessage(0);
                    }

                }
            }
        } catch(Exception e){e.printStackTrace();} }};

        //anti lag
        mythread = new Thread(r); //to thread the runnable object we launched
        mythread.start();
    }

    private void switch_to_next_adan() {

        for(int i=0; i<5; i++) {
            find_slider(i, true);
            slider.setVisibility(View.GONE);
        }

        find_slider(next_adan, false);
        if(still_scoping_on_previous_adan)
            handler4.sendEmptyMessage(0);
        else
            handler3.sendEmptyMessage(0);
        if(can_find_in)
            fade_slider_in();

        clean_titles_and_times();

        if(next_adan!=-1) {
            prayerdisplayviews.get(next_adan).setTextColor(Color.GREEN);
            prayerdisplayviews2.get(next_adan).setTextColor(Color.GREEN);
        }

        color_pray_buttons();

        // don't display time till next adan if it's at end of day
        if(!end_of_day)
            InitialDelayForNextAdanAnimation();
    }

    private void clean_titles_and_times() {

        if(darkmode)
            for (int i = 0; i < 5; i++){
                prayerdisplayviews.get(i).setTextColor(resources.getColor(R.color.white));
                prayerdisplayviews2.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
            }
        else
            for (int i = 0; i < 5; i++) {
                prayerdisplayviews.get(i).setTextColor(resources.getColor(R.color.white));
                prayerdisplayviews2.get(i).setTextColor(resources.getColor(R.color.white));
            }
    }

    private void check_next_adan() {
        for(int i=0;i<prayers.size();i++){
            if(rightnowcomparable<prayers.get(0)) {
                temp_next_adan = 0;
                break;
            }
            if(rightnowcomparable>prayers.get(i)) {
                temp_next_adan = i + 1;
            }
        }


        if(temp_next_adan>=5) {
            end_of_day = true;
            temp_next_adan = 0;
        } else
            end_of_day = false;

        if(temp_next_adan != next_adan) {
            next_adan = temp_next_adan;
            new_adan = true;
        } else
            new_adan = false;

        if(changing_day)
            new_adan = true;

    }

    private void display_neg_if_possible() {
        if((temp_negatifise != negatifise || changing_day) && next_adan!=0){
            changing_day = false;
            negatifise = temp_negatifise;
            if(negatifise <= SQLSharing.minute_limit_to_display_negatifise){
                still_scoping_on_previous_adan = true;
                if (next_adan-1!=current_displayed_next_adan) {
                    handler7.sendEmptyMessage(0);
                    handler5.sendEmptyMessage(0);
                }
                handler4.sendEmptyMessage(0);
            } else
                still_scoping_on_previous_adan = false;
        }
    }

    private void calculate_rightnowcomparable() {
        Date todayos = new Date();
        if(!String.valueOf(todayos).split(" ")[2].equals(String.valueOf(CurrentDisplayedDay).split(" ")[2]) || rightnowcomparable==0 || next_adan == -1) {
            CurrentDisplayedDay = todayos;
            end_of_day = false;
            /*no_new_days = false;*/
            calluse.sendEmptyMessage(0);
        }
        String temptime = String.valueOf(todayos).split(" ")[3];
        rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 60 + Integer.valueOf(temptime.split(":")[1]);
    }

    private void wait_1_second() {
        long futuretime = System.currentTimeMillis() + 1000;

        while (System.currentTimeMillis() < futuretime && running) {
            //prevents multiple threads from crashing into each other
            synchronized (this) {
                try {
                    wait(futuretime - System.currentTimeMillis());
                } catch(Exception e){e.printStackTrace();}
            }
        }
    }

    private TextView prayedthisdaybefore;
    private void variables_setup() {

        prayedthisdaybefore = findViewById(R.id.prayedthisdaybefore);
        ImageView arrowback = findViewById(R.id.arrowback);
        ImageView statslogo = findViewById(R.id.statslogo);
        ImageView nightmodebutton = findViewById(R.id.nightmodebutton);
        ImageView arrowright = findViewById(R.id.arrowright);
        ImageView arrowleft = findViewById(R.id.arrowleft);
        ImageView settingsbutton = findViewById(R.id.settingsbutton);
        try {
            Glide.with(this).load(R.drawable.backarrowdark).into(arrowback);
        } catch (Exception ignored) {
            arrowback.setImageDrawable(resources.getDrawable(R.drawable.backarrowdark));
        }
        try {
            Glide.with(this).load(R.drawable.stats).into(statslogo);
        } catch (Exception ignored) {
            statslogo.setImageDrawable(resources.getDrawable(R.drawable.stats));
        }
        try {
            Glide.with(this).load(R.drawable.nightmodedark).into(nightmodebutton);
        } catch (Exception ignored) {
            nightmodebutton.setImageDrawable(resources.getDrawable(R.drawable.nightmodedark));
        }
        try {
            Glide.with(this).load(R.drawable.settingsforce).into(settingsbutton);
        } catch (Exception ignored) {
            nightmodebutton.setImageDrawable(resources.getDrawable(R.drawable.settingsforce));
        }
        try {
            Glide.with(this).load(R.drawable.arrowright).into(arrowright);
        } catch (Exception ignored) {
            arrowright.setImageDrawable(resources.getDrawable(R.drawable.arrowright));
        }
        try {
            Glide.with(this).load(R.drawable.arrowleft).into(arrowleft);
        } catch (Exception ignored) {
            arrowleft.setImageDrawable(resources.getDrawable(R.drawable.arrowleft));
        }

        //praybuttons
        praybuttons = new ArrayList<>();
        praybuttons.add((TextView) findViewById(R.id.prayfajr));
        praybuttons.add((TextView) findViewById(R.id.praydhuhr));
        praybuttons.add((TextView) findViewById(R.id.prayasr));
        praybuttons.add((TextView) findViewById(R.id.praymaghrib));
        praybuttons.add((TextView) findViewById(R.id.prayisha));

        // checkmarks
        checkmarks = new ArrayList<>();
        checkmarks.add((ImageView) findViewById(R.id.fajrcheckmark));
        checkmarks.add((ImageView) findViewById(R.id.dhuhrcheckmark));
        checkmarks.add((ImageView) findViewById(R.id.asrcheckmark));
        checkmarks.add((ImageView) findViewById(R.id.maghribcheckmark));
        checkmarks.add((ImageView) findViewById(R.id.ishacheckmark));

        daterr = findViewById(R.id.daterr);
        doublearrowsbackground = findViewById(R.id.doublearrowsbackground);
        fajrtitle = findViewById(R.id.fajrtitle);
        citydisplay = findViewById(R.id.city);
        title = findViewById(R.id.title);
        doublearrowsbackground = findViewById(R.id.doublearrowsbackground);
        doublearrows = findViewById(R.id.doublearrows);
        full = findViewById(R.id.full);
        dohrtitle = findViewById(R.id.dohrtitle);
        asrtitle = findViewById(R.id.asrtitle);
        maghrebtitle = findViewById(R.id.maghrebtitle);
        ishatitle = findViewById(R.id.ishatitle);
        fajrtime = findViewById(R.id.fajrtime);
        dhuhrtime = findViewById(R.id.dohrtime);
        asrtime = findViewById(R.id.asrtime);
        maghribtime = findViewById(R.id.maghrebtime);
        ishatime = findViewById(R.id.ishatime);
        risetitle = findViewById(R.id.risetitle);
        risetime = findViewById(R.id.risetime);

        prayerdisplayviews = new ArrayList<>();
        prayerdisplayviews2 = new ArrayList<>();
        prayerdisplayviews.add(fajrtime);prayerdisplayviews.add(dhuhrtime);prayerdisplayviews.add(asrtime);prayerdisplayviews.add(maghribtime);prayerdisplayviews.add(ishatime);
        prayerdisplayviews2.add(fajrtitle);prayerdisplayviews2.add(dohrtitle);prayerdisplayviews2.add(asrtitle);prayerdisplayviews2.add(maghrebtitle);prayerdisplayviews2.add(ishatitle);
        resources = getResources();
        /*doublearrowleft = resources.getDrawable(R.drawable.doublearrowleftt);
        doublearrowright = resources.getDrawable(R.drawable.doublearrowright);*/


        prayernames = new ArrayList<>();
        prayernames.add(resources.getString(R.string.fajrtitle));
        prayernames.add(resources.getString(R.string.dohrtitle));
        prayernames.add(resources.getString(R.string.asrtitle));
        prayernames.add(resources.getString(R.string.maghrebtitle));
        prayernames.add(resources.getString(R.string.ishatitle));

        prayernames_arabe = new ArrayList<>();
        prayernames_arabe.add(resources.getString(R.string.fajrtitle_arabe));
        prayernames_arabe.add(resources.getString(R.string.dohrtitle_arabe));
        prayernames_arabe.add(resources.getString(R.string.asrtitle_arabe));
        prayernames_arabe.add(resources.getString(R.string.maghrebtitle_arabe));
        prayernames_arabe.add(resources.getString(R.string.ishatitle_arabe));
    }

    private void hijri_date_setup() {
        if(miladi_month!=0) {
            String[] lel = todaycomparable.split(" ");
            String[] t = PlainDate.of(Integer.valueOf(temptoday[5]), miladi_month, Integer.valueOf(lel[1])) // TODO: fix me Integer.valueOf(lel[5]) + 2000
                    .transform(HijriCalendar.class, HijriCalendar.VARIANT_UMALQURA).toString().split("-");
            t[3] = t[3].replace("[islamic", "");
            hijri_year = Integer.valueOf(t[1]);
            hijri_month = Integer.valueOf(t[2]);
            hijri_day = Integer.valueOf(t[3]);
            convert_hijri_to_cute();
        }
    }

    private void convert_hijri_to_cute() {
        if(language.equals(resources.getString(R.string.ar))){
            hijri = "";
            hijri += hijri_day + " ";
            switch(hijri_month) {
                case 1:
                    hijri += resources.getString(R.string.muharram_arabe);
                    break;
                case 2:
                    hijri += resources.getString(R.string.safar_arabe);
                    break;
                case 3:
                    hijri += resources.getString(R.string.rabialawwal_arabe);
                    break;
                case 4:
                    hijri += resources.getString(R.string.rabialthani_arabe);
                    break;
                case 5:
                    hijri += resources.getString(R.string.jumadialawwal_arabe);
                    break;
                case 6:
                    hijri += resources.getString(R.string.jumadialthani_arabe);
                    break;
                case 7:
                    hijri += resources.getString(R.string.rajab_arabe);
                    break;
                case 8:
                    hijri += resources.getString(R.string.chaaban_arabe);
                    break;
                case 9:
                    hijri += resources.getString(R.string.ramadhan_arabe);
                    break;
                case 10:
                    hijri += resources.getString(R.string.shawwal_arabe);
                    break;
                case 11:
                    hijri += resources.getString(R.string.dhualqaada_arabe);
                    break;
                case 12:
                    hijri += resources.getString(R.string.dhualhijja_arabe);
                    break;
            }

            hijri += " " + hijri_year;
        } else {
            hijri = "";
            switch(hijri_month) {
                case 1:
                    hijri += resources.getString(R.string.muharram);
                    break;
                case 2:
                    hijri += resources.getString(R.string.safar);
                    break;
                case 3:
                    hijri += resources.getString(R.string.rabialawwal);
                    break;
                case 4:
                    hijri += resources.getString(R.string.rabialthani);
                    break;
                case 5:
                    hijri += resources.getString(R.string.jumadialawwal);
                    break;
                case 6:
                    hijri += resources.getString(R.string.jumadialthani);
                    break;
                case 7:
                    hijri += resources.getString(R.string.rajab);
                    break;
                case 8:
                    hijri += resources.getString(R.string.chaaban);
                    break;
                case 9:
                    hijri += resources.getString(R.string.ramadhan);
                    break;
                case 10:
                    hijri += resources.getString(R.string.shawwal);
                    break;
                case 11:
                    hijri += resources.getString(R.string.dhualqaada);
                    break;
                case 12:
                    hijri += resources.getString(R.string.dhualhijja);
                    break;
            }

            hijri += " " + hijri_day;
            if (hijri_day==2 || hijri_day==22)
                hijri += "nd";
            else if (hijri_day==3 || hijri_day==23)
                hijri += "rd";
            else if (hijri_day==1 || hijri_day==21)
                hijri += "st";
            else
                hijri += "th";

            hijri += " " + hijri_year;
        }
    }

    private void fontAndy() {
        Typeface arabic_typeface = Typeface.createFromAsset(getAssets(), "Tajawal-Light.ttf");
        Typeface arabic_typeface2 = Typeface.createFromAsset(getAssets(), "Tajawal-Regular.ttf");

        title.setTypeface(arabic_typeface);
        daterr.setTypeface(arabic_typeface);
        prayedthisdaybefore.setTypeface(arabic_typeface);

        for(TextView praybutton:praybuttons)
            praybutton.setTypeface(arabic_typeface);

        /*datedisplay.setTypeface(arabic_typeface);*/
        citydisplay.setTypeface(arabic_typeface);

        for(int i=0; i<prayerdisplayviews.size();i++){
            prayerdisplayviews.get(i).setTypeface(arabic_typeface2);
            prayerdisplayviews2.get(i).setTypeface(arabic_typeface2);
        }
        risetime.setTypeface(arabic_typeface2);
        risetitle.setTypeface(arabic_typeface2);

    }

    private void languageshet() {
        if(language.equals(resources.getString(R.string.en))) {
            fajrtitle.setText(resources.getString(R.string.fajrtitle));
            risetitle.setText(resources.getString(R.string.rise));
            dohrtitle.setText(resources.getString(R.string.dohrtitle));
            asrtitle.setText(resources.getString(R.string.asrtitle));
            maghrebtitle.setText(resources.getString(R.string.maghrebtitle));
            ishatitle.setText(resources.getString(R.string.ishatitle));
            for(TextView praybutton:praybuttons)
                praybutton.setText(resources.getString(R.string.joinprayer));
            title.setText(resources.getString(R.string.force));
            prayedthisdaybefore.setText(resources.getString(R.string.prayallquestion));
        }
    }

    private boolean request_protected_menu = true;
    private void load_data_from_slat_sql() {
        SQLSharing.mycursorslat.moveToPosition(6);
        language = SQLSharing.mycursorslat.getString(1);

        SQLSharing.mycursorslat.moveToPosition(9);
        request_protected_menu = SQLSharing.mycursorslat.getString(1).equals("yes");

        SQLSharing.mycursorslat.moveToPosition(1);
        if(SQLSharing.mycursorslat.getString(1).equals("no")) {
            lmfaoimage = findViewById(R.id.lmfao);
            light_mode();
        }
        else {
            darkmode = true;

            // jame3 drawing must be loaded
            lmfaoimage = findViewById(R.id.lmfao);
            try {
                Glide.with(this).load(R.drawable.lmfao).into(lmfaoimage);
            } catch (Exception ignored) {
                lmfaoimage.setImageDrawable(resources.getDrawable(R.drawable.lmfao));
            }
        }
    }

    private void low_light_alert() {
        try {
            if (getIntent().getStringExtra("light_alert").equals("yes")) {
                if (language.equals(resources.getString(R.string.en)))
                    Snackbar.make(full, getString(R.string.low_light), Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(full, getString(R.string.low_light_arabe), Snackbar.LENGTH_LONG).show();
            }
        } catch(Exception e){e.printStackTrace();}
    }

    private void location_shit(final Date date) {
        sql(resources.getString(R.string.justforce));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (SQLSharing.mycursorforce.getCount() > 0)
            if_theres_previous_info_load_it_n_display(date);
        else
            new_coordinates = true;
        if_first_launch_get_longitude_n_lattitude_n_ville_n_hijri_date(date);
    }

    private void if_first_launch_get_longitude_n_lattitude_n_ville_n_hijri_date(Date date) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return; }
        if(checkLocation())
            AttemptToGetLocationCoordinates();
    }

    private void if_theres_previous_info_load_it_n_display(Date date) {
        new_coordinates = false;
        SQLSharing.mycursorforce.moveToFirst();
        longitude = Double.valueOf(SQLSharing.mycursorforce.getString(1));
        latitude = Double.valueOf(SQLSharing.mycursorforce.getString(2));
        use(longitude, latitude, new_coordinates, date);
    }

    private void sql(final String table) {
        SQLSharing.TABLE_NAME_INPUTER = table;
        switch (table) {
            case "slat":
                SQLSharing.mydbslat = new SQL(this);
                SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
                break;
            case "force":
                SQLSharing.mydbforce = new SQL(this);
                SQLSharing.mycursorforce = SQLSharing.mydbforce.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mydbforce3 = new SQL(this);
                SQLSharing.mycursorforce3 = SQLSharing.mydbforce3.getAllDateforce3();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(REQUEST_CODE==requestCode && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                AttemptToGetLocationCoordinates();
            else if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                back_to_main();
            }
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void AttemptToGetLocationCoordinates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return; }
        if(checkLocation()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                                use(longitude, latitude, new_coordinates, CurrentDisplayedDay);
                            }
                        }
                    });
        }
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        an_alert_to_turn_location_on_was_displayed = true;
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main);
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    protected void onPause() {
        running = false;
        unregisterReceiver(receiver);


        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();

        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.krimzon.scuffedbots.raka3at.background.iprayeditmate"); //further more

        registerReceiver(receiver, filter);
/*

        // this is to update the slider as it's weirdly not updating instantly after onresuming
        if(!running) {
            calculate_temp_pos_and_neg();

            print("next_adan: " + next_adan);
            display_neg_if_possible();

            if ((temp_positifise != positifise || changing_day) && !still_scoping_on_previous_adan) {
                print(positifise);
                changing_day = false;
                positifise = temp_positifise;
                if(positifise!=0)
                    if(slider!=null) {
                        slider.setText("- " + positifise);
                    }
                    else
                        begonethot();

                check_next_adan();
            }
        }
*/

        running = true;
        live_updates();
        /*handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handlerer = new Handler(handlerThread.getLooper());*/

        sql("force");
        if(SQLSharing.mycursorforce.getCount()>0){
            // adan service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
            } else {
                ProcessMainClass bck = new ProcessMainClass();
                bck.launchService(getApplicationContext());
            }
        }


        if(an_alert_to_turn_location_on_was_displayed)
            AttemptToGetLocationCoordinates();

    }

    private boolean isLocationEnabled() {
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private Handler display_prayer_times = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            // display prayer times
            display_prayer_times();
            return true; }});

    private Handler work_on_date_n_display_it_and_display_dates = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            // work on date n then display
            work_on_date_n_display_it();
            display_dates();
            retrieveAndy();
            return true; }});

    private void use(final double longitude, final double latitude, final boolean new_coordinates, final Date today) {

        Runnable useRunnable=new Runnable() {@Override public void run() { try {

            // declarations
            prayers = new ArrayList<>();

            temptoday = today.toString().split(" ");
            todaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];

            //update coords in sql
            update_coords_in_sql(longitude, latitude, new_coordinates);

            // pull date and shape it
            pull_date_and_shape_it(longitude, latitude, today);

            // pull prayer times and shape them
            pull_prayer_times_and_shape_them();

            // convert prayertimes into seconds for comparaison and save in prayers Array
            convert_prayertimes_into_seconds();

            display_prayer_times.sendEmptyMessage(0);


            // pull location to display city or wtvr
            pull_location(longitude, latitude);

            work_on_date_n_display_it_and_display_dates.sendEmptyMessage(0);

        } catch(Exception e){e.printStackTrace();} }};

        Thread useThread = new Thread(useRunnable);
        useThread.start();
        /*datedisplay.setText(datin);*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    private void pull_location(double longitude, double latitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        Locale ar = new Locale(resources.getString(R.string.ar));
        if(language.equals(resources.getString(R.string.en)))
            geocoder = new Geocoder(this, Locale.US);
        else
            geocoder = new Geocoder(this, ar);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            displaycity.sendEmptyMessage(0);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private String city = "";
    private void convert_prayertimes_into_seconds() {
        int fajrtemp = Integer.valueOf(fajr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(fajr.split(" ")[0].split(":")[1]);
        if(fajr.split(" ")[1].equals(resources.getString(R.string.pmer)))
            fajrtemp += 720; //12*60
        //Integer risetemp = Integer.valueOf(rise.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(rise.split(" ")[0].split(":")[1])*60;
        int dhuhrtemp = Integer.valueOf(dhuhr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(dhuhr.split(" ")[0].split(":")[1]);
        if(dhuhr.split(" ")[1].equals(resources.getString(R.string.pmer)) && !dhuhr.split(":")[0].equals("12"))
            dhuhrtemp += 720; //12*60
        int asrtemp = Integer.valueOf(asr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(asr.split(" ")[0].split(":")[1]);
        if(asr.split(" ")[1].equals(resources.getString(R.string.pmer)))
            asrtemp += 720; //12*60
        int maghribtemp = Integer.valueOf(maghrib.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(maghrib.split(" ")[0].split(":")[1]);
        if(maghrib.split(" ")[1].equals(resources.getString(R.string.pmer)))
            maghribtemp += 720; //12*60
        int ishatemp = Integer.valueOf(isha.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(isha.split(" ")[0].split(":")[1]);
        if(isha.split(" ")[1].equals(resources.getString(R.string.pmer)))
            ishatemp += 720; //12*60
        prayers.add(fajrtemp);
        prayers.add(dhuhrtemp);
        prayers.add(asrtemp);
        prayers.add(maghribtemp);
        prayers.add(ishatemp);
    }

    private void display_prayer_times() {
        if(changing_day){
            if(going_left){
                if(only_once) {
                    only_once = false;
                    to_right1 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right2 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right3 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right4 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right5 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right6 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right7 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right8 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right9 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right10 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right11 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);
                    to_right12 = loadAnimation(this, R.anim.slide_prayertimes_to_right_n_fade);

                    from_left1 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left2 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left3 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left4 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left5 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left6 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left7 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left8 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left9 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left10 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left11 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                    from_left12 = loadAnimation(this, R.anim.slide_prayertimes_from_left_n_fade);
                }

                fajrtime.startAnimation(to_right1);
                to_right1.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    fajrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        fajrtime.setText(fajr);
                    else if(language.equals(resources.getString(R.string.ar)))
                        fajrtime.setText(tfajr);
                    fajrtime.startAnimation(from_left1);
                    from_left1.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        fajrtime.setVisibility(VISIBLE);
                    }});
                }});
                risetime.startAnimation(to_right2);
                to_right2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    risetime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        risetime.setText(rise);
                    else if(language.equals(resources.getString(R.string.ar)))
                        risetime.setText(trise);
                    risetime.startAnimation(from_left2);
                    from_left2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        risetime.setVisibility(VISIBLE);
                    }});
                }});
                dhuhrtime.startAnimation(to_right12);
                to_right12.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    dhuhrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        dhuhrtime.setText(dhuhr);
                    else if(language.equals(resources.getString(R.string.ar)))
                        dhuhrtime.setText(tdhuhr);
                    dhuhrtime.startAnimation(from_left3);
                    from_left3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        dhuhrtime.setVisibility(VISIBLE);
                    }});
                }});
                asrtime.startAnimation(to_right3);
                to_right3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    asrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        asrtime.setText(asr);
                    else if(language.equals(resources.getString(R.string.ar)))
                        asrtime.setText(tasr);
                    asrtime.startAnimation(from_left4);
                    from_left4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        asrtime.setVisibility(VISIBLE);
                    }});
                }});
                maghribtime.startAnimation(to_right4);
                to_right4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    maghribtime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        maghribtime.setText(maghrib);
                    else if(language.equals(resources.getString(R.string.ar)))
                        maghribtime.setText(tmaghrib);
                    maghribtime.startAnimation(from_left5);
                    from_left5.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        maghribtime.setVisibility(VISIBLE);
                    }});
                }});
                ishatime.startAnimation(to_right5);
                to_right5.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    ishatime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        ishatime.setText(isha);
                    else if(language.equals(resources.getString(R.string.ar)))
                        ishatime.setText(tisha);
                    ishatime.startAnimation(from_left6);
                    from_left6.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        ishatime.setVisibility(VISIBLE);
                    }});
                }});
                fajrtitle.startAnimation(to_right6);
                to_right6.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    fajrtitle.setVisibility(View.INVISIBLE);
                    fajrtitle.startAnimation(from_left7);
                    from_left7.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        fajrtitle.setVisibility(VISIBLE);
                    }});
                }});
                risetitle.startAnimation(to_right7);
                to_right7.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    risetitle.setVisibility(View.INVISIBLE);
                    risetitle.startAnimation(from_left8);
                    from_left8.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        risetitle.setVisibility(VISIBLE);
                    }});
                }});
                dohrtitle.startAnimation(to_right8);
                to_right8.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    dohrtitle.setVisibility(View.INVISIBLE);
                    dohrtitle.startAnimation(from_left9);
                    from_left9.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        dohrtitle.setVisibility(VISIBLE);
                    }});
                }});
                asrtitle.startAnimation(to_right9);
                to_right9.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    asrtitle.setVisibility(View.INVISIBLE);
                    asrtitle.startAnimation(from_left10);
                    from_left10.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        asrtitle.setVisibility(VISIBLE);
                    }});
                }});
                maghrebtitle.startAnimation(to_right10);
                to_right10.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    maghrebtitle.setVisibility(View.INVISIBLE);
                    maghrebtitle.startAnimation(from_left11);
                    from_left11.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        maghrebtitle.setVisibility(VISIBLE);
                    }});
                }});
                ishatitle.startAnimation(to_right11);
                to_right11.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    ishatitle.setVisibility(View.INVISIBLE);
                    ishatitle.startAnimation(from_left12);
                    from_left12.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        ishatitle.setVisibility(VISIBLE);
                        if(it_is_today) {
                            can_find_in = true;
                            handler5.sendEmptyMessage(0);
                        }
                    }});
                }});
            }

            if(going_right){
                if(only_once2) {
                    only_once2 = false;
                    fromright1 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright2 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright3 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright4 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright5 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright6 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright7 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright8 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright9 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright10 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright11 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);
                    fromright12 = loadAnimation(this, R.anim.slide_prayertimes_from_right_n_fade);

                    toleft1 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft2 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft3 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft4 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft5 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft6 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft7 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft8 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft9 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft10 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft11 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                    toleft12 = loadAnimation(this, R.anim.slide_prayertimes_to_left_n_fade);
                }

                fajrtime.startAnimation(toleft1);
                toleft1.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    fajrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        fajrtime.setText(fajr);
                    else if(language.equals(resources.getString(R.string.ar)))
                        fajrtime.setText(tfajr);
                    fajrtime.startAnimation(fromright1);
                    fromright1.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        fajrtime.setVisibility(VISIBLE);
                    }});
                }});
                risetime.startAnimation(toleft2);
                toleft2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    risetime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        risetime.setText(rise);
                    else if(language.equals(resources.getString(R.string.ar)))
                        risetime.setText(trise);
                    risetime.startAnimation(fromright2);
                    fromright2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        risetime.setVisibility(VISIBLE);
                    }});
                }});
                dhuhrtime.startAnimation(toleft3);
                toleft3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    dhuhrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        dhuhrtime.setText(dhuhr);
                    else if(language.equals(resources.getString(R.string.ar)))
                        dhuhrtime.setText(tdhuhr);
                    dhuhrtime.startAnimation(fromright3);
                    fromright3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        dhuhrtime.setVisibility(VISIBLE);
                    }});
                }});
                asrtime.startAnimation(toleft5);
                toleft5.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    asrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        asrtime.setText(asr);
                    else if(language.equals(resources.getString(R.string.ar)))
                        asrtime.setText(tasr);
                    asrtime.startAnimation(fromright4);
                    fromright4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        asrtime.setVisibility(VISIBLE);
                    }});
                }});
                maghribtime.startAnimation(toleft6);
                toleft6.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    maghribtime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        maghribtime.setText(maghrib);
                    else if(language.equals(resources.getString(R.string.ar)))
                        maghribtime.setText(tmaghrib);
                    maghribtime.startAnimation(fromright5);
                    fromright5.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        maghribtime.setVisibility(VISIBLE);
                    }});
                }});
                ishatime.startAnimation(toleft7);
                toleft7.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    ishatime.setVisibility(View.INVISIBLE);
                    if(language.equals(resources.getString(R.string.en)))
                        ishatime.setText(isha);
                    else if(language.equals(resources.getString(R.string.ar)))
                        ishatime.setText(tisha);
                    ishatime.startAnimation(fromright6);
                    fromright6.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        ishatime.setVisibility(VISIBLE);
                    }});
                }});
                fajrtitle.startAnimation(toleft8);
                toleft8.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    fajrtitle.setVisibility(View.INVISIBLE);
                    fajrtitle.startAnimation(fromright7);
                    fromright7.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        fajrtitle.setVisibility(VISIBLE);
                    }});
                }});
                risetitle.startAnimation(toleft9);
                toleft9.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    risetitle.setVisibility(View.INVISIBLE);
                    risetitle.startAnimation(fromright8);
                    fromright8.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        risetitle.setVisibility(VISIBLE);
                    }});
                }});
                dohrtitle.startAnimation(toleft10);
                toleft10.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    dohrtitle.setVisibility(View.INVISIBLE);
                    dohrtitle.startAnimation(fromright9);
                    fromright9.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        dohrtitle.setVisibility(VISIBLE);
                    }});
                }});
                asrtitle.startAnimation(toleft11);
                toleft11.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    asrtitle.setVisibility(View.INVISIBLE);
                    asrtitle.startAnimation(fromright10);
                    fromright10.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        asrtitle.setVisibility(VISIBLE);
                    }});
                }});
                maghrebtitle.startAnimation(toleft12);
                toleft12.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    maghrebtitle.setVisibility(View.INVISIBLE);
                    maghrebtitle.startAnimation(fromright11);
                    fromright11.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        maghrebtitle.setVisibility(VISIBLE);
                    }});
                }});
                ishatitle.startAnimation(toleft4);
                toleft4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    ishatitle.setVisibility(View.INVISIBLE);
                    ishatitle.startAnimation(fromright12);
                    fromright12.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        ishatitle.setVisibility(VISIBLE);
                        if(it_is_today) {
                            can_find_in = true;
                            handler5.sendEmptyMessage(0);
                        }
                    }});
                }});
            }
        } else {
            if(language.equals(resources.getString(R.string.en))) {
                fajrtime.setText(fajr);
                risetime.setText(rise);
                dhuhrtime.setText(dhuhr);
                asrtime.setText(asr);
                maghribtime.setText(maghrib);
                ishatime.setText(isha);

            } else if(language.equals(resources.getString(R.string.ar))){ // the arabic am and pm
                fajrtime.setText(tfajr);
                risetime.setText(trise);
                dhuhrtime.setText(tdhuhr);
                asrtime.setText(tasr);
                maghribtime.setText(tmaghrib);
                ishatime.setText(tisha);
            }
        }
    }

    private void pull_prayer_times_and_shape_them() {
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, date, params);
        try {
            String timeshape = "hh:mm a";
            fajr = DateFormat.format(timeshape, new Date(prayerTimes.fajr.getTime())).toString();
            rise = DateFormat.format(timeshape, new Date(prayerTimes.sunrise.getTime())).toString();
            dhuhr = DateFormat.format(timeshape, new Date(prayerTimes.dhuhr.getTime())).toString();
            asr = DateFormat.format(timeshape, new Date(prayerTimes.asr.getTime())).toString();
            maghrib = DateFormat.format(timeshape, new Date(prayerTimes.maghrib.getTime())).toString();
            isha = DateFormat.format(timeshape, new Date(prayerTimes.isha.getTime())).toString();
        } catch(Exception e){e.printStackTrace();}


        if(language.equals(resources.getString(R.string.ar))){ // the arabic am and pm
            String pm = getString(R.string.pm);
            String am = getString(R.string.am);
            if(fajr.split(" ")[1].equals("AM")) tfajr = fajr.split(" ")[0] + " " + am;else tfajr = fajr.split(" ")[0] + " " + pm;
            if(rise.split(" ")[1].equals("AM")) trise = rise.split(" ")[0] + " " + am;else trise = rise.split(" ")[0] + " " + pm;
            if(dhuhr.split(" ")[1].equals("AM")) tdhuhr = dhuhr.split(" ")[0] + " " + am;else tdhuhr = dhuhr.split(" ")[0] + " " + pm;
            if(asr.split(" ")[1].equals("AM")) tasr = asr.split(" ")[0] + " " + am;else tasr = asr.split(" ")[0] + " " + pm;
            if(maghrib.split(" ")[1].equals("AM")) tmaghrib = maghrib.split(" ")[0] + " " + am;else tmaghrib = maghrib.split(" ")[0] + " " + pm;
            if(isha.split(" ")[1].equals("AM")) tisha = isha.split(" ")[0] + " " + am;else tisha = isha.split(" ")[0] + " " + pm;
        }
    }

    private void update_coords_in_sql(double longitude, double latitude, boolean new_coordinates) {
        SQLSharing.mycursorforce.moveToFirst();
        if(SQLSharing.mycursorforce.getCount()<=0)
            SQLSharing.mydbforce.insertMawa9it(String.valueOf(longitude), String.valueOf(latitude));
        else
            SQLSharing.mydbforce.updateMawa9it(SQLSharing.mycursorforce.getString(0), String.valueOf(longitude), String.valueOf(latitude));
    }

    private void pull_date_and_shape_it(double longitude, double latitude, Date today) {
        coordinates = new Coordinates(latitude, longitude);
        date = DateComponents.from(today);
    }

    private void display_dates() {
        daterr.setText( datin + '\n' + hijri);
    }

    private void work_on_date_n_display_it() {
        datin = "";
        String tempdatin;
        if(language.equals(resources.getString(R.string.en))) {
            tempdatin = temptoday[0];


            if (tempdatin.equals(resources.getString(R.string.satu))) {
                datin += resources.getString(R.string.sat);
                friday = false;
                dohrtitle.setText(resources.getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(resources.getString(R.string.sunu))) {
                datin += resources.getString(R.string.sun);
                friday = false;
                dohrtitle.setText(resources.getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(resources.getString(R.string.monu))) {
                datin += resources.getString(R.string.mon);
                friday = false;
                dohrtitle.setText(resources.getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(resources.getString(R.string.tueu))) {
                datin += resources.getString(R.string.tue);
                friday = false;
                dohrtitle.setText(resources.getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(resources.getString(R.string.wedu))) {
                datin += resources.getString(R.string.wed);
                friday = false;
                dohrtitle.setText(resources.getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(resources.getString(R.string.thuru))) {
                datin += resources.getString(R.string.thu);
                friday = false;
                dohrtitle.setText(resources.getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(resources.getString(R.string.fridu))) {
                datin += resources.getString(R.string.fri);
                friday = true;
                dohrtitle.setText(resources.getString(R.string.Jamo3a));
            }


            // add week day to hijri date
            //hijri += datin + " ";

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals(resources.getString(R.string.jan))) {
                datin += resources.getString(R.string.january);
                miladi_month = 1;
            }
            else if (tempdatin.equals(resources.getString(R.string.feb))) {
                datin += resources.getString(R.string.february);
                miladi_month = 2;
            }
            else if (tempdatin.equals(resources.getString(R.string.mar))) {
                datin += resources.getString(R.string.march);
                miladi_month = 3;
            }
            else if (tempdatin.equals(resources.getString(R.string.apr))) {
                datin += resources.getString(R.string.april);
                miladi_month = 4;
            }
            else if (tempdatin.contains(resources.getString(R.string.mao))) {
                datin += resources.getString(R.string.may);
                miladi_month = 5;
            }
            else if (tempdatin.contains(resources.getString(R.string.june))) {
                datin += resources.getString(R.string.junee);
                miladi_month = 6;
            }
            else if (tempdatin.contains(resources.getString(R.string.july))) {
                datin += resources.getString(R.string.julyy);

                miladi_month = 7;
            }
            else if (tempdatin.equals(resources.getString(R.string.aug))) {
                miladi_month = 8;
                datin += resources.getString(R.string.august);
            }
            else if (tempdatin.equals("Sep")) {
                miladi_month = 9;
                datin += resources.getString(R.string.september);
            }
            else if (tempdatin.equals("Oct")) {
                miladi_month = 10;
                datin += resources.getString(R.string.october);
            }
            else if (tempdatin.equals("Nov")) {
                miladi_month = 11;
                datin += resources.getString(R.string.november);
            }
            else if (tempdatin.equals("Dec")) {
                miladi_month = 12;
                datin += resources.getString(R.string.december);
            }

            tempdatin = temptoday[2];
            int temper = Integer.valueOf(tempdatin);
            datin += " " + temper;
            if (temper==2 || temper==22)
                datin += resources.getString(R.string.nd);
            else if (temper==3 || temper==23)
                datin += resources.getString(R.string.rd);
            else if (temper==1 || temper==21)
                datin += resources.getString(R.string.st);
            else
                datin += resources.getString(R.string.th);

        } else if(language.equals(resources.getString(R.string.ar))){
            tempdatin = temptoday[0];


            switch (tempdatin) {
                case "Sat":
                    datin += resources.getString(R.string.satarabe);
                    friday = false;
                    dohrtitle.setText(resources.getString(R.string.dohrtitle_arabe));
                    break;
                case "Sun":
                    datin += resources.getString(R.string.sunarabe);
                    friday = false;
                    dohrtitle.setText(resources.getString(R.string.dohrtitle_arabe));
                    break;
                case "Mon":
                    datin += resources.getString(R.string.monarabe);
                    friday = false;
                    dohrtitle.setText(resources.getString(R.string.dohrtitle_arabe));

                    break;
                case "Tue":
                    datin += resources.getString(R.string.tuearabe);
                    friday = false;
                    dohrtitle.setText(resources.getString(R.string.dohrtitle_arabe));
                    break;
                case "Wed":
                    datin += resources.getString(R.string.wedarabe);
                    friday = false;
                    dohrtitle.setText(resources.getString(R.string.dohrtitle_arabe));
                    break;
                case "Thu":
                    datin += resources.getString(R.string.thurarabe);
                    friday = false;
                    dohrtitle.setText(resources.getString(R.string.dohrtitle_arabe));
                    break;
                case "Fri":
                    datin += resources.getString(R.string.friarabe);
                    friday = true;
                    dohrtitle.setText(resources.getString(R.string.friarabe));
                    break;
            }

            //hijri += datin + " ";

            tempdatin = temptoday[2];
            int temper = Integer.valueOf(tempdatin);
            datin += " " + temper;

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals("Jan")) {
                miladi_month = 1;
                datin += resources.getString(R.string.janarabe);
            }
            else if (tempdatin.equals("Feb")) {
                miladi_month = 2;
                datin += resources.getString(R.string.febarabe);
            }
            else if (tempdatin.equals("Mar")) {
                miladi_month = 3;
                datin += resources.getString(R.string.mararabe);
            }
            else if (tempdatin.equals("Apr")) {
                miladi_month = 4;
                datin += resources.getString(R.string.aprarabe);
            }
            else if (tempdatin.contains("Ma")) {
                miladi_month = 5;
                datin += resources.getString(R.string.maarabe);
            }
            else if (tempdatin.contains("Jun")) {
                miladi_month = 6;
                datin += resources.getString(R.string.junarabe);
            }
            else if (tempdatin.contains("Jul")) {
                miladi_month = 7;
                datin += resources.getString(R.string.jularabe);
            }
            else if (tempdatin.equals("Aug")) {
                miladi_month = 8;
                datin += resources.getString(R.string.augusarabe);
            }
            else if (tempdatin.equals("Sep")) {
                miladi_month = 9;
                datin += resources.getString(R.string.separabe);
            }
            else if (tempdatin.equals("Oct")) {
                miladi_month = 10;
                datin += resources.getString(R.string.octarabe);
            }
            else if (tempdatin.equals("Nov")) {
                miladi_month = 11;
                datin += resources.getString(R.string.novarabe);
            }
            else if (tempdatin.equals("Dec")) {
                miladi_month = 12;
                datin += resources.getString(R.string.decarabe);
            }

        }
        datin += " " + temptoday[5];


        hijri_date_setup();
    }

    private void log(Object dumps){
        Log.i("HH", String.valueOf(dumps));

    }

    private void print(Object dumps) {
        Toast.makeText(getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }

    private void print2(Object s) {
        Snackbar.make(full, String.valueOf(s), Snackbar.LENGTH_SHORT).show();
    }

    private void print3(String s, String s2, final int prayer){
        //https://stackoverflow.com/questions/33033157/adding-button-to-snackbar-android
        final SnackBar mSnackBar = new SnackBar.Builder(this)
                .withMessage(s)
                .withActionMessage(s2)
                .withStyle(SnackBar.Style.DEFAULT)
                .show();

        TextView snackButton = mSnackBar.getContainerView().findViewById(R.id.snackButton);
        snackButton.setTextColor(Color.GREEN);
        snackButton.setTextSize(16);
        snackButton.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            display_selection(prayer);
            mSnackBar.hide();
        }});
    }

    private void check_state(int prayer) {

        // setup the prayed array (contains 1s and 0s that determine if prayed or not
        process_prayed_request(prayer);

        // present
        if(it_is_today) {
            // green
            if (String.valueOf(prayed.charAt(prayer)).equals("1")) {
                want_to_pray_this_again(prayer);
            }
            // red
            else if (prayer < next_adan) {
                if (allow_pray)
                    display_selection(prayer);
                else
                    did_you_pray_this_previous_prayer(prayer);
            }
            // special case of red (next_adan not being set)
            else if(end_of_day){
                if (allow_pray)
                    display_selection(prayer);
                else
                    did_you_pray_this_previous_prayer(prayer);
            }
            // gray
            else {
                if(prayer == next_adan && positifise <= 30 && !still_scoping_on_previous_adan) {
                    theres_still_until_this_prayer(prayer);
                } else
                    too_early(prayer);
            }
        }

        // future
        else if(all_white){
            cannot_pray_futures();
        }

        // past
        else if(fill_all){
            // green
            if (String.valueOf(prayed.charAt(prayer)).equals("1")) {
                want_to_pray_this_again(prayer);
            }
            // red
            else {
                if (allow_pray)
                    display_selection(prayer);
                else
                    did_you_pray_this_previous_prayer(prayer);
            }
        }

        clean_up();
    }

    private void want_to_pray_this_again(int prayer) {
        if(language.equals("en"))
            print3(resources.getString(R.string.wanttopraythisagain), resources.getString(R.string.yes), prayer);
        else if(language.equals("ar"))
            print3(resources.getString(R.string.wanttopraythisagain_arabe), resources.getString(R.string.yes_arabe), prayer);
    }

    private void theres_still_until_this_prayer(int prayer) {
        if(language.equals("en"))
            print3(resources.getString(R.string.theresstill) + positifise + resources.getString(R.string.minutesuntilthisprayer), resources.getString(R.string.joinprayer), prayer);
        else if(language.equals("ar")){
            String lmao = " " + positifise + " ";
            print3(resources.getString(R.string.theresstill_arabe) + lmao + resources.getString(R.string.minutesuntilthisprayer_arabe), resources.getString(R.string.joinprayer_arabic), prayer);
        }
    }

    private void cannot_pray_futures() {
        if(language.equals(resources.getString(R.string.en)))
            print2(resources.getString(R.string.cannot));
        else if(language.equals(resources.getString(R.string.ar)))
            print2(resources.getString(R.string.cannot_arabe));
    }

    private void did_you_pray_this_previous_prayer(int prayer) {
        if (language.equals(resources.getString(R.string.en)))
            print2(resources.getString(R.string.didyoupray) + " " + prayernames.get(prayer - 1) + resources.getString(R.string.questionmark));
        else if (language.equals(resources.getString(R.string.ar)))
            print2(getString(R.string.didyoupray_arabe) + " " + prayernames_arabe.get(prayer - 1) + resources.getString(R.string.questionmark_arabe));
    }

    private void too_early(int prayer) {
        if (language.equals(resources.getString(R.string.en)))
            print2(getString(R.string.waytooearly) + " " + prayernames.get(prayer));
        else if (language.equals(resources.getString(R.string.ar)))
            print2(getString(R.string.waytooearly_arabe) + " " + prayernames_arabe.get(prayer));
    }

    private void display_selection(int prayerer) {
        // prayerer = 0(fajr), 1(dhuhr), 2(asr), 3(maghrib), 4(isha)
        HomeOrMosque mosqueorhome=new HomeOrMosque(this, friday, prayed, todaycomparable, prayerer, darkmode, language, verified, athome, false);
        mosqueorhome.show();
    }

    private void check_if_prayed_or_verified_are_empty() {
        if(prayed == null)
            prayed = "00000";

        if(prayed.equals(""))
            prayed = "00000";

        if(verified == null)
            verified = "00000";

        if(verified.equals(""))
            verified = "00000";

        if(athome == null)
            athome = "00000";

        if(athome.equals(""))
            athome = "00000";

    }

    private void compare(int i) {

        // if fajr instantly allow
        if(i==0){
            allow_pray = true;
        } else {
            for (int j = 0; j < i; j++) { // check if all previous prayers are prayed
                if (String.valueOf(prayed.charAt(j)).equals("0")) {
                    one_of_previous_is_zero = true;
                    break;
                }
            }
            if (!one_of_previous_is_zero)
                allow_pray = true;
        }

        one_of_previous_is_zero = false;
    }

    private void retrieveAndy(){

        // if theres smt in sql then  look up  prayed
        sql(resources.getString(R.string.justforce2));
        if (SQLSharing.mycursorforce3.getCount() > 0) {
            pull_prayed_one_hot_encoding_from_sql();
            check_if_prayed_or_verified_are_empty();
            for (int i = 0; i < 5; i++) {
                if (String.valueOf(verified.charAt(i)).equals("1")) {
                    checkmarks.get(i).setVisibility(VISIBLE);
                    try {
                        Glide.with(getApplicationContext()).load(R.drawable.checkmark).into(checkmarks.get(i));
                    } catch (Exception ignored) {
                        checkmarks.get(i).setImageDrawable(resources.getDrawable(R.drawable.checkmark));
                    }
                } else
                    checkmarks.get(i).setVisibility(GONE);
            }
        } else { // else fill it up with zeros and insert
            check_if_prayed_or_verified_are_empty();
            /*SQLSharing.mydb.insertPrayed(todaycomparable, prayed, verified, athome);*/
        }

        // what is soon adan
        what_is_soon_adan_and_one_before_it();

        // color pray buttons
        color_pray_buttons();

        // don't display time till next adan if it's at end of day
        if(it_is_today)
            if(!end_of_day)
                InitialDelayForNextAdanAnimation();

    }

    private void color_pray_buttons() {

        if(!all_white) {

            if(fill_all){
                if(darkmode) {
                    praybuttonsdone = true;
                    for (int i = 0; i < 5; i++) {
                        if (String.valueOf(prayed.charAt(i)).equals("0")){
                            praybuttons.get(i).setTextColor(resources.getColor(R.color.lighterred));
                        }else
                            praybuttons.get(i).setTextColor(Color.GREEN);
                    }
                } else {
                    praybuttonsdone = true;
                    for (int i = 0; i < 5; i++) {
                        if (String.valueOf(prayed.charAt(i)).equals("0")) {
                            praybuttons.get(i).setTextColor(Color.WHITE);
                            praybuttons.get(i).setBackground(resources.getDrawable(R.drawable.lightforcebuttons));
                        } else {
                            praybuttons.get(i).setTextColor(Color.WHITE);
                            praybuttons.get(i).setBackground(resources.getDrawable(R.drawable.lightforcebuttonsgreen));
                            praybuttons.get(i).setTextColor(Color.BLACK);
                        }
                    }
                }
            } else {
                if (end_of_day) {
                    if(darkmode) {
                        praybuttonsdone = true;
                        for (int i = 0; i < 5; i++) {
                            if (String.valueOf(prayed.charAt(i)).equals("0"))
                                praybuttons.get(i).setTextColor(resources.getColor(R.color.lighterred));
                            else
                                praybuttons.get(i).setTextColor(Color.GREEN);
                        }
                    } else {
                        praybuttonsdone = true;
                        for (int i = 0; i < 5; i++) {
                            if (String.valueOf(prayed.charAt(i)).equals("0")) {
                                praybuttons.get(i).setTextColor(Color.WHITE);
                                praybuttons.get(i).setBackground(resources.getDrawable(R.drawable.lightforcebuttons));
                            } else {
                                praybuttons.get(i).setTextColor(Color.WHITE);
                                praybuttons.get(i).setBackground(resources.getDrawable(R.drawable.lightforcebuttonsgreen));
                                praybuttons.get(i).setTextColor(Color.BLACK);
                            }
                        }
                    }
                } else {
                    if(darkmode && next_adan!=-1) {
                        praybuttonsdone = true;
                        for (int i = 0; i < next_adan; i++) {
                            if (String.valueOf(prayed.charAt(i)).equals("0"))
                                praybuttons.get(i).setTextColor(resources.getColor(R.color.lighterred));
                            else
                                praybuttons.get(i).setTextColor(Color.GREEN);
                        }

                        for (int i = next_adan; i < 5; i++) {
                            praybuttons.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                        }

                    } else if(next_adan!=-1){
                        praybuttonsdone = true;
                        for (int i = 0; i < next_adan; i++) {
                            if (String.valueOf(prayed.charAt(i)).equals("0")) {
                                praybuttons.get(i).setTextColor(Color.WHITE);
                                praybuttons.get(i).setBackground(resources.getDrawable(R.drawable.lightforcebuttons));
                            } else {
                                praybuttons.get(i).setTextColor(Color.WHITE);
                                praybuttons.get(i).setBackground(resources.getDrawable(R.drawable.lightforcebuttonsgreen));
                                praybuttons.get(i).setTextColor(Color.BLACK);
                            }
                        }


                        for (int i = next_adan; i < 5; i++) {
                            praybuttons.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                            praybuttons.get(i).setBackground(null);
                        }
                    }

                }
            }

        } else {
            if(darkmode) {
                for (int i = 0; i < 5; i++) {
                    praybuttons.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                }
            }
            else {
                for (int i = 0; i < 5; i++) {
                    praybuttons.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                    praybuttons.get(i).setBackground(null);
                }
            }
        }
    }

    private void what_is_soon_adan_and_one_before_it() {
        String temptime = String.valueOf(new Date()).split(" ")[3];
        rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 60 + Integer.valueOf(temptime.split(":")[1]);

        for(int i=0;i<prayers.size();i++){
            if(rightnowcomparable<prayers.get(0)) {
                temp_next_adan = 0;
                break;
            }
            if(rightnowcomparable>prayers.get(i))
                temp_next_adan = i+1;
        }

        /*int previous_adan = 0;*/
        if(temp_next_adan==5) {
            end_of_day = true;
            temp_next_adan = 0;
            /*previous_adan = 0;*/
        } else
            end_of_day = false;

        if(temp_next_adan<0) temp_next_adan = 0;
        if(temp_next_adan!=next_adan) {
            new_adan = true;
            next_adan = temp_next_adan;
            if(next_adan>4)
                next_adan = 0;
            //next_adan = 0;
            /*if (next_adan != 0) // so we don't assign -1 to previous_adan
                previous_adan = next_adan - 1;*/
        }

        if(changing_day)
            new_adan = true;

    }

    private void process_prayed_request(int compareandy) {
        pull_prayed_one_hot_encoding_from_sql();

        compare(compareandy); // check whether to accept allow request or not
        check_if_prayed_or_verified_are_empty();
    }

    private void clean_up() {
        one_of_previous_is_zero = false;
        allow_pray = false;
    }

    private void pull_prayed_one_hot_encoding_from_sql() {
        sql(resources.getString(R.string.justforce2));
        prayed = "00000";
        verified = "00000";
        athome = "00000";
        while(SQLSharing.mycursorforce3.moveToNext()) {
            if (todaycomparable.equals(SQLSharing.mycursorforce3.getString(1))){
                prayed = SQLSharing.mycursorforce3.getString(2);
                verified = SQLSharing.mycursorforce3.getString(3);
                athome = SQLSharing.mycursorforce3.getString(4);
                /*print(SQLSharing.mycursor.getColumnCount());*/
                break;
            }
        }
    }

    private void animatenextadan() {
        if(new_adan && next_adan!=-1) { new_adan = false;
            prayerdisplayviews.get(next_adan).setTextColor(Color.GREEN);
            prayerdisplayviews2.get(next_adan).setTextColor(Color.GREEN);
            slide_in_dem_dpz();
        }
    }

    private void slide_in_dem_dpz() {
        if(it_is_today && next_adan!=-1) {
            temp_positifise = Math.round( Math.abs((prayers.get(next_adan) - rightnowcomparable)) );

            if(next_adan!=0)
                temp_negatifise = Math.round( Math.abs((rightnowcomparable - prayers.get(next_adan-1))) );

            String lol;
            if((temp_negatifise != negatifise || changing_day) && next_adan!=0){
                negatifise = temp_negatifise;
                if(negatifise <= 30){
                    still_scoping_on_previous_adan = true;
                    find_slider(next_adan, false);
                    if(slider!=null)
                        if(negatifise!=0){
                            lol = resources.getString(R.string.plusser) + negatifise;
                            slider.setText(lol); // for sm ass reason it's over by 1 min
                            if(can_find_in)
                                handler5.sendEmptyMessage(0);
                        } else
                            begonethot();
                } else
                    still_scoping_on_previous_adan = false;
            }

            if ((temp_positifise != positifise || changing_day) && !still_scoping_on_previous_adan){
                positifise = temp_positifise;
                if (positifise < SQLSharing.minute_limit_to_display_positifise) { // TODO: revert this wuz for tasting
                    find_slider(next_adan, false);
                    if(slider!=null)
                        if(positifise!=0){
                            lol = resources.getString(R.string.minuser) + positifise;
                            slider.setText(lol); // for sm ass reason it's over by 1 min
                            if(can_find_in)
                                handler5.sendEmptyMessage(0);
                        } else
                            begonethot();
                }
            }

            live_updates();
        } else {
            find_slider(next_adan, false);
            begonethot();
        }

    }

    private void begonethot() {
        handler6.sendEmptyMessage(0);
    }

    private void fade_slider_in() {
        if(!end_of_day){
            if(it_is_today && (negatifise<=SQLSharing.minute_limit_to_display_negatifise || positifise<SQLSharing.minute_limit_to_display_positifise)){
                if(slider.getVisibility()!=VISIBLE){
                    Animation fromfajrtolol = loadAnimation(this, R.anim.fromfajrtofajr);
                    slider.setVisibility(View.INVISIBLE);
                    slider.startAnimation(fromfajrtolol);
                    fromfajrtolol.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        if (it_is_today && (negatifise<=SQLSharing.minute_limit_to_display_negatifise || positifise<SQLSharing.minute_limit_to_display_positifise))
                            slider.setVisibility(VISIBLE);
                        else if(!it_is_today)
                            slider.setVisibility(GONE);
                    }});
                }
            } else {
                find_slider(next_adan, false);
                begonethot();
            }
        }
    }

    private void find_slider(final int next_adaner, boolean just_cleaning) {
        int temp;
        if (still_scoping_on_previous_adan)
            temp = next_adaner - 1;
        else
            temp = next_adaner;
        switch (temp) {
            case -1:
                current_displayed_next_adan = -1;
                break;
            case 0:
            case 5:
                slider = findViewById(R.id.sliderfajr);
                if(!just_cleaning)
                    current_displayed_next_adan = 0;
                break;
            case 1:
                slider = findViewById(R.id.sliderdhuhr);
                if(!just_cleaning)
                    current_displayed_next_adan = 1;
                break;
            case 2:
                slider = findViewById(R.id.sliderasr);
                if(!just_cleaning)
                    current_displayed_next_adan = 2;
                break;
            case 3:
                slider = findViewById(R.id.slidermaghrib);
                if(!just_cleaning)
                    current_displayed_next_adan = 3;
                break;
            case 4:
                slider = findViewById(R.id.sliderisha);
                if(!just_cleaning)
                    current_displayed_next_adan = 4;
                break;
        }
    }

    public void InitialDelayForNextAdanAnimation(){

        if(initialdelayoncebrk){ initialdelayoncebrk = false;
            //runs in the background
            Runnable r=new Runnable() {
                @Override
                public void run() {
                    long futuretime = System.currentTimeMillis() + 900;

                    while (System.currentTimeMillis() < futuretime){
                        //prevents multiple threads from crashing into each other
                        synchronized (this){
                            try{
                                wait(futuretime - System.currentTimeMillis());
                            } catch(Exception e){e.printStackTrace();}
                        }
                    }

                    //run the handler
                    handler.sendEmptyMessage(0);
                }
            };

            //anti lag
            Thread mythread = new Thread(r); //to thread the runnable object we launched
            mythread.start();
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    private int get_month(String month){
        switch (month) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
        }
        return 1;
    }

    public void is_it_future_present_or_past(int day, int month, int year){
        todaysplittemparray = String.valueOf((new Date())).split(" ");
        int day2 = Integer.valueOf(todaysplittemparray[2]);
        int year2 = Integer.valueOf(todaysplittemparray[5]);
        int month2 = get_month(todaysplittemparray[1]);

        // compare between current day and currently displayed day

        if(year2 < year){ // definitely future
            all_white = true;
            fill_all = false;
            it_is_today = false;
        } else if(year2 > year){ // definitely past
            all_white = false;
            fill_all = true;
            it_is_today = false;
        } else if(month2 < month){ // definitely future
            all_white = true;
            fill_all = false;
            it_is_today = false;
        } else if(month2 > month){ // definitely past
            all_white = false;
            fill_all = true;
            it_is_today = false;
        } else if(day2 > day){ // definitely past
            all_white = false;
            fill_all = true;
            it_is_today = false;
        } else if(day2 < day){ // definitely future
            all_white = true;
            fill_all = false;
            it_is_today = false;
        } else {
            all_white = false;
            fill_all = false;
            it_is_today = true;
        }

    }

    private void light_mode() {
        darkmode = false;

        if(onlyonceu) {
            onlyonceu = false;
            fajrbackground = findViewById(R.id.fajrbackground);
            risebackground = findViewById(R.id.risebackground);
            dhuhrbackground = findViewById(R.id.dhuhrbackground);
            asrbackground = findViewById(R.id.asrbackground);
            maghribbackground = findViewById(R.id.maghribbackground);
            ishabackground = findViewById(R.id.ishabackground);
            yesterdayarrowbackground = findViewById(R.id.yesterdayarrowbackground);
            tommorowarrowbackground = findViewById(R.id.tommorowarrowbackground);
            rightsideelementsbackground = findViewById(R.id.rightsideelementsbackground);
            leftsideelementsbackground = findViewById(R.id.leftsideelementsbackground);
        }

        for(TextView praybutton:praybuttons){
            praybutton.setBackground(resources.getDrawable(R.drawable.forcebuttons));
            praybutton.setTextColor(Color.WHITE);
        }

        full.setBackground(resources.getDrawable(R.drawable.simpelbackground));
        doublearrowsbackground.setBackground(resources.getDrawable(R.drawable.lightbacktotoday));
        title.setTextColor(Color.WHITE);
        title.setBackgroundColor(resources.getColor(R.color.transparentblacker));


        if (fajrtitle.getCurrentTextColor() != Color.GREEN)
            fajrtitle.setTextColor(Color.WHITE);
        risetitle.setTextColor(Color.WHITE);
        if (dohrtitle.getCurrentTextColor() != Color.GREEN)
            dohrtitle.setTextColor(Color.WHITE);
        if (asrtitle.getCurrentTextColor() != Color.GREEN)
            asrtitle.setTextColor(Color.WHITE);
        if (maghrebtitle.getCurrentTextColor() != Color.GREEN)
            maghrebtitle.setTextColor(Color.WHITE);
        if (ishatitle.getCurrentTextColor() != Color.GREEN)
            ishatitle.setTextColor(Color.WHITE);

        daterr.setBackground(resources.getDrawable(R.drawable.lightdate));
        fajrbackground.setBackground(resources.getDrawable(R.drawable.lightmultipledayselectionbackground));
        risebackground.setBackground(resources.getDrawable(R.drawable.lightmultipledayselectionbackground));
        dhuhrbackground.setBackground(resources.getDrawable(R.drawable.lightmultipledayselectionbackground));
        asrbackground.setBackground(resources.getDrawable(R.drawable.lightmultipledayselectionbackground));
        maghribbackground.setBackground(resources.getDrawable(R.drawable.lightmultipledayselectionbackground));
        ishabackground.setBackground(resources.getDrawable(R.drawable.lightcity));
        yesterdayarrowbackground.setBackground(resources.getDrawable(R.drawable.lighttmrandyst));
        tommorowarrowbackground.setBackground(resources.getDrawable(R.drawable.lighttmrandyst));

        rightsideelementsbackground.setBackground(resources.getDrawable(R.drawable.lightstatsback));
        leftsideelementsbackground.setBackground(resources.getDrawable(R.drawable.lightbackback));
        try {
            Glide.with(this).load(R.drawable.lightlmfao).into(lmfaoimage);
        } catch (Exception ignored) {
            lmfaoimage.setImageDrawable(resources.getDrawable(R.drawable.lightlmfao));
        }

        sql(resources.getString(R.string.slat));
        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToNext();
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("no", ID);
    }

    private void dark_mode() {
        darkmode = true;

        if(onlyonce) {
            onlyonce = false;
            fajrbackground = findViewById(R.id.fajrbackground);
            risebackground = findViewById(R.id.risebackground);
            dhuhrbackground = findViewById(R.id.dhuhrbackground);
            asrbackground = findViewById(R.id.asrbackground);
            maghribbackground = findViewById(R.id.maghribbackground);
            ishabackground = findViewById(R.id.ishabackground);
            yesterdayarrowbackground = findViewById(R.id.yesterdayarrowbackground);
            tommorowarrowbackground = findViewById(R.id.tommorowarrowbackground);
            rightsideelementsbackground = findViewById(R.id.rightsideelementsbackground);
            leftsideelementsbackground = findViewById(R.id.leftsideelementsbackground);
        }

        try{ slider.setTextColor(resources.getColor(R.color.grayerthanwhite));}catch(Exception ignored){}

        try {
            Glide.with(this).load(R.drawable.lmfao).into(lmfaoimage);
        } catch (Exception ignored) {
            lmfaoimage.setImageDrawable(resources.getDrawable(R.drawable.lmfao));
        }

        for(TextView praybutton:praybuttons) {
            praybutton.setBackground(resources.getDrawable(R.drawable.darkbuttons));
            praybutton.setTextColor(resources.getColor(R.color.grayerthanwhite));
        }

        full.setBackground(resources.getDrawable(R.drawable.forcefull));
        doublearrowsbackground.setBackground(resources.getDrawable(R.drawable.backtotoday));
        title.setTextColor(Color.WHITE);
        title.setBackground(null);


        if (fajrtitle.getCurrentTextColor() != Color.GREEN)
            fajrtitle.setTextColor(resources.getColor(R.color.grayerthanwhite));
        risetitle.setTextColor(resources.getColor(R.color.grayerthanwhite));
        if (dohrtitle.getCurrentTextColor() != Color.GREEN)
            dohrtitle.setTextColor(resources.getColor(R.color.grayerthanwhite));
        if (asrtitle.getCurrentTextColor() != Color.GREEN)
            asrtitle.setTextColor(resources.getColor(R.color.grayerthanwhite));
        if (maghrebtitle.getCurrentTextColor() != Color.GREEN)
            maghrebtitle.setTextColor(resources.getColor(R.color.grayerthanwhite));
        if (ishatitle.getCurrentTextColor() != Color.GREEN)
            ishatitle.setTextColor(resources.getColor(R.color.grayerthanwhite));

        yesterdayarrowbackground.setBackground(resources.getDrawable(R.drawable.tmrandyst));
        tommorowarrowbackground.setBackground(resources.getDrawable(R.drawable.tmrandyst));
        daterr.setBackground(resources.getDrawable(R.drawable.date));
        fajrbackground.setBackground(resources.getDrawable(R.drawable.multipledayselectionbackground));
        risebackground.setBackground(resources.getDrawable(R.drawable.multipledayselectionbackground));
        dhuhrbackground.setBackground(resources.getDrawable(R.drawable.multipledayselectionbackground));
        asrbackground.setBackground(resources.getDrawable(R.drawable.multipledayselectionbackground));
        maghribbackground.setBackground(resources.getDrawable(R.drawable.multipledayselectionbackground));
        ishabackground.setBackground(resources.getDrawable(R.drawable.city));

        rightsideelementsbackground.setBackground(resources.getDrawable(R.drawable.statsback));
        leftsideelementsbackground.setBackground(resources.getDrawable(R.drawable.backback));
        try {
            Glide.with(this).load(R.drawable.lmfao).into(lmfaoimage);
        } catch (Exception ignored) {
            lmfaoimage.setImageDrawable(resources.getDrawable(R.drawable.lmfao));
        }

        sql(resources.getString(R.string.slat));
        SQLSharing.mycursorslat.moveToPosition(1);
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("yes", ID);
    }

    private void back_to_main() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    public void fajrClicked(View view) {
        check_state(0);
    }

    public void dhuhrClicked(View view) {
        check_state(1);
    }

    public void asrClicked(View view) {
        check_state(2);
    }

    public void maghribClicked(View view) {
        check_state(3);
    }

    public void ishaClicked(View view) {
        check_state(4);
    }

    public void backtotodayClicked(View view) {

        if(doublearrows.getVisibility()==VISIBLE) {
            can_find_in = false;
            changing_day = true;
            new_adan = true;
            CurrentDisplayedDay = new Date();
            it_is_today = true;
            all_white = false;
            fill_all = false;

            going_right = !going_right;
            going_left = !going_left;

            location_shit(CurrentDisplayedDay);

            // Hide double arrows that take you back to today
            doublearrows.setVisibility(View.INVISIBLE);
            citydisplay.setVisibility(VISIBLE);
        }
    }

    public void checkTommorow(View view) {

        can_find_in = false;
        new_adan = true;
        changing_day = true;
        going_left = false;
        going_right = true;

        todaysplittemparray = CurrentDisplayedDay.toString().split(" ");
        day = Integer.valueOf(todaysplittemparray[2]);
        year = Integer.valueOf(todaysplittemparray[5]);
        month = get_month(todaysplittemparray[1]);

        gc = new GregorianCalendar(year, month-1, day);
        gc.add(Calendar.DATE, 1);
        CurrentDisplayedDay = gc.getTime();

        todaysplittemparray = CurrentDisplayedDay.toString().split(" ");
        day = Integer.valueOf(todaysplittemparray[2]);
        year = Integer.valueOf(todaysplittemparray[5]);
        month = get_month(todaysplittemparray[1]);

        is_it_future_present_or_past(day, month, year);

        location_shit(CurrentDisplayedDay);

        if(!it_is_today) {
            slide_in_dem_dpz();
            for (int i = 0; i < 5; i++) {
                if (prayerdisplayviews.get(i).getCurrentTextColor() == Color.GREEN) {
                    if(darkmode) {
                        prayerdisplayviews.get(i).setTextColor(resources.getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                    } else {
                        prayerdisplayviews.get(i).setTextColor(resources.getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(resources.getColor(R.color.white));
                    }
                }
            }
            // Display double arrows that take you back to today
            doublearrows.setVisibility(VISIBLE);
            citydisplay.setVisibility(View.INVISIBLE);
            if(fill_all) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowright).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(resources.getDrawable(R.drawable.doublearrowright));
                }
            } if(all_white) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowleftt).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(resources.getDrawable(R.drawable.doublearrowleftt));
                }
            }
        } else {
            // Hide double arrows that take you back to today
            doublearrows.setVisibility(View.INVISIBLE);
            citydisplay.setVisibility(VISIBLE);
        }

    }



    private void gotoday(int day, int month, int year) {

        month += 1;
        gc = new GregorianCalendar(year, month-1, day);
        CurrentDisplayedDay = gc.getTime();

        is_it_future_present_or_past(day, month, year);

        if(fill_all){
            can_find_in = false;
            new_adan = true;
            changing_day = true;
            going_left = true;
            going_right = false;
        } else if(all_white){
            can_find_in = false;
            new_adan = true;
            changing_day = true;
            going_left = false;
            going_right = true;
        } if(it_is_today){
            can_find_in = false;
            new_adan = true;
            changing_day = false;
            going_left = false;
            going_right = false;
        }

        location_shit(CurrentDisplayedDay);

        if(!it_is_today) {
            slide_in_dem_dpz();
            for (int i = 0; i < 5; i++) {
                if (prayerdisplayviews.get(i).getCurrentTextColor() == Color.GREEN) {
                    if(darkmode) {
                        prayerdisplayviews.get(i).setTextColor(resources.getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                    } else {
                        prayerdisplayviews.get(i).setTextColor(resources.getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(resources.getColor(R.color.white));
                    }
                }
            }
            // Display double arrows that take you back to today
            doublearrows.setVisibility(VISIBLE);
            citydisplay.setVisibility(View.INVISIBLE);
            if(fill_all) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowright).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(resources.getDrawable(R.drawable.doublearrowright));
                }
            } if(all_white) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowleftt).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(resources.getDrawable(R.drawable.doublearrowleftt));
                }
            }
        } else {
            // Hide double arrows that take you back to today
            doublearrows.setVisibility(View.INVISIBLE);
            citydisplay.setVisibility(VISIBLE);
        }

    }




    public void checkYesterday(View view) {

        can_find_in = false;
        new_adan = true;
        changing_day = true;
        going_left = true;
        going_right = false;

        todaysplittemparray = CurrentDisplayedDay.toString().split(" ");
        day = Integer.valueOf(todaysplittemparray[2]);
        year = Integer.valueOf(todaysplittemparray[5]);
        month = get_month(todaysplittemparray[1]);

        gc = new GregorianCalendar(year, month-1, day);
        gc.add(Calendar.DATE, -1);
        CurrentDisplayedDay = gc.getTime();

        todaysplittemparray = CurrentDisplayedDay.toString().split(" ");
        day = Integer.valueOf(todaysplittemparray[2]);
        year = Integer.valueOf(todaysplittemparray[5]);
        month = get_month(todaysplittemparray[1]);

        is_it_future_present_or_past(day, month, year);

        location_shit(CurrentDisplayedDay);

        if(!it_is_today) {
            slide_in_dem_dpz();
            for (int i = 0; i < 5; i++) {
                if (prayerdisplayviews.get(i).getCurrentTextColor() == Color.GREEN) {
                    if(darkmode) {
                        prayerdisplayviews.get(i).setTextColor(resources.getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                    } else {
                        prayerdisplayviews.get(i).setTextColor(resources.getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(resources.getColor(R.color.white));
                    }
                }
            }

            // Display double arrows that take you back to today
            doublearrows.setVisibility(VISIBLE);
            citydisplay.setVisibility(View.INVISIBLE);
            if(fill_all) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowright).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(resources.getDrawable(R.drawable.doublearrowright));
                }
            } if(all_white) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowleftt).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(resources.getDrawable(R.drawable.doublearrowleftt));
                }
            }
        } else {
            // Hide double arrows that take you back to today
            doublearrows.setVisibility(View.INVISIBLE);
            citydisplay.setVisibility(VISIBLE);
        }

    }

    public void ShowStatisticsClicked(View view) {
        Statistictictictictic statisticstab=new Statistictictictictic(this, darkmode, language);
        statisticstab.show();
    }

    public void arrowbackClicked(View view) {
        exit();
    }

    public void nightmodeClicked(View view) {
        if(darkmode)
            light_mode();
        else
            dark_mode();

        color_pray_buttons();
    }

    public void settingsClicked(View view) {
        Intent open_settings = new Intent(this, force_settings.class);
        startActivity(open_settings);
    }

    public void prayedthisdaybeforeClicked(View view) {
        HomeOrMosque mosqueorhome=new HomeOrMosque(this, friday, prayed, todaycomparable, 1, darkmode, language, verified, athome, true);
        mosqueorhome.show();
    }
}
