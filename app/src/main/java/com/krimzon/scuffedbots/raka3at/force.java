package com.krimzon.scuffedbots.raka3at;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
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
import com.krimzon.scuffedbots.raka3at.Dates.DatesCollectionAdapter;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;

import net.time4j.PlainDate;
import net.time4j.calendar.HijriCalendar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    protected Date today;
    protected String todaycomparable;

    protected RelativeLayout full;
    protected String language = "";

    protected Typeface arabic_typeface;

    protected Button prayfajr, praydhuhr, prayasr, praymaghrib, prayisha;

    protected TextView title;

    protected Animation zoom_in, zoom_out, zoom_in2, zoom_out2; // for next adan
    protected TextView citydisplay;

    protected ViewPager pager;
    protected DatesCollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force);

        params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        params = CalculationMethod.EGYPTIAN.getParameters();
        params.madhab = Madhab.SHAFI; // SHAFI made 95% accuracy, HANAFI had 1hour different for l'3asr
        params.adjustments.fajr = 2;

        dateFormat = new SimpleDateFormat(pattern);

        variables_setup();
        fontAndy();
        sql("slat");
        load_data_from_slat_sql();
        languageshet();
        sql("force");
        //use(30, 30, true);
        location_shit();
        low_light_alert();

    }

    public static String hijri = "";
    protected int hijri_month = 0, hijri_year = 0, hijri_day = 0;
    private void hijri_date_setup() {
        String[] t = PlainDate.of(2019, 10, 12)
                .transform(HijriCalendar.class, HijriCalendar.VARIANT_UMALQURA).toString().split("-");
        t[3] = t[3].replace("[islamic", "");
        hijri_year = Integer.valueOf(t[1]);
        hijri_month = Integer.valueOf(t[2]);
        hijri_day = Integer.valueOf(t[3]);
        convert_hijri_to_cute();
    }

    private void convert_hijri_to_cute() {
        hijri += hijri_day + " ";
        if(language.equals("ar")){
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
        }
    }

    protected List<TextView> prayerdisplayviews, prayerdisplayviews2;
    protected Typeface arabic_typeface2, arabic_typeface3;
    private void fontAndy() {
        arabic_typeface = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");
        arabic_typeface2 = Typeface.createFromAsset(getAssets(),  "Tajawal-Regular.ttf");
        arabic_typeface3 = Typeface.createFromAsset(getAssets(),  "Tajawal-Bold.ttf");


        title.setTypeface(arabic_typeface);

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
    private void getStrings(){
        fajrtitlel = getString(R.string.fajrtitle);
        risetitlel = getString(R.string.rise);
        dohrtitlel = getString(R.string.dohrtitle);
        asrtitlel = getString(R.string.asrtitle);
        maghrebtitlel = getString(R.string.maghrebtitle);
        ishatitlel = getString(R.string.ishatitle);
    }


    private void load_data_from_slat_sql() {
        SQLSharing.mycursor.moveToPosition(6);
        language = SQLSharing.mycursor.getString(1);
    }


    private void low_light_alert() {
        if(getIntent().getStringExtra("light_alert").equals("yes")) {
            if (language.equals("en"))
                Snackbar.make(full, getString(R.string.low_light), BaseTransientBottomBar.LENGTH_LONG).show();
            else
                Snackbar.make(full, getString(R.string.low_light_arabe), BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }


    private void location_shit() {
        sql("force");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(SQLSharing.mycursor.getCount()>0)
            if_theres_previous_info_load_it_n_display();
        if_first_launch_get_longitude_n_lattitude_n_ville_n_hijri_date();
    }


    private void if_first_launch_get_longitude_n_lattitude_n_ville_n_hijri_date() {
        new_coordinates = true;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return; }
        if(checkLocation())
            AttemptToGetLocationCoordinates();
    }


    private void if_theres_previous_info_load_it_n_display() {
        new_coordinates = false;
        SQLSharing.mycursor.moveToFirst();
        double longitude = Double.valueOf(SQLSharing.mycursor.getString(1));
        double latitude = Double.valueOf(SQLSharing.mycursor.getString(2));
        use(longitude, latitude, new_coordinates);
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


    protected TextView slider;
    private void variables_setup() {
        slideholder = findViewById(R.id.slideholder);
        slider = findViewById(R.id.slider);
        fajrtitle = findViewById(R.id.fajrtitle);
        /*datedisplay = findViewById(R.id.date);*/
        citydisplay = findViewById(R.id.city);
        title = findViewById(R.id.title);
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
        zoom_out = loadAnimation(this, R.anim.zoom_out);
        zoom_in2 = loadAnimation(this, R.anim.zoom_in);
        zoom_out2 = loadAnimation(this, R.anim.zoom_out);

    }

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
                                if(new_coordinates)
                                    new_coordinates = false;
                                use(location.getLongitude(), location.getLatitude(), new_coordinates);
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
    public void use(double longitude, double latitude, boolean new_coordinates) {
        hijri = "";
        prayers = new ArrayList<>();

        if(new_coordinates)
            SQLSharing.mydb.insertMawa9it(String.valueOf(longitude), String.valueOf(latitude));
        else
            SQLSharing.mydb.updateMawa9it("1", String.valueOf(longitude), String.valueOf(latitude));

        today = new Date();
        temptoday = today.toString().split(" ");
        todaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5].charAt(2) + temptoday[5].charAt(3);
        coordinates = new Coordinates(latitude, longitude);
        date = DateComponents.from(today);
        prayerTimes = new PrayerTimes(coordinates, date, params);
        try {
            fajr = DateFormat.format(timeshape, new Date(prayerTimes.fajr.getTime())).toString();
            rise = DateFormat.format(timeshape, new Date(prayerTimes.sunrise.getTime())).toString();
            dhuhr = DateFormat.format(timeshape, new Date(prayerTimes.dhuhr.getTime())).toString();
            asr = DateFormat.format(timeshape, new Date(prayerTimes.asr.getTime())).toString();
            maghrib = DateFormat.format(timeshape, new Date(prayerTimes.maghrib.getTime())).toString();
            isha = DateFormat.format(timeshape, new Date(prayerTimes.isha.getTime())).toString();
        } catch(Exception ignored){}
        if(language.equals("en")) {
            fajrtime.setText(fajr);
            risetime.setText(rise);
            dhuhrtime.setText(dhuhr);
            asrtime.setText(asr);
            maghribtime.setText(maghrib);
            ishatime.setText(isha);
        } else if(language.equals("ar")){ // the arabic am and pm
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

        work_on_date_n_display_it();
        display_dates();
        /*datedisplay.setText(datin);*/

        if(onetimer) { onetimer = false;
            retrieveAndy();
        }
    }

    private void display_dates() {
        pager = findViewById(R.id.pager);
        adapter = new DatesCollectionAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        Displai();
    }

    Handler handlerr = new Handler(){
        //alt+enter for function below
        @Override
        public void handleMessage(Message msg) {
            switchswitch();
        }
    };

    private void switchswitch() {
        pager.setCurrentItem(2);
    }

    public void Displai(){

        //runs in the background
        Runnable r=new Runnable() {
            @Override
            public void run() {
                long futuretime = System.currentTimeMillis() + 5000;

                while (System.currentTimeMillis() < futuretime){
                    //prevents multiple threads from crashing into each other
                    synchronized (this){
                        try{
                            wait(futuretime - System.currentTimeMillis());
                        } catch( Exception ignored){}
                    }
                }

                //run the handler
                handlerr.sendEmptyMessage(0);
            }
        };

        //anti lag
        Thread mythread = new Thread(r); //to thread the runnable object we launched
        mythread.start();
    }

    protected boolean onetimer = true;
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
            else if (tempdatin.equals(resources.getString(R.string.fridu)))
                datin += resources.getString(R.string.fri);

            hijri += datin + " ";

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals(resources.getString(R.string.jan)))
                datin += "January";
            else if (tempdatin.equals(resources.getString(R.string.feb)))
                datin += "February";
            else if (tempdatin.equals(resources.getString(R.string.mar)))
                datin += "March";
            else if (tempdatin.equals(resources.getString(R.string.apr)))
                datin += "April";
            else if (tempdatin.contains(resources.getString(R.string.mao)))
                datin += "May";
            else if (tempdatin.contains(resources.getString(R.string.june)))
                datin += "June";
            else if (tempdatin.contains(resources.getString(R.string.july)))
                datin += "July";
            else if (tempdatin.equals(resources.getString(R.string.aug)))
                datin += "August";
            else if (tempdatin.equals("Sep"))
                datin += "September";
            else if (tempdatin.equals("Oct"))
                datin += "October";
            else if (tempdatin.equals("Nov"))
                datin += "November";
            else if (tempdatin.equals("Dec"))
                datin += "December";

            tempdatin = temptoday[2];
            datin += " " + tempdatin;
            if (tempdatin.equals("2") || tempdatin.equals("22"))
                datin += "nd";
            else if (tempdatin.equals("3") || tempdatin.equals("23"))
                datin += "rd";
            else if (tempdatin.equals("1") || tempdatin.equals("21"))
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
                    break;
            }

            hijri += datin + " ";

            tempdatin = temptoday[2];
            datin += " " + tempdatin;

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals("Jan"))
                datin += resources.getString(R.string.janarabe);
            else if (tempdatin.equals("Feb"))
                datin += resources.getString(R.string.febarabe);
            else if (tempdatin.equals("Mar"))
                datin += resources.getString(R.string.mararabe);
            else if (tempdatin.equals("Apr"))
                datin += resources.getString(R.string.aprarabe);
            else if (tempdatin.contains("Ma"))
                datin += resources.getString(R.string.maarabe);
            else if (tempdatin.contains("Jun"))
                datin += resources.getString(R.string.junarabe);
            else if (tempdatin.contains("Jul"))
                datin += resources.getString(R.string.jularabe);
            else if (tempdatin.equals("Aug"))
                datin += resources.getString(R.string.augusarabe);
            else if (tempdatin.equals("Sep"))
                datin += resources.getString(R.string.separabe);
            else if (tempdatin.equals("Oct"))
                datin += resources.getString(R.string.octarabe);
            else if (tempdatin.equals("Nov"))
                datin += resources.getString(R.string.novarabe);
            else if (tempdatin.equals("Dec"))
                datin += resources.getString(R.string.decarabe);

        }
        datin += " " + temptoday[5];


        hijri_date_setup();
    }


    private void print(Object dumps) {
        Toast.makeText(getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }


    protected String temptime;
    protected Integer rightnowcomparable;
    protected List<String> forces;
    protected String prayed = "";
    protected boolean found_prayed_history_in_sql = false;
    protected Integer clickedprayertime;
    protected boolean one_of_previous_is_zero = false;
    protected String tempo;
    public void resetClicked(View view) {
        /*sql("force2");
        if(SQLSharing.mycursor.getCount()>0) {
            while(SQLSharing.mycursor.moveToNext()) {
                if (todaycomparable.equals(SQLSharing.mycursor.getString(1))){
                    SQLSharing.mydb.updatePrayed(todaycomparable, "00000");
                    break;
                }
            }
        }
        if(SQLSharing.mycursor!=null && SQLSharing.mydb!=null) {
            SQLSharing.mycursor.close();
            SQLSharing.mydb.close();
        }

        back_to_main();*/
    }


    private void back_to_main() {
        main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }


    public void ishaClicked(View view) {
        if(prayisha.getCurrentTextColor()==resources.getColor(R.color.lighterred)) {
            process_prayed_request(4);

            if (allow_pray)
                send(4);
            else
                clean_up();
        }
    }


    public void maghribClicked(View view) {
        if(praymaghrib.getCurrentTextColor()==resources.getColor(R.color.lighterred)) {
            process_prayed_request(3);

            if (allow_pray)
                send(3);
            else
                clean_up();
        }
    }


    public void asrClicked(View view) {
        if(prayasr.getCurrentTextColor()==resources.getColor(R.color.lighterred)) {
            process_prayed_request(2);

            if (allow_pray)
                send(2);
            else
                clean_up();
        }
    }


    public void dhuhrClicked(View view) {
        if(praydhuhr.getCurrentTextColor()==resources.getColor(R.color.lighterred)) {
            process_prayed_request(1);

            if (allow_pray)
                send(1);
            else
                clean_up();
        }
    }


    public void fajrClicked(View view) {
        if(prayfajr.getCurrentTextColor()==resources.getColor(R.color.lighterred)) {
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
    protected List<Button> praybuttons;
    private void retrieveAndy(){
        forces = new ArrayList<>();
        praybuttons = new ArrayList<>();
        praybuttons.add(prayfajr); praybuttons.add(praydhuhr); praybuttons.add(prayasr); praybuttons.add(praymaghrib); praybuttons.add(prayisha);
        forces.add("0");forces.add("0");forces.add("0");forces.add("0");forces.add("0");
        sql("force2");

        if(SQLSharing.mycursor.getCount()>0){
            today = new Date();
            todaycomparable = today.toString().split(" ")[1] + " " + today.toString().split(" ")[2];
            while(SQLSharing.mycursor.moveToNext()) {
                if(SQLSharing.mycursor.getString(1).equals(todaycomparable)) {
                    forces = new ArrayList<>();
                    temper = SQLSharing.mycursor.getString(2);
                    for(int i=0;i<temper.length();i++)
                        forces.add( Character.toString(temper.charAt(i)) );
                    break;
                }
            }

        } else {
            fill_up_prayed();
            SQLSharing.mydb.insertPrayed(todaycomparable, prayed);
        }

        // what is soon adan
        what_is_soon_adan_and_one_before_it();

        // color pray buttons
        color_pray_buttons();

        if(!end_of_day)
            InitialDelayForNextAdanAnimation();

    }

    private void color_pray_buttons() {
        if(end_of_day){
            for(int i=0; i<5; i++)
                if(forces.get(i).equals("0"))
                    praybuttons.get(i).setTextColor(resources.getColor(R.color.lighterred));
        } else {
            for (int i = 0; i < next_adan; i++)
                if (forces.get(i).equals("0"))
                    praybuttons.get(i).setTextColor(resources.getColor(R.color.lighterred));
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
        //alt+enter for function below
        @Override
        public void handleMessage(Message msg) {
            animatenextadan();
        }
    };

    Handler handler2 = new Handler(){
        //alt+enter for function below
        @Override
        public void handleMessage(Message msg) {
            slide_in_dem_dpz();
        }
    };

    private void animatenextadan() {
        temp_next_adan_textview = prayerdisplayviews.get(next_adan);
        temp_next_adan_textview2 = prayerdisplayviews2.get(next_adan);
        temp_next_adan_textview2.setTextSize(normal_text_size); // 23
        temp_next_adan_textview.setTextSize(normal_text_size);  // to prepare for animation
        temp_next_adan_textview.startAnimation(zoom_in);
        zoom_in.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) { }@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            temp_next_adan_textview.setTextSize((float) (normal_text_size*1.3));
            temp_next_adan_textview.setTextColor(Color.GREEN);
            temp_next_adan_textview.startAnimation(zoom_out);
            zoom_out.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                temp_next_adan_textview.setTextSize(normal_text_size);
            }});
        }});
        temp_next_adan_textview2.startAnimation(zoom_in2);
        zoom_in2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) { }@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            temp_next_adan_textview2.setTextSize((float) (normal_text_size*1.3));
            temp_next_adan_textview2.setTextColor(Color.GREEN);
            temp_next_adan_textview2.startAnimation(zoom_out2);
            zoom_out2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                temp_next_adan_textview2.setTextSize(normal_text_size);

                slide_in_dem_dpz();
            }});
        }});
    }

    protected Animation fromfajrtolol;
    protected RelativeLayout slideholder;
    protected int positifise = 0;
    protected int lol = 0;
    private void slide_in_dem_dpz() {
        lol = ((prayers.get(next_adan)-rightnowcomparable)/60 - 1);
        if(lol<0) positifise = -lol;
        else positifise = lol;

        slider.setText("- " + positifise); // for sm ass reason it's over by 1 min
        if(next_adan==0)
            fromfajrtolol = loadAnimation(this, R.anim.fromfajrtofajr);
        else if(next_adan==1)
            fromfajrtolol = loadAnimation(this, R.anim.fromfajrtodhuhr);
        else if(next_adan==2)
            fromfajrtolol = loadAnimation(this, R.anim.frofajrtoasr);
        else if(next_adan==3)
            fromfajrtolol = loadAnimation(this, R.anim.fromfajrtomaghrib);
        else if(next_adan==4)
            fromfajrtolol = loadAnimation(this, R.anim.fromfajrtoisha);

        slideholder.startAnimation(fromfajrtolol);
        fromfajrtolol.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            slideholder.setVisibility(View.VISIBLE);
        }});

    }

    public void InitialDelayForNextAdanAnimation(){

        //runs in the background
        Runnable r=new Runnable() {
            @Override
            public void run() {
                long futuretime = System.currentTimeMillis() + 1500;

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
                long futuretime = System.currentTimeMillis() + 1500;

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


}
