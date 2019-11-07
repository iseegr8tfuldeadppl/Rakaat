package com.krimzon.scuffedbots.raka3at;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Qibla;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.dialogs.CustomDialogClass;

public class kibla extends AppCompatActivity implements SensorEventListener {

    // TODO work on translation settexts and shit for me
    // TODO and my popup menu
    // TODO and on force.java

    private String language;
    ImageView compass_img;
    Button back, fix;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    protected TextView kiblatitle;
    String kibla, kiblatitleh, betterquality, backh;
    private TextView maintitle;
    Typeface custom_font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kibla);

        location_shit();

        kiblatitle = findViewById(R.id.kiblatitle);
        maintitle = findViewById(R.id.maintitle);
        back = findViewById(R.id.back);
        fix = findViewById(R.id.fix);
        custom_font = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");
        back.setTypeface(custom_font);
        fix.setTypeface(custom_font);
        kiblatitle.setTypeface(custom_font);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img = (ImageView) findViewById(R.id.compass);

        work_on_language();
        start();

        hideNavigationBar();
    }

    private void work_on_language(){
        getLanguage();
        getStrings();
        if(language.equals("en"))
            english();
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

    private void getLanguage(){
        sql("slat");

        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        language = SQLSharing.mycursor.getString(1);
    }

    private void getStrings(){
        if(language.equals("en")){
            backh = getString(R.string.back);
            betterquality = getString(R.string.betterquality);
            kiblatitleh = getString(R.string.kiblatitle);
            kibla = getString(R.string.kibla);
        }
    }

    private void english(){
        maintitle.setText(kibla);
        kiblatitle.setText(kiblatitleh);
        fix.setText(betterquality);
        back.setText(backh);
        maintitle.setTypeface(custom_font);
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

        if(qibla_angle - qibla_range <= mAzimuth && mAzimuth <= qibla_angle + qibla_range)
            compass_img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.compass));
        else
            compass_img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.compass2));
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
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
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

        // update coordinates in sql folks
        if(new_coordinates)
            SQLSharing.mydb.insertMawa9it(String.valueOf(longitude), String.valueOf(latitude));
        else
            SQLSharing.mydb.updateMawa9it("1", String.valueOf(longitude), String.valueOf(latitude));

        coordinates = new Coordinates(latitude, longitude);
        qibla = new Qibla(coordinates);
        qibla_angle = qibla.direction;
    }

    protected double longitude = 0, latitude = 0;
    private void if_theres_previous_info_load_it_n_display() {
        new_coordinates = false;
        SQLSharing.mycursor.moveToFirst();
        longitude = Double.valueOf(SQLSharing.mycursor.getString(1));
        latitude = Double.valueOf(SQLSharing.mycursor.getString(2));
        wegotcoordsboiz(longitude, latitude);
    }

    private static final int REQUEST_CODE = 1000;
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

    public void backClicked(View view) {
        exit();
    }

    public void fixClicked(View view) {
        CustomDialogClass cdd=new CustomDialogClass(this);
        cdd.show();
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

}