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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.dialogs.LanguageChange;
import com.krimzon.scuffedbots.raka3at.dialogs.SlatCustomDialogClass;
import com.krimzon.scuffedbots.raka3at.background.ProcessMainClass;
import com.krimzon.scuffedbots.raka3at.background.restarter.RestartServiceBroadcastReceiver;

import java.util.Locale;

import static android.view.animation.AnimationUtils.loadAnimation;

public class MainActivity extends AppCompatActivity {

    private TextView maintitle;

    private Button slatjoin;
    private Button kiblajoin;
    private Button forcejoin;

    private Typeface font;

    private Intent kiblaIntent;
    private Intent slatIntent;
    private Intent forceIntent;

    private long backPressedTime;

    private Toast backToast;

    private Resources resources;
    private int darkbackgroundcolor;

    private String language = "ar";

    private String force, kibla, prayer, app_name;
    private SlatCustomDialogClass cddd;
    private boolean tutorial = false;
    private LinearLayout botton, botton2;
    private Animation diagonal, diagonal2;

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

    private FrameLayout full;
    private boolean once = true, once2 = true;
    private Drawable forcefull;
    private int white;
    private Drawable buttons, buttons2, buttons3;
    private Drawable darkbuttons, darkbuttons2, darkbuttons3;
    private Drawable simpelbackground;

    private int black;
    private View view, view2;
    private void light_mode() {
        darkmode = false;
        if(once){
            once = false;
            full = findViewById(R.id.full);
            view = findViewById(R.id.view);
            view2 = findViewById(R.id.view2);
            simpelbackground = resources.getDrawable(R.drawable.simpelbackground);
            buttons = resources.getDrawable(R.drawable.buttons);
            buttons2 = resources.getDrawable(R.drawable.buttons);
            buttons3 = resources.getDrawable(R.drawable.buttons);
            black = resources.getColor(R.color.black);
        }
        view.setVisibility(View.GONE);
        view2.setVisibility(View.VISIBLE);
        full.setBackground(simpelbackground);
        kiblajoin.setBackground(buttons);
        slatjoin.setBackground(buttons2);
        forcejoin.setBackground(buttons3);
        maintitle.setTextColor(black);
        sql(resources.getString(R.string.slat));
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("no", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

    }


    private String ID = "";
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
            view = findViewById(R.id.view);
            view2 = findViewById(R.id.view2);
            forcefull = resources.getDrawable(R.drawable.forcefull);
            darkbuttons = resources.getDrawable(R.drawable.darkbuttons2);
            darkbuttons2 = resources.getDrawable(R.drawable.darkbuttons2);
            darkbuttons3 = resources.getDrawable(R.drawable.darkbuttons2);
            white = resources.getColor(R.color.white);
        }
        view.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
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

    private ImageView nightmodebutton, languagebutton;
    private void variables() {

        nightmodebutton = findViewById(R.id.nightmodebutton);
        languagebutton = findViewById(R.id.languagebutton);
        try {
            Glide.with(this).load(R.drawable.blacknightmode).into(nightmodebutton);
        } catch (Exception ignored) {
            nightmodebutton.setImageDrawable(resources.getDrawable(R.drawable.blacknightmode));
        }
        try {
            Glide.with(this).load(R.drawable.blacklanguage).into(languagebutton);
        } catch (Exception ignored) {
            languagebutton.setImageDrawable(resources.getDrawable(R.drawable.blacklanguage));
        }

        cddd=new SlatCustomDialogClass(this, true, language);
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
        font = Typeface.createFromAsset(getAssets(),  "Tajawal-Light.ttf");

        maintitle.setTypeface(font);
        slatjoin.setTypeface(font);
        kiblajoin.setTypeface(font);
        forcejoin.setTypeface(font);
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


    private boolean darkmode = true;
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

        // adan service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }

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


    public void changeLanguageClicked(View view) {
        LanguageChange languageChange = new LanguageChange(this, darkmode, language);
        languageChange.show();
    }


    public void nightmodeClicked(View view) {
        if(darkmode)
            light_mode();
        else
            dark_mode();
    }



}
