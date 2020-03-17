package com.krimzon.scuffedbots.raka3at;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import java.util.ArrayList;
import java.util.List;

public class force_settings_adan_selection extends AppCompatActivity {

    private List<TextView> adansselection;
    private List<ImageView> audioplayer;
    private TextView selectiontitle;
    private String language;
    private Resources resources;
    private boolean darkmode;
    private String adanSelections;
    private String[] selections;
    private int selectedadan = -1, currentlyplayingadan = 0;
    private int prayertobemodified;
    private boolean audioisplaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_settings_adan_selection);

        pull_currently_modified_prayer();
        variables_of_selection();
        apply_font_to_selection();
        sql_work();
        apply_english_if_possible();
        onclicklisteners();
        apply_lightmode_if_found_to_play_buttons();
    }

    private void pull_currently_modified_prayer() {
        try{
            prayertobemodified = Integer.parseInt(getIntent().getStringExtra("prayertobemodified"));
        } catch(Exception e){
            e.printStackTrace();
            Log.i("HH", String.valueOf(e));
            exit();
        }
    }

    private void exit() {
        Intent backtoforce_settings = new Intent(getApplicationContext(), force_settings.class);
        startActivity(backtoforce_settings);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        update_sql();

        close_sql();
        if(language.equals("ar"))
            Toast.makeText(this, getString(R.string.saved_arabe), Toast.LENGTH_SHORT).show();
        else if(language.equals("en"))
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();

        exit();
    }

    private void apply_lightmode_if_found_to_play_buttons() {
        if(darkmode){
            for (ImageView audioplayers : audioplayer) {
                try {
                    Glide.with(this).load(R.drawable.play).into(audioplayers);
                } catch (Exception ignored) {
                    audioplayers.setImageDrawable(resources.getDrawable(R.drawable.play));
                }
            }
        } else {
            for (ImageView audioplayers : audioplayer) {
                try {
                    Glide.with(this).load(R.drawable.playlightmode).into(audioplayers);
                } catch (Exception ignored) {
                    audioplayers.setImageDrawable(resources.getDrawable(R.drawable.playlightmode));
                }
            }
        }
    }

    private void apply_already_selected_mouadin(int prayertobemodified) {
        selectedadan = Integer.parseInt(selections[prayertobemodified].split(",")[0]) - 1;
        adansselection.get(selectedadan).setBackground(getResources().getDrawable(R.drawable.selectionreponsivereverse));
    }

    private void onclicklisteners() {

        arrow.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            update_sql();

            close_sql();
            if(language.equals("ar"))
                Toast.makeText(getApplicationContext(), getString(R.string.saved_arabe), Toast.LENGTH_SHORT).show();
            else if(language.equals("en"))
                Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();

            exit();
        }});

        if(prayertobemodified>0)
            apply_already_selected_mouadin(prayertobemodified+1);
        else
            apply_already_selected_mouadin(prayertobemodified);

        adansselection.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(0, prayertobemodified); }});
        adansselection.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(1, prayertobemodified); }});
        adansselection.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(2, prayertobemodified); }});
        adansselection.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(3, prayertobemodified); }});
        adansselection.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(4, prayertobemodified); }});
        adansselection.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(5, prayertobemodified); }});

        audioplayer.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(0);
            }
        });
        audioplayer.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(1);
            }
        });
        audioplayer.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(2);
            }
        });
        audioplayer.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(3);
            }
        });
        audioplayer.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(4);
            }
        });
        audioplayer.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(5);
            }
        });

    }

    private void playcorrespondingaudio(int i){
        if(!audioisplaying || currentlyplayingadan!=i){
            audioisplaying = true;
            if(darkmode) {
                try {
                    Glide.with(this).load(R.drawable.play).into(audioplayer.get(currentlyplayingadan));
                } catch (Exception ignored) {
                    audioplayer.get(currentlyplayingadan).setImageDrawable(resources.getDrawable(R.drawable.play));
                }
                try {
                    Glide.with(this).load(R.drawable.pause).into(audioplayer.get(i));
                } catch (Exception ignored) {
                    audioplayer.get(i).setImageDrawable(resources.getDrawable(R.drawable.pause));
                }
            } else {
                try {
                    Glide.with(this).load(R.drawable.playlightmode).into(audioplayer.get(currentlyplayingadan));
                } catch (Exception ignored) {
                    audioplayer.get(currentlyplayingadan).setImageDrawable(resources.getDrawable(R.drawable.playlightmode));
                }
                try {
                    Glide.with(this).load(R.drawable.pauselightmode).into(audioplayer.get(i));
                } catch (Exception ignored) {
                    audioplayer.get(i).setImageDrawable(resources.getDrawable(R.drawable.pauselightmode));
                }
            }
            currentlyplayingadan = i;
            playadan(currentlyplayingadan);
        } else {
            audioisplaying = false;
            stopadan();
            if(darkmode) {
                try {
                    Glide.with(this).load(R.drawable.play).into(audioplayer.get(currentlyplayingadan));
                } catch (Exception ignored) {
                    audioplayer.get(currentlyplayingadan).setImageDrawable(resources.getDrawable(R.drawable.play));
                }
            } else {
                try {
                    Glide.with(this).load(R.drawable.playlightmode).into(audioplayer.get(currentlyplayingadan));
                } catch (Exception ignored) {
                    audioplayer.get(currentlyplayingadan).setImageDrawable(resources.getDrawable(R.drawable.playlightmode));
                }
            }
        }
    }

    private void stopadan(){
        try{
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        } catch(Exception ignored){}
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(audioisplaying){
            stopadan();
        }

        update_sql();

        close_sql();
        if(language.equals("ar"))
            Toast.makeText(this, getString(R.string.saved_arabe), Toast.LENGTH_SHORT).show();
        else if(language.equals("en"))
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(audioisplaying){
            stopadan();
        }

        update_sql();

        close_sql();
        if(language.equals("ar"))
            Toast.makeText(this, getString(R.string.saved_arabe), Toast.LENGTH_SHORT).show();
        else if(language.equals("en"))
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
    }

    private SimpleExoPlayer simpleExoPlayer;
    private void playadan(int adantag) {
        try{
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        } catch(Exception ignored){}
        String adan = "";
        switch(adantag){
            case 0:
                adan = "amine.mp3";
                break;
            case 1:
                adan = "altazi.mp3";
                break;
            case 2:
                adan = "karim.mp3";
                break;
            case 3:
                adan = "ismail.mp3";
                break;
            case 4:
                adan = "afasi.mp3";
                break;
            case 5:
                adan = "madani.mp3";
        }
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(
                this,
                null,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        );
        TrackSelector trackSelector = new DefaultTrackSelector();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                renderersFactory,
                trackSelector
        );
        String userAgent = Util.getUserAgent(this, getResources().getString(R.string.adanner));
        try {
            ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(getResources().getString(R.string.idek) + adan), // file audio ada di folder assets
                    new DefaultDataSourceFactory(this, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null
            );
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        } catch(Exception ignored){}
    }

    private void applyselectedadan(int i, int prayertobemodified) {
        if(selectedadan!=i){
            adansselection.get(i).setBackground(getResources().getDrawable(R.drawable.selectionreponsivereverse));
            if(selectedadan!=-1)
                adansselection.get(selectedadan).setBackground(getResources().getDrawable(R.drawable.selectorresposive));
            selectedadan = i;
            applyselectedadantoadanselections(prayertobemodified, selectedadan + 1);
            update_sql();
        }
    }

    private String ID;
    private boolean once = true;
    private void update_sql() {
        sql("slat");
        if(once){
            once = false;
            SQLSharing.mycursorslat.moveToPosition(7);
            ID = SQLSharing.mycursorslat.getString(0);
        }
        SQLSharing.mydbslat.updateData(adanSelections, ID);
    }

    private void applyselectedadantoadanselections(int prayedtobemodified, int selectedmouadin) {
        String[] yes;
        if(prayedtobemodified>0)
            prayedtobemodified++;
        yes = selections[prayedtobemodified].split(",");
        selections[prayedtobemodified] = String.valueOf(selectedmouadin) + "," + yes[1];
        StringBuilder temp = new StringBuilder();
        for(int j=0; j<6; j++){
            temp.append(selections[j]);
            temp.append(" ");
        }
        adanSelections = temp.toString();
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

    private void sql_work() {
        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(1);
        darkmode = SQLSharing.mycursorslat.getString(1).equals("yes");
        SQLSharing.mycursorslat.moveToPosition(6);
        language = SQLSharing.mycursorslat.getString(1);
        SQLSharing.mycursorslat.moveToNext();
        adanSelections = SQLSharing.mycursorslat.getString(1);
        selections = adanSelections.split(" ");
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

    private void apply_english_if_possible() {
        if(language.equals("en")){
            arrow.setText(resources.getString(R.string.back));
            adansselection.get(0).setText(resources.getString(R.string.adan1));
            adansselection.get(1).setText(resources.getString(R.string.adan2));
            adansselection.get(2).setText(resources.getString(R.string.adan3));
            adansselection.get(3).setText(resources.getString(R.string.adan4));
            adansselection.get(4).setText(resources.getString(R.string.adan5));
            adansselection.get(5).setText(resources.getString(R.string.adan6));
            selectiontitle.setText(resources.getString(R.string.select_an_adan));
        }
    }

    private void print(Object dumps) {
        Toast.makeText(this, String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }


    private void apply_font_to_selection() {
        try{
            Typeface arabic_typeface = Typeface.createFromAsset(this.getAssets(), "Tajawal-Light.ttf");
            selectiontitle.setTypeface(arabic_typeface);
            for(int i=0; i<6; i++)
                adansselection.get(i).setTypeface(arabic_typeface);
        } catch(Exception ignored){ print("applying fonts failed"); }
    }

    private Button arrow;
    private void variables_of_selection() {

        arrow = findViewById(R.id.arrow);

        resources = getResources();

        adansselection = new ArrayList<>();
        adansselection.add((TextView) findViewById(R.id.adan1));
        adansselection.add((TextView) findViewById(R.id.adan2));
        adansselection.add((TextView) findViewById(R.id.adan3));
        adansselection.add((TextView) findViewById(R.id.adan4));
        adansselection.add((TextView) findViewById(R.id.adan5));
        adansselection.add((TextView) findViewById(R.id.adan6));

        audioplayer = new ArrayList<>();
        audioplayer.add((ImageView) findViewById(R.id.adan1audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan2audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan3audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan4audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan5audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan6audioplayer));

        selectiontitle = findViewById(R.id.selectiontitle);
    }
}
