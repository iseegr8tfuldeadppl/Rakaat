package com.krimzon.scuffedbots.raka3at;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Qibla;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.dialogs.CustomDialogClass;
import java.util.Locale;

public class kibla extends AppCompatActivity implements SensorEventListener {

    private String language;
    private ImageView compass_img;
    private Button fix;
    private int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    private boolean haveSensor = false;
    private float[] rMat = new float[9];
    private float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private TextView kiblatitle;
    private String kibla, kiblatitleh, betterquality;
    private TextView maintitle;
    private FrameLayout full;
    private RelativeLayout backarrowbackground;
    private RelativeLayout nightmodebackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kibla);

        ImageView arrowback = findViewById(R.id.arrowback);
        ImageView nightmodebutton = findViewById(R.id.nightmodebutton);
        try {
            Glide.with(this).load(R.drawable.backarrowdark).into(arrowback);
        } catch (Exception ignored) {
            arrowback.setImageDrawable(resources.getDrawable(R.drawable.backarrowdark));
        }
        try {
            Glide.with(this).load(R.drawable.nightmodedark).into(nightmodebutton);
        } catch (Exception ignored) {
            nightmodebutton.setImageDrawable(resources.getDrawable(R.drawable.nightmodedark));
        }

        // override system locale
        Configuration cfg = new Configuration();
        cfg.locale = new Locale("en");
        this.getResources().updateConfiguration(cfg, null);

        location_shit();

        kiblatitle = findViewById(R.id.kiblatitle);
        nightmodebackground = findViewById(R.id.nightmodebackground);
        backarrowbackground = findViewById(R.id.backarrowbackground);
        maintitle = findViewById(R.id.maintitle);
        full = findViewById(R.id.full);
        fix = findViewById(R.id.fix);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");
        fix.setTypeface(custom_font);
        kiblatitle.setTypeface(custom_font);
        maintitle.setTypeface(custom_font);


        resources = getResources();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img = findViewById(R.id.compass);

        work_on_language();
        start();

        /*hideNavigationBar();*/

        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(6);
        language = SQLSharing.mycursorslat.getString(1);

        SQLSharing.mycursorslat.moveToPosition(1);
        if(SQLSharing.mycursorslat.getString(1).equals("no"))
            light_mode();

        SQLSharing.mycursorslat.moveToPosition(12);
        boolean show_calibration = SQLSharing.mycursorslat.getString(1).equals("yes");
        if(show_calibration){
            CustomDialogClass cdd=new CustomDialogClass(this, language);
            cdd.show();
            SQLSharing.mydbslat.updateData("no", SQLSharing.mycursorslat.getString(0));
        }
        close_sql();

    }

    private void close_sql() {
        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    private void work_on_language(){
        getLanguage();
        getStrings();
        if(language.equals("en"))
            english();
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

    private void getLanguage(){
        sql("slat");

        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToPosition(6);
        language = SQLSharing.mycursorslat.getString(1);
        close_sql();
    }

    private void getStrings(){
        if(language.equals("en")){
            betterquality = getString(R.string.betterquality);
            kiblatitleh = getString(R.string.kiblatitle);
            kibla = getString(R.string.kibla);
        }
    }

    private void english(){
        maintitle.setText(kibla);
        kiblatitle.setText(kiblatitleh);
        fix.setText(betterquality);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        compass_img.setRotation(-mAzimuth);

        int higher = (int) qibla_angle + qibla_range;
        int lower = (int) qibla_angle - qibla_range;
        if(higher >360) {
            higher -= 360;
            if (higher <= mAzimuth && mAzimuth <= lower) { // they needed to be flipped here don't worry
                try {
                    Glide.with(this).load(R.drawable.compass).into(compass_img);
                } catch (Exception ignored) {
                    compass_img.setImageDrawable(resources.getDrawable(R.drawable.compass));
                }
            } else {
                try {
                    Glide.with(this).load(R.drawable.compass2).into(compass_img);
                } catch (Exception ignored) {
                    compass_img.setImageDrawable(resources.getDrawable(R.drawable.compass2));
                }
            }
        }
        else if(lower <0) {
            lower += 360;
            if (higher <= mAzimuth && mAzimuth <= lower) { // they needed to be flipped here don't worry
                try {
                    Glide.with(this).load(R.drawable.compass).into(compass_img);
                } catch (Exception ignored) {
                    compass_img.setImageDrawable(resources.getDrawable(R.drawable.compass));
                }
            } else {
                try {
                    Glide.with(this).load(R.drawable.compass2).into(compass_img);
                } catch (Exception ignored) {
                    compass_img.setImageDrawable(resources.getDrawable(R.drawable.compass2));
                }
            }
        } else {
            if (lower <= mAzimuth && mAzimuth <= higher) { // they needed to be flipped here don't worry
                try {
                    Glide.with(this).load(R.drawable.compass).into(compass_img);
                } catch (Exception ignored) {
                    compass_img.setImageDrawable(resources.getDrawable(R.drawable.compass));
                }
            } else {
                try {
                    Glide.with(this).load(R.drawable.compass2).into(compass_img);
                } catch (Exception ignored) {
                    compass_img.setImageDrawable(resources.getDrawable(R.drawable.compass2));
                }
            }
        }


    }

    // TODO: make this range modifyable by user by the form of a seekbar
    // TODO: add light mode to this activity
    protected int qibla_range = 20; // actually half of range, more like reach
    protected double qibla_angle = 105; // default is manual, but it will be updated if library got some food
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                boolean haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
                // TODO use this ^
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        if (haveSensor) {
            mSensorManager.unregisterListener(this, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();

        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
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

    @Override
    protected void onResume() {
        super.onResume();
        if(an_alert_to_turn_location_on_was_displayed)
            AttemptToGetLocationCoordinates();
        start();
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    protected boolean an_alert_to_turn_location_on_was_displayed = false;
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

    protected LocationManager mLocationManager;
    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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
                                wegotcoordsboiz(location.getLongitude(), location.getLatitude());
                            }
                        }
                    });
        }
    }

    protected boolean new_coordinates = false;
    protected Qibla qibla;
    protected Coordinates coordinates;
    private void wegotcoordsboiz(double longitude, double latitude){

        sql("force");
        // update coordinates in sql folks
        if(new_coordinates)
            SQLSharing.mydbforce.insertMawa9it(String.valueOf(longitude), String.valueOf(latitude));
        else
            SQLSharing.mydbforce.updateMawa9it("1", String.valueOf(longitude), String.valueOf(latitude));

        close_sql();

        coordinates = new Coordinates(latitude, longitude);
        qibla = new Qibla(coordinates);
        qibla_angle = qibla.direction;
    }

    protected double longitude = 0, latitude = 0;
    private void if_theres_previous_info_load_it_n_display() {
        new_coordinates = false;
        SQLSharing.mycursorforce.moveToFirst();
        longitude = Double.parseDouble(SQLSharing.mycursorforce.getString(1));
        latitude = Double.parseDouble(SQLSharing.mycursorforce.getString(2));
        close_sql();
        wegotcoordsboiz(longitude, latitude);
    }

    private static final int REQUEST_CODE = 1000;
    private void location_shit() {
        sql("force");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(SQLSharing.mycursorforce.getCount()>0){
            if_theres_previous_info_load_it_n_display();
        } else {
            close_sql();
        }
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

    protected FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainActivity);
        finish();
    }


    public void fixClicked(View view) {
        CustomDialogClass cdd=new CustomDialogClass(this, language);
        cdd.show();
    }


    /*int darkbackgroundcolor;*/
    /*private void hideNavigationBar() {
        darkbackgroundcolor = resources.getColor(R.color.black);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getWindow().setStatusBarColor(darkbackgroundcolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }*/

    public void arrowbackClicked(View view) {
        exit();
    }


    boolean darkmode = true;
    String ID = "";
    public void nightmodeClicked(View view) {
        if(darkmode)
            light_mode();
        else
            dark_mode();
    }


    boolean once2 = true;
    int white;
    Drawable darkbuttons2;
    Drawable forcefull;
    Drawable backback;
    Drawable statsback;
    private void dark_mode() {
        darkmode = true;

        if(once2) {
            once2 = false;
            white = resources.getColor(R.color.white);
            darkbuttons2 = resources.getDrawable(R.drawable.darkbuttons2);
            forcefull = resources.getDrawable(R.drawable.forcefull);
            backback = resources.getDrawable(R.drawable.backback);
            statsback = resources.getDrawable(R.drawable.statsback);
        }

        backarrowbackground.setBackground(backback);
        nightmodebackground.setBackground(statsback);
        maintitle.setTextColor(white);
        kiblatitle.setTextColor(white);
        fix.setBackground(darkbuttons2);
        full.setBackground(forcefull);

        sql("slat");
        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToNext();
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("yes", ID);
        close_sql();
    }


    int lightelement;
    Resources resources;
    boolean once = true;
    Drawable buttons;
    Drawable simpelbackground;
    Drawable lightbackback;
    Drawable lightstatsback;
    int black;
    private void light_mode() {
        darkmode = false;

        if(once) {
            once = false;
            lightelement = resources.getColor(R.color.lightelement);
            buttons = resources.getDrawable(R.drawable.buttons);
            simpelbackground = resources.getDrawable(R.drawable.simpelbackground);
            lightbackback = resources.getDrawable(R.drawable.lightbackback);
            lightstatsback = resources.getDrawable(R.drawable.lightstatsback);
            black = resources.getColor(R.color.black);
        }

        nightmodebackground.setBackground(lightstatsback);
        maintitle.setTextColor(lightelement);
        backarrowbackground.setBackground(lightbackback);
        kiblatitle.setTextColor(black);
        fix.setBackground(buttons);
        full.setBackground(simpelbackground);

        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(1);
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("no", ID);
        close_sql();
    }

}