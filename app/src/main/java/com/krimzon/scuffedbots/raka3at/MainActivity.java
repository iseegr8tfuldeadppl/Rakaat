package com.krimzon.scuffedbots.raka3at;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.dialogs.SlatCustomDialogClass;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView maintitle;

    private Button slatjoin;
    private Button kiblajoin;
    private Button forcejoin;

    private Typeface english_font;
    private Typeface arabic_font;

    private Intent kiblaIntent;
    private Intent slatIntent;
    private Intent forceIntent;

    private long backPressedTime;

    private Toast backToast;

    protected Resources resources;
    private int darkbackgroundcolor;

    public static String language = "ar";

    private String force_arabe, kibla_arabe, prayer_arabe, app_name_arabe;
    private String force, kibla, prayer, app_name;
    private SlatCustomDialogClass cddd;
    private boolean tutorial = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set custom font
        variables();
        sql_work();
        set_fonts();
        work_on_language();

        showNavigationBar();
    }

    private void work_on_language() {
        load_strings();

        if(language.equals("en"))
            english();
    }

    private void variables() {
        cddd=new SlatCustomDialogClass(this, true);
        maintitle = findViewById(R.id.maintitle);
        slatjoin = findViewById(R.id.slatjoin);
        kiblajoin = findViewById(R.id.kiblajoin);
        forcejoin = findViewById(R.id.forcejoin);
        resources = getResources();
        darkbackgroundcolor = resources.getColor(R.color.black);

        /*PrivacyPolicy = new Intent(this, PrivacyPolicy.class);*/
        slatIntent = new Intent(this, slat.class);
        /*scuffedbots = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/scuffedbots"));*/
        kiblaIntent = new Intent(this, kibla.class);
        forceIntent = new Intent(this, force.class);
        forceIntent.putExtra("light_alert", "no");
    }

    private void set_fonts() {
        arabic_font = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");
        english_font = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");
        if(language.equals("en")) {
            maintitle.setTypeface(english_font);
            slatjoin.setTypeface(english_font);
            kiblajoin.setTypeface(english_font);
            forcejoin.setTypeface(english_font);
        } else if(language.equals("ar")) {
            maintitle.setTypeface(arabic_font);
            slatjoin.setTypeface(arabic_font);
            kiblajoin.setTypeface(arabic_font);
            forcejoin.setTypeface(arabic_font);
        }
    }

    private void load_strings() {
        if(language.equals("en")) {
            force = resources.getString(R.string.force);
            kibla = resources.getString(R.string.kibla);
            prayer = resources.getString(R.string.prayer);
            app_name = resources.getString(R.string.app_name2);
        }

    }

    private void english() {
        maintitle.setText(app_name);
        kiblajoin.setText(kibla);
        forcejoin.setText(force);
        slatjoin.setText(prayer);
    }

    private void arabic() {
        maintitle.setText(app_name_arabe);
        kiblajoin.setText(kibla_arabe);
        forcejoin.setText(force_arabe);
        slatjoin.setText(prayer_arabe);
    }

    private void sql_work() {


        SQLSharing.TABLE_NAME_INPUTER = "force";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        if(SQLSharing.mycursor.getCount()!=1) // TODO always update this
            SQLSharing.mydb.delete(this);
        SQLSharing.mydb.close();
        SQLSharing.mycursor.close();

        // this is to avoid issues with added rows with google play updates to avoid crashing users
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        if(SQLSharing.mycursor.getCount()<7)  // TODO always update this
            SQLSharing.mydb.delete(this);
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        if (SQLSharing.mycursor.getCount() <= 0) {
            SQLSharing.mydb.insertData("");
            SQLSharing.mydb.insertData("yes");
            SQLSharing.mydb.insertData("3"); // scheme
            SQLSharing.mydb.insertData("0"); // scheme_light_mode
            SQLSharing.mydb.insertData("5"); // delay before starting detection
            SQLSharing.mydb.insertData("1"); // sounds default: on
            SQLSharing.mydb.insertData("ar"); // language
            tutorial = true;
        } else {
            SQLSharing.mycursor.moveToFirst();
            if(SQLSharing.mycursor.getString(1).equals(""))
                tutorial = true;
            SQLSharing.mycursor.moveToNext();
            SQLSharing.mycursor.moveToNext();
            SQLSharing.mycursor.moveToNext();
            SQLSharing.mycursor.moveToNext();
            SQLSharing.mycursor.moveToNext();
            SQLSharing.mycursor.moveToNext();
            language = SQLSharing.mycursor.getString(1);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.pressbackagainenglish), Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    /*public void privacyClicked(View view) {
        startActivity(PrivacyPolicy);
    }*/

    public void salatClicked(View view) {
        if(tutorial)
            cddd.show();
        else {
            slatIntent.putExtra("sender", "main");
            startActivity(slatIntent);
            finish();
        }
    }

    public void kiblaClicked(View view) {
        startActivity(kiblaIntent);
        finish();
    }

    /*public void scuffedClicked(View view) {
        startActivity(scuffedbots);
        finish();
    }*/

    public void forceClicked(View view) { startActivity(forceIntent); finish(); }

    private void showNavigationBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getWindow().setStatusBarColor(darkbackgroundcolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    private void print(Object lol) {
        Toast.makeText(getApplicationContext(), String.valueOf(lol), Toast.LENGTH_SHORT).show();
    }


}
