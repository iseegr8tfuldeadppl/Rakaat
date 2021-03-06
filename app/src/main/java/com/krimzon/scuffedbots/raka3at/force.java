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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import static android.graphics.Color.WHITE;
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
    private String todaycomparable;
    private DateComponents date;
    private int hijri_month = 0, hijri_year = 0, hijriD = 0;
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
    private boolean going_left = false, going_right = false, changingD = false;
    private GregorianCalendar gc;
    private Date CurrentDisplayedDay;
    private int day, year, month = 0;
    private String[] todaysplittemparray;
    private boolean all_white = false, fill_all = false;
    private String athome = "11111";
    private int next_adan = -1, temp_next_adan = -1;
    private boolean end_ofD = false, it_is_today = true, new_adan = false;
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
    private int stoppableandroid = 28;

    private Handler displaycity = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            sql("slat");
            SQLSharing.mycursorslat.moveToPosition(14);
            if(city!=null){
                SQLSharing.mydbslat.updateData(city, SQLSharing.mycursorslat.getString(0));
                citydisplay.setText(city);
            } else if(SQLSharing.mycursorslat.getString(1)!=null){
                citydisplay.setText(SQLSharing.mycursorslat.getString(1));
            }
            close_sql();
            return true;}});
    private Handler hide_prayallbutton = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            prayedthisdaybefore.setVisibility(GONE);
            return true;}});
    private Handler locationshett = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            location_shit(CurrentDisplayedDay);
            return true;}});
    private Handler show_prayallbutton = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            prayedthisdaybefore.setVisibility(VISIBLE);
            return true;}});

    private Handler handler3 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { if(slider!=null) { if(positifise!=0) slider.setText("- " + positifise);else begonethot(); }return true;}});
    private Handler calluse = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Calendar cal = Calendar.getInstance(Locale.US);
            use(longitude, latitude, new_coordinates, new Date(cal.getTimeInMillis())); return true;}});
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
        public boolean handleMessage(@NonNull Message msg) {
            try {
                color_pray_buttons();
            } catch(Exception e){
                e.printStackTrace();

            } return true; }});

    private Handler updatepraybuttons = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            try {
                retrieveAndy();
            } catch(Exception e){
                e.printStackTrace();

            } return true; }});


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
        params = CalculationMethod.EGYPTIAN.getParameters();
        params.madhab = Madhab.SHAFI; // SHAFI made 95% accuracy, HANAFI had 1hour different for l'3asr
        //params.adjustments.fajr = SQLSharing.params_adjustments_fajr; //2
        params.fajrAngle = SQLSharing.fajrangle;
        params.ishaAngle = SQLSharing.ishaangle;
        //params.adjustments.isha = SQLSharing.params_adjustments_isha; //2
        /*String pattern = "dd-MMM-yyyy";*/
        /*SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);*/

        Calendar cal = Calendar.getInstance(Locale.US);
        CurrentDisplayedDay = new Date(cal.getTimeInMillis());
        String[] temptoday = CurrentDisplayedDay.toString().split(" ");
        currentdisplayeddaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];



        /*preparing_background_handler();*/

        variables_setup();

        fontAndy();

        sql(getResources().getString(R.string.slat));

        load_data_from_slat_sql();

        close_sql();

        if(request_protected_menu)
            protected_apps_request();

        languageshet();


        location_shit(CurrentDisplayedDay);

        // TODO; check if it show up correctly and if it doesn't show up when quickpraying after it said it before ofc, kinda keeps that value
        low_light_alert();
        if_sent_from_slat_after_prayer_check_whichD_we_were_praying_and_display_that();

        //longitude = 30;latitude = 30;use(longitude, latitude, true, new Date());


        // TODO remove when fixed
        if(Build.VERSION.SDK_INT > stoppableandroid){
            settingsbutton.setVisibility(GONE);
        }

        check_firebase_if_updated_today();

    }

    private DatabaseReference  userRef;
    private void check_firebase_if_updated_today() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            sync_SQL_and_Firebase(uid);
        }
    }

    private String getUserEmail(FirebaseUser user) {
        if (user != null) {
            String email = user.getEmail();
            if(email!=null){
                return email.replace(".", "").replace("@", "");
            }
        }
        return null;
    }

    private void sync_SQL_and_Firebase(final String uid) {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    userRef = database.getReference("users").child(uid).child("p");
                    /*lastupdatedRef = database.getReference("users").child(uid).child("lastupdated");*/
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //mostrecentrequest = String.valueOf(dataSnapshot.child("appside").child("mostrecentrequest").getValue());

                            boolean newstuff = false;

                            try {
                            close_sql();
                            sql(getResources().getString(R.string.justforce3));

                            if (SQLSharing.mycursorforce3.moveToFirst()) {
                                do {
                                    boolean found = false;
                                        if (dataSnapshot.getChildrenCount() != 0) {
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                if (!found) {
                                                    if (child.child("D").getValue() != null) {
                                                        String day = child.child("D").getValue().toString();
                                                        if (day.equals(SQLSharing.mycursorforce3.getString(1))) {
                                                            if (child.child("P").getValue() != null && child.child("V").getValue() != null && child.child("H").getValue() != null) {


                                                                if (child.getKey() != null) {
                                                                    String prayedfb = child.child("P").getValue().toString();
                                                                    String prayedsql = SQLSharing.mycursorforce3.getString(2);
                                                                    String verifiedfb = child.child("V").getValue().toString();
                                                                    String verifiedsql = SQLSharing.mycursorforce3.getString(3);
                                                                    String homefb = child.child("H").getValue().toString();
                                                                    String homesql = SQLSharing.mycursorforce3.getString(4);
                                                                    StringBuilder yesser = new StringBuilder();
                                                                    StringBuilder yesser3 = new StringBuilder();
                                                                    StringBuilder yesser4 = new StringBuilder();
                                                                    for (int i = 0; i < 5; i++) {
                                                                        if (String.valueOf(prayedfb.charAt(i)).equals("1")) {
                                                                            yesser.append("1");
                                                                        } else if (String.valueOf(prayedsql.charAt(i)).equals("1")) {
                                                                            yesser.append("1");
                                                                        } else {
                                                                            yesser.append("0");
                                                                        }
                                                                    }

                                                                    for (int i = 0; i < 5; i++) {
                                                                        if (String.valueOf(verifiedfb.charAt(i)).equals("1")) {
                                                                            yesser3.append("1");
                                                                        } else if (String.valueOf(verifiedsql.charAt(i)).equals("1")) {
                                                                            yesser3.append("1");
                                                                        } else {
                                                                            yesser3.append("0");
                                                                        }
                                                                    }

                                                                    for (int i = 0; i < 5; i++) {
                                                                        if (String.valueOf(homefb.charAt(i)).equals("0")) {
                                                                            yesser4.append("0");
                                                                        } else if (String.valueOf(homesql.charAt(i)).equals("0")) {
                                                                            yesser4.append("0");
                                                                        } else {
                                                                            yesser4.append("1");
                                                                        }
                                                                    }
                                                                    if (day.equals(currentdisplayeddaycomparable)) {
                                                                        if (!yesser.toString().equals(prayed) || !yesser3.toString().equals(verified) || !yesser4.toString().equals(athome)) {
                                                                            newstuff = true;
                                                                        }
                                                                    }
                                                                    userRef.child(child.getKey()).child("H").setValue(yesser4.toString());
                                                                    userRef.child(child.getKey()).child("V").setValue(yesser3.toString());
                                                                    userRef.child(child.getKey()).child("P").setValue(yesser.toString());
                                                                    SQLSharing.mydbforce3.updatePrayed(day, yesser.toString(), yesser3.toString(), yesser4.toString());
                                                                }
                                                            }
                                                            found = true;
                                                        }
                                                    }
                                                }
                                            }
                                    }
                                    if (!found) {
                                        String ingredientKey = userRef.push().getKey();
                                        if (ingredientKey != null) {
                                            userRef.child(ingredientKey).child("D").setValue(SQLSharing.mycursorforce3.getString(1));
                                            userRef.child(ingredientKey).child("P").setValue(SQLSharing.mycursorforce3.getString(2));
                                            userRef.child(ingredientKey).child("V").setValue(SQLSharing.mycursorforce3.getString(3));
                                            userRef.child(ingredientKey).child("H").setValue(SQLSharing.mycursorforce3.getString(4));
                                        }
                                    }
                                } while (SQLSharing.mycursorforce3.moveToNext());
                            }

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                boolean found = false;
                                if (child.child("D").getValue() != null) {
                                String day = child.child("D").getValue().toString();
                                if (SQLSharing.mycursorforce3.moveToFirst()) {
                                    do {
                                        if (day.equals(SQLSharing.mycursorforce3.getString(1))) {
                                            found = true;
                                        }
                                    } while (SQLSharing.mycursorforce3.moveToNext() && !found);
                                }
                                }

                                if (!found) {
                                    SQLSharing.mydbforce3.insertPrayed(child.child("D").getValue().toString(), child.child("P").getValue().toString(), child.child("V").getValue().toString(), child.child("H").getValue().toString());
                                }
                            }

                            close_sql();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (newstuff)
                                updatepraybuttons.sendEmptyMessage(0);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //print("loading data failed");
                            close_sql();
                        }
                    });

                }
            }
        };

        //anti lag
        Thread mythread = new Thread(r); //to thread the runnable object we launched
        mythread.start();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if(action.equals("com.krimzon.scuffedbots.raka3at.background.iprayeditmate")){
                Intent restart = new Intent(getApplicationContext(), force.class);
                startActivity(restart);
                finish();
            }
        }
    };

    private void if_sent_from_slat_after_prayer_check_whichD_we_were_praying_and_display_that() {
        try {
            String gtodaycomparable = getIntent().getStringExtra("todaycomparable");
            assert gtodaycomparable != null;
            String[] todaycomparablesplit = gtodaycomparable.split(" ");
            if(todaycomparablesplit.length==3) {
                gotoday(Integer.parseInt(todaycomparablesplit[1]), get_month2(todaycomparablesplit[0]), Integer.parseInt(todaycomparablesplit[2]));
            } else {
                mapActivity();
            }
        } catch(Exception ignored){
            mapActivity();
        }
    }
    private void protected_apps_request() {
        // TODO remove when fixed
        if(Build.VERSION.SDK_INT < stoppableandroid) {
            try {
                if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                    protected_apps_request request = new protected_apps_request(this, darkmode, language);
                    request.show();
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void load_service() {
        // adan service
        if (Build.VERSION.SDK_INT < 28) {
            try {
                close_sql();
                sql("force");
                if (SQLSharing.mycursorforce.getCount() > 0) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
                } else {
                    ProcessMainClass bck = new ProcessMainClass();
                    bck.launchService(getApplicationContext());
                }
                }
                close_sql();
            }
            catch(Exception ignored){}
        }
    }

    private void pprint(Object log){
        Log.i("HH", String.valueOf(log));
    }

    private void live_updates() {
        Runnable r=new Runnable() {@Override public void run() { try {
            while(running) {

                wait_1_second();

                if (it_is_today) {

                    calculate_rightnowcomparable();
                    if (!praybuttonsdone){
                        praybuttonsdone = true;
                        color_pray_buttonshandler.sendEmptyMessage(0);
                    }

                    if (rightnowcomparable != rightnowcomparable_temp || (next_adan==0 && positifise > SQLSharing.minute_limit_to_display_positifise+1)){
                        if(next_adan!=-1)
                            temp_positifise = Math.round((prayers.get(next_adan) - rightnowcomparable));

                        if(end_ofD)
                            show_prayallbutton.sendEmptyMessage(0);
                        else
                            hide_prayallbutton.sendEmptyMessage(0);

                        check_next_adan();
                        if(changingD)
                            new_adan = false;

                        if(next_adan!=-1){
                            // this check is to fix the glitch of changing time from 3AM to 11PM instantly, gets stuck on fajr
                            // TODO: might remove idk
                            if (temp_positifise < 0) {
                                handler5.sendEmptyMessage(0);
                                temp_positifise = Math.abs(temp_positifise);
                            }

                            if (next_adan != 0)
                                temp_negatifise = Math.round(Math.abs((rightnowcomparable - prayers.get(next_adan - 1))));
                            else
                                temp_negatifise = Math.round(Math.abs((rightnowcomparable - prayers.get(4))));

                            /*handlos.sendEmptyMessage(0);*/

                            // move to next adan if available
                            if (next_adan != current_displayed_next_adan || end_ofD)
                                handler5.sendEmptyMessage(0);

                            if (next_adan == 0 && current_displayed_next_adan == 0)
                                checkonfajr.sendEmptyMessage(0);

                            display_neg_if_possible();


                            if ((temp_positifise != positifise) && !still_scoping_on_previous_adan) {
                                positifise = temp_positifise;
                                handler3.sendEmptyMessage(0);
                            }
                        }
                    }


                }
            }
        } catch(Exception ignored){} }};

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
        if(!end_ofD)
            InitialDelayForNextAdanAnimation();
    }

    private void clean_titles_and_times() {

        if(darkmode)
            for (int i = 0; i < 5; i++){
                prayerdisplayviews.get(i).setTextColor(getResources().getColor(R.color.white));
                prayerdisplayviews2.get(i).setTextColor(getResources().getColor(R.color.grayerthanwhite));
            }
        else
            for (int i = 0; i < 5; i++) {
                prayerdisplayviews.get(i).setTextColor(getResources().getColor(R.color.white));
                prayerdisplayviews2.get(i).setTextColor(getResources().getColor(R.color.white));
            }
    }

    private void check_next_adan() {
        for(int i=0;i<prayers.size();i++){
            if(rightnowcomparable<prayers.get(0)) {
                temp_next_adan = 0;
                break;
            }
            if(rightnowcomparable>prayers.get(i)) {
                temp_next_adan = i+1;
            }
        }


        if(temp_next_adan>4) {
            end_ofD = true;
            temp_next_adan = 4;
        } else
            end_ofD = false;

        if(temp_next_adan != next_adan) {
            next_adan = temp_next_adan;
            new_adan = true;
        } else
            new_adan = false;

        if(changingD)
            new_adan = true;

    }

    private void display_neg_if_possible() {
        if((temp_negatifise != negatifise || changingD) && next_adan!=0){
            changingD = false;
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

    private int rightnowcomparable_temp=-1;
    private void calculate_rightnowcomparable() {

        Calendar cal = Calendar.getInstance(Locale.US);
        Date todayos = new Date(cal.getTimeInMillis());
        if(!String.valueOf(todayos).split(" ")[2].equals(String.valueOf(CurrentDisplayedDay).split(" ")[2]) || rightnowcomparable==0 || next_adan == -1) {
            CurrentDisplayedDay = todayos;
            String[] temptoday = CurrentDisplayedDay.toString().split(" ");
            currentdisplayeddaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];
            end_ofD = false;
            /*no_newDs = false;*/
            calluse.sendEmptyMessage(0);
        }
        String temptime = String.valueOf(todayos).split(" ")[3];
        rightnowcomparable_temp = rightnowcomparable;
        rightnowcomparable = Integer.parseInt(temptime.split(":")[0]) * 60 + Integer.parseInt(temptime.split(":")[1]);
    }

    private ImageView settingsbutton;
    private void wait_1_second() {
        long futuretime = System.currentTimeMillis() + 1000;

        while (System.currentTimeMillis() < futuretime && running) {
            //prevents multiple threads from crashing into each other
            synchronized (this) {
                try {
                    wait(futuretime - System.currentTimeMillis());
                } catch(Exception ignored){}
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
        settingsbutton = findViewById(R.id.settingsbutton);
            arrowback.setImageDrawable(getResources().getDrawable(R.drawable.backarrowdark));
        try {
            Glide.with(this).load(R.drawable.stats).into(statslogo);
        } catch (Exception ignored) {
            statslogo.setImageDrawable(getResources().getDrawable(R.drawable.stats));
        }
            nightmodebutton.setImageDrawable(getResources().getDrawable(R.drawable.nightmodedark));
            settingsbutton.setImageDrawable(getResources().getDrawable(R.drawable.settings2));
            arrowright.setImageDrawable(getResources().getDrawable(R.drawable.arrowright));
            arrowleft.setImageDrawable(getResources().getDrawable(R.drawable.arrowleft));

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
        /*doublearrowleft = getResources().getDrawable(R.drawable.doublearrowleftt);
        doublearrowright = getResources().getDrawable(R.drawable.doublearrowright);*/


        prayernames = new ArrayList<>();
        prayernames.add(getResources().getString(R.string.fajrtitle));
        prayernames.add(getResources().getString(R.string.dohrtitle));
        prayernames.add(getResources().getString(R.string.asrtitle));
        prayernames.add(getResources().getString(R.string.maghrebtitle));
        prayernames.add(getResources().getString(R.string.ishatitle));

        prayernames_arabe = new ArrayList<>();
        prayernames_arabe.add(getResources().getString(R.string.fajrtitle_arabe));
        prayernames_arabe.add(getResources().getString(R.string.dohrtitle_arabe));
        prayernames_arabe.add(getResources().getString(R.string.asrtitle_arabe));
        prayernames_arabe.add(getResources().getString(R.string.maghrebtitle_arabe));
        prayernames_arabe.add(getResources().getString(R.string.ishatitle_arabe));
    }

    private void hijri_date_setup() {
        if(miladi_month!=0) {
            String[] lel = todaycomparable.split(" ");
            String[] t = PlainDate.of(Integer.parseInt(temptoday[5]), miladi_month, Integer.parseInt(lel[1])) // TODO: fix me Integer.parseInt(lel[5]) + 2000
                    .transform(HijriCalendar.class, HijriCalendar.VARIANT_UMALQURA).toString().split("-");
            t[3] = t[3].replace("[islamic", "");
            hijri_year = Integer.parseInt(t[1]);
            hijri_month = Integer.parseInt(t[2]);
            hijriD = Integer.parseInt(t[3]);
            convert_hijri_to_cute();
        }
    }

    private void convert_hijri_to_cute() {
        if(language.equals(getResources().getString(R.string.ar))){
            hijri = "";
            hijri += hijriD + " ";
            switch(hijri_month) {
                case 1:
                    hijri += getResources().getString(R.string.muharram_arabe);
                    break;
                case 2:
                    hijri += getResources().getString(R.string.safar_arabe);
                    break;
                case 3:
                    hijri += getResources().getString(R.string.rabialawwal_arabe);
                    break;
                case 4:
                    hijri += getResources().getString(R.string.rabialthani_arabe);
                    break;
                case 5:
                    hijri += getResources().getString(R.string.jumadialawwal_arabe);
                    break;
                case 6:
                    hijri += getResources().getString(R.string.jumadialthani_arabe);
                    break;
                case 7:
                    hijri += getResources().getString(R.string.rajab_arabe);
                    break;
                case 8:
                    hijri += getResources().getString(R.string.chaaban_arabe);
                    break;
                case 9:
                    hijri += getResources().getString(R.string.ramadhan_arabe);
                    break;
                case 10:
                    hijri += getResources().getString(R.string.shawwal_arabe);
                    break;
                case 11:
                    hijri += getResources().getString(R.string.dhualqaada_arabe);
                    break;
                case 12:
                    hijri += getResources().getString(R.string.dhualhijja_arabe);
                    break;
            }

            hijri += " " + hijri_year;
        } else {
            hijri = "";
            switch(hijri_month) {
                case 1:
                    hijri += getResources().getString(R.string.muharram);
                    break;
                case 2:
                    hijri += getResources().getString(R.string.safar);
                    break;
                case 3:
                    hijri += getResources().getString(R.string.rabialawwal);
                    break;
                case 4:
                    hijri += getResources().getString(R.string.rabialthani);
                    break;
                case 5:
                    hijri += getResources().getString(R.string.jumadialawwal);
                    break;
                case 6:
                    hijri += getResources().getString(R.string.jumadialthani);
                    break;
                case 7:
                    hijri += getResources().getString(R.string.rajab);
                    break;
                case 8:
                    hijri += getResources().getString(R.string.chaaban);
                    break;
                case 9:
                    hijri += getResources().getString(R.string.ramadhan);
                    break;
                case 10:
                    hijri += getResources().getString(R.string.shawwal);
                    break;
                case 11:
                    hijri += getResources().getString(R.string.dhualqaada);
                    break;
                case 12:
                    hijri += getResources().getString(R.string.dhualhijja);
                    break;
            }

            hijri += " " + hijriD;
            if (hijriD==2 || hijriD==22)
                hijri += "nd";
            else if (hijriD==3 || hijriD==23)
                hijri += "rd";
            else if (hijriD==1 || hijriD==21)
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
        if(language.equals(getResources().getString(R.string.en))) {
            fajrtitle.setText(getResources().getString(R.string.fajrtitle));
            risetitle.setText(getResources().getString(R.string.rise));
            dohrtitle.setText(getResources().getString(R.string.dohrtitle));
            asrtitle.setText(getResources().getString(R.string.asrtitle));
            maghrebtitle.setText(getResources().getString(R.string.maghrebtitle));
            ishatitle.setText(getResources().getString(R.string.ishatitle));
            for(TextView praybutton:praybuttons)
                praybutton.setText(getResources().getString(R.string.joinprayer));
            title.setText(getResources().getString(R.string.force));
            prayedthisdaybefore.setText(getResources().getString(R.string.prayallquestion));
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
                lmfaoimage.setImageDrawable(getResources().getDrawable(R.drawable.lmfao));
            }
        }
    }

    private void low_light_alert() {
        try {
            if (getIntent().getStringExtra("light_alert").equals("yes")) {
                if (language.equals(getResources().getString(R.string.en)))
                    Snackbar.make(full, getString(R.string.low_light), Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(full, getString(R.string.low_light_arabe), Snackbar.LENGTH_LONG).show();
            }
        } catch(Exception ignored){}
    }

    private void location_shit(final Date date) {
        sql(getResources().getString(R.string.justforce));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(force.this);
        if (SQLSharing.mycursorforce.getCount() > 0)
            if_theres_previous_info_load_it_n_display(date);
        else
            new_coordinates = true;
        if_first_launch_get_longitude_n_lattitude_n_ville_n_hijri_date(date);
        close_sql();
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
        longitude = Double.parseDouble(SQLSharing.mycursorforce.getString(1));
        latitude = Double.parseDouble(SQLSharing.mycursorforce.getString(2));
        use(longitude, latitude, new_coordinates, date);
    }

    private void sql(final String table) {
        SQLSharing.TABLE_NAME_INPUTER = table;
        switch (table) {
            case "slat":
                SQLSharing.mydbslat = SQL.getInstance(getApplicationContext());
                SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
                break;
            case "force":
                SQLSharing.mydbforce = SQL.getInstance(getApplicationContext());
                SQLSharing.mycursorforce = SQLSharing.mydbforce.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mydbforce3 = SQL.getInstance(getApplicationContext());
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
                exit();
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
        if(language.equals("ar")){
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.enablelocation_arabe)
                    .setMessage(getString(R.string.locationisoff_arabe))
                    .setPositiveButton(R.string.locationsettings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    })
                    .setNegativeButton(R.string.nothanks_arabe, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            exit();
                        }
                    });
            dialog.show();
        } else if(language.equals("en")){
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.enablelocation)
                    .setMessage(getString(R.string.locationisoff))
                    .setPositiveButton(R.string.locationsettings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            exit();
                        }
                    });
            dialog.show();
        }
    }

    @Override
    protected void onPause() {
        running = false;
        if(receiver!=null)
            unregisterReceiver(receiver);


        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();

        try {
            mythread.join();
        } catch (InterruptedException ignored) {
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

            if ((temp_positifise != positifise || changingD) && !still_scoping_on_previous_adan) {
                print(positifise);
                changingD = false;
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

        load_service();


        //boolean an_alert_to_turn_location_on_was_displayed = false;
        //if(an_alert_to_turn_location_on_was_displayed)
        //    AttemptToGetLocationCoordinates();

    }

    private void close_sql() {
        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
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

        } catch(Exception ignored){} }};

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
        Locale ar = new Locale(getResources().getString(R.string.ar));
        if(language.equals(getResources().getString(R.string.en)))
            geocoder = new Geocoder(this, Locale.US);
        else
            geocoder = new Geocoder(this, ar);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException ignored) {
        }
        try{
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            displaycity.sendEmptyMessage(0);
        } catch(Exception ignored){
            //e.printStackTrace();
        }
    }

    private String city = "";
    private void convert_prayertimes_into_seconds() {

        String pm = getResources().getString(R.string.pm);

        int fajrtemp = Integer.parseInt(fajr.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(fajr.split(" ")[0].split(":")[1]);
        if(fajr.split(" ")[1].equals(getResources().getString(R.string.pmer))|| fajr.split(" ")[1].equals(pm))
            fajrtemp += 720; //12*60
        //Integer risetemp = Integer.parseInt(rise.split(" ")[0].split(":")[0])*3600 + Integer.parseInt(rise.split(" ")[0].split(":")[1])*60;
        int dhuhrtemp = Integer.parseInt(dhuhr.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(dhuhr.split(" ")[0].split(":")[1]);
        if((dhuhr.split(" ")[1].equals(getResources().getString(R.string.pmer)) || dhuhr.split(" ")[1].equals(pm)) && !dhuhr.split(":")[0].equals("12"))
            dhuhrtemp += 720; //12*60
        int asrtemp = Integer.parseInt(asr.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(asr.split(" ")[0].split(":")[1]);
        if(asr.split(" ")[1].equals(getResources().getString(R.string.pmer)) || asr.split(" ")[1].equals(pm))
            asrtemp += 720; //12*60
        int maghribtemp = Integer.parseInt(maghrib.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(maghrib.split(" ")[0].split(":")[1]);
        if(maghrib.split(" ")[1].equals(getResources().getString(R.string.pmer)) || maghrib.split(" ")[1].equals(pm))
            maghribtemp += 720; //12*60
        int ishatemp = Integer.parseInt(isha.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(isha.split(" ")[0].split(":")[1]);
        if(isha.split(" ")[1].equals(getResources().getString(R.string.pmer)) || isha.split(" ")[1].equals(pm))
            ishatemp += 720; //12*60

        prayers.add(fajrtemp);
        prayers.add(dhuhrtemp);
        prayers.add(asrtemp);
        prayers.add(maghribtemp);
        prayers.add(ishatemp);
    }

    private void display_prayer_times() {
        if(changingD){
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
                    if(language.equals(getResources().getString(R.string.en)))
                        fajrtime.setText(fajr);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        fajrtime.setText(tfajr);
                    fajrtime.startAnimation(from_left1);
                    from_left1.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        fajrtime.setVisibility(VISIBLE);
                    }});
                }});
                risetime.startAnimation(to_right2);
                to_right2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    risetime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        risetime.setText(rise);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        risetime.setText(trise);
                    risetime.startAnimation(from_left2);
                    from_left2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        risetime.setVisibility(VISIBLE);
                    }});
                }});
                dhuhrtime.startAnimation(to_right12);
                to_right12.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    dhuhrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        dhuhrtime.setText(dhuhr);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        dhuhrtime.setText(tdhuhr);
                    dhuhrtime.startAnimation(from_left3);
                    from_left3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        dhuhrtime.setVisibility(VISIBLE);
                    }});
                }});
                asrtime.startAnimation(to_right3);
                to_right3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    asrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        asrtime.setText(asr);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        asrtime.setText(tasr);
                    asrtime.startAnimation(from_left4);
                    from_left4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        asrtime.setVisibility(VISIBLE);
                    }});
                }});
                maghribtime.startAnimation(to_right4);
                to_right4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    maghribtime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        maghribtime.setText(maghrib);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        maghribtime.setText(tmaghrib);
                    maghribtime.startAnimation(from_left5);
                    from_left5.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        maghribtime.setVisibility(VISIBLE);
                    }});
                }});
                ishatime.startAnimation(to_right5);
                to_right5.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    ishatime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        ishatime.setText(isha);
                    else if(language.equals(getResources().getString(R.string.ar)))
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
                    if(language.equals(getResources().getString(R.string.en)))
                        fajrtime.setText(fajr);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        fajrtime.setText(tfajr);
                    fajrtime.startAnimation(fromright1);
                    fromright1.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        fajrtime.setVisibility(VISIBLE);
                    }});
                }});
                risetime.startAnimation(toleft2);
                toleft2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    risetime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        risetime.setText(rise);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        risetime.setText(trise);
                    risetime.startAnimation(fromright2);
                    fromright2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        risetime.setVisibility(VISIBLE);
                    }});
                }});
                dhuhrtime.startAnimation(toleft3);
                toleft3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    dhuhrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        dhuhrtime.setText(dhuhr);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        dhuhrtime.setText(tdhuhr);
                    dhuhrtime.startAnimation(fromright3);
                    fromright3.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        dhuhrtime.setVisibility(VISIBLE);
                    }});
                }});
                asrtime.startAnimation(toleft5);
                toleft5.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    asrtime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        asrtime.setText(asr);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        asrtime.setText(tasr);
                    asrtime.startAnimation(fromright4);
                    fromright4.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        asrtime.setVisibility(VISIBLE);
                    }});
                }});
                maghribtime.startAnimation(toleft6);
                toleft6.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    maghribtime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        maghribtime.setText(maghrib);
                    else if(language.equals(getResources().getString(R.string.ar)))
                        maghribtime.setText(tmaghrib);
                    maghribtime.startAnimation(fromright5);
                    fromright5.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                        maghribtime.setVisibility(VISIBLE);
                    }});
                }});
                ishatime.startAnimation(toleft7);
                toleft7.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    ishatime.setVisibility(View.INVISIBLE);
                    if(language.equals(getResources().getString(R.string.en)))
                        ishatime.setText(isha);
                    else if(language.equals(getResources().getString(R.string.ar)))
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
            if(language.equals(getResources().getString(R.string.en))) {
                fajrtime.setText(fajr);
                risetime.setText(rise);
                dhuhrtime.setText(dhuhr);
                asrtime.setText(asr);
                maghribtime.setText(maghrib);
                ishatime.setText(isha);

            } else if(language.equals(getResources().getString(R.string.ar))){ // the arabic am and pm
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

            String[] temp = fajr.split(" ")[0].split(":");
            if(temp[1].length()==1){
                fajr = temp[0] +
                        ":" +
                        "0" +
                        temp[1] +
                        " " +
                        fajr.split(" ")[1];
            }
            temp = rise.split(" ")[0].split(":");
            if(temp[1].length()==1){
                rise = temp[0] +
                        ":" +
                        "0" +
                        temp[1] +
                        " " +
                        rise.split(" ")[1];
            }
            temp = dhuhr.split(" ")[0].split(":");
            if(temp[1].length()==1){
                dhuhr = temp[0] +
                        ":" +
                        "0" +
                        temp[1] +
                        " " +
                        dhuhr.split(" ")[1];
            }
            temp = asr.split(" ")[0].split(":");
            if(temp[1].length()==1){
                asr = temp[0] +
                        ":" +
                        "0" +
                        temp[1] +
                        " " +
                        asr.split(" ")[1];
            }
            temp = maghrib.split(" ")[0].split(":");
            if(temp[1].length()==1){
                maghrib = temp[0] +
                        ":" +
                        "0" +
                        temp[1] +
                        " " +
                        maghrib.split(" ")[1];
            }
            temp = isha.split(" ")[0].split(":");
            if(temp[1].length()==1){
                isha = temp[0] +
                        ":" +
                        "0" +
                        temp[1] +
                        " " +
                        isha.split(" ")[1];
            }
        } catch(Exception ignored){ }


        if(language.equals(getResources().getString(R.string.ar))){ // the arabic am and pm
            String pm = getResources().getString(R.string.pm);
            String am = getResources().getString(R.string.am);
            if(fajr.split(" ")[1].equals("AM") || fajr.split(" ")[1].equals(am)) tfajr = fajr.split(" ")[0] + " " + am;
            else tfajr = fajr.split(" ")[0] + " " + pm;
            if(rise.split(" ")[1].equals("AM") || rise.split(" ")[1].equals(am)) trise = rise.split(" ")[0] + " " + am;
            else trise = rise.split(" ")[0] + " " + pm;
            if(dhuhr.split(" ")[1].equals("AM") || dhuhr.split(" ")[1].equals(am)) tdhuhr = dhuhr.split(" ")[0] + " " + am;
            else tdhuhr = dhuhr.split(" ")[0] + " " + pm;
            if(asr.split(" ")[1].equals("AM") || asr.split(" ")[1].equals(am)) tasr = asr.split(" ")[0] + " " + am;
            else tasr = asr.split(" ")[0] + " " + pm;
            if(maghrib.split(" ")[1].equals("AM") || maghrib.split(" ")[1].equals(am)) tmaghrib = maghrib.split(" ")[0]  + " " + am;
            else tmaghrib = maghrib.split(" ")[0]  + " " + pm;
            if(isha.split(" ")[1].equals("AM") || isha.split(" ")[1].equals(am)) tisha = isha.split(" ")[0] + " " + am;
            else tisha = isha.split(" ")[0] + " " + pm;
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
        if(language.equals(getResources().getString(R.string.en))) {
            tempdatin = temptoday[0];


            if (tempdatin.equals(getResources().getString(R.string.satu))) {
                datin += getResources().getString(R.string.sat);
                friday = false;
                dohrtitle.setText(getResources().getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(getResources().getString(R.string.sunu))) {
                datin += getResources().getString(R.string.sun);
                friday = false;
                dohrtitle.setText(getResources().getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(getResources().getString(R.string.monu))) {
                datin += getResources().getString(R.string.mon);
                friday = false;
                dohrtitle.setText(getResources().getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(getResources().getString(R.string.tueu))) {
                datin += getResources().getString(R.string.tue);
                friday = false;
                dohrtitle.setText(getResources().getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(getResources().getString(R.string.wedu))) {
                datin += getResources().getString(R.string.wed);
                friday = false;
                dohrtitle.setText(getResources().getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(getResources().getString(R.string.thuru))) {
                datin += getResources().getString(R.string.thu);
                friday = false;
                dohrtitle.setText(getResources().getString(R.string.dohrtitle));
            }
            else if (tempdatin.equals(getResources().getString(R.string.fridu))) {
                datin += getResources().getString(R.string.fri);
                friday = true;
                dohrtitle.setText(getResources().getString(R.string.Jamo3a));
            }


            // add week day to hijri date
            //hijri += datin + " ";

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals(getResources().getString(R.string.jan))) {
                datin += getResources().getString(R.string.january);
                miladi_month = 1;
            }
            else if (tempdatin.equals(getResources().getString(R.string.feb))) {
                datin += getResources().getString(R.string.february);
                miladi_month = 2;
            }
            else if (tempdatin.equals(getResources().getString(R.string.mar))) {
                datin += getResources().getString(R.string.march);
                miladi_month = 3;
            }
            else if (tempdatin.equals(getResources().getString(R.string.apr))) {
                datin += getResources().getString(R.string.april);
                miladi_month = 4;
            }
            else if (tempdatin.contains(getResources().getString(R.string.mao))) {
                datin += getResources().getString(R.string.may);
                miladi_month = 5;
            }
            else if (tempdatin.contains(getResources().getString(R.string.june))) {
                datin += getResources().getString(R.string.junee);
                miladi_month = 6;
            }
            else if (tempdatin.contains(getResources().getString(R.string.july))) {
                datin += getResources().getString(R.string.julyy);

                miladi_month = 7;
            }
            else if (tempdatin.equals(getResources().getString(R.string.aug))) {
                miladi_month = 8;
                datin += getResources().getString(R.string.august);
            }
            else if (tempdatin.equals("Sep")) {
                miladi_month = 9;
                datin += getResources().getString(R.string.september);
            }
            else if (tempdatin.equals("Oct")) {
                miladi_month = 10;
                datin += getResources().getString(R.string.october);
            }
            else if (tempdatin.equals("Nov")) {
                miladi_month = 11;
                datin += getResources().getString(R.string.november);
            }
            else if (tempdatin.equals("Dec")) {
                miladi_month = 12;
                datin += getResources().getString(R.string.december);
            }

            tempdatin = temptoday[2];
            int temper = Integer.parseInt(tempdatin);
            datin += " " + temper;
            if (temper==2 || temper==22)
                datin += getResources().getString(R.string.nd);
            else if (temper==3 || temper==23)
                datin += getResources().getString(R.string.rd);
            else if (temper==1 || temper==21)
                datin += getResources().getString(R.string.st);
            else
                datin += getResources().getString(R.string.th);

        } else if(language.equals(getResources().getString(R.string.ar))){
            tempdatin = temptoday[0];


            switch (tempdatin) {
                case "Sat":
                    datin += getResources().getString(R.string.satarabe);
                    friday = false;
                    dohrtitle.setText(getResources().getString(R.string.dohrtitle_arabe));
                    break;
                case "Sun":
                    datin += getResources().getString(R.string.sunarabe);
                    friday = false;
                    dohrtitle.setText(getResources().getString(R.string.dohrtitle_arabe));
                    break;
                case "Mon":
                    datin += getResources().getString(R.string.monarabe);
                    friday = false;
                    dohrtitle.setText(getResources().getString(R.string.dohrtitle_arabe));

                    break;
                case "Tue":
                    datin += getResources().getString(R.string.tuearabe);
                    friday = false;
                    dohrtitle.setText(getResources().getString(R.string.dohrtitle_arabe));
                    break;
                case "Wed":
                    datin += getResources().getString(R.string.wedarabe);
                    friday = false;
                    dohrtitle.setText(getResources().getString(R.string.dohrtitle_arabe));
                    break;
                case "Thu":
                    datin += getResources().getString(R.string.thurarabe);
                    friday = false;
                    dohrtitle.setText(getResources().getString(R.string.dohrtitle_arabe));
                    break;
                case "Fri":
                    datin += getResources().getString(R.string.friarabe);
                    friday = true;
                    dohrtitle.setText(getResources().getString(R.string.friarabe));
                    break;
            }

            //hijri += datin + " ";

            tempdatin = temptoday[2];
            int temper = Integer.parseInt(tempdatin);
            datin += " " + temper;

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals("Jan")) {
                miladi_month = 1;
                datin += getResources().getString(R.string.janarabe);
            }
            else if (tempdatin.equals("Feb")) {
                miladi_month = 2;
                datin += getResources().getString(R.string.febarabe);
            }
            else if (tempdatin.equals("Mar")) {
                miladi_month = 3;
                datin += getResources().getString(R.string.mararabe);
            }
            else if (tempdatin.equals("Apr")) {
                miladi_month = 4;
                datin += getResources().getString(R.string.aprarabe);
            }
            else if (tempdatin.contains("Ma")) {
                miladi_month = 5;
                datin += getResources().getString(R.string.maarabe);
            }
            else if (tempdatin.contains("Jun")) {
                miladi_month = 6;
                datin += getResources().getString(R.string.junarabe);
            }
            else if (tempdatin.contains("Jul")) {
                miladi_month = 7;
                datin += getResources().getString(R.string.jularabe);
            }
            else if (tempdatin.equals("Aug")) {
                miladi_month = 8;
                datin += getResources().getString(R.string.augusarabe);
            }
            else if (tempdatin.equals("Sep")) {
                miladi_month = 9;
                datin += getResources().getString(R.string.separabe);
            }
            else if (tempdatin.equals("Oct")) {
                miladi_month = 10;
                datin += getResources().getString(R.string.octarabe);
            }
            else if (tempdatin.equals("Nov")) {
                miladi_month = 11;
                datin += getResources().getString(R.string.novarabe);
            }
            else if (tempdatin.equals("Dec")) {
                miladi_month = 12;
                datin += getResources().getString(R.string.decarabe);
            }

        }
        datin += " " + temptoday[5];


        hijri_date_setup();
    }

    private void log(Object dumps){
        Log.i("HH", String.valueOf(dumps));

    }


    private void mapActivity() {
        Intent dd = new Intent(this, MapActivity.class);
        startActivity(dd);
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
        processP_request(prayer);

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
            else if(end_ofD){
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
            print3(getResources().getString(R.string.wanttopraythisagain), getResources().getString(R.string.yes), prayer);
        else if(language.equals("ar"))
            print3(getResources().getString(R.string.wanttopraythisagain_arabe), getResources().getString(R.string.yes_arabe), prayer);
    }

    private void theres_still_until_this_prayer(int prayer) {
        if(language.equals("en"))
            print3(getResources().getString(R.string.theresstill) + positifise + getResources().getString(R.string.minutesuntilthisprayer), getResources().getString(R.string.joinprayer), prayer);
        else if(language.equals("ar")){
            String lmao = " " + positifise + " ";
            print3(getResources().getString(R.string.theresstill_arabe) + lmao + getResources().getString(R.string.minutesuntilthisprayer_arabe), getResources().getString(R.string.joinprayer_arabic), prayer);
        }
    }

    private void cannot_pray_futures() {
        if(language.equals(getResources().getString(R.string.en)))
            print2(getResources().getString(R.string.cannot));
        else if(language.equals(getResources().getString(R.string.ar)))
            print2(getResources().getString(R.string.cannot_arabe));
    }

    private void did_you_pray_this_previous_prayer(int prayer) {
        if (language.equals(getResources().getString(R.string.en)))
            print2(getResources().getString(R.string.didyoupray) + " " + prayernames.get(prayer - 1) + getResources().getString(R.string.questionmark));
        else if (language.equals(getResources().getString(R.string.ar)))
            print2(getString(R.string.didyoupray_arabe) + " " + prayernames_arabe.get(prayer - 1) + getResources().getString(R.string.questionmark_arabe));
    }

    private void too_early(int prayer) {
        if (language.equals(getResources().getString(R.string.en)))
            print2(getString(R.string.waytooearly) + " " + prayernames.get(prayer));
        else if (language.equals(getResources().getString(R.string.ar)))
            print2(getString(R.string.waytooearly_arabe) + " " + prayernames_arabe.get(prayer));
    }

    private void display_selection(int prayerer) {
        // prayerer = 0(fajr), 1(dhuhr), 2(asr), 3(maghrib), 4(isha)
        HomeOrMosque mosqueorhome=new HomeOrMosque(this, friday, prayed, todaycomparable, prayerer, darkmode, language, verified, athome, false);
        mosqueorhome.show();
    }

    private void check_ifP_orV_are_empty() {
        if(prayed == null)
            prayed = "00000";

        if(prayed.equals(""))
            prayed = "00000";

        if(verified == null)
            verified = "00000";

        if(verified.equals(""))
            verified = "00000";

        if(athome == null)
            athome = "11111";

        if(athome.equals(""))
            athome = "11111";

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
        sql(getResources().getString(R.string.justforce3));
        if (SQLSharing.mycursorforce3.getCount() > 0) {
            pullP_one_hot_encoding_from_sql();
            check_ifP_orV_are_empty();
            for (int i = 0; i < 5; i++) {
                if (String.valueOf(verified.charAt(i)).equals("1")) {
                    checkmarks.get(i).setVisibility(VISIBLE);
                        checkmarks.get(i).setImageDrawable(getResources().getDrawable(R.drawable.checkmark));
                } else
                    checkmarks.get(i).setVisibility(GONE);
            }
        } else { // else fill it up with zeros and insert
            check_ifP_orV_are_empty();
            /*SQLSharing.mydb.insertPrayed(todaycomparable, prayed, verified, athome);*/
        }
        close_sql();

        // what is soon adan
        what_is_soon_adan_and_one_before_it();

        // color pray buttons
        try {
            color_pray_buttons();
        } catch(Exception e){
            e.printStackTrace();
        }

        // don't display time till next adan if it's at end of day
        if(it_is_today)
            if(!end_ofD)
                InitialDelayForNextAdanAnimation();

    }

    private void color_pray_buttons() {
        if(prayed.length()!=0){

        if(!all_white) {

            if(fill_all){
                if(darkmode) {
                    praybuttonsdone = true;
                    for (int i = 0; i < 5; i++) {
                        if (String.valueOf(prayed.charAt(i)).equals("0")){
                            praybuttons.get(i).setTextColor(getResources().getColor(R.color.lighterred));
                        }else
                            praybuttons.get(i).setTextColor(Color.GREEN);
                    }
                } else {
                    praybuttonsdone = true;
                    for (int i = 0; i < 5; i++) {
                        if (String.valueOf(prayed.charAt(i)).equals("0")) {
                            praybuttons.get(i).setTextColor(WHITE);
                            praybuttons.get(i).setBackground(getResources().getDrawable(R.drawable.lightforcebuttons));
                        } else {
                            praybuttons.get(i).setTextColor(WHITE);
                            praybuttons.get(i).setBackground(getResources().getDrawable(R.drawable.lightforcebuttonsgreen));
                            praybuttons.get(i).setTextColor(Color.BLACK);
                        }
                    }
                }
            } else {
                if (end_ofD) {
                    if(darkmode) {
                        praybuttonsdone = true;
                        for (int i = 0; i < 5; i++) {
                            if (String.valueOf(prayed.charAt(i)).equals("0"))
                                praybuttons.get(i).setTextColor(getResources().getColor(R.color.lighterred));
                            else
                                praybuttons.get(i).setTextColor(Color.GREEN);
                        }
                    } else {
                        praybuttonsdone = true;
                        for (int i = 0; i < 5; i++) {
                            if (String.valueOf(prayed.charAt(i)).equals("0")) {
                                praybuttons.get(i).setTextColor(WHITE);
                                praybuttons.get(i).setBackground(getResources().getDrawable(R.drawable.lightforcebuttons));
                            } else {
                                praybuttons.get(i).setTextColor(WHITE);
                                praybuttons.get(i).setBackground(getResources().getDrawable(R.drawable.lightforcebuttonsgreen));
                                praybuttons.get(i).setTextColor(Color.BLACK);
                            }
                        }
                    }
                } else {
                    if(darkmode && next_adan!=-1) {
                        praybuttonsdone = true;
                        for (int i = 0; i < next_adan; i++) {
                            if (String.valueOf(prayed.charAt(i)).equals("0"))
                                praybuttons.get(i).setTextColor(getResources().getColor(R.color.lighterred));
                            else
                                praybuttons.get(i).setTextColor(Color.GREEN);
                        }

                        for (int i = next_adan; i < 5; i++) {
                            praybuttons.get(i).setTextColor(getResources().getColor(R.color.grayerthanwhite));
                        }

                    } else if(next_adan!=-1){
                        praybuttonsdone = true;
                        for (int i = 0; i < next_adan; i++) {
                            if (String.valueOf(prayed.charAt(i)).equals("0")) {
                                praybuttons.get(i).setTextColor(WHITE);
                                praybuttons.get(i).setBackground(getResources().getDrawable(R.drawable.lightforcebuttons));
                            } else {
                                praybuttons.get(i).setTextColor(WHITE);
                                praybuttons.get(i).setBackground(getResources().getDrawable(R.drawable.lightforcebuttonsgreen));
                                praybuttons.get(i).setTextColor(Color.BLACK);
                            }
                        }


                        for (int i = next_adan; i < 5; i++) {
                            praybuttons.get(i).setTextColor(getResources().getColor(R.color.grayerthanwhite));
                            praybuttons.get(i).setBackground(null);
                        }
                    }

                }
            }

        } else {
            if(darkmode) {
                for (int i = 0; i < 5; i++) {
                    praybuttons.get(i).setTextColor(getResources().getColor(R.color.grayerthanwhite));
                }
            }
            else {
                for (int i = 0; i < 5; i++) {
                    praybuttons.get(i).setTextColor(getResources().getColor(R.color.grayerthanwhite));
                    praybuttons.get(i).setBackground(null);
                }
            }
        }
        }
    }

    private void what_is_soon_adan_and_one_before_it() {
        Calendar cal = Calendar.getInstance(Locale.US);
        String temptime = String.valueOf(new Date(cal.getTimeInMillis())).split(" ")[3];
        rightnowcomparable = Integer.parseInt(temptime.split(":")[0]) * 60 + Integer.parseInt(temptime.split(":")[1]);

        for(int i=0;i<prayers.size();i++){
            if(rightnowcomparable<prayers.get(0)) {
                temp_next_adan = 0;
                break;
            }
            if(rightnowcomparable>prayers.get(i))
                temp_next_adan = i+1;
        }

        /*int previous_adan = 0;*/
        if(temp_next_adan>4) {
            end_ofD = true;
            temp_next_adan = 4;
            /*previous_adan = 0;*/
        } else
            end_ofD = false;

        if(temp_next_adan!=next_adan) {
            new_adan = true;
            next_adan = temp_next_adan;
            //next_adan = 0;
            /*if (next_adan != 0) // so we don't assign -1 to previous_adan
                previous_adan = next_adan - 1;*/
        }

        if(changingD)
            new_adan = true;

    }

    private void processP_request(int compareandy) {
        pullP_one_hot_encoding_from_sql();

        compare(compareandy); // check whether to accept allow request or not
        check_ifP_orV_are_empty();
    }

    private void clean_up() {
        one_of_previous_is_zero = false;
        allow_pray = false;
    }

    private void pullP_one_hot_encoding_from_sql() {
        sql(getResources().getString(R.string.justforce3));
        prayed = "00000";
        verified = "00000";
        athome = "11111";
        while(SQLSharing.mycursorforce3.moveToNext()) {
            if (currentdisplayeddaycomparable.equals(SQLSharing.mycursorforce3.getString(1))){
                prayed = SQLSharing.mycursorforce3.getString(2);
                verified = SQLSharing.mycursorforce3.getString(3);
                athome = SQLSharing.mycursorforce3.getString(4);
                /*print(SQLSharing.mycursor.getColumnCount());*/
                break;
            }
        }
        close_sql();
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
            else
                temp_negatifise = Math.round( Math.abs((rightnowcomparable - prayers.get(4))) );

            String lol;
            if((temp_negatifise != negatifise || changingD) && next_adan!=0){
                negatifise = temp_negatifise;
                if(negatifise <= 30){
                    still_scoping_on_previous_adan = true;
                    find_slider(next_adan, false);
                    if(slider!=null)
                        if(negatifise!=0){
                            lol = getResources().getString(R.string.plusser) + negatifise;
                            slider.setText(lol); // for sm ass reason it's over by 1 min
                            if(can_find_in)
                                handler5.sendEmptyMessage(0);
                        } else
                            begonethot();
                } else
                    still_scoping_on_previous_adan = false;
            }

            if ((temp_positifise != positifise || changingD) && !still_scoping_on_previous_adan){
                positifise = temp_positifise;
                if (positifise < SQLSharing.minute_limit_to_display_positifise) { // TODO: revert this wuz for tasting
                    find_slider(next_adan, false);
                    if(slider!=null)
                        if(positifise!=0){
                            lol = getResources().getString(R.string.minuser) + positifise;
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
        if(!end_ofD){
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
        if (still_scoping_on_previous_adan) {
            if(next_adaner==0)
                temp = 4;
            else
                temp = next_adaner - 1;
        } else
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
                            } catch(Exception ignored){ }
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

    private int get_month2(String month){
        switch (month) {
            case "Jan":
                return 0;
            case "Feb":
                return 1;
            case "Mar":
                return 2;
            case "Apr":
                return 3;
            case "May":
                return 4;
            case "Jun":
                return 5;
            case "Jul":
                return 6;
            case "Aug":
                return 7;
            case "Sep":
                return 8;
            case "Oct":
                return 9;
            case "Nov":
                return 10;
            case "Dec":
                return 11;
            default:
                return 0;
        }
    }

    public void is_it_future_present_or_past(int day, int month, int year){
        Calendar cal = Calendar.getInstance(Locale.US);
        todaysplittemparray = String.valueOf((new Date(cal.getTimeInMillis()))).split(" ");
        int day2 = Integer.parseInt(todaysplittemparray[2]);
        int year2 = Integer.parseInt(todaysplittemparray[5]);
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
            praybutton.setBackground(getResources().getDrawable(R.drawable.forcebuttons));
            praybutton.setTextColor(WHITE);
        }

        full.setBackground(getResources().getDrawable(R.drawable.simpelbackground));
        doublearrowsbackground.setBackground(getResources().getDrawable(R.drawable.lightbacktotoday));
        title.setTextColor(WHITE);
        title.setBackgroundColor(getResources().getColor(R.color.transparentblacker));


        if (fajrtitle.getCurrentTextColor() != Color.GREEN)
            fajrtitle.setTextColor(WHITE);
        risetitle.setTextColor(WHITE);
        if (dohrtitle.getCurrentTextColor() != Color.GREEN)
            dohrtitle.setTextColor(WHITE);
        if (asrtitle.getCurrentTextColor() != Color.GREEN)
            asrtitle.setTextColor(WHITE);
        if (maghrebtitle.getCurrentTextColor() != Color.GREEN)
            maghrebtitle.setTextColor(WHITE);
        if (ishatitle.getCurrentTextColor() != Color.GREEN)
            ishatitle.setTextColor(WHITE);

        daterr.setBackground(getResources().getDrawable(R.drawable.lightdate));
        fajrbackground.setBackground(getResources().getDrawable(R.drawable.lightmultipledayselectionbackground));
        risebackground.setBackground(getResources().getDrawable(R.drawable.lightmultipledayselectionbackground));
        dhuhrbackground.setBackground(getResources().getDrawable(R.drawable.lightmultipledayselectionbackground));
        asrbackground.setBackground(getResources().getDrawable(R.drawable.lightmultipledayselectionbackground));
        maghribbackground.setBackground(getResources().getDrawable(R.drawable.lightmultipledayselectionbackground));
        ishabackground.setBackground(getResources().getDrawable(R.drawable.lightcity));
        yesterdayarrowbackground.setBackground(getResources().getDrawable(R.drawable.lighttmrandyst));
        tommorowarrowbackground.setBackground(getResources().getDrawable(R.drawable.lighttmrandyst));

        rightsideelementsbackground.setBackground(getResources().getDrawable(R.drawable.lightstatsback));
        leftsideelementsbackground.setBackground(getResources().getDrawable(R.drawable.lightbackback));
        try {
            Glide.with(this).load(R.drawable.lightlmfao).into(lmfaoimage);
        } catch (Exception ignored) {
            lmfaoimage.setImageDrawable(getResources().getDrawable(R.drawable.lightlmfao));
        }

        sql(getResources().getString(R.string.slat));
        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToNext();
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("no", ID);
        close_sql();
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

        try{ slider.setTextColor(getResources().getColor(R.color.grayerthanwhite));}catch(Exception ignored){}

        try {
            Glide.with(this).load(R.drawable.lmfao).into(lmfaoimage);
        } catch (Exception ignored) {
            lmfaoimage.setImageDrawable(getResources().getDrawable(R.drawable.lmfao));
        }

        for(TextView praybutton:praybuttons) {
            praybutton.setBackground(getResources().getDrawable(R.drawable.darkbuttons));
            praybutton.setTextColor(getResources().getColor(R.color.grayerthanwhite));
        }

        full.setBackground(getResources().getDrawable(R.drawable.forcefull));
        doublearrowsbackground.setBackground(getResources().getDrawable(R.drawable.backtotoday));
        title.setTextColor(WHITE);
        title.setBackground(null);


        if (fajrtitle.getCurrentTextColor() != Color.GREEN)
            fajrtitle.setTextColor(getResources().getColor(R.color.grayerthanwhite));
        risetitle.setTextColor(getResources().getColor(R.color.grayerthanwhite));
        if (dohrtitle.getCurrentTextColor() != Color.GREEN)
            dohrtitle.setTextColor(getResources().getColor(R.color.grayerthanwhite));
        if (asrtitle.getCurrentTextColor() != Color.GREEN)
            asrtitle.setTextColor(getResources().getColor(R.color.grayerthanwhite));
        if (maghrebtitle.getCurrentTextColor() != Color.GREEN)
            maghrebtitle.setTextColor(getResources().getColor(R.color.grayerthanwhite));
        if (ishatitle.getCurrentTextColor() != Color.GREEN)
            ishatitle.setTextColor(getResources().getColor(R.color.grayerthanwhite));

        yesterdayarrowbackground.setBackground(getResources().getDrawable(R.drawable.tmrandyst));
        tommorowarrowbackground.setBackground(getResources().getDrawable(R.drawable.tmrandyst));
        daterr.setBackground(getResources().getDrawable(R.drawable.date));
        fajrbackground.setBackground(getResources().getDrawable(R.drawable.multipledayselectionbackground));
        risebackground.setBackground(getResources().getDrawable(R.drawable.multipledayselectionbackground));
        dhuhrbackground.setBackground(getResources().getDrawable(R.drawable.multipledayselectionbackground));
        asrbackground.setBackground(getResources().getDrawable(R.drawable.multipledayselectionbackground));
        maghribbackground.setBackground(getResources().getDrawable(R.drawable.multipledayselectionbackground));
        ishabackground.setBackground(getResources().getDrawable(R.drawable.city));

        rightsideelementsbackground.setBackground(getResources().getDrawable(R.drawable.statsback));
        leftsideelementsbackground.setBackground(getResources().getDrawable(R.drawable.backback));
        try {
            Glide.with(this).load(R.drawable.lmfao).into(lmfaoimage);
        } catch (Exception ignored) {
            lmfaoimage.setImageDrawable(getResources().getDrawable(R.drawable.lmfao));
        }

        sql(getResources().getString(R.string.slat));
        SQLSharing.mycursorslat.moveToPosition(1);
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("yes", ID);
        close_sql();
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

    private String currentdisplayeddaycomparable = "";
    public void backtotodayClicked(View view) {

        if(doublearrows.getVisibility()==VISIBLE) {
            can_find_in = false;
            changingD = true;
            new_adan = true;

            Calendar cal = Calendar.getInstance(Locale.US);
            CurrentDisplayedDay = new Date(cal.getTimeInMillis());
            String[] temptoday = CurrentDisplayedDay.toString().split(" ");
            currentdisplayeddaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];
            it_is_today = true;
            all_white = false;
            fill_all = false;

            if(end_ofD)
                prayedthisdaybefore.setVisibility(VISIBLE);
            else
                prayedthisdaybefore.setVisibility(GONE);

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
        changingD = true;
        going_left = false;
        going_right = true;

        todaysplittemparray = CurrentDisplayedDay.toString().split(" ");
        day = Integer.parseInt(todaysplittemparray[2]);
        year = Integer.parseInt(todaysplittemparray[5]);
        month = get_month(todaysplittemparray[1]);

        gc = new GregorianCalendar(year, month-1, day);
        gc.add(Calendar.DATE, 1);
        CurrentDisplayedDay = gc.getTime();
        String[] temptoday = CurrentDisplayedDay.toString().split(" ");
        currentdisplayeddaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];

        todaysplittemparray = CurrentDisplayedDay.toString().split(" ");
        day = Integer.parseInt(todaysplittemparray[2]);
        year = Integer.parseInt(todaysplittemparray[5]);
        month = get_month(todaysplittemparray[1]);

        is_it_future_present_or_past(day, month, year);

        if((it_is_today && end_ofD) || fill_all)
            prayedthisdaybefore.setVisibility(VISIBLE);
        else if(it_is_today || all_white)
            prayedthisdaybefore.setVisibility(GONE);

        location_shit(CurrentDisplayedDay);

        if(!it_is_today) {
            slide_in_dem_dpz();
            for (int i = 0; i < 5; i++) {
                if (prayerdisplayviews.get(i).getCurrentTextColor() == Color.GREEN) {
                    if(darkmode) {
                        prayerdisplayviews.get(i).setTextColor(getResources().getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(getResources().getColor(R.color.grayerthanwhite));
                    } else {
                        prayerdisplayviews.get(i).setTextColor(getResources().getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(getResources().getColor(R.color.white));
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
                    doublearrows.setImageDrawable(getResources().getDrawable(R.drawable.doublearrowright));
                }
            } if(all_white) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowleftt).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(getResources().getDrawable(R.drawable.doublearrowleftt));
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
        String[] temptoday = CurrentDisplayedDay.toString().split(" ");
        currentdisplayeddaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];

        is_it_future_present_or_past(day, month, year);

        if(end_ofD)
            prayedthisdaybefore.setVisibility(VISIBLE);
        else
            prayedthisdaybefore.setVisibility(GONE);

        if(fill_all){
            can_find_in = false;
            new_adan = true;
            changingD = true;
            going_left = true;
            going_right = false;
        } else if(all_white){
            can_find_in = false;
            new_adan = true;
            changingD = true;
            going_left = false;
            going_right = true;
        } if(it_is_today){
            can_find_in = false;
            new_adan = true;
            changingD = false;
            going_left = false;
            going_right = false;
        }

        location_shit(CurrentDisplayedDay);

        if(!it_is_today) {
            slide_in_dem_dpz();
            for (int i = 0; i < 5; i++) {
                if (prayerdisplayviews.get(i).getCurrentTextColor() == Color.GREEN) {
                    if(darkmode) {
                        prayerdisplayviews.get(i).setTextColor(getResources().getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(getResources().getColor(R.color.grayerthanwhite));
                    } else {
                        prayerdisplayviews.get(i).setTextColor(getResources().getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(getResources().getColor(R.color.white));
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
                    doublearrows.setImageDrawable(getResources().getDrawable(R.drawable.doublearrowright));
                }
            } if(all_white) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowleftt).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(getResources().getDrawable(R.drawable.doublearrowleftt));
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
        changingD = true;
        going_left = true;
        going_right = false;

        todaysplittemparray = CurrentDisplayedDay.toString().split(" ");
        day = Integer.parseInt(todaysplittemparray[2]);
        year = Integer.parseInt(todaysplittemparray[5]);
        month = get_month(todaysplittemparray[1]);

        gc = new GregorianCalendar(year, month-1, day);
        gc.add(Calendar.DATE, -1);
        CurrentDisplayedDay = gc.getTime();
        String[] temptoday = CurrentDisplayedDay.toString().split(" ");
        currentdisplayeddaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];

        todaysplittemparray = CurrentDisplayedDay.toString().split(" ");
        day = Integer.parseInt(todaysplittemparray[2]);
        year = Integer.parseInt(todaysplittemparray[5]);
        month = get_month(todaysplittemparray[1]);

        is_it_future_present_or_past(day, month, year);

        if((it_is_today && end_ofD) || fill_all)
            prayedthisdaybefore.setVisibility(VISIBLE);
        else if(it_is_today || all_white)
            prayedthisdaybefore.setVisibility(GONE);

        location_shit(CurrentDisplayedDay);

        if(!it_is_today) {
            slide_in_dem_dpz();
            for (int i = 0; i < 5; i++) {
                if (prayerdisplayviews.get(i).getCurrentTextColor() == Color.GREEN) {
                    if(darkmode) {
                        prayerdisplayviews.get(i).setTextColor(getResources().getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(getResources().getColor(R.color.grayerthanwhite));
                    } else {
                        prayerdisplayviews.get(i).setTextColor(getResources().getColor(R.color.white));
                        prayerdisplayviews2.get(i).setTextColor(getResources().getColor(R.color.white));
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
                    doublearrows.setImageDrawable(getResources().getDrawable(R.drawable.doublearrowright));
                }
            } if(all_white) {
                try {
                    Glide.with(this).load(R.drawable.doublearrowleftt).into(doublearrows);
                } catch (Exception ignored) {
                    doublearrows.setImageDrawable(getResources().getDrawable(R.drawable.doublearrowleftt));
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
        // TODO remove when fixed
        if(Build.VERSION.SDK_INT < stoppableandroid) {
            Intent open_settings = new Intent(this, force_settings.class);
            startActivity(open_settings);
        }
    }

    public void prayedthisdaybeforeClicked(View view) {
        if((it_is_today && end_ofD) || fill_all) {
            HomeOrMosque mosqueorhome = new HomeOrMosque(this, friday, prayed, todaycomparable, 1, darkmode, language, verified, athome, true);
            mosqueorhome.show();
        }
    }
}
