package com.krimzon.scuffedbots.raka3at;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
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
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class force extends AppCompatActivity /*implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener */{

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
        sql("slat");
        load_data_from_slat_sql();
        languageshet();
        sql("force");
        location_shit();
        fontAndy();
        low_light_alert();
    }

    protected List<TextView> prayerdisplayviews, prayerdisplayviews2;
    protected Typeface arabic_typeface2;
    private void fontAndy() {
        arabic_typeface = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");
        arabic_typeface2 = Typeface.createFromAsset(getAssets(),  "Tajawal-Regular.ttf");
        prayerdisplayviews = new ArrayList<>();
        prayerdisplayviews2 = new ArrayList<>();

        risetime.setTypeface(arabic_typeface);
        risetitle.setTypeface(arabic_typeface);

        title.setTypeface(arabic_typeface);

        prayfajr.setTypeface(arabic_typeface);
        praydhuhr.setTypeface(arabic_typeface);
        prayasr.setTypeface(arabic_typeface);
        praymaghrib.setTypeface(arabic_typeface);
        prayisha.setTypeface(arabic_typeface);

        prayerdisplayviews.add(fajrtime);prayerdisplayviews.add(dhuhrtime);prayerdisplayviews.add(asrtime);prayerdisplayviews.add(maghribtime);prayerdisplayviews.add(ishatime);

        prayerdisplayviews2.add(fajrtitle);prayerdisplayviews2.add(dohrtitle);prayerdisplayviews2.add(asrtitle);prayerdisplayviews2.add(maghrebtitle);prayerdisplayviews2.add(ishatitle);

        for(int i=0; i<prayerdisplayviews.size();i++){
            prayerdisplayviews.get(i).setTypeface(arabic_typeface2);
            prayerdisplayviews2.get(i).setTypeface(arabic_typeface2);
        }

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
        resources = getResources();

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


    private void variables_setup() {
        prayers = new ArrayList<>();
        fajrtitle = findViewById(R.id.fajrtitle);
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
    public void use(double longitude, double latitude, boolean new_coordinates) {
        if(new_coordinates)
            SQLSharing.mydb.insertMawa9it(String.valueOf(longitude), String.valueOf(latitude));
        else {
            prayers = new ArrayList<>();
            SQLSharing.mydb.updateMawa9it("1", String.valueOf(longitude), String.valueOf(latitude));
        }
        today = new Date();
        todaycomparable = today.toString().split(" ")[1] + " " + today.toString().split(" ")[2];
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
        fajrtemp = Integer.valueOf(fajr.split(" ")[0].split(":")[0])*60 + Integer.valueOf(fajr.split(" ")[0].split(":")[1]);
        //Integer risetemp = Integer.valueOf(rise.split(" ")[0].split(":")[0])*60 + Integer.valueOf(rise.split(" ")[0].split(":")[1]);
        dhuhrtemp = Integer.valueOf(dhuhr.split(" ")[0].split(":")[0])*60 + Integer.valueOf(dhuhr.split(" ")[0].split(":")[1]);
        asrtemp = Integer.valueOf(asr.split(" ")[0].split(":")[0])*60 + Integer.valueOf(asr.split(" ")[0].split(":")[1]);
        maghribtemp = Integer.valueOf(maghrib.split(" ")[0].split(":")[0])*60 + Integer.valueOf(maghrib.split(" ")[0].split(":")[1]);
        ishatemp = Integer.valueOf(isha.split(" ")[0].split(":")[0])*60 + Integer.valueOf(isha.split(" ")[0].split(":")[1]);
        prayers.add(fajrtemp);
        /*prayers.add(risetemp);*/
        prayers.add(dhuhrtemp);
        prayers.add(asrtemp);
        prayers.add(maghribtemp);
        prayers.add(ishatemp);
        /*Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.US);
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(country + "/" + state));
        String lol = formatter.format(prayerTimes.fajr.getTime());*/


        retrieveAndy();
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
        sql("force2");
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

        back_to_main();
    }

    private void back_to_main() {
        main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    public void ishaClicked(View view) {
        process_prayed_request(4);

        if(allow_pray)
            send(4);
        else
            clean_up();
    }

    public void maghribClicked(View view) {
        process_prayed_request(3);

        if(allow_pray)
            send(3);
        else
            clean_up();
    }

    public void asrClicked(View view) {
        process_prayed_request(2);

        if(allow_pray)
            send(2);
        else
            clean_up();
    }

    public void dhuhrClicked(View view) {
        process_prayed_request(1);

        if(allow_pray)
            send(1);
        else
            clean_up();
    }

    public void fajrClicked(View view) {
        process_prayed_request(0);

        if(allow_pray)
            send(0);
        else
            clean_up();
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

    protected int next_prayer = 0;
    protected String temper;
    protected List<Button> praybuttons;
    private void retrieveAndy(){
        forces = new ArrayList<>();
        praybuttons = new ArrayList<>();
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

            // color pray buttons
            praybuttons.add(prayfajr); praybuttons.add(praydhuhr); praybuttons.add(prayasr); praybuttons.add(praymaghrib); praybuttons.add(prayisha);
            for(int i=0; i<forces.size(); i++){
                if(forces.get(i).equals("0")) {
                    praybuttons.get(i).setTextColor(getResources().getColor(R.color.lighterred));
                    break;
                } else {
                    praybuttons.get(i).setTextColor(Color.GREEN);
                }
            }

            // find next prayer
            for(int i=0; i<forces.size(); i++){
                if(forces.get(i).equals("0")){
                    next_prayer = i;
                    break;
                }
            }

        }

        // what is soon adan
        what_is_soon_adan_and_one_before_it();

    }


    protected int next_adan = 0;
    protected int temp_next_adan = 0;
    protected boolean new_adan = false;
    protected int previous_adan = 0;
    private void what_is_soon_adan_and_one_before_it() {
        temptime = String.valueOf(new Date()).split(" ")[3];
        rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 60 + Integer.valueOf(temptime.split(":")[1]);
        for(int i=0;i<prayers.size();i++){
            if(rightnowcomparable>=prayers.get(i))
                temp_next_adan = i;
        }
        if(temp_next_adan!=next_adan) {
            next_adan = temp_next_adan;
            if (next_adan != 0) // so we don't assign -1 to previous_adan
                previous_adan = next_adan - 1;
        }

        // apply vision onto all prayers only if changed
        if(new_adan) { new_adan = false;
            for (int i = 0; i < prayerdisplayviews.size(); i += 1) {
                if (i != next_adan) {
                    prayerdisplayviews2.get(i).setTextSize(16);
                    prayerdisplayviews.get(i).setTextSize(16);
                } else {
                    prayerdisplayviews2.get(i).setTextSize(18);
                    prayerdisplayviews.get(i).setTextSize(18);
                }
            }
        }

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

}
