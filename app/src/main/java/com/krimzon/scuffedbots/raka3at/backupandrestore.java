package com.krimzon.scuffedbots.raka3at;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import java.io.File;

public class backupandrestore extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private int REQUEST_CODE = 1000;
    private TextView secureexplanation;
    private Resources resources;
    private Button dontlogin, nevershowagain, loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backupandrestore);

        loginbutton = findViewById(R.id.loginbutton);
        nevershowagain = findViewById(R.id.nevershowagain);
        dontlogin = findViewById(R.id.dontlogin);
        progressBar = findViewById(R.id.progress);
        ImageView image = findViewById(R.id.displayer);
        secureexplanation = findViewById(R.id.secureexplanation);
        resources = getResources();

        font();
        sql_work();
        language();
        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            setup_the_stuff();
        }

    }

    private void font() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Tajawal-Regular.ttf");

        loginbutton.setTypeface(font);
        nevershowagain.setTypeface(font);
        dontlogin.setTypeface(font);
        secureexplanation.setTypeface(font);
    }

    private String language;
    private void sql_work() {
        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(6);
        language = SQLSharing.mycursorslat.getString(1);
        close_sql();
    }

    private void sql(final String table) {
        SQLSharing.TABLE_NAME_INPUTER = table;
        switch (table) {
            case "slat":
                SQLSharing.mydbslat = SQL.getInstance(getApplicationContext());
                SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
                break;
            case "force":
                SQLSharing.mydbforce = SQL.getInstance(getApplicationContext());
                SQLSharing.mycursorforce = SQLSharing.mydbforce.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mydbforce3 = SQL.getInstance(getApplicationContext());
                SQLSharing.mycursorforce3 = SQLSharing.mydbforce3.getAllDateforce3();
                break;
        }
    }

    private void language() {
        if(language.equals("en")){
            secureexplanation.setText(resources.getString(R.string.secureexplanation));
            loginbutton.setText(resources.getString(R.string.login_with_google));
            dontlogin.setText(resources.getString(R.string.dont_login_arabe));
            nevershowagain.setText(resources.getString(R.string.noshow));
        }
    }


    private void setup_the_stuff() {

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(REQUEST_CODE==requestCode && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setup_the_stuff();
                if(clicked){
                    SignInGoogle();
                    clicked = false;
                }
            } else if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                exit();
            }
        }
    }


    public void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        secureexplanation.setVisibility(View.INVISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
        }
    }


    private FirebaseUser user_global;
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            user_global = user;
            String photo = String.valueOf(user.getPhotoUrl());

            String email = user.getEmail();
            if(email!=null){
                String userer = email.replace(".", "").replace("@", "");
                File profilpic = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/com.krimzon.scuffedbots.raka3at/files/random/" + userer + ".png");
                if(!profilpic.exists())
                    downloadMeSenpai(this, userer + ".png", "random", photo);
                else
                    getout();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit();
    }

    private void exit() {
        Intent mainactivity = new Intent(this, MainActivity.class);
        mainactivity.putExtra("senderr", "dont");
        startActivity(mainactivity);
        finish();
    }

    public void dontloginClicked(View view) {
        exit();
    }


    public void nevershowagainClicked(View view) {
        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(13);
        SQLSharing.mydbslat.updateData("no", SQLSharing.mycursorslat.getString(0));
        close_sql();
        exit();
    }

    private void close_sql() {
        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    private void check_firebase_if_updated_today() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            String refinedemail = getUserEmail(user);
            if(refinedemail!=null)
                sync_SQL_and_Firebase(refinedemail, uid);
        }
    }


    private void getout(){
        check_firebase_if_updated_today();
    }

    private String getUserEmail(FirebaseUser user) {
        if (user != null) {
            String email = user.getEmail();
            if(email!=null){
                return email.replace(".", "").replace("@", "");
            }
        }
        return null;
    }

    private void sync_SQL_and_Firebase(final String email, final String uid) {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    userRef = database.getReference("users").child(uid).child(email).child("p");
                    /*lastupdatedRef = database.getReference("users").child(email).child("lastupdated");*/
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //mostrecentrequest = String.valueOf(dataSnapshot.child("appside").child("mostrecentrequest").getValue());

                            boolean newstuff = false;

                            close_sql();
                            sql(resources.getString(R.string.justforce3));

                            if (SQLSharing.mycursorforce3.moveToFirst()) {
                                do {
                                    boolean found = false;
                                    try {
                                        if (dataSnapshot.getChildrenCount() != 0) {
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                if (!found) {
                                                    if (child.child("D").getValue() != null) {
                                                        String day = child.child("D").getValue().toString();
                                                        if (day.equals(SQLSharing.mycursorforce3.getString(1))) {
                                                            if (child.child("P").getValue() != null && child.child("V").getValue() != null && child.child("H").getValue() != null) {


                                                                if (child.getKey() != null) {
                                                                    String prayedfb = child.child("P").getValue().toString();
                                                                    String prayedsql = SQLSharing.mycursorforce3.getString(2);
                                                                    String verifiedfb = child.child("V").getValue().toString();
                                                                    String verifiedsql = SQLSharing.mycursorforce3.getString(3);
                                                                    String homefb = child.child("H").getValue().toString();
                                                                    String homesql = SQLSharing.mycursorforce3.getString(4);
                                                                    StringBuilder yesser = new StringBuilder();
                                                                    StringBuilder yesser3 = new StringBuilder();
                                                                    StringBuilder yesser4 = new StringBuilder();
                                                                    for (int i = 0; i < 5; i++) {
                                                                        if (String.valueOf(prayedfb.charAt(i)).equals("1")) {
                                                                            yesser.append("1");
                                                                        } else if (String.valueOf(prayedsql.charAt(i)).equals("1")) {
                                                                            yesser.append("1");
                                                                        } else {
                                                                            yesser.append("0");
                                                                        }
                                                                    }

                                                                    for (int i = 0; i < 5; i++) {
                                                                        if (String.valueOf(verifiedfb.charAt(i)).equals("1")) {
                                                                            yesser3.append("1");
                                                                        } else if (String.valueOf(verifiedsql.charAt(i)).equals("1")) {
                                                                            yesser3.append("1");
                                                                        } else {
                                                                            yesser3.append("0");
                                                                        }
                                                                    }

                                                                    for (int i = 0; i < 5; i++) {
                                                                        if (String.valueOf(homefb.charAt(i)).equals("0")) {
                                                                            yesser4.append("0");
                                                                        } else if (String.valueOf(homesql.charAt(i)).equals("0")) {
                                                                            yesser4.append("0");
                                                                        } else {
                                                                            yesser4.append("1");
                                                                        }
                                                                    }
                                                                    userRef.child(child.getKey()).child("H").setValue(yesser4.toString());
                                                                    userRef.child(child.getKey()).child("V").setValue(yesser3.toString());
                                                                    userRef.child(child.getKey()).child("P").setValue(yesser.toString());
                                                                    SQLSharing.mydbforce3.updatePrayed(day, yesser.toString(), yesser3.toString(), yesser4.toString());
                                                                }
                                                            }
                                                            found = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (!found) {
                                        String ingredientKey = userRef.push().getKey();
                                        if (ingredientKey != null) {
                                            userRef.child(ingredientKey).child("D").setValue(SQLSharing.mycursorforce3.getString(1));
                                            userRef.child(ingredientKey).child("P").setValue(SQLSharing.mycursorforce3.getString(2));
                                            userRef.child(ingredientKey).child("V").setValue(SQLSharing.mycursorforce3.getString(3));
                                            userRef.child(ingredientKey).child("H").setValue(SQLSharing.mycursorforce3.getString(4));
                                        }
                                    }
                                } while (SQLSharing.mycursorforce3.moveToNext());
                            }

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                boolean found = false;
                                if (child.child("D").getValue() != null) {
                                    String day = child.child("D").getValue().toString();
                                    if (SQLSharing.mycursorforce3.moveToFirst()) {
                                        do {
                                            if (day.equals(SQLSharing.mycursorforce3.getString(1))) {
                                                found = true;
                                            }
                                        } while (SQLSharing.mycursorforce3.moveToNext() && !found);
                                    }
                                }

                                if (!found) {
                                    SQLSharing.mydbforce3.insertPrayed(child.child("D").getValue().toString(), child.child("P").getValue().toString(), child.child("V").getValue().toString(), child.child("H").getValue().toString());
                                }
                            }

                            close_sql();

                            exit();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //print("loading data failed");
                            close_sql();
                            exit();
                        }
                    });

                }
            }
        };

        //anti lag
        Thread mythread = new Thread(r); //to thread the runnable object we launched
        mythread.start();
    }
    private DatabaseReference userRef;


    private void print(Object dumps) {
        Toast.makeText(getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(onComplete!=null)
            unregisterReceiver(onComplete);
    }

    private BroadcastReceiver onComplete;
    public void downloadMeSenpai(final Context context, String DISPLAY, String destinationDirectory, String url) {
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
                        getout();
                    }
                }
            };
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean clicked = false;
    public void loginClicked(View view) {
        clicked = true;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            SignInGoogle();
        }
    }
}

