package com.krimzon.scuffedbots.raka3at;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.dialogs.Statistictictictictic;
import net.time4j.PlainDate;
import net.time4j.calendar.HijriCalendar;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import static android.view.animation.AnimationUtils.loadAnimation;


public class force extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    protected LocationManager mLocationManager;
    protected PrayerTimes prayerTimes;

    protected DateComponents date;
    protected Coordinates coordinates;
    protected boolean an_alert_to_turn_location_on_was_displayed = false;

    protected TextView fajrtitle;
    protected TextView dohrtitle;
    protected TextView asrtitle;
    protected TextView maghrebtitle;
    protected TextView ishatitle;
    private TextView fajrtime;
    private TextView dhuhrtime;
    private TextView asrtime;
    private TextView maghribtime;
    private TextView ishatime;
    protected TextView risetitle;
    private TextView risetime;

    protected String fajr;
    protected String rise;
    protected String dhuhr;
    protected String asr;
    protected String maghrib;
    protected String isha;
    protected String timeshape = "hh:mm a";
    protected FusedLocationProviderClient mFusedLocationClient;

    protected Intent MainActivity;
    protected Intent main;
    private CalculationParameters params;
    protected SimpleDateFormat dateFormat;
    protected String pattern = "dd-MMM-yyyy";
    protected boolean new_coordinates = false;

    protected List<Integer> prayers;
    protected String todaycomparable;

    protected RelativeLayout full;
    public static String language = "";

    protected Typeface arabic_typeface;

    protected TextView prayfajr, praydhuhr, prayasr, praymaghrib, prayisha;

    protected TextView title;

    protected Animation zoom_in, zoom_out, zoom_in2, zoom_out2; // for next adan
    protected TextView citydisplay;

    protected TextView daterr;
    private boolean fajr_is_red, dhuhr_is_red, asr_is_red, maghrib_is_red, isha_is_red;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force);


        params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        params = CalculationMethod.EGYPTIAN.getParameters();
        params.madhab = Madhab.SHAFI; // SHAFI made 95% accuracy, HANAFI had 1hour different for l'3asr
        params.adjustments.fajr = 2;
        dateFormat = new SimpleDateFormat(pattern);

        CurrentDisplayedDay = new Date();

        variables_setup();

        fontAndy();

        sql("slat");

        load_data_from_slat_sql();

        languageshet();

        sql("force");

        location_shit(CurrentDisplayedDay);

        low_light_alert();

        //longitude = 30;latitude = 30;use(longitude, latitude, false, new Date());
    }


    public static String hijri = "";
    protected int hijri_month = 0, hijri_year = 0, hijri_day = 0;
    protected String[] lel;
    protected int miladi_month = 0;

    private void hijri_date_setup() {
        if(miladi_month!=0) {
            lel = todaycomparable.split(" ");
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
        if(language.equals("ar")){
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


    protected List<TextView> prayerdisplayviews, prayerdisplayviews2;
    protected Typeface arabic_typeface2, arabic_typeface3;

    private void fontAndy() {
        arabic_typeface = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");
        arabic_typeface2 = Typeface.createFromAsset(getAssets(),  "Tajawal-Regular.ttf");
        arabic_typeface3 = Typeface.createFromAsset(getAssets(),  "Tajawal-Bold.ttf");

        title.setTypeface(arabic_typeface);
        daterr.setTypeface(arabic_typeface);

        prayfajr.setTypeface(arabic_typeface);
        praydhuhr.setTypeface(arabic_typeface);
        prayasr.setTypeface(arabic_typeface);
        praymaghrib.setTypeface(arabic_typeface);
        prayisha.setTypeface(arabic_typeface);
        slider.setTypeface(arabic_typeface);
        slider.setTypeface(arabic_typeface3);
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
        if(language.equals("en")) {
            getStrings();
            fajrtitle.setText(fajrtitlel);
            risetitle.setText(risetitlel);
            dohrtitle.setText(dohrtitlel);
            asrtitle.setText(asrtitlel);
            maghrebtitle.setText(maghrebtitlel);
            ishatitle.setText(ishatitlel);
            prayisha.setText(pray);
            praymaghrib.setText(pray);
            prayasr.setText(pray);
            praydhuhr.setText(pray);
            prayfajr.setText(pray);
            title.setText(force);
        }
    }


    protected Resources resources;
    protected String fajrtitlel;
    protected String risetitlel;
    protected String dohrtitlel;
    protected String asrtitlel;
    protected String maghrebtitlel;
    protected String ishatitlel;
    protected String pm;
    protected String am;
    protected String pray;
    protected String force;

    private void getStrings(){
        fajrtitlel = getString(R.string.fajrtitle);
        risetitlel = getString(R.string.rise);
        dohrtitlel = getString(R.string.dohrtitle);
        asrtitlel = getString(R.string.asrtitle);
        maghrebtitlel = getString(R.string.maghrebtitle);
        ishatitlel = getString(R.string.ishatitle);
        pray = getString(R.string.joinprayer);
        force = getString(R.string.force);
    }


    protected boolean darkmode = true;

    private void load_data_from_slat_sql() {
        SQLSharing.mycursor.moveToPosition(6);
        language = SQLSharing.mycursor.getString(1);

        SQLSharing.mycursor.moveToPosition(1);
        if(SQLSharing.mycursor.getString(1).equals("no"))
            light_mode();
    }


    private void low_light_alert() {
        if(getIntent().getStringExtra("light_alert").equals("yes")) {
            if (language.equals("en"))
                Snackbar.make(full, getString(R.string.low_light), BaseTransientBottomBar.LENGTH_LONG).show();
            else
                Snackbar.make(full, getString(R.string.low_light_arabe), BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }


    private void location_shit(Date date) {
        sql("force");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(SQLSharing.mycursor.getCount()>0)
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


    protected double longitude;
    protected double latitude;

    private void if_theres_previous_info_load_it_n_display(Date date) {
        new_coordinates = false;
        SQLSharing.mycursor.moveToFirst();
        longitude = Double.valueOf(SQLSharing.mycursor.getString(1));
        latitude = Double.valueOf(SQLSharing.mycursor.getString(2));
        use(longitude, latitude, new_coordinates, date);
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


    protected Animation begone;
    protected TextView slider;
    RelativeLayout doublearrowsbackground;
    ImageView doublearrows;
    private void variables_setup() {
        daterr = findViewById(R.id.daterr);
        doublearrowsbackground = findViewById(R.id.doublearrowsbackground);
        slideholder = findViewById(R.id.slideholder);
        slider = findViewById(R.id.slider);
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
        prayfajr = findViewById(R.id.prayfajr);
        praydhuhr = findViewById(R.id.praydhuhr);
        prayasr = findViewById(R.id.prayasr);
        praymaghrib = findViewById(R.id.praymaghrib);
        prayisha = findViewById(R.id.prayisha);

        prayerdisplayviews = new ArrayList<>();
        prayerdisplayviews2 = new ArrayList<>();
        prayerdisplayviews.add(fajrtime);prayerdisplayviews.add(dhuhrtime);prayerdisplayviews.add(asrtime);prayerdisplayviews.add(maghribtime);prayerdisplayviews.add(ishatime);
        prayerdisplayviews2.add(fajrtitle);prayerdisplayviews2.add(dohrtitle);prayerdisplayviews2.add(asrtitle);prayerdisplayviews2.add(maghrebtitle);prayerdisplayviews2.add(ishatitle);
        resources = getResources();
        next_adan_pop_out_large = resources.getDimension(R.dimen.next_adan_pop_out_large); ///TypedValue.COMPLEX_UNIT_PX
        next_adan_pop_out_shrink = resources.getDimension(R.dimen.next_adan_pop_out_shrink); ///TypedValue.COMPLEX_UNIT_PX
        next_adan_size = resources.getDimension(R.dimen.next_adan_size); ///TypedValue.COMPLEX_UNIT_PX
        zoom_in = loadAnimation(this, R.anim.zoom_in);
        begone = loadAnimation(this, R.anim.begone);
        zoom_out = loadAnimation(this, R.anim.zoom_out);
        zoom_in2 = loadAnimation(this, R.anim.zoom_in);
        zoom_out2 = loadAnimation(this, R.anim.zoom_out);
        doublearrowleft = resources.getDrawable(R.drawable.doublearrowleftt);
        doublearrowright = resources.getDrawable(R.drawable.doublearrowright);

    }


    Drawable doublearrowright;
    Drawable doublearrowleft;
    protected float next_adan_pop_out_large, next_adan_pop_out_shrink, next_adan_size, twelve;

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
        MainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainActivity);
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
    protected void onResume() {
        super.onResume();
        if(an_alert_to_turn_location_on_was_displayed)
            AttemptToGetLocationCoordinates();
    }


    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    protected Integer fajrtemp;
    protected Integer dhuhrtemp;
    protected Integer asrtemp;
    protected Integer maghribtemp;
    protected Integer ishatemp;
    protected String tfajr, trise, tdhuhr, tasr, tmaghrib, tisha;
    protected String[] temptoday;
    protected String address = "";
    protected String city = "";
    protected String state = "";
    protected String country = "";
    protected String knownName = "";

    public void use(double longitude, double latitude, boolean new_coordinates, Date today) {

        // declarations
        hijri = "";
        prayers = new ArrayList<>();

        temptoday = today.toString().split(" ");
        todaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5].charAt(2) + temptoday[5].charAt(3);


        //update coords in sql
        update_coords_in_sql(longitude, latitude, new_coordinates);

        // pull date and shape it
        pull_date_and_shape_it(longitude, latitude, today);

        // pull prayer times and shape them
        pull_prayer_times_and_shape_them();

        // display prayer times
        display_prayer_times();

        // convert prayertimes into milliseconds for comparaison and save in prayers Array
        convert_prayertimes_into_milliseconds();

        // pull location to display city or wtvr
        pull_location(longitude, latitude);

        // work on date n then display
        work_on_date_n_display_it();
        display_dates();
        /*datedisplay.setText(datin);*/

        // retrieve prayers array from sql so we color force buttons accordingly
        retrieveAndy();
    }


    private void pull_location(double longitude, double latitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        Locale ar = new Locale(resources.getString(R.string.ar));
        if(language.equals("en"))
            geocoder = new Geocoder(this, Locale.US);
        else
            geocoder = new Geocoder(this, ar);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses!=null) {
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            citydisplay.setText(city);
        }
    }


    private void convert_prayertimes_into_milliseconds() {
        fajrtemp = Integer.valueOf(fajr.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(fajr.split(" ")[0].split(":")[1])*60;
        if(fajr.split(" ")[1].equals(resources.getString(R.string.pmer)))
            fajrtemp += 43200; //12*3600
        //Integer risetemp = Integer.valueOf(rise.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(rise.split(" ")[0].split(":")[1])*60;
        dhuhrtemp = Integer.valueOf(dhuhr.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(dhuhr.split(" ")[0].split(":")[1])*60;
        if(dhuhr.split(" ")[1].equals(resources.getString(R.string.pmer)) && !dhuhr.split(":")[0].equals("12"))
            dhuhrtemp += 43200; //12*3600
        asrtemp = Integer.valueOf(asr.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(asr.split(" ")[0].split(":")[1])*60;
        if(asr.split(" ")[1].equals(resources.getString(R.string.pmer)))
            asrtemp += 43200; //12*3600
        maghribtemp = Integer.valueOf(maghrib.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(maghrib.split(" ")[0].split(":")[1])*60;
        if(maghrib.split(" ")[1].equals(resources.getString(R.string.pmer)))
            maghribtemp += 43200; //12*3600
        ishatemp = Integer.valueOf(isha.split(" ")[0].split(":")[0])*3600 + Integer.valueOf(isha.split(" ")[0].split(":")[1])*60;
        if(isha.split(" ")[1].equals(resources.getString(R.string.pmer)))
            ishatemp += 43200; //12*3600
        prayers.add(fajrtemp);
        /*prayers.add(risetemp);*/
        prayers.add(dhuhrtemp);
        prayers.add(asrtemp);
        prayers.add(maghribtemp);
        prayers.add(ishatemp);
    }


    private void display_prayer_times() {
        if(language.equals("en")) {
            fajrtime.setText(fajr);
            risetime.setText(rise);
            dhuhrtime.setText(dhuhr);
            asrtime.setText(asr);
            maghribtime.setText(maghrib);
            ishatime.setText(isha);
        } else if(language.equals("ar")){ // the arabic am and pm
            fajrtime.setText(tfajr);
            risetime.setText(trise);
            dhuhrtime.setText(tdhuhr);
            asrtime.setText(tasr);
            maghribtime.setText(tmaghrib);
            ishatime.setText(tisha);
        }
    }


    private void pull_prayer_times_and_shape_them() {
        prayerTimes = new PrayerTimes(coordinates, date, params);
        try {
            fajr = DateFormat.format(timeshape, new Date(prayerTimes.fajr.getTime())).toString();
            rise = DateFormat.format(timeshape, new Date(prayerTimes.sunrise.getTime())).toString();
            dhuhr = DateFormat.format(timeshape, new Date(prayerTimes.dhuhr.getTime())).toString();
            asr = DateFormat.format(timeshape, new Date(prayerTimes.asr.getTime())).toString();
            maghrib = DateFormat.format(timeshape, new Date(prayerTimes.maghrib.getTime())).toString();
            isha = DateFormat.format(timeshape, new Date(prayerTimes.isha.getTime())).toString();
        } catch(Exception ignored){}


        if(language.equals("ar")){ // the arabic am and pm
            pm = getString(R.string.pm);
            am = getString(R.string.am);
            if(fajr.split(" ")[1].equals("AM")) tfajr = fajr.split(" ")[0] + " " + am;else tfajr = fajr.split(" ")[0] + " " +  pm;
            if(rise.split(" ")[1].equals("AM")) trise = rise.split(" ")[0] + " " + am;else trise = rise.split(" ")[0] + " " +  pm;
            if(dhuhr.split(" ")[1].equals("AM")) tdhuhr = dhuhr.split(" ")[0] + " " + am;else tdhuhr = dhuhr.split(" ")[0] + " " +  pm;
            if(asr.split(" ")[1].equals("AM")) tasr = asr.split(" ")[0] + " " + am;else tasr = asr.split(" ")[0] + " " +  pm;
            if(maghrib.split(" ")[1].equals("AM")) tmaghrib = maghrib.split(" ")[0] + " " + am;else tmaghrib = maghrib.split(" ")[0] + " " +  pm;
            if(isha.split(" ")[1].equals("AM")) tisha = isha.split(" ")[0] + " " + am;else tisha = isha.split(" ")[0] + " " +  pm;
            fajrtime.setText(tfajr);
            risetime.setText(trise);
            dhuhrtime.setText(tdhuhr);
            asrtime.setText(tasr);
            maghribtime.setText(tmaghrib);
            ishatime.setText(tisha);
        }
    }


    private void update_coords_in_sql(double longitude, double latitude, boolean new_coordinates) {
        if(new_coordinates)
            SQLSharing.mydb.insertMawa9it(String.valueOf(longitude), String.valueOf(latitude));
        else
            SQLSharing.mydb.updateMawa9it("1", String.valueOf(longitude), String.valueOf(latitude));
    }


    private void pull_date_and_shape_it(double longitude, double latitudeDate, Date today) {
        coordinates = new Coordinates(latitude, longitude);
        date = DateComponents.from(today);
    }


    private void display_dates() {
        daterr.setText(datin + '\n' + hijri);
    }


    public static String datin = "";
    protected String tempdatin = "";

    private void work_on_date_n_display_it() {
        datin = "";
        tempdatin = "";
        if(language.equals("en")) {
            tempdatin = temptoday[0];
            if (tempdatin.equals(resources.getString(R.string.satu)))
                datin += resources.getString(R.string.sat);
            else if (tempdatin.equals(resources.getString(R.string.sunu)))
                datin += resources.getString(R.string.sun);
            else if (tempdatin.equals(resources.getString(R.string.monu)))
                datin += resources.getString(R.string.mon);
            else if (tempdatin.equals(resources.getString(R.string.tueu)))
                datin += resources.getString(R.string.tue);
            else if (tempdatin.equals(resources.getString(R.string.wedu)))
                datin += resources.getString(R.string.wed);
            else if (tempdatin.equals(resources.getString(R.string.thuru)))
                datin += resources.getString(R.string.thu);
            else if (tempdatin.equals(resources.getString(R.string.fridu))) {
                datin += resources.getString(R.string.fri);
                dohrtitle.setText(resources.getString(R.string.Jamo3a));
            }


            // add week day to hijri date
            //hijri += datin + " ";

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals(resources.getString(R.string.jan))) {
                datin += "January";
                miladi_month = 1;
            }
            else if (tempdatin.equals(resources.getString(R.string.feb))) {
                datin += "February";
                miladi_month = 2;
            }
            else if (tempdatin.equals(resources.getString(R.string.mar))) {
                datin += "March";
                miladi_month = 3;
            }
            else if (tempdatin.equals(resources.getString(R.string.apr))) {
                datin += "April";
                miladi_month = 4;
            }
            else if (tempdatin.contains(resources.getString(R.string.mao))) {
                datin += "May";
                miladi_month = 5;
            }
            else if (tempdatin.contains(resources.getString(R.string.june))) {
                datin += "June";
                miladi_month = 6;
            }
            else if (tempdatin.contains(resources.getString(R.string.july))) {
                datin += "July";

                miladi_month = 7;
            }
            else if (tempdatin.equals(resources.getString(R.string.aug))) {
                miladi_month = 8;
                datin += "August";
            }
            else if (tempdatin.equals("Sep")) {
                miladi_month = 9;
                datin += "September";
            }
            else if (tempdatin.equals("Oct")) {
                miladi_month = 10;
                datin += "October";
            }
            else if (tempdatin.equals("Nov")) {
                miladi_month = 11;
                datin += "November";
            }
            else if (tempdatin.equals("Dec")) {
                miladi_month = 12;
                datin += "December";
            }

            tempdatin = temptoday[2];
            int temper = Integer.valueOf(tempdatin);
            datin += " " + String.valueOf(temper);
            if (temper==2 || temper==22)
                datin += "nd";
            else if (temper==3 || temper==23)
                datin += "rd";
            else if (temper==1 || temper==21)
                datin += "st";
            else
                datin += "th";

        } else if(language.equals("ar")){
            tempdatin = temptoday[0];
            switch (tempdatin) {
                case "Sat":
                    datin += resources.getString(R.string.satarabe);
                    break;
                case "Sun":
                    datin += resources.getString(R.string.sunarabe);
                    break;
                case "Mon":
                    datin += resources.getString(R.string.monarabe);
                    break;
                case "Tue":
                    datin += resources.getString(R.string.tuearabe);
                    break;
                case "Wed":
                    datin += resources.getString(R.string.wedarabe);
                    break;
                case "Thu":
                    datin += resources.getString(R.string.thurarabe);
                    break;
                case "Fri":
                    datin += resources.getString(R.string.friarabe);
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


    private void print(Object dumps) {
        Toast.makeText(getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_LONG).show();
    }


    protected String temptime;
    protected Integer rightnowcomparable;
    protected List<String> forces;
    protected String prayed = "";
    protected boolean found_prayed_history_in_sql = false;
    protected Integer clickedprayertime;
    protected boolean one_of_previous_is_zero = false;
    protected String tempo;

    private void back_to_main() {
        main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }


    public void ishaClicked(View view) {
        if(prayisha.getCurrentTextColor()==resources.getColor(R.color.lighterred) ||  isha_is_red) {
            process_prayed_request(4);

            if (allow_pray)
                send(4);
            else
                clean_up();
        }
    }


    public void maghribClicked(View view) {
        if(praymaghrib.getCurrentTextColor()==resources.getColor(R.color.lighterred) || maghrib_is_red) {
            process_prayed_request(3);

            if (allow_pray)
                send(3);
            else
                clean_up();
        }
    }


    public void asrClicked(View view) {
        if(prayasr.getCurrentTextColor()==resources.getColor(R.color.lighterred) || asr_is_red) {
            process_prayed_request(2);

            if (allow_pray)
                send(2);
            else
                clean_up();
        }
    }


    public void dhuhrClicked(View view) {
        if(praydhuhr.getCurrentTextColor()==resources.getColor(R.color.lighterred) || dhuhr_is_red) {
            process_prayed_request(1);

            if (allow_pray)
                send(1);
            else
                clean_up();
        }
    }


    public void fajrClicked(View view) {
        if(prayfajr.getCurrentTextColor()==resources.getColor(R.color.lighterred) ||
                fajr_is_red) {
            process_prayed_request(0);

            if (allow_pray)
                send(0);
            else
                clean_up();
        }
    }


    private void send(int prayer){
        Intent slatIntent = new Intent(this, slat.class);
        slatIntent.putExtra("sender", "force");
        slatIntent.putExtra("prayer", String.valueOf(prayer));
        slatIntent.putExtra("todaycomparable", todaycomparable);
        slatIntent.putExtra("prayed", prayed);
        startActivity(slatIntent);
        finish();
    }


    private void fill_up_prayed() {
        prayed = "";
        for(int i=0; i<forces.size(); i++)
            prayed += forces.get(i);
    }


    protected boolean allow_pray = false;

    private void compare(int i) {
        clickedprayertime = prayers.get(i);

        for (int j = 0; j < i; j++) { // check if all prayers have been clicked
            if (forces.get(j).equals("0")) {
                one_of_previous_is_zero = true;
                break;
            }
        }
        if(!one_of_previous_is_zero)
            allow_pray = true;

        if(forces.get(i).equals("1")) // don't allow repraying already prayed ones
            allow_pray = false;
    }


    protected String temper;
    protected List<TextView> praybuttons;

    private void retrieveAndy(){

        // prepare sql and variables
        retrieveAndyVariableSetup();

        // if theres smt in sql then  look up  prayed
        if(SQLSharing.mycursor.getCount()>0)
            pull_prayed_array_from_sql();
        else { // else fill it up with zeros and insert
            fill_up_prayed();
            SQLSharing.mydb.insertPrayed(todaycomparable, prayed);
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


    boolean it_is_today = true;

    private void retrieveAndyVariableSetup() {
        forces = new ArrayList<>();
        praybuttons = new ArrayList<>();
        praybuttons.add(prayfajr); praybuttons.add(praydhuhr); praybuttons.add(prayasr); praybuttons.add(praymaghrib); praybuttons.add(prayisha);
        forces.add("0");forces.add("0");forces.add("0");forces.add("0");forces.add("0");
        sql("force2");
    }


    private void pull_prayed_array_from_sql() {
        while(SQLSharing.mycursor.moveToNext()) {
            if(SQLSharing.mycursor.getString(1).equals(todaycomparable)) {
                forces = new ArrayList<>();
                temper = SQLSharing.mycursor.getString(2);
                for(int i=0;i<temper.length();i++)
                    forces.add( Character.toString(temper.charAt(i)) );
                break;
            }
        }
    }

    Drawable lightforcebuttons;
    Drawable lightforcebuttonsgreen;
    List<Drawable> lightforcebuttonsarray;
    List<Drawable> lightforcebuttonsgreenarray;
    boolean  onlyfillonce = true;
    Drawable t1, t2, t3, t4, t5, t6, t7, t8, t9, t10;
    private void color_pray_buttons() {

        if(onlyfillonce){
            lightforcebuttonsarray = new ArrayList<>();
            lightforcebuttonsgreenarray = new ArrayList<>();
            onlyfillonce = false;
            t1 = resources.getDrawable(R.drawable.lightforcebuttons);
            t2 = resources.getDrawable(R.drawable.lightforcebuttons);
            t3 = resources.getDrawable(R.drawable.lightforcebuttons);
            t4 = resources.getDrawable(R.drawable.lightforcebuttons);
            t5 = resources.getDrawable(R.drawable.lightforcebuttons);
            lightforcebuttonsarray.add(t1);
            lightforcebuttonsarray.add(t2);
            lightforcebuttonsarray.add(t3);
            lightforcebuttonsarray.add(t4);
            lightforcebuttonsarray.add(t5);
            t6 = resources.getDrawable(R.drawable.lightforcebuttonsgreen);
            t7 = resources.getDrawable(R.drawable.lightforcebuttonsgreen);
            t8 = resources.getDrawable(R.drawable.lightforcebuttonsgreen);
            t9 = resources.getDrawable(R.drawable.lightforcebuttonsgreen);
            t10 = resources.getDrawable(R.drawable.lightforcebuttonsgreen);
            lightforcebuttonsgreenarray.add(t6);
            lightforcebuttonsgreenarray.add(t7);
            lightforcebuttonsgreenarray.add(t8);
            lightforcebuttonsgreenarray.add(t9);
            lightforcebuttonsgreenarray.add(t10);
        }

        if(!all_white) {

            if(fill_all){
                if(darkmode) {
                    for (int i = 0; i < 5; i++) {
                        if(i==0)
                            fajr_is_red = false;
                        if(i==1)
                            dhuhr_is_red = false;
                        if(i==2)
                            asr_is_red = false;
                        if(i==3)
                            maghrib_is_red = false;
                        if(i==4)
                            isha_is_red = false;
                        if (forces.get(i).equals("0")){
                            praybuttons.get(i).setTextColor(resources.getColor(R.color.lighterred));
                        }else
                            praybuttons.get(i).setTextColor(Color.GREEN);
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        if (forces.get(i).equals("0")) {
                            if(i==0)
                                fajr_is_red = true;
                            if(i==1)
                                dhuhr_is_red = true;
                            if(i==2)
                                asr_is_red = true;
                            if(i==3)
                                maghrib_is_red = true;
                            if(i==4)
                                isha_is_red = true;
                            praybuttons.get(i).setTextColor(Color.WHITE);
                            praybuttons.get(i).setBackground(lightforcebuttonsarray.get(i));
                        } else {
                            if(i==0)
                                fajr_is_red = false;
                            if(i==1)
                                dhuhr_is_red = false;
                            if(i==2)
                                asr_is_red = false;
                            if(i==3)
                                maghrib_is_red = false;
                            if(i==4)
                                isha_is_red = false;
                            praybuttons.get(i).setTextColor(Color.WHITE);
                            praybuttons.get(i).setBackground(lightforcebuttonsgreenarray.get(i));
                        }
                    }
                }
            } else {
                if (end_of_day) {
                    if(darkmode) {
                        for (int i = 0; i < 5; i++) {
                            if(i==0)
                                fajr_is_red = false;
                            if(i==1)
                                dhuhr_is_red = false;
                            if(i==2)
                                asr_is_red = false;
                            if(i==3)
                                maghrib_is_red = false;
                            if(i==4)
                                isha_is_red = false;
                            if (forces.get(i).equals("0"))
                                praybuttons.get(i).setTextColor(resources.getColor(R.color.lighterred));
                            else
                                praybuttons.get(i).setTextColor(Color.GREEN);
                        }
                    } else {
                        for (int i = 0; i < 5; i++) {
                            if (forces.get(i).equals("0")) {
                                if(i==0)
                                    fajr_is_red = true;
                                if(i==1)
                                    dhuhr_is_red = true;
                                if(i==2)
                                    asr_is_red = true;
                                if(i==3)
                                    maghrib_is_red = true;
                                if(i==4)
                                    isha_is_red = true;
                                praybuttons.get(i).setTextColor(Color.WHITE);
                                praybuttons.get(i).setBackground(lightforcebuttonsarray.get(i));
                            } else {
                                if(i==0)
                                    fajr_is_red = false;
                                if(i==1)
                                    dhuhr_is_red = false;
                                if(i==2)
                                    asr_is_red = false;
                                if(i==3)
                                    maghrib_is_red = false;
                                if(i==4)
                                    isha_is_red = false;
                                praybuttons.get(i).setTextColor(Color.WHITE);
                                praybuttons.get(i).setBackground(lightforcebuttonsgreenarray.get(i));
                            }
                        }
                    }
                } else {
                    if(darkmode) {
                        for (int i = 0; i < next_adan; i++) {
                            if(i==0)
                                fajr_is_red = false;
                            if(i==1)
                                dhuhr_is_red = false;
                            if(i==2)
                                asr_is_red = false;
                            if(i==3)
                                maghrib_is_red = false;
                            if(i==4)
                                isha_is_red = false;
                            if (forces.get(i).equals("0"))
                                praybuttons.get(i).setTextColor(resources.getColor(R.color.lighterred));
                            else
                                praybuttons.get(i).setTextColor(Color.GREEN);
                        }
                    } else {
                        for (int i = 0; i < next_adan; i++) {
                            if (forces.get(i).equals("0")) {
                                if(i==0)
                                    fajr_is_red = true;
                                if(i==1)
                                    dhuhr_is_red = true;
                                if(i==2)
                                    asr_is_red = true;
                                if(i==3)
                                    maghrib_is_red = true;
                                if(i==4)
                                    isha_is_red = true;
                                praybuttons.get(i).setTextColor(Color.WHITE);
                                praybuttons.get(i).setBackground(lightforcebuttonsarray.get(i));
                            } else {
                                if(i==0)
                                    fajr_is_red = false;
                                if(i==1)
                                    dhuhr_is_red = false;
                                if(i==2)
                                    asr_is_red = false;
                                if(i==3)
                                    maghrib_is_red = false;
                                if(i==4)
                                    isha_is_red = false;
                                praybuttons.get(i).setTextColor(Color.WHITE);
                                praybuttons.get(i).setBackground(lightforcebuttonsgreenarray.get(i));
                            }
                        }


                        if(darkmode)
                            for(int i = next_adan; i < 5; i++) {
                                if(i==0)
                                    fajr_is_red = false;
                                if(i==1)
                                    dhuhr_is_red = false;
                                if(i==2)
                                    asr_is_red = false;
                                if(i==3)
                                    maghrib_is_red = false;
                                if(i==4)
                                    isha_is_red = false;
                                praybuttons.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                            }
                        else {
                            for (int i = next_adan; i < 5; i++) {
                                if(i==0)
                                    fajr_is_red = false;
                                if(i==1)
                                    dhuhr_is_red = false;
                                if(i==2)
                                    asr_is_red = false;
                                if(i==3)
                                    maghrib_is_red = false;
                                if(i==4)
                                    isha_is_red = false;
                                praybuttons.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                                praybuttons.get(i).setBackground(null);
                            }
                        }

                    }

                }
            }

        } else {
            if(darkmode) {
                for (int i = 0; i < 5; i++) {
                    if(i==0)
                        fajr_is_red = false;
                    if(i==1)
                        dhuhr_is_red = false;
                    if(i==2)
                        asr_is_red = false;
                    if(i==3)
                        maghrib_is_red = false;
                    if(i==4)
                        isha_is_red = false;
                    praybuttons.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                }
            }
            else {
                for (int i = 0; i < 5; i++) {
                    if(i==0)
                        fajr_is_red = false;
                    if(i==1)
                        dhuhr_is_red = false;
                    if(i==2)
                        asr_is_red = false;
                    if(i==3)
                        maghrib_is_red = false;
                    if(i==4)
                        isha_is_red = false;
                    praybuttons.get(i).setTextColor(resources.getColor(R.color.grayerthanwhite));
                    praybuttons.get(i).setBackground(null);
                }
            }
        }

    }


    protected int normal_text_size = 19;
    protected int difference_in_scale = 4;
    protected TextView temp_next_adan_textview, temp_next_adan_textview2;
    protected int next_adan = 0;
    protected int temp_next_adan = 0;
    protected boolean new_adan = false;
    protected int previous_adan = 0;
    protected boolean end_of_day = false;

    private void what_is_soon_adan_and_one_before_it() {
        temptime = String.valueOf(new Date()).split(" ")[3];
        rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 3600 + Integer.valueOf(temptime.split(":")[1]) * 60 + Integer.valueOf(temptime.split(":")[2]);

        for(int i=0;i<prayers.size();i++){
            if(rightnowcomparable>prayers.get(i))
                temp_next_adan = i+1;
        }

        if(temp_next_adan==5) {
            end_of_day = true;
            temp_next_adan = 0;
            previous_adan = 0;
        } else
            end_of_day = false;

        if(temp_next_adan!=next_adan) {
            new_adan = true;
            next_adan = temp_next_adan;
            //next_adan = 0;
            if (next_adan != 0) // so we don't assign -1 to previous_adan
                previous_adan = next_adan - 1;
        }

        // apply vision onto all prayers only if changed
        for (int i = 0; i < prayerdisplayviews.size(); i += 1) {
                prayerdisplayviews2.get(i).setTextSize(normal_text_size-difference_in_scale); // 16
                prayerdisplayviews.get(i).setTextSize(normal_text_size-difference_in_scale);
        }
        // don't forget mi
        risetitle.setTextSize(normal_text_size-difference_in_scale); // 16
        risetime.setTextSize(normal_text_size-difference_in_scale);

    }


    private void process_prayed_request(int compareandy) {
        forces = new ArrayList<>();
        forces.add("0");forces.add("0");forces.add("0");forces.add("0");forces.add("0");

        pull_prayed_one_hot_encoding_from_sql();

        compare(compareandy); // check whether to accept allow request or not
        fill_up_prayed();
    }


    private void clean_up() {
        if(SQLSharing.mycursor!=null && SQLSharing.mydb!=null) {
            SQLSharing.mycursor.close();
            SQLSharing.mydb.close();
        }
        prayed = "";
        one_of_previous_is_zero = false;
        found_prayed_history_in_sql = false;
        allow_pray = false;
    }


    private void pull_prayed_one_hot_encoding_from_sql() {
        sql("force2");
        if(SQLSharing.mycursor.getCount()<=0)
            found_prayed_history_in_sql = false;
        else {
            while(SQLSharing.mycursor.moveToNext()) {
                if (todaycomparable.equals(SQLSharing.mycursor.getString(1))){
                    forces = new ArrayList<>();
                    tempo = SQLSharing.mycursor.getString(2);
                    forces.add(Character.toString(tempo.charAt(0)));
                    forces.add(Character.toString(tempo.charAt(1)));
                    forces.add(Character.toString(tempo.charAt(2)));
                    forces.add(Character.toString(tempo.charAt(3)));
                    forces.add(Character.toString(tempo.charAt(4)));
                    found_prayed_history_in_sql = true;
                    break;
                }
            }
        }
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(it_is_today) {
                one_of_prayers_is_prolly_larger_size = true;
                animatenextadan();
            }
        }
    };


    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(it_is_today)
                slide_in_dem_dpz();
        }
    };


    protected boolean one_of_prayers_is_prolly_larger_size = false;
    private void animatenextadan() {
        temp_next_adan_textview = prayerdisplayviews.get(next_adan);
        temp_next_adan_textview2 = prayerdisplayviews2.get(next_adan);
        temp_next_adan_textview2.setTextSize(normal_text_size); // 23
        temp_next_adan_textview.setTextSize(normal_text_size);  // to prepare for animation
        temp_next_adan_textview.startAnimation(zoom_in);
        zoom_in.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) { }@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            if(it_is_today){
                temp_next_adan_textview.setTextSize((float) (normal_text_size * 1.06));
                temp_next_adan_textview.setTextColor(Color.GREEN);
                temp_next_adan_textview.startAnimation(zoom_out);
            }
            zoom_out.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                if(it_is_today)
                    temp_next_adan_textview.setTextSize(normal_text_size);
            }});
        }});
        temp_next_adan_textview2.startAnimation(zoom_in2);
        zoom_in2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) { }@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            if(it_is_today) {
                temp_next_adan_textview2.setTextSize((float) (normal_text_size * 1.06));
                temp_next_adan_textview2.setTextColor(Color.GREEN);
                temp_next_adan_textview2.startAnimation(zoom_out2);
            }
            zoom_out2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                if(it_is_today) {
                    temp_next_adan_textview2.setTextSize(normal_text_size);

                    slide_in_dem_dpz();
                }
            }});
        }});
    }


    protected Animation fromfajrtolol;
    protected RelativeLayout slideholder;
    protected int positifise = 0;
    protected int lol = 0;

    private void slide_in_dem_dpz() {
        if(it_is_today) {
            lol = ((prayers.get(next_adan) - rightnowcomparable) / 60 - 1);
            if (lol < 0) positifise = -lol;
            else positifise = lol;

            if (lol < 99) {

                slider.setText("- " + positifise); // for sm ass reason it's over by 1 min
                if (next_adan == 0)
                    fromfajrtolol = loadAnimation(this, R.anim.fromfajrtofajr);
                else if (next_adan == 1)
                    fromfajrtolol = loadAnimation(this, R.anim.fromfajrtodhuhr);
                else if (next_adan == 2)
                    fromfajrtolol = loadAnimation(this, R.anim.frofajrtoasr);
                else if (next_adan == 3)
                    fromfajrtolol = loadAnimation(this, R.anim.fromfajrtomaghrib);
                else if (next_adan == 4)
                    fromfajrtolol = loadAnimation(this, R.anim.fromfajrtoisha);

                fromfajrtolol.setFillAfter(true);
                slideholder.startAnimation(fromfajrtolol);
                fromfajrtolol.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (it_is_today)
                            slideholder.setVisibility(View.VISIBLE);
                    }
                });
            }
        } else {
            slideholder.setVisibility(View.INVISIBLE);
            slideholder.startAnimation(begone);
            begone.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    slideholder.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!it_is_today)
                        slideholder.setVisibility(View.INVISIBLE);
                }
            });
        }

    }


    public void InitialDelayForNextAdanAnimation(){

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
                        } catch( Exception ignored){}
                    }
                }

                //run the handler
                handler.sendEmptyMessage(0);
            }
        };

        //anti lag
        Thread mythread = new Thread(r); //to thread the runnable object we launched
        mythread.start();
    }


    public void SecondaryDelayForDelayDisplay(){

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
                        } catch( Exception ignored){}
                    }
                }

                //run the handler
                handler2.sendEmptyMessage(0);
            }
        };

        //anti lag
        Thread mythread = new Thread(r); //to thread the runnable object we launched
        mythread.start();
    }


    public int get_month(String month){
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


    private GregorianCalendar gc;
    Date CurrentDisplayedDay;
    int day, year, month = 0;
    String[] todaysplittemparray;
    boolean all_white = false;
    int day2, month2, year2 = 0;
    boolean fill_all = false;

    public void is_it_future_present_or_past(){
        todaysplittemparray = String.valueOf((new Date())).split(" ");
        day2 = Integer.valueOf(todaysplittemparray[2]);
        year2 = Integer.valueOf(todaysplittemparray[5]);
        month2 = get_month(todaysplittemparray[1]);

        // compare between current day and currently displayed day

        if(year2 < year){
            all_white = true;
            fill_all = false;
            it_is_today = false;
        } else if(month2 < month && year2 <= year){
            all_white = true;
            fill_all = false;
            it_is_today = false;
        } else if(day2 < day && month2 <= month && year2 <= year){
            all_white = true;
            fill_all = false;
            it_is_today = false;
        } else if(day2 == day && month2 == month && year2 == year){
            all_white = false;
            fill_all = false;
            it_is_today = true;
        } else if(year2 > year){
            all_white = false;
            fill_all = true;
            it_is_today = false;
        } else if(month2 > month && year2 >= year){
            all_white = false;
            fill_all = true;
            it_is_today = false;
        } else if(day2 > day && month2 >= month && year2 >= year){
            all_white = false;
            fill_all = true;
            it_is_today = false;
        }

    }


    public void checkTommorow(View view) {

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

        is_it_future_present_or_past();

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

            if(temp_next_adan_textview2!=null) {
                temp_next_adan_textview2.setTextSize(normal_text_size - difference_in_scale); // 23
                temp_next_adan_textview.setTextSize(normal_text_size - difference_in_scale);  // to prepare for animation
            }

            // Display double arrows that take you back to today
            doublearrowsbackground.setVisibility(View.VISIBLE);
            if(fill_all)
                doublearrows.setImageDrawable(doublearrowright);
            if(all_white)
                doublearrows.setImageDrawable(doublearrowleft);
        } else {
            // Hide double arrows that take you back to today
            doublearrowsbackground.setVisibility(View.INVISIBLE);
        }
    }


    public void checkYesterday(View view) {

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

        is_it_future_present_or_past();

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

            if(one_of_prayers_is_prolly_larger_size) {
                temp_next_adan_textview2.setTextSize(normal_text_size - difference_in_scale); // 23
                temp_next_adan_textview.setTextSize(normal_text_size - difference_in_scale);  // to prepare for animation
            }

            // Display double arrows that take you back to today
            doublearrowsbackground.setVisibility(View.VISIBLE);
            if(fill_all)
                doublearrows.setImageDrawable(doublearrowright);
            if(all_white)
                doublearrows.setImageDrawable(doublearrowleft);
        } else {
            // Hide double arrows that take you back to today
            doublearrowsbackground.setVisibility(View.INVISIBLE);
        }

    }


    public void ShowStatisticsClicked(View view) {
        Statistictictictictic statisticstab=new Statistictictictictic(this);
        statisticstab.show();
    }


    public void arrowbackClicked(View view) {
        Intent out = new Intent(this, MainActivity.class);
        startActivity(out);
        finish();
    }


    public void nightmodeClicked(View view) {
        if(darkmode)
            light_mode();
        else
            dark_mode();

        color_pray_buttons();
    }


    Drawable forcebuttons, forcebuttons2, forcebuttons3, forcebuttons4, forcebuttons5;
    Drawable lightdate;
    Drawable lightcity;
    Drawable simpelbackground;
    RelativeLayout datebackground;
    LinearLayout fajrbackground;
    LinearLayout risebackground;
    LinearLayout dhuhrbackground;
    LinearLayout asrbackground;
    LinearLayout maghribbackground;
    LinearLayout ishabackground;
    RelativeLayout yesterdayarrowbackground;
    RelativeLayout tommorowarrowbackground;
    LinearLayout rightsideelementsbackground;
    Drawable lightmultipledayselectionbackground;
    Drawable lightbackback;
    Drawable lightstatsback;
    RelativeLayout backarrowbackground;
    Drawable lightlmfao;
    ImageView lmfaoimage;
    int transparentblacker;
    int grayerthanwhite;
    boolean onlyonceu = true;

    private void light_mode() {
        darkmode = false;

        if(onlyonceu) {
            onlyonceu = false;
            forcebuttons = resources.getDrawable(R.drawable.forcebuttons);
            forcebuttons2 = resources.getDrawable(R.drawable.forcebuttons);
            forcebuttons3 = resources.getDrawable(R.drawable.forcebuttons);
            forcebuttons4 = resources.getDrawable(R.drawable.forcebuttons);
            forcebuttons5 = resources.getDrawable(R.drawable.forcebuttons);
            simpelbackground = resources.getDrawable(R.drawable.simpelbackground);
            lightdate = resources.getDrawable(R.drawable.lightdate);
            lightcity = resources.getDrawable(R.drawable.lightcity);
            datebackground = findViewById(R.id.datebackground);
            fajrbackground = findViewById(R.id.fajrbackground);
            risebackground = findViewById(R.id.risebackground);
            dhuhrbackground = findViewById(R.id.dhuhrbackground);
            asrbackground = findViewById(R.id.asrbackground);
            maghribbackground = findViewById(R.id.maghribbackground);
            ishabackground = findViewById(R.id.ishabackground);
            yesterdayarrowbackground = findViewById(R.id.yesterdayarrowbackground);
            tommorowarrowbackground = findViewById(R.id.tommorowarrowbackground);
            rightsideelementsbackground = findViewById(R.id.rightsideelementsbackground);
            lightmultipledayselectionbackground = resources.getDrawable(R.drawable.lightmultipledayselectionbackground);
            lightbackback = resources.getDrawable(R.drawable.lightbackback);
            lightstatsback = resources.getDrawable(R.drawable.lightstatsback);
            backarrowbackground = findViewById(R.id.backarrowbackground);
            lightlmfao = resources.getDrawable(R.drawable.lightlmfao);
            lmfaoimage = findViewById(R.id.lmfao);
            transparentblacker = resources.getColor(R.color.transparentblacker);
        }

        slider.setTextColor(Color.WHITE);

        prayfajr.setBackground(forcebuttons);
        praydhuhr.setBackground(forcebuttons2);
        prayasr.setBackground(forcebuttons3);
        praymaghrib.setBackground(forcebuttons4);
        prayisha.setBackground(forcebuttons5);

        prayfajr.setTextColor(Color.WHITE);
        praydhuhr.setTextColor(Color.WHITE);
        prayasr.setTextColor(Color.WHITE);
        praymaghrib.setTextColor(Color.WHITE);
        prayisha.setTextColor(Color.WHITE);

        full.setBackground(simpelbackground);
        title.setTextColor(Color.WHITE);
        title.setBackgroundColor(transparentblacker);


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

        datebackground.setBackground(lightdate);
        fajrbackground.setBackground(lightmultipledayselectionbackground);
        risebackground.setBackground(lightmultipledayselectionbackground);
        dhuhrbackground.setBackground(lightmultipledayselectionbackground);
        asrbackground.setBackground(lightmultipledayselectionbackground);
        maghribbackground.setBackground(lightmultipledayselectionbackground);
        ishabackground.setBackground(lightmultipledayselectionbackground);
        yesterdayarrowbackground.setBackground(lightmultipledayselectionbackground);
        tommorowarrowbackground.setBackground(lightmultipledayselectionbackground);
        citydisplay.setBackground(lightcity);

        rightsideelementsbackground.setBackground(lightstatsback);
        backarrowbackground.setBackground(lightbackback);
        lmfaoimage.setImageDrawable(lightlmfao);

        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("no", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();
    }


    Drawable darkforcebuttons, darkforcebuttons2, darkforcebuttons3, darkforcebuttons4, darkforcebuttons5;
    Drawable forcefull;
    Drawable dateer;
    Drawable cityeer;
    Drawable multipledayselectionbackground;
    Drawable backback;
    Drawable statsback;
    Drawable lmfaodrawable;
    boolean onlyonce = true;
    String ID;

    private void dark_mode() {
        darkmode = true;

        if(onlyonce) {
            onlyonce = false;
            darkforcebuttons = resources.getDrawable(R.drawable.darkbuttons);
            darkforcebuttons2 = resources.getDrawable(R.drawable.darkbuttons);
            darkforcebuttons3 = resources.getDrawable(R.drawable.darkbuttons);
            darkforcebuttons4 = resources.getDrawable(R.drawable.darkbuttons);
            darkforcebuttons5 = resources.getDrawable(R.drawable.darkbuttons);
            forcefull = resources.getDrawable(R.drawable.forcefull);
            dateer = resources.getDrawable(R.drawable.date);
            cityeer = resources.getDrawable(R.drawable.city);
            datebackground = findViewById(R.id.datebackground);
            fajrbackground = findViewById(R.id.fajrbackground);
            risebackground = findViewById(R.id.risebackground);
            dhuhrbackground = findViewById(R.id.dhuhrbackground);
            asrbackground = findViewById(R.id.asrbackground);
            maghribbackground = findViewById(R.id.maghribbackground);
            ishabackground = findViewById(R.id.ishabackground);
            multipledayselectionbackground = resources.getDrawable(R.drawable.multipledayselectionbackground);
            yesterdayarrowbackground = findViewById(R.id.yesterdayarrowbackground);
            tommorowarrowbackground = findViewById(R.id.tommorowarrowbackground);
            rightsideelementsbackground = findViewById(R.id.rightsideelementsbackground);
            backback = resources.getDrawable(R.drawable.backback);
            statsback = resources.getDrawable(R.drawable.statsback);
            backarrowbackground = findViewById(R.id.backarrowbackground);
            lmfaodrawable = resources.getDrawable(R.drawable.lmfao);
            lmfaoimage = findViewById(R.id.lmfao);
            grayerthanwhite = resources.getColor(R.color.grayerthanwhite);
        }

        slider.setTextColor(grayerthanwhite);

        prayfajr.setBackground(darkforcebuttons);
        praydhuhr.setBackground(darkforcebuttons2);
        prayasr.setBackground(darkforcebuttons3);
        praymaghrib.setBackground(darkforcebuttons4);
        prayisha.setBackground(darkforcebuttons5);

        prayfajr.setTextColor(grayerthanwhite);
        praydhuhr.setTextColor(grayerthanwhite);
        prayasr.setTextColor(grayerthanwhite);
        praymaghrib.setTextColor(grayerthanwhite);
        prayisha.setTextColor(grayerthanwhite);

        full.setBackground(forcefull);
        title.setTextColor(Color.WHITE);
        title.setBackground(null);

        if (fajrtitle.getCurrentTextColor() != Color.GREEN)
            fajrtitle.setTextColor(grayerthanwhite);
        risetitle.setTextColor(grayerthanwhite);
        if (dohrtitle.getCurrentTextColor() != Color.GREEN)
            dohrtitle.setTextColor(grayerthanwhite);
        if (asrtitle.getCurrentTextColor() != Color.GREEN)
            asrtitle.setTextColor(grayerthanwhite);
        if (maghrebtitle.getCurrentTextColor() != Color.GREEN)
            maghrebtitle.setTextColor(grayerthanwhite);
        if (ishatitle.getCurrentTextColor() != Color.GREEN)
            ishatitle.setTextColor(grayerthanwhite);

        yesterdayarrowbackground.setBackground(multipledayselectionbackground);
        tommorowarrowbackground.setBackground(multipledayselectionbackground);
        datebackground.setBackground(dateer);
        fajrbackground.setBackground(multipledayselectionbackground);
        risebackground.setBackground(multipledayselectionbackground);
        dhuhrbackground.setBackground(multipledayselectionbackground);
        asrbackground.setBackground(multipledayselectionbackground);
        maghribbackground.setBackground(multipledayselectionbackground);
        ishabackground.setBackground(multipledayselectionbackground);

        rightsideelementsbackground.setBackground(statsback);
        backarrowbackground.setBackground(backback);
        citydisplay.setBackground(cityeer);
        lmfaoimage.setImageDrawable(lmfaodrawable);

        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("yes", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();
    }


    public void backtotodayClicked(View view) {

        CurrentDisplayedDay = new Date();
        it_is_today = true;
        all_white = false;
        fill_all = false;

        location_shit(CurrentDisplayedDay);

        // Hide double arrows that take you back to today
        doublearrowsbackground.setVisibility(View.INVISIBLE);

    }
}
