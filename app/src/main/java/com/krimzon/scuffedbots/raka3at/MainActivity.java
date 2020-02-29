package com.krimzon.scuffedbots.raka3at;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
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
import com.krimzon.scuffedbots.raka3at.background.restarter.RestartServiceBroadcastReceiver;
import java.util.Locale;

import static android.view.animation.AnimationUtils.loadAnimation;

public class MainActivity extends AppCompatActivity {

    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 6969;
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


    /*private void h(){

        String didyoupray = "";
        String gopray = "";
        String iprayed = "";
        String expandmefortext = "";

        if (language.equals("en")) {
            gopray = getResources().getString(R.string.prayedit);
            iprayed = getResources().getString(R.string.joinprayer);
            expandmefortext = getResources().getString(R.string.expandme);

            switch (most_recent_unprayed) {
                case 0:
                    didyoupray = getResources().getString(R.string.didyouprayfajr);
                    break;
                case 1:
                    didyoupray = getResources().getString(R.string.didyoupraydhuhr);
                    break;
                case 2:
                    didyoupray = getResources().getString(R.string.didyouprayasr);
                    break;
                case 3:
                    didyoupray = getResources().getString(R.string.didyoupraymaghreb);
                    break;
                case 4:
                    didyoupray = getResources().getString(R.string.didyouprayisha);
                    break;
            }
        } else if(language.equals("ar")){
            gopray = getResources().getString(R.string.prayedit_arabe);
            iprayed = getResources().getString(R.string.joinprayer_arabic);
            expandmefortext = getResources().getString(R.string.expandme_arabe);

            switch (most_recent_unprayed) {
                case 0:
                    didyoupray = getResources().getString(R.string.didyouprayfajr_arabe);
                    break;
                case 1:
                    didyoupray = getResources().getString(R.string.didyoupraydhuhr_arabe);
                    break;
                case 2:
                    didyoupray = getResources().getString(R.string.didyouprayasr_arabe);
                    break;
                case 3:
                    didyoupray = getResources().getString(R.string.didyoupraymaghreb_arabe);
                    break;
                case 4:
                    didyoupray = getResources().getString(R.string.didyouprayisha_arabe);
                    break;
            }
        }



        Intent goprayintent = new Intent("com.krimzon.scuffedbots.raka3at.background.gopraymate");
        PendingIntent goprayintent_pending_event = PendingIntent.getBroadcast(getApplicationContext(),NOTIFICATION_ID2, goprayintent,0);

        Intent iprayeditintent = new Intent("com.krimzon.scuffedbots.raka3at.background.iprayeditmate");
        PendingIntent iprayedit_pending_event = PendingIntent.getBroadcast(getApplicationContext(),NOTIFICATION_ID2, iprayeditintent,0);

        Intent notification_intent = new Intent(getApplicationContext(), force.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notification_intent, 0);
        Notification noti = new Notification.Builder(this)
                .setContentTitle(didyoupray)
                .setContentText(expandmefortext).setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .addAction(R.mipmap.ic_launcher, iprayed, iprayedit_pending_event)
                .addAction(R.mipmap.ic_launcher, gopray, goprayintent_pending_event).build();
        try {
            NotificationManager notificationManager2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager2.notify(NOTIFICATION_ID2, noti);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /*private void permission_intents_test() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS, Uri.parse("package:" + getPackageName())));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }*/

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
        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToNext();
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("no", ID);

    }

    private void sql(String table) {
        SQLSharing.TABLE_NAME_INPUTER = table;
        switch (table) {
            case "slat":
                SQLSharing.mydbslat = new SQL(this);
                SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
                break;
            case "force":
                SQLSharing.mydbforce = new SQL(this);
                SQLSharing.mycursorforce = SQLSharing.mydbforce.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mydbforce3 = new SQL(this);
                SQLSharing.mycursorforce3 = SQLSharing.mydbforce3.getAllDateforce3();
                break;
        }
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
        SQLSharing.mycursorslat.moveToPosition(1);
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("yes", ID);

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

    }

    @Override
    protected void onResume() {
        super.onResume();

        // adan service
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
            } else {
                ProcessMainClass bck = new ProcessMainClass();
                bck.launchService(getApplicationContext());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    private void muter() {
        try{

            if (Build.VERSION.SDK_INT >= 23)
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            else {

                AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                //am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void unmuter() {
        try{
            if (Build.VERSION.SDK_INT >= 23)
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            else {
                AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
            if ( notificationManager.isNotificationPolicyAccessGranted()) {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else{
                // Open Setting screen to ask for permisssion
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult( intent, ON_DO_NOT_DISTURB_CALLBACK_CODE );
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
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

    private void close_sql() {
        if(SQLSharing.servicemydbforce!=null)
            SQLSharing.servicemydbforce.close();
        if(SQLSharing.servicemydbslat!=null)
            SQLSharing.servicemydbslat.close();
        if(SQLSharing.servicemydbforce3!=null)
            SQLSharing.servicemydbforce3.close();

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
        if(SQLSharing.mycursorslat.getCount()<12)  // TODO always update this
            SQLSharing.mydbslat.delete(this);
        sql("slat");
        if (SQLSharing.mycursorslat.getCount() <= 0) {
            SQLSharing.mydbslat.insertData("");
            SQLSharing.mydbslat.insertData("yes"); // darkmode
            SQLSharing.mydbslat.insertData("3"); // scheme
            SQLSharing.mydbslat.insertData("0"); // scheme_light_mode
            SQLSharing.mydbslat.insertData("5"); // delay before starting detection
            SQLSharing.mydbslat.insertData("1"); // sounds default: on
            SQLSharing.mydbslat.insertData("ar"); // language
            SQLSharing.mydbslat.insertData("1,2 1,1 1,2 1,2 1,2 1,2"); // 1,2 => default adan, adan sounds fully on (1 is for vibrte, 0 is for no sounds)
            SQLSharing.mydbslat.insertData("yes"); // display the main app notification (essential for newer androids to keep app running
            SQLSharing.mydbslat.insertData("yes"); // do i ask for protected apps on launch?
            SQLSharing.mydbslat.insertData("5,35 5,35 5,35 5,20 5,35"); // delaysbeforeandafterdan to mute ringtones
            SQLSharing.mydbslat.insertData(""); // tutorial for force
            tutorial = true;
        } else {
            SQLSharing.mycursorslat.moveToPosition(0);
            if(SQLSharing.mycursorslat.getString(1).equals(""))
                tutorial = true;
            SQLSharing.mycursorslat.moveToPosition(6);
            language = SQLSharing.mycursorslat.getString(1);

            SQLSharing.mycursorslat.moveToPosition(11);
            forcetutorial = SQLSharing.mycursorslat.getString(1);

            SQLSharing.mycursorslat.moveToPosition(1);
            if(SQLSharing.mycursorslat.getString(1).equals("no"))
                light_mode();
            else
                darkmode = true;
        }

    }


    private String forcetutorial = "";
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
            Intent tutorial = new Intent(this, Tutorial.class);
            tutorial.putExtra("sender", "main");
            startActivity(tutorial);
            finish();
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
        if(!forcetutorial.equals("1")){
            Intent forceIntent = new Intent(this, forceTutorial.class);
            startActivity(forceIntent);
            finish();
        } else {
            Intent forceIntent = new Intent(this, force.class);
            forceIntent.putExtra("light_alert", "no");
            startActivity(forceIntent);
            finish();
        }
    }

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
