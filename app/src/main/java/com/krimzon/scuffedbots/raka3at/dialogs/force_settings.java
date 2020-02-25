package com.krimzon.scuffedbots.raka3at.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.background.ProcessMainClass;
import com.krimzon.scuffedbots.raka3at.background.restarter.RestartServiceBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

public class force_settings extends BottomSheetDialogFragment {

    private Button arrow;
    private View v;
    private String language = "ar", ID = "", adanSelections = "";
    private boolean darkmode = true, once2 = true, once = true, audioisplaying = false;
    private String[] selections;
    private Resources resources;
    private TextView adantitle, selectiontitle;
    private List<TextView> adans, titles, adansselection;
    private List<ImageView> ringmodes, audioplayer;
    private List<FrameLayout> ringmodesbackground;
    private int selectedadan = -1, currentlyplayingadan = 0;

    private void print(Object dumps) {
        Toast.makeText(v.getContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.force_settings, container, false);

        prepare_variables();
        applyfont();
        sql_work();
        apply_selected_language();
        apply_previous_settings();
        apply_adanSelections();
        onClickListeners();
        dark_light_mode();



        arrow.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            if(!mouadineselectionpageison){
                dismiss();
            } else {
                mouadineselectionpageison = false;
                adansselection.get(selectedadan).setBackground(getResources().getDrawable(R.drawable.selectorresposive));
                settingsmenu.setVisibility(View.VISIBLE);
                selectionmenu.setVisibility(View.GONE);
                apply_adanSelectionsshort();
                if(language.equals("ar"))
                    arrow.setText(resources.getString(R.string.save_arabe));
                else if(language.equals("en"))
                    arrow.setText(resources.getString(R.string.save));
            }
        }});
        return v;
    }

    private void apply_previous_settings() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (main_notification_sql.equals("yes")) {
                notiswitch.setChecked(true);
            }
        }
    }

    private void dark_light_mode() {
        if(!darkmode){
            v.findViewById(R.id.settings).setBackground(getResources().getDrawable(R.drawable.simpelbackground));
            v.findViewById(R.id.arrow).setBackground(getResources().getDrawable(R.drawable.arrowbutton));
            for(TextView adan:adans)
                adan.setBackgroundColor(getResources().getColor(R.color.typicallightbuttoncolors));

            adantitle.setTextColor(getResources().getColor(R.color.dimmest));
            settingstitle.setTextColor(getResources().getColor(R.color.dimmest));
            notitext.setTextColor(getResources().getColor(R.color.dimmest));
        }
    }

    private boolean mouadineselectionpageison = false;
    private void apply_selected_language() {
        if(language.equals("en")){
            titles.get(0).setText(resources.getString(R.string.fajrtitle));
            titles.get(1).setText(resources.getString(R.string.rise));
            titles.get(2).setText(resources.getString(R.string.dohrtitle));
            titles.get(3).setText(resources.getString(R.string.asrtitle));
            titles.get(4).setText(resources.getString(R.string.maghrebtitle));
            titles.get(5).setText(resources.getString(R.string.ishatitle));
            arrow.setText(resources.getString(R.string.save));
            adantitle.setText(resources.getString(R.string.adan));
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                settingstitle.setText(resources.getString(R.string.settings));
                notitext.setText(resources.getString(R.string.main_noti));
            }
        }

    }

    private void applyfont() {
        try{
            Typeface arabic_typeface = Typeface.createFromAsset(getContext().getAssets(), "Tajawal-Light.ttf");
            arrow.setTypeface(arabic_typeface);
            adantitle.setTypeface(arabic_typeface);
            notitext.setTypeface(arabic_typeface);
            settingstitle.setTypeface(arabic_typeface);
            for(int i=0; i<6; i++){
                if(i!=1) {
                    adans.get(i).setTypeface(arabic_typeface);
                }
                titles.get(i).setTypeface(arabic_typeface);
            }
        } catch(Exception ignored){}
    }

    private void sql(String table) {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = table;
        SQLSharing.mydb = new SQL(context);
        switch (table) {
            case "slat":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateslat();
                break;
            case "force":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateforce3();
                break;
        }
    }

    private boolean once22 = true;
    private void update_switch_setting_in_sql(String switch_setting){
        sql("slat");
        if(once22){
            once22 = false;
            SQLSharing.mycursor.moveToPosition(8);
            SWITCHSETTINGID = SQLSharing.mycursor.getString(0);
        }
        SQLSharing.mydb.updateData(switch_setting, SWITCHSETTINGID);
        SQLSharing.mydb.close();
    }

    private void protected_apps_request() {
        try {
            if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                protected_apps_request request = new protected_apps_request(getActivity(), darkmode, language);
                request.show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private String SWITCHSETTINGID = "";
    private void onClickListeners() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notiswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        update_switch_setting_in_sql("yes");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            RestartServiceBroadcastReceiver.scheduleJob(context);
                        } else {
                            ProcessMainClass bck = new ProcessMainClass();
                            bck.launchService(context);
                        }

                        if(request_protected_menu)
                            protected_apps_request();
                    }else
                        update_switch_setting_in_sql("no");
                }
            });
        }

        adans.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(0);
            }
        });
        // we ignored get(1) as fajr has no adan
        adans.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(1);
            }
        });
        adans.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(2);
            }
        });
        adans.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(3);
            }
        });
        adans.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(4);
            }
        });


        ringmodes.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(0);
            }
        });
        ringmodes.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(1);
            }
        });
        ringmodes.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(2);
            }
        });
        ringmodes.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(3);
            }
        });
        ringmodes.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(4);
            }
        });
        ringmodes.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(5);
            }
        });
        ringmodesbackground.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(0);
            }
        });
        ringmodesbackground.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(1);
            }
        });
        ringmodesbackground.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(2);
            }
        });
        ringmodesbackground.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(3);
            }
        });
        ringmodesbackground.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(4);
            }
        });
        ringmodesbackground.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(5);
            }
        });
    }

    private void launch_selection(final int prayertobemodified) {
        settingsmenu.setVisibility(View.GONE);
        selectionmenu.setVisibility(View.VISIBLE);
        mouadineselectionpageison = true;


        if(language.equals("en")){
            adansselection.get(0).setText(resources.getString(R.string.adan1));
            adansselection.get(1).setText(resources.getString(R.string.adan2));
            adansselection.get(2).setText(resources.getString(R.string.adan3));
            adansselection.get(3).setText(resources.getString(R.string.adan4));
            adansselection.get(4).setText(resources.getString(R.string.adan5));
            adansselection.get(5).setText(resources.getString(R.string.adan6));
            selectiontitle.setText(resources.getString(R.string.select_an_adan));
        }

        if(language.equals("ar"))
            arrow.setText(resources.getString(R.string.back_arabe));
        else if(language.equals("en"))
            arrow.setText(resources.getString(R.string.back));

        variables_of_selection();
        apply_font_to_selection();
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

        apply_lightmode_if_found_to_play_buttons();
    }

    private void apply_lightmode_if_found_to_play_buttons() {
        for(ImageView audioplayers:audioplayer){
            try {
                Glide.with(context).load(R.drawable.playlightmode).into(audioplayers);
            } catch (Exception ignored) {
                audioplayers.setImageDrawable(resources.getDrawable(R.drawable.playlightmode));
            }
        }
    }

    private void apply_already_selected_mouadin(int prayertobemodified) {
        selectedadan = Integer.valueOf(selections[prayertobemodified].split(",")[0]) - 1;
        adansselection.get(selectedadan).setBackground(getResources().getDrawable(R.drawable.selectionreponsivereverse));
    }

    private void playcorrespondingaudio(int i){
        if(!audioisplaying || currentlyplayingadan!=i){
            audioisplaying = true;
            if(darkmode) {
                try {
                    Glide.with(context).load(R.drawable.play).into(audioplayer.get(currentlyplayingadan));
                } catch (Exception ignored) {
                    audioplayer.get(currentlyplayingadan).setImageDrawable(resources.getDrawable(R.drawable.play));
                }
                try {
                    Glide.with(context).load(R.drawable.pause).into(audioplayer.get(i));
                } catch (Exception ignored) {
                    audioplayer.get(i).setImageDrawable(resources.getDrawable(R.drawable.pause));
                }
            } else {
                try {
                    Glide.with(context).load(R.drawable.playlightmode).into(audioplayer.get(currentlyplayingadan));
                } catch (Exception ignored) {
                    audioplayer.get(currentlyplayingadan).setImageDrawable(resources.getDrawable(R.drawable.playlightmode));
                }
                try {
                    Glide.with(context).load(R.drawable.pauselightmode).into(audioplayer.get(i));
                } catch (Exception ignored) {
                    audioplayer.get(i).setImageDrawable(resources.getDrawable(R.drawable.pauselightmode));
                }
            }
            currentlyplayingadan = i;
            playadan(currentlyplayingadan);
        } else {
            audioisplaying = false;
            stopadan();
            try {
                Glide.with(context).load(R.drawable.play).into(audioplayer.get(i));
            } catch (Exception ignored) {
                audioplayer.get(i).setImageDrawable(resources.getDrawable(R.drawable.play));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopadan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopadan();
    }

    private void stopadan(){
        try{
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        } catch(Exception ignored){}
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
                getContext(),
                null,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        );
        TrackSelector trackSelector = new DefaultTrackSelector();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                renderersFactory,
                trackSelector
        );
        String userAgent = Util.getUserAgent(getContext(), getResources().getString(R.string.adanner));
        try {
            ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(getResources().getString(R.string.idek) + adan), // file audio ada di folder assets
                    new DefaultDataSourceFactory(getContext(), userAgent),
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

    private void apply_font_to_selection() {
        try{
            Typeface arabic_typeface = Typeface.createFromAsset(getContext().getAssets(), "Tajawal-Light.ttf");
            selectiontitle.setTypeface(arabic_typeface);
            for(int i=0; i<6; i++)
                adansselection.get(i).setTypeface(arabic_typeface);
        } catch(Exception ignored){ print("applying fonts failed"); }
    }

    private void variables_of_selection() {
        adansselection = new ArrayList<>();
        adansselection.add((TextView) v.findViewById(R.id.adan1));
        adansselection.add((TextView) v.findViewById(R.id.adan2));
        adansselection.add((TextView) v.findViewById(R.id.adan3));
        adansselection.add((TextView) v.findViewById(R.id.adan4));
        adansselection.add((TextView) v.findViewById(R.id.adan5));
        adansselection.add((TextView) v.findViewById(R.id.adan6));

        audioplayer = new ArrayList<>();
        audioplayer.add((ImageView) v.findViewById(R.id.adan1audioplayer));
        audioplayer.add((ImageView) v.findViewById(R.id.adan2audioplayer));
        audioplayer.add((ImageView) v.findViewById(R.id.adan3audioplayer));
        audioplayer.add((ImageView) v.findViewById(R.id.adan4audioplayer));
        audioplayer.add((ImageView) v.findViewById(R.id.adan5audioplayer));
        audioplayer.add((ImageView) v.findViewById(R.id.adan6audioplayer));

        selectiontitle = v.findViewById(R.id.selectiontitle);
    }

    private void rotate_sound_flags(int i) {

        String[] yes;
        yes = selections[i].split(",");

        if(yes[1].equals(String.valueOf(0))){
            selections[i] = yes[0] + "," + String.valueOf(1);
            StringBuilder temp = new StringBuilder();
            for(int j=0; j<6; j++){
                temp.append(selections[j]);
                temp.append(" ");
            }
            adanSelections = temp.toString();
        }

        else if(yes[1].equals(String.valueOf(1))){
            selections[i] = yes[0] + "," + String.valueOf(2);

            if(i==1) // fajr will not have a sound, only vibrate/no sound
                selections[i] = yes[0] + "," + String.valueOf(0);
            else
                adans.get(i).setVisibility(View.VISIBLE);

            StringBuilder temp = new StringBuilder();
            for(int j=0; j<6; j++){
                temp.append(selections[j]);
                temp.append(" ");
            }
            adanSelections = temp.toString();
        }

        else if(yes[1].equals(String.valueOf(2))){
            selections[i] = yes[0] + "," + String.valueOf(0);
            StringBuilder temp = new StringBuilder();
            for(int j=0; j<6; j++){
                temp.append(selections[j]);
                temp.append(" ");
            }
            adanSelections = temp.toString();

            adans.get(i).setVisibility(View.INVISIBLE);
        }

        update_bitmap(i);
        update_sql();
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

    private void update_bitmap(int i) {
        String[] yes;
        yes = selections[i].split(",");
        if(once2){
            switch (yes[1]) {
                case "0":
                    // no ring no vibrate
                    if(darkmode) {
                        try {
                            Glide.with(this).load(R.drawable.soundsoff).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsoff));
                        }
                    } else {
                        try {
                            Glide.with(this).load(R.drawable.soundsofflightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsofflightmode));
                        }
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "1":
                    // vibrate only
                    if(darkmode){
                        try {
                            Glide.with(this).load(R.drawable.vibrate).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.vibrate));
                        }
                    } else {
                        try {
                            Glide.with(this).load(R.drawable.vibratelightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.vibratelightmode));
                        }
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "2":
                    // ring only
                    if(darkmode){
                        try {
                            Glide.with(this).load(R.drawable.soundson).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundson));
                        }
                    }else {
                        try {
                            Glide.with(this).load(R.drawable.soundsonlightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsonlightmode));
                        }
                    }
            }
        } else {
            switch (yes[1]) {
                case "0":
                    // no ring no vibrate
                    if(darkmode) {
                        try {
                            Glide.with(this).load(R.drawable.soundsoff).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsoff));
                        }
                    } else {
                        try {
                            Glide.with(this).load(R.drawable.soundsofflightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsofflightmode));
                        }
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "1":
                    // vibrate only
                    if(darkmode){
                        try {
                            Glide.with(this).load(R.drawable.vibrate).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.vibrate));
                        }
                    } else {
                        try {
                            Glide.with(this).load(R.drawable.vibratelightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.vibratelightmode));
                        }
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "2":
                    // ring only
                    if(darkmode){
                        try {
                            Glide.with(this).load(R.drawable.soundson).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundson));
                        }
                    }else {
                        try {
                            Glide.with(this).load(R.drawable.soundsonlightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsonlightmode));
                        }
                    }
            }
        }

    }

    private void apply_adanSelections() {
        for(int i=0; i<6;i++){
            update_adan(i);
            update_bitmap(i);
        }
        once2 = false;
    }

    private void apply_adanSelectionsshort() {
        for(int i=0; i<6;i++)
            update_adan(i);
    }

    private void update_adan(int i) {
        String[] yes = selections[i].split(",");
        if(i!=1) {
            // applied currently selected adan
            switch (yes[0]) {
                case "1":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan1_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan1));
                    break;
                case "2":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan2_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan2));
                    break;
                case "3":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan3_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan3));
                    break;
                case "4":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan4_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan4));
                    break;
                case "5":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan5_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan5));
                    break;
                case "6":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan6_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan6));
            }
        }
    }

    private void sql_work() {
        sql("slat");
        SQLSharing.mycursor.moveToPosition(1);
        darkmode = SQLSharing.mycursor.getString(1).equals("yes");
        SQLSharing.mycursor.moveToPosition(6);
        language = SQLSharing.mycursor.getString(1);
        SQLSharing.mycursor.moveToNext();
        adanSelections = SQLSharing.mycursor.getString(1);
        selections = adanSelections.split(" ");
        SQLSharing.mycursor.moveToNext();
        main_notification_sql = SQLSharing.mycursor.getString(1);

        SQLSharing.mycursor.moveToNext();
        request_protected_menu = SQLSharing.mycursor.getString(1).equals("yes");

        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();
    }

    private void close_sql() {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
    }

    private boolean request_protected_menu;
    private String main_notification_sql = "yes";
    private void update_sql() {
        sql("slat");
        if(once){
            once = false;
            SQLSharing.mycursor.moveToPosition(7);
            ID = SQLSharing.mycursor.getString(0);
        }
        SQLSharing.mydb.updateData(adanSelections, ID);
        SQLSharing.mydb.close();
    }

    private LinearLayout selectionmenu, settingsmenu;
    private Switch notiswitch;
    private TextView settingstitle, notitext;
    private Context context;
    private void prepare_variables() {
        context = getContext();

        selectionmenu = v.findViewById(R.id.selectionmenu);
        settingsmenu = v.findViewById(R.id.settingsmenu);
        arrow = v.findViewById(R.id.arrow);
        adantitle = v.findViewById(R.id.adantitle);

        notiswitch = v.findViewById(R.id.notiswitch);
        settingstitle = v.findViewById(R.id.settingstitle);
        notitext = v.findViewById(R.id.notitext);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notiswitch.setVisibility(View.GONE);
            settingstitle.setVisibility(View.GONE);
            notitext.setVisibility(View.GONE);
        }

        adans = new ArrayList<>();
        adans.add((TextView) v.findViewById(R.id.fajradan));
        adans.add((TextView) v.findViewById(R.id.riseadan));
        adans.add((TextView) v.findViewById(R.id.dhuhradan));
        adans.add((TextView) v.findViewById(R.id.asradan));
        adans.add((TextView) v.findViewById(R.id.maghribadan));
        adans.add((TextView) v.findViewById(R.id.ishaadan));

        titles = new ArrayList<>();
        titles.add((TextView) v.findViewById(R.id.fajrtitle));
        titles.add((TextView) v.findViewById(R.id.risetitle));
        titles.add((TextView) v.findViewById(R.id.dhuhrtitle));
        titles.add((TextView) v.findViewById(R.id.asrtitle));
        titles.add((TextView) v.findViewById(R.id.maghribtitle));
        titles.add((TextView) v.findViewById(R.id.ishatitle));

        ringmodes = new ArrayList<>();
        ringmodes.add((ImageView) v.findViewById(R.id.fajrringmode));
        ringmodes.add((ImageView) v.findViewById(R.id.riseringmode));
        ringmodes.add((ImageView) v.findViewById(R.id.dhuhrringmode));
        ringmodes.add((ImageView) v.findViewById(R.id.asrringmode));
        ringmodes.add((ImageView) v.findViewById(R.id.maghribringmode));
        ringmodes.add((ImageView) v.findViewById(R.id.isharingmode));

        ringmodesbackground = new ArrayList<>();
        ringmodesbackground.add((FrameLayout) v.findViewById(R.id.fajrringmodebackground));
        ringmodesbackground.add((FrameLayout) v.findViewById(R.id.riseringmodebackground));
        ringmodesbackground.add((FrameLayout) v.findViewById(R.id.dhuhrringmodebackground));
        ringmodesbackground.add((FrameLayout) v.findViewById(R.id.asrringmodebackground));
        ringmodesbackground.add((FrameLayout) v.findViewById(R.id.maghribringmodebackground));
        ringmodesbackground.add((FrameLayout) v.findViewById(R.id.isharingmodebackground));

        resources = getResources();


        /*if(getContext()!=null) {
            Typeface arabic_font = Typeface.createFromAsset(getContext().getAssets(), "Tajawal-Light.ttf");
            Typeface english_font = Typeface.createFromAsset(getContext().getAssets(), "Tajawal-Light.ttf");

            if (language.equals("ar")) {
                brightnesstitle.setTypeface(arabic_font);
                zero.setTypeface(arabic_font);
                three.setTypeface(arabic_font);
                five.setTypeface(arabic_font);
                delaytitle.setTypeface(arabic_font);
                arrow.setTypeface(arabic_font);
            } else if (language.equals("en")) {
                brightnesstitle.setTypeface(english_font);
                zero.setTypeface(english_font);
                three.setTypeface(english_font);
                five.setTypeface(english_font);
                delaytitle.setTypeface(english_font);
                arrow.setTypeface(english_font);
            }
        }*/

    }

    @Override
    public void onDetach() {
        super.onDetach();
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();
        if(language.equals("ar"))
            Toast.makeText(getContext(), getString(R.string.saved_arabe), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
        stopadan();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            BottomSheetListener mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text); // can pass anything back
    }

}
