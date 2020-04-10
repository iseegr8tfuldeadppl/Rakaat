package com.krimzon.scuffedbots.raka3at;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.background.ProcessMainClass;
import com.krimzon.scuffedbots.raka3at.dialogs.LanguageChange;
import com.krimzon.scuffedbots.raka3at.background.restarter.RestartServiceBroadcastReceiver;
import java.io.File;
import java.io.FileInputStream;
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
    private String language = "ar";
    private boolean tutorial = false;
    private LinearLayout botton, botton2;
    private Animation diagonal2;
    private FrameLayout full;
    private View view, view2;
    private String ID = "";
    private boolean darkmode = true;

    private void print(Object dumps) {
        Toast.makeText(getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // override system locale
        Configuration cfg = new Configuration();
        cfg.locale = new Locale(getResources().getString(R.string.en));
        this.getResources().updateConfiguration(cfg, null);

        //set custom font
        setup_the_stuff();
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

        /*if(shouldyouopenloginpage.equals("yes") && mAuth.getCurrentUser() == null){
            try{
                    if (!getIntent().getStringExtra("senderr").equals("dont")) {
                        openloginpage();
                    }
            } catch(Exception ignored){
                openloginpage();
            }
        }*/

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

    private LinearLayout rightsideelementsbackground, rightsideelementsbackground2;
    private void light_mode() {
        darkmode = false;
        rightsideelementsbackground = findViewById(R.id.rightsideelementsbackground);
        rightsideelementsbackground.setBackground(getResources().getDrawable(R.drawable.lightmainactivityback));
        rightsideelementsbackground2 = findViewById(R.id.rightsideelementsbackground2);
        rightsideelementsbackground2.setBackground(getResources().getDrawable(R.drawable.lightmainactivityback));
        view.setVisibility(View.GONE);
        view2.setVisibility(View.VISIBLE);
        full.setBackground(getResources().getDrawable(R.drawable.simpelbackground));
        kiblajoin.setBackground(getResources().getDrawable(R.drawable.buttons));
        slatjoin.setBackground(getResources().getDrawable(R.drawable.buttons));
        forcejoin.setBackground(getResources().getDrawable(R.drawable.buttons));
        maintitle.setTextColor(getResources().getColor(R.color.black));
        sql(getResources().getString(R.string.slat));
        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToNext();
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("no", ID);
        close_sql();

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

    private void dark_mode() {
        darkmode = true;
        rightsideelementsbackground = findViewById(R.id.rightsideelementsbackground);
        rightsideelementsbackground.setBackground(getResources().getDrawable(R.drawable.mainactivityback));
        rightsideelementsbackground2 = findViewById(R.id.rightsideelementsbackground2);
        rightsideelementsbackground2.setBackground(getResources().getDrawable(R.drawable.mainactivityback));
        view.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        full.setBackground(getResources().getDrawable(R.drawable.forcefull));
        kiblajoin.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));
        slatjoin.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));
        forcejoin.setBackground(getResources().getDrawable(R.drawable.darkbuttons2));
        maintitle.setTextColor(getResources().getColor(R.color.white));

        sql(getResources().getString(R.string.slat));
        SQLSharing.mycursorslat.moveToPosition(1);
        ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mydbslat.updateData("yes", ID);
        close_sql();

    }

    private void variables() {

        ImageView languagebutton = findViewById(R.id.languagebutton);
        full = findViewById(R.id.full);
        view = findViewById(R.id.view);
        view2 = findViewById(R.id.view2);
        maintitle = findViewById(R.id.maintitle);
        slatjoin = findViewById(R.id.slatjoin);
        kiblajoin = findViewById(R.id.kiblajoin);
        forcejoin = findViewById(R.id.forcejoin);


        ImageView nightmodebutton = findViewById(R.id.nightmodebutton);
            nightmodebutton.setImageDrawable(getResources().getDrawable(R.drawable.nightmodedark));
        ImageView sharebutton = findViewById(R.id.sharebutton);
            sharebutton.setImageDrawable(getResources().getDrawable(R.drawable.shre));

        try {
            Glide.with(this).load(R.drawable.blacklanguage).into(languagebutton);
        } catch (Exception ignored) {
            languagebutton.setImageDrawable(getResources().getDrawable(R.drawable.blacklanguage));
        }

        if (mAuth.getCurrentUser() == null) {
                image.setImageDrawable(getResources().getDrawable(R.drawable.ic_google_logo));
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        load_service();

        /*try {
            NotificationManager notificationManager2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder notif = new NotificationCompat.Builder(this, "channel3");
            notif.setLights(Color.MAGENTA, 100, 100)
            .setPriority(Notification.PRIORITY_MAX)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Prayer done!");
            notificationManager2.notify(5, notif.build());
        } catch(Exception ignored){}*/
    }


    @Override
    protected void onPause() {
        super.onPause();

        /*try {
            NotificationManager notificationManager3 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager3.cancel(5);
        } catch(Exception ignored){}*/

        if(onComplete!=null)
            unregisterReceiver(onComplete);

        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    private void load_service() {
        // adan service
        if (Build.VERSION.SDK_INT < 28) {
            try {
                close_sql();
                sql("force");
                if (SQLSharing.mycursorforce.getCount() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
                    } else {
                        ProcessMainClass bck = new ProcessMainClass();
                        bck.launchService(getApplicationContext());
                    }
                }
                close_sql();
            } catch (Exception ignored) {
            }
        }
    }
    private BroadcastReceiver onComplete;
    public void downloadMeSenpai(final Context context, String DISPLAY, String destinationDirectory, String url, final File imageplace) {
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, DISPLAY);
        try{
            final long downloadID = downloadmanager.enqueue(request);

            onComplete=new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (downloadID == id) {
                        getout(imageplace);
                    }
                }
            };
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    private static final int GOOGLE_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private int REQUEST_CODE = 1000;
    private de.hdodenhof.circleimageview.CircleImageView image;

    private void setup_the_stuff() {

        progressBar = findViewById(R.id.progress);
        image = findViewById(R.id.loginbutton);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String photo = String.valueOf(user.getPhotoUrl());

            String email = user.getEmail();
            if(email!=null){
                String userer = email.replace(".", "").replace("@", "");
                File profilpic = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/com.krimzon.scuffedbots.raka3at/files/random/" + userer + ".png");
                if(!profilpic.exists())
                    downloadMeSenpai(this, userer + ".png", "random", photo, profilpic);
                else
                    getout(profilpic);
            }
        }
    }

    private void getout(File imageplace){
        progressBar.setVisibility(View.INVISIBLE);
        try{
            FileInputStream inputStream = new FileInputStream(imageplace);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Bitmap bitmap_mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Drawable profilpic = new BitmapDrawable(getResources(), bitmap_mutable);
            try {
                Glide.with(this).load(profilpic).into(image);
            } catch (Exception ignored) {
                image.setImageDrawable(profilpic);
            }
        } catch(Exception e){ e.printStackTrace(); }
        /*Intent start = new Intent(this, MainActivity.class);
        startActivity(start);
        finish();*/
    }

    public void loginClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            if (mAuth.getCurrentUser() != null) {
                prompt_logout_confirmation();
            } else {
                SignInGoogle();
            }
        }
    }

    private void prompt_logout_confirmation() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        if(language.equals("en")){
            alertDialog.setTitle(getString(R.string.logoutsecurityalerttitle));
            alertDialog.setMessage(getString(R.string.logoutsecurityalerttext));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.allgood),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else if(language.equals("ar")){
            alertDialog.setTitle(getString(R.string.logoutsecurityalerttitle_arabe));
            alertDialog.setMessage(getString(R.string.logoutsecurityalerttext_arabe));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.allgood_arabe),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.back2_arabe),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        alertDialog.show();
    }

    private void logout() {

        // set SQL back to display default login page
        close_sql();
        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(13);
        SQLSharing.mydbslat.updateData("no", SQLSharing.mycursorslat.getString(0));
        close_sql();

        // start loading
        progressBar.setVisibility(View.VISIBLE);

        // firebase sign out
        mAuth.signOut();


        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        // stop loading
                        progressBar.setVisibility(View.INVISIBLE);

                        // redisplay Google logo there
                            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_google_logo));
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(REQUEST_CODE==requestCode && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SignInGoogle();
            } else if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                if(language.equals("en"))
                    print(getResources().getString(R.string.permissiondenied));
                else if(language.equals("ar"))
                    print(getResources().getString(R.string.permissiondenied_arabe));
                progressBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    public void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else
                            updateUI(null);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException ignored) {}
        } else if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
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
        } catch(Exception ignored){
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
    protected void onDestroy() {
        super.onDestroy();

        if(onComplete!=null)
         unregisterReceiver(onComplete);

        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }



    private void set_fonts() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Tajawal-Light.ttf");

        maintitle.setTypeface(font);
        slatjoin.setTypeface(font);
        kiblajoin.setTypeface(font);
        forcejoin.setTypeface(font);
    }

    private void english() {
        maintitle.setText(getResources().getString(R.string.app_name2));
        kiblajoin.setText(getResources().getString(R.string.kibla));
        forcejoin.setText(getResources().getString(R.string.force));
        slatjoin.setText(getResources().getString(R.string.prayer));
    }

    private void close_sql() {
        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    private void sql_work() {


        // TODO MUST KEEP
        /*SQLSharing.TABLE_NAME_INPUTER = "force";
        SQLSharing.mydb = SQL.getInstance(this);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        if(SQLSharing.mycursor.getCount()!=1) // TODO always update this
            SQLSharing.mydb.delete(this);
        if(SQLSharing.mydb!=null && SQLSharing.mycursor!=null) {
            SQLSharing.mydb.close();
            SQLSharing.mycursor.close();
        }*/

        // this is to avoid issues with added rows with google darkplay updates to avoid crashing users
        sql("slat");
        int amount_of_parameters_in_slat = 15;
        if(SQLSharing.mycursorslat.getCount()<5) { // this excludes any very old versions
            SQLSharing.mydbslat.delete(this);
        }
        if(SQLSharing.mycursorslat.getCount()<amount_of_parameters_in_slat) { // TODO always update this
            switch(SQLSharing.mycursorslat.getCount()){
                case 5: // you add the one numbered after it, say if there's 5 in sql settings you add thhe 6th, and then all of the others would get added too
                    SQLSharing.mydbslat.insertData("1"); // sounds default: on
                case 6:
                    SQLSharing.mydbslat.insertData("ar"); // language
                case 7:
                    SQLSharing.mydbslat.insertData("1,2 1,1 1,2 1,2 1,2 1,2"); // 1,2 => default adan, adan sounds fully on (1 is for vibrte, 0 is for no sounds)
                case 8:
                    SQLSharing.mydbslat.insertData("yes"); // display the main app notification (essential for newer androids to keep app running
                case 9:
                    SQLSharing.mydbslat.insertData("yes"); // do i ask for protected apps on launch?
                case 10:
                    SQLSharing.mydbslat.insertData("5,35,1 5,35,1 5,35,1 5,20,1 5,35,1"); // delaysbeforeandafterdan to mute ringtones before adan/after adan/enabled or not
                case 11:
                    SQLSharing.mydbslat.insertData(""); // tutorial for force
                case 12:
                    SQLSharing.mydbslat.insertData("yes"); // onetimedisplayofqiblacalibrationwindow
                case 13:
                    SQLSharing.mydbslat.insertData("yes"); // should you send to the login page?
                case 14:
                    SQLSharing.mydbslat.insertData(""); // city
            }
        }
        close_sql();
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
            SQLSharing.mydbslat.insertData("5,35,1 5,35,1 5,35,1 5,20,1 5,35,1"); // delaysbeforeandafterdan to mute ringtones before adan/after adan/enabled or not
            SQLSharing.mydbslat.insertData(""); // tutorial for force
            SQLSharing.mydbslat.insertData("yes"); // onetimedisplayofqiblacalibrationwindow
            SQLSharing.mydbslat.insertData("yes"); // should you send to the login page?
            SQLSharing.mydbslat.insertData(""); // city
            tutorial = true;
        } else {
            SQLSharing.mycursorslat.moveToPosition(0);
            if(SQLSharing.mycursorslat.getString(1).equals(""))
                tutorial = true;
            SQLSharing.mycursorslat.moveToPosition(6);
            language = SQLSharing.mycursorslat.getString(1);

            SQLSharing.mycursorslat.moveToPosition(11);
            forcetutorial = SQLSharing.mycursorslat.getString(1);

            SQLSharing.mycursorslat.moveToNext();
            SQLSharing.mycursorslat.moveToNext();

            shouldyouopenloginpage = SQLSharing.mycursorslat.getString(1);

            SQLSharing.mycursorslat.moveToPosition(1);
            if(SQLSharing.mycursorslat.getString(1).equals("no"))
                light_mode();
            else
                darkmode = true;
        }
        close_sql();

    }


    private String shouldyouopenloginpage = "yes";
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

    private void openloginpage() {
        Intent backupandrestore = new Intent(this, backupandrestore.class);
        startActivity(backupandrestore);
        finish();
    }

    public void shareClicked(View view) {
        if(language.equals("en")) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType(getString(R.string.textplain));
            String shareBody = getString(R.string.share) + getString(R.string.link);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.sharetitle_arabe));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharevia)));
        } else if(language.equals("ar")){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType(getString(R.string.textplain));
            String shareBody = getString(R.string.share_arabe) + getString(R.string.link);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.sharetitle_arabe));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharevia_arabe)));

        }
    }
}
