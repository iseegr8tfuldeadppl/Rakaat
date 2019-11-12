package com.krimzon.scuffedbots.raka3at;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.dialogs.LanguageChange;
import com.krimzon.scuffedbots.raka3at.dialogs.SlatCustomDialogClass;

import java.util.Locale;

import static android.view.animation.AnimationUtils.loadAnimation;

public class MainActivity extends AppCompatActivity {

    private TextView maintitle;

    private Button slatjoin;
    private Button kiblajoin;
    private Button forcejoin;

    protected Typeface english_font;
    protected Typeface arabic_font;

    private Intent kiblaIntent;
    private Intent slatIntent;
    private Intent forceIntent;

    private long backPressedTime;

    private Toast backToast;

    protected Resources resources;
    private int darkbackgroundcolor;

    public static String language = "ar";

    private String force, kibla, prayer, app_name;
    private SlatCustomDialogClass cddd;
    private boolean tutorial = false;
    protected LinearLayout botton, botton2;
    protected Animation diagonal, diagonal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // override system locale
        Configuration cfg = new Configuration();
        cfg.locale = new Locale(getResources().getString(R.string.en));
        this.getResources().updateConfiguration(cfg, null);

        //set custom font
        variables();
        sql_work();
        set_fonts();
        work_on_language();

        // slidein nightmode button
        botton = findViewById(R.id.botton);
        diagonal = loadAnimation(getApplicationContext(), R.anim.diagonalslide);
        botton.startAnimation(diagonal);
        diagonal.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            botton.setVisibility(View.VISIBLE);

            // slidein language button
            botton2 = findViewById(R.id.botton2);
            diagonal2 = loadAnimation(getApplicationContext(), R.anim.diagonalslide2);
            botton2.startAnimation(diagonal2);
            diagonal2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                botton2.setVisibility(View.VISIBLE);
            }});
        }});

        //showNavigationBar();
    }

    FrameLayout full;
    boolean once = true, once2 = true;
    Drawable forcefull;
    int lightelement, white;
    Drawable buttons, buttons2, buttons3;
    Drawable darkbuttons, darkbuttons2, darkbuttons3;
    Drawable simpelbackground;

    private void light_mode() {
        darkmode = false;
        if(once){
            once = false;
            full = findViewById(R.id.full);
            simpelbackground = resources.getDrawable(R.drawable.simpelbackground);
            buttons = resources.getDrawable(R.drawable.buttons);
            buttons2 = resources.getDrawable(R.drawable.buttons);
            buttons3 = resources.getDrawable(R.drawable.buttons);
            lightelement = resources.getColor(R.color.lightelement);
        }
        full.setBackground(simpelbackground);
        kiblajoin.setBackground(buttons);
        slatjoin.setBackground(buttons2);
        forcejoin.setBackground(buttons3);
        maintitle.setTextColor(lightelement);

        sql(resources.getString(R.string.slat));
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("no", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

    }


    String ID = "";
    private void sql(String table) {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = table;
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
    }

    private void dark_mode() {
        darkmode = true;
        if(once2){
            once2 = false;
            full = findViewById(R.id.full);
            forcefull = resources.getDrawable(R.drawable.forcefull);
            darkbuttons = resources.getDrawable(R.drawable.darkbuttons2);
            darkbuttons2 = resources.getDrawable(R.drawable.darkbuttons2);
            darkbuttons3 = resources.getDrawable(R.drawable.darkbuttons2);
            white = resources.getColor(R.color.white);
        }
        full.setBackground(forcefull);
        kiblajoin.setBackground(darkbuttons);
        slatjoin.setBackground(darkbuttons2);
        forcejoin.setBackground(darkbuttons3);
        maintitle.setTextColor(white);

        sql(resources.getString(R.string.slat));
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("yes", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

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


    boolean darkmode = true;
    private void sql_work() {


        // TODO MUST KEEP
        /*SQLSharing.TABLE_NAME_INPUTER = "force";
        SQLSharing.mydb = new SQL(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        if(SQLSharing.mycursor.getCount()!=1) // TODO always update this
            SQLSharing.mydb.delete(this);
        if(SQLSharing.mydb!=null && SQLSharing.mycursor!=null) {
            SQLSharing.mydb.close();
            SQLSharing.mycursor.close();
        }*/

        // this is to avoid issues with added rows with google play updates to avoid crashing users
        sql("slat");
        if(SQLSharing.mycursor.getCount()<7)  // TODO always update this
            SQLSharing.mydb.delete(this);
        sql("slat");
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
            SQLSharing.mycursor.moveToPosition(0);
            if(SQLSharing.mycursor.getString(1).equals(""))
                tutorial = true;
            SQLSharing.mycursor.moveToPosition(6);
            language = SQLSharing.mycursor.getString(1);

            SQLSharing.mycursor.moveToPosition(1);
            if(SQLSharing.mycursor.getString(1).equals("no"))
                light_mode();
            else
                darkmode = true;
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
            if(language.contains("en"))
                backToast = Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.pressbackagainenglish), Toast.LENGTH_SHORT);
            else if(language.contains("ar"))
                backToast = Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.pressbackagainenglish_arabe), Toast.LENGTH_SHORT);
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


    public void changeLanguageClicked(View view) {
        LanguageChange languageChange = new LanguageChange(this);
        languageChange.show();
    }


    public void nightmodeClicked(View view) {
        if(darkmode)
            light_mode();
        else
            dark_mode();
    }
}
