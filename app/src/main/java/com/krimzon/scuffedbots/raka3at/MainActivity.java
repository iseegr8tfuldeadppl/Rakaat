package com.krimzon.scuffedbots.raka3at;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.krimzon.scuffedbots.raka3at.background.ProcessMainClass;
import com.krimzon.scuffedbots.raka3at.dialogs.LanguageChange;
import com.krimzon.scuffedbots.raka3at.dialogs.SlatCustomDialogClass;
import com.krimzon.scuffedbots.raka3at.background.restarter.RestartServiceBroadcastReceiver;

import java.util.Locale;

import static android.view.animation.AnimationUtils.loadAnimation;

public class MainActivity extends AppCompatActivity {

    private TextView maintitle;

    private Button slatjoin;
    private Button kiblajoin;
    private Button forcejoin;
    private long backPressedTime;
    private Toast backToast;
    private Resources resources;
    private String language = "ar";

    private boolean tutorial = false;
    private LinearLayout botton, botton2;
    private Animation diagonal2;
    private FrameLayout full;

    private View view, view2;
    private String ID = "";
    private boolean darkmode = true;

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
        if(language.equals("en"))
            english();

        // slidein nightmode button
        botton = findViewById(R.id.botton);
        Animation diagonal = loadAnimation(getApplicationContext(), R.anim.diagonalslide);
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

    private void light_mode() {
        darkmode = false;
        view.setVisibility(View.GONE);
        view2.setVisibility(View.VISIBLE);
        full.setBackground(resources.getDrawable(R.drawable.simpelbackground));
        kiblajoin.setBackground(resources.getDrawable(R.drawable.buttons));
        slatjoin.setBackground(resources.getDrawable(R.drawable.buttons));
        forcejoin.setBackground(resources.getDrawable(R.drawable.buttons));
        maintitle.setTextColor(resources.getColor(R.color.black));
        sql(resources.getString(R.string.slat));
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("no", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

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

    private void dark_mode() {
        darkmode = true;
        view.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        full.setBackground(resources.getDrawable(R.drawable.forcefull));
        kiblajoin.setBackground(resources.getDrawable(R.drawable.darkbuttons2));
        slatjoin.setBackground(resources.getDrawable(R.drawable.darkbuttons2));
        forcejoin.setBackground(resources.getDrawable(R.drawable.darkbuttons2));
        maintitle.setTextColor(resources.getColor(R.color.white));

        sql(resources.getString(R.string.slat));
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData("yes", ID);
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();

    }

    private void variables() {

        ImageView nightmodebutton = findViewById(R.id.nightmodebutton);
        ImageView languagebutton = findViewById(R.id.languagebutton);
        full = findViewById(R.id.full);
        view = findViewById(R.id.view);
        view2 = findViewById(R.id.view2);
        maintitle = findViewById(R.id.maintitle);
        slatjoin = findViewById(R.id.slatjoin);
        kiblajoin = findViewById(R.id.kiblajoin);
        forcejoin = findViewById(R.id.forcejoin);
        resources = getResources();
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

        /*PrivacyPolicy = new Intent(this, PrivacyPolicy.class);*/
        /*scuffedbots = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/scuffedbots"));*/
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

    private void set_fonts() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Tajawal-Light.ttf");

        maintitle.setTypeface(font);
        slatjoin.setTypeface(font);
        kiblajoin.setTypeface(font);
        forcejoin.setTypeface(font);
    }

    private void english() {
        maintitle.setText(resources.getString(R.string.app_name2));
        kiblajoin.setText(resources.getString(R.string.kibla));
        forcejoin.setText(resources.getString(R.string.force));
        slatjoin.setText(resources.getString(R.string.prayer));
    }

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

        // this is to avoid issues with added rows with google darkplay updates to avoid crashing users
        sql("slat");
        if(SQLSharing.mycursor.getCount()<8)  // TODO always update this
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
            SQLSharing.mydb.insertData("1,2 1,1 1,2 1,2 1,2 1,2"); // 1,2 => default adan, adan sounds fully on (1 is for vibrte, 0 is for no sounds)
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
    /*public void scuffedClicked(View view) {
        startActivity(scuffedbots);
        finish();
    }*/

    public void salatClicked(View view) {
        if(tutorial) {
            SlatCustomDialogClass cddd = new SlatCustomDialogClass(this, true, language);
            cddd.show();
        } else {
            Intent slatIntent = new Intent(this, slat.class);
            slatIntent.putExtra("sender", "main");
            startActivity(slatIntent);
            finish();
        }
    }

    public void kiblaClicked(View view) {
        Intent kiblaIntent = new Intent(this, kibla.class);
        startActivity(kiblaIntent);
        finish();
    }

    public void forceClicked(View view) {
        Intent forceIntent = new Intent(this, force.class);
        forceIntent.putExtra("light_alert", "no");
        startActivity(forceIntent);
        finish(); }

    /*private void showNavigationBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getWindow().setStatusBarColor(darkbackgroundcolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }*/

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
