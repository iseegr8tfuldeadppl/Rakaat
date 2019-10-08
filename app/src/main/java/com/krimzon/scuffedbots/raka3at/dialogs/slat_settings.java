package com.krimzon.scuffedbots.raka3at.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.slat;

import static com.krimzon.scuffedbots.raka3at.slat.arabic_font;
import static com.krimzon.scuffedbots.raka3at.slat.check_language;
import static com.krimzon.scuffedbots.raka3at.slat.dark;
import static com.krimzon.scuffedbots.raka3at.slat.darkest;
import static com.krimzon.scuffedbots.raka3at.slat.darksimpelbackground;
import static com.krimzon.scuffedbots.raka3at.slat.delaybeforecounting;
import static com.krimzon.scuffedbots.raka3at.slat.english_font;
import static com.krimzon.scuffedbots.raka3at.slat.it_is_nightmode_since_lightmode_shines_and_ruins_measurement;
import static com.krimzon.scuffedbots.raka3at.slat.raka3at;
import static com.krimzon.scuffedbots.raka3at.slat.sajda;
import static com.krimzon.scuffedbots.raka3at.slat.scheme;
import static com.krimzon.scuffedbots.raka3at.slat.scheme_light_mode;
import static com.krimzon.scuffedbots.raka3at.slat.simpelbackground;
import static com.krimzon.scuffedbots.raka3at.slat.white;

public class slat_settings extends BottomSheetDialogFragment {

    protected BottomSheetListener mListener;
    protected SeekBar brightness;
    protected Button arrow;
    protected TextView brightnesstitle;
    protected LinearLayout settings;
    protected Drawable darkarrowbutton;
    protected Drawable arrowbutton;
    protected Resources resources;
    protected Drawable darkest_thumb;
    protected Drawable dark_thumb;
    protected Drawable thumb;
    protected Drawable volume_control;
    protected Drawable dark_volume_control;
    protected Drawable darkest_volume_control;
    protected Drawable dimm_thumb;
    protected Drawable dimmer_thumb;
    protected Drawable dimmest_thumb;
    protected Drawable dimm_volume_control;
    protected Drawable dimmer_volume_control;
    protected Drawable dimmest_volume_control;
    protected int dimm;
    protected int dimmer;
    protected int dimmest;
    protected View v;
    protected String SCHEME_ID;
    protected String SCHEME_LIGHT_MODE_ID;
    protected Button five, three, zero;
    protected Drawable darkzerodelayunselected, darkzerodelayselected, darkthreedelayunselected, darkthreedelayselected, darkfivedelayunselected, darkfivedelayselected;
    protected Drawable zerodelayunselected, zerodelayselected, threedelayunselected, threedelayselected, fivedelayunselected, fivedelayselected;
    protected TextView delaytitle;

    protected String delaybeforecounting_arabe;
    protected String save_arabe;
    protected String zeroseconds_arabe;
    protected String threeseconds_arabe;
    protected String fiveseconds_arabe;
    protected String setting1_arabe;

    protected String delaybeforecountingg;
    protected String save;
    protected String zeroseconds;
    protected String threeseconds;
    protected String fiveseconds;
    protected String setting1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.slat_settings, container, false);

        prepare_variables();
        sql_work();

        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement)
            load_previous_dark_mode_settings();
        else
            load_previous_light_mode_settings();

        if(delaybeforecounting==5)
            select_five();
        else if(delaybeforecounting==3)
            select_three();
        else if(delaybeforecounting==0)
            select_zero();

        work_on_language();

        arrow.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            dismiss();
        }});

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) {}@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            update_brightness(progress);
        }});

        zero.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            select_zero();
        }});

        three.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            select_three();
        }});

        five.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            select_five();
        }});

        return v;
    }

    private void work_on_language() {
        load_strings();
        if(slat.language.equals("en"))
            english();
    }

    private void load_strings(){
        if(slat.language.equals("ar")) {
            delaybeforecounting_arabe = getString(R.string.delaybeforecounting_arabe);
            save_arabe = getString(R.string.save_arabe);
            zeroseconds_arabe = getString(R.string.zeroseconds_arabe);
            threeseconds_arabe = getString(R.string.threeseconds_arabe);
            fiveseconds_arabe = getString(R.string.fiveseconds_arabe);
            setting1_arabe = getString(R.string.setting1_arabe);
        } else if(slat.language.equals("en")){
            delaybeforecountingg = getString(R.string.delaybeforecounting);
            save = getString(R.string.save);
            zeroseconds = getString(R.string.zeroseconds);
            threeseconds = getString(R.string.threeseconds);
            fiveseconds = getString(R.string.fiveseconds);
            setting1 = getString(R.string.setting1);
        }
    }

    private void arabic(){
        arrow.setText(save_arabe);
        brightnesstitle.setText(setting1_arabe);
        zero.setText(zeroseconds_arabe);
        three.setText(threeseconds_arabe);
        five.setText(fiveseconds_arabe);
        delaytitle.setText(delaybeforecounting_arabe);
    }

    private void english(){
        arrow.setText(save);
        brightnesstitle.setText(setting1);
        zero.setText(zeroseconds);
        three.setText(threeseconds);
        five.setText(fiveseconds);
        delaytitle.setText(delaybeforecountingg);
    }

    private void select_three() {
        delaybeforecounting = 3;
        SQLSharing.mydb.updateData(String.valueOf(delaybeforecounting), String.valueOf(5));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
            zero.setBackground(darkzerodelayunselected);
            three.setBackground(darkthreedelayselected);
            five.setBackground(darkfivedelayunselected);
        } else {
            zero.setBackground(zerodelayunselected);
            three.setBackground(threedelayselected);
            five.setBackground(fivedelayunselected);
        }
    }

    private void select_five() {
        delaybeforecounting = 5;
        SQLSharing.mydb.updateData(String.valueOf(delaybeforecounting), String.valueOf(5));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
            zero.setBackground(darkzerodelayunselected);
            three.setBackground(darkthreedelayunselected);
            five.setBackground(darkfivedelayselected);
        } else {
            zero.setBackground(zerodelayunselected);
            three.setBackground(threedelayunselected);
            five.setBackground(fivedelayselected);
        }
    }

    private void select_zero() {
        delaybeforecounting = 0;
        SQLSharing.mydb.updateData(String.valueOf(delaybeforecounting), String.valueOf(5));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
            zero.setBackground(darkzerodelayselected);
            three.setBackground(darkthreedelayunselected);
            five.setBackground(darkfivedelayunselected);
        } else {
            zero.setBackground(zerodelayselected);
            three.setBackground(threedelayunselected);
            five.setBackground(fivedelayunselected);
        }
    }

    private void sql_work() {
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(getContext());
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SCHEME_ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mycursor.moveToNext();
        SCHEME_LIGHT_MODE_ID = SQLSharing.mycursor.getString(0);
    }

    private void update_brightness(int progress) {
        if(progress<=30){
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
                brightness.setThumb(darkest_thumb);
                brightness.setProgressDrawable(darkest_volume_control);
                scheme = 0;
                raka3at.setTextColor(darkest);
                sajda.setTextColor(darkest);
                brightnesstitle.setTextColor(darkest);
                SQLSharing.mydb.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(dimmest_thumb);
                brightness.setProgressDrawable(dimmest_volume_control);
                scheme_light_mode = 0;
                raka3at.setTextColor(dimmest);
                sajda.setTextColor(dimmest);
                brightnesstitle.setTextColor(dimmest);
                SQLSharing.mydb.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        } else if(progress<=69){
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement){
                brightness.setThumb(dark_thumb);
                brightness.setProgressDrawable(dark_volume_control);
                scheme = 1;
                raka3at.setTextColor(dark);
                sajda.setTextColor(dark);
                brightnesstitle.setTextColor(dark);
                SQLSharing.mydb.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(dimmer_thumb);
                brightness.setProgressDrawable(dimmer_volume_control);
                scheme_light_mode = 1;
                raka3at.setTextColor(dimmer);
                sajda.setTextColor(dimmer);
                brightnesstitle.setTextColor(dimmer);
                SQLSharing.mydb.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        } else {
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
                brightness.setThumb(thumb);
                brightness.setProgressDrawable(volume_control);
                scheme = 2;
                raka3at.setTextColor(white);
                sajda.setTextColor(white);
                brightnesstitle.setTextColor(white);
                SQLSharing.mydb.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(dimm_thumb);
                brightness.setProgressDrawable(dimm_volume_control);
                scheme_light_mode = 2;
                raka3at.setTextColor(dimm);
                sajda.setTextColor(dimm);
                brightnesstitle.setTextColor(dimm);
                SQLSharing.mydb.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        }
    }

    private void load_previous_light_mode_settings() {

        settings.setBackground(simpelbackground);
        delaytitle.setTextColor(dimmest);
        arrow.setBackground(arrowbutton);
        if (scheme_light_mode == 0) {
            brightness.setProgress(10);
            brightness.setThumb(dimmest_thumb);
            brightness.setProgressDrawable(dimmest_volume_control);
            raka3at.setTextColor(dimmest);
            sajda.setTextColor(dimmest);
            brightnesstitle.setTextColor(dimmest);
        } else if (scheme_light_mode == 1) {
            brightness.setProgress(50);
            brightness.setThumb(dimmer_thumb);
            brightness.setProgressDrawable(dimmer_volume_control);
            raka3at.setTextColor(dimmer);
            sajda.setTextColor(dimmer);
            brightnesstitle.setTextColor(dimmer);
        } else {
            brightness.setProgress(80);
            brightness.setThumb(dimm_thumb);
            brightness.setProgressDrawable(dimm_volume_control);
            raka3at.setTextColor(dimm);
            sajda.setTextColor(dimm);
            brightnesstitle.setTextColor(dimm);
        }
    }

    private void load_previous_dark_mode_settings() {
        settings.setBackground(darksimpelbackground);
        delaytitle.setTextColor(white);
        arrow.setBackground(darkarrowbutton);
        if (scheme == 0) {
            brightness.setProgress(10);
            brightness.setThumb(darkest_thumb);
            brightness.setProgressDrawable(darkest_volume_control);
            raka3at.setTextColor(darkest);
            sajda.setTextColor(darkest);
            brightnesstitle.setTextColor(darkest);
        } else if (scheme == 1) {
            brightness.setProgress(50);
            brightness.setThumb(dark_thumb);
            brightness.setProgressDrawable(dark_volume_control);
            raka3at.setTextColor(dark);
            sajda.setTextColor(dark);
            brightnesstitle.setTextColor(dark);
        } else {
            brightness.setProgress(80);
            brightness.setThumb(thumb);
            brightness.setProgressDrawable(volume_control);
            raka3at.setTextColor(white);
            sajda.setTextColor(white);
            brightnesstitle.setTextColor(white);
        }
    }

    private void prepare_variables() {
        brightness = v.findViewById(R.id.brightness);
        five = v.findViewById(R.id.five);
        three = v.findViewById(R.id.three);
        zero = v.findViewById(R.id.zero);
        arrow = v.findViewById(R.id.arrow);
        brightnesstitle = v.findViewById(R.id.brightnesstitle);
        delaytitle = v.findViewById(R.id.delaytitle);

        if(check_language().equals("ar")) {
            brightnesstitle.setTypeface(arabic_font);
            zero.setTypeface(arabic_font);
            three.setTypeface(arabic_font);
            five.setTypeface(arabic_font);
            delaytitle.setTypeface(arabic_font);
        } else if(check_language().equals("en")) {
            brightnesstitle.setTypeface(english_font);
            zero.setTypeface(english_font);
            three.setTypeface(english_font);
            five.setTypeface(english_font);
            delaytitle.setTypeface(english_font);
        }

        settings = v.findViewById(R.id.settings);
        resources = getResources();

        darkzerodelayunselected = resources.getDrawable(R.drawable.darkzerodelayunselected);
        darkzerodelayselected = resources.getDrawable(R.drawable.darkzerodelayselected);
        zerodelayunselected = resources.getDrawable(R.drawable.zerodelayunselected);
        zerodelayselected = resources.getDrawable(R.drawable.zerodelayselected);

        darkthreedelayunselected = resources.getDrawable(R.drawable.darkthreedelayunselected);
        darkthreedelayselected = resources.getDrawable(R.drawable.darkthreedelayselected);
        threedelayunselected = resources.getDrawable(R.drawable.threedelayunselected);
        threedelayselected = resources.getDrawable(R.drawable.threedelayselected);

        darkfivedelayunselected = resources.getDrawable(R.drawable.darkfivedelayunselected);
        darkfivedelayselected = resources.getDrawable(R.drawable.darkfivedelayselected);
        fivedelayunselected = resources.getDrawable(R.drawable.fivedelayunselected);
        fivedelayselected = resources.getDrawable(R.drawable.fivedelayselected);

        darkarrowbutton = resources.getDrawable(R.drawable.darkarrowbutton);
        arrowbutton = resources.getDrawable(R.drawable.arrowbutton);
        dark_thumb = resources.getDrawable(R.drawable.dark_thumb);
        thumb = resources.getDrawable(R.drawable.thumb);
        volume_control = resources.getDrawable(R.drawable.volume_control);
        dark_volume_control = resources.getDrawable(R.drawable.dark_volume_control);
        darkest_volume_control = resources.getDrawable(R.drawable.darkest_volume_control);
        dimm_thumb = resources.getDrawable(R.drawable.dimm_thumb);
        dimmer_thumb = resources.getDrawable(R.drawable.dimmer_thumb);
        dimmest_thumb = resources.getDrawable(R.drawable.dimmest_thumb);
        dimm_volume_control = resources.getDrawable(R.drawable.dimm_volume_control);
        dimmer_volume_control = resources.getDrawable(R.drawable.dimmer_volume_control);
        dimmest_volume_control = resources.getDrawable(R.drawable.dimmest_volume_control);
        dimm = resources.getColor(R.color.dimm);
        dimmer = resources.getColor(R.color.dimmer);
        dimmest = resources.getColor(R.color.dimmest);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();
        print(getString(R.string.saved));
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text); // can pass anything back
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

    protected void print(Object lol){
        Toast.makeText(getContext(), String.valueOf(lol), Toast.LENGTH_SHORT).show();
    }

}
