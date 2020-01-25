package com.krimzon.scuffedbots.raka3at.dialogs;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import static com.krimzon.scuffedbots.raka3at.slat.delaybeforecounting;
import static com.krimzon.scuffedbots.raka3at.slat.scheme;
import static com.krimzon.scuffedbots.raka3at.slat.scheme_light_mode;

public class slat_settings extends BottomSheetDialogFragment {

    private SeekBar brightness;
    private Button arrow;
    private TextView brightnesstitle;
    private LinearLayout settings;
    private Drawable darkarrowbutton;
    private Drawable arrowbutton;
    private Drawable darkest_thumb;
    private Drawable dark_thumb;
    private Drawable thumb;
    private Drawable volume_control;
    private Drawable dark_volume_control;
    private Drawable darkest_volume_control;
    private Drawable dimm_thumb;
    private Drawable dimmer_thumb;
    private Drawable dimmest_thumb;
    private Drawable dimm_volume_control;
    private Drawable dimmer_volume_control;
    private Drawable dimmest_volume_control;
    private int dimm;
    private int dimmer;
    private int dimmest;
    private View v;
    private String SCHEME_ID;
    private String SCHEME_LIGHT_MODE_ID;
    private Button five, three, zero;
    private Drawable darkzerodelayunselected, darkzerodelayselected, darkthreedelayunselected, darkthreedelayselected, darkfivedelayunselected, darkfivedelayselected;
    private Drawable zerodelayunselected, zerodelayselected, threedelayunselected, threedelayselected, fivedelayunselected, fivedelayselected;
    private TextView delaytitle;

    private String delaybeforecounting_arabe;
    private String save_arabe;
    private String zeroseconds_arabe;
    private String threeseconds_arabe;
    private String fiveseconds_arabe;
    private String setting1_arabe;

    private String delaybeforecountingg;
    private String save;
    private String zeroseconds;
    private String threeseconds;
    private String fiveseconds;
    private String setting1;
    private String language = "ar";

    private boolean it_is_nightmode_since_lightmode_shines_and_ruins_measurement = false;

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
        if(language.equals("en"))
            english();
    }

    private void load_strings(){
        if(language.equals("ar")) {
            delaybeforecounting_arabe = getString(R.string.delaybeforecounting_arabe);
            save_arabe = getString(R.string.save_arabe);
            zeroseconds_arabe = getString(R.string.zeroseconds_arabe);
            threeseconds_arabe = getString(R.string.threeseconds_arabe);
            fiveseconds_arabe = getString(R.string.fiveseconds_arabe);
            setting1_arabe = getString(R.string.setting1_arabe);
        } else if(language.equals("en")){
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
        it_is_nightmode_since_lightmode_shines_and_ruins_measurement = SQLSharing.mycursor.getString(1).equals("yes");
        SQLSharing.mycursor.moveToNext();
        SCHEME_ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mycursor.moveToNext();
        SCHEME_LIGHT_MODE_ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        language = SQLSharing.mycursor.getString(1);
    }

    private void update_brightness(int progress) {
        if(progress<=30){
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
                brightness.setThumb(darkest_thumb);
                brightness.setProgressDrawable(darkest_volume_control);
                scheme = 0;
                /*raka3at.setTextColor(darkest);
                sajda.setTextColor(darkest);
                brightnesstitle.setTextColor(darkest);*/
                SQLSharing.mydb.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(dimmest_thumb);
                brightness.setProgressDrawable(dimmest_volume_control);
                scheme_light_mode = 0;
                /*raka3at.setTextColor(dimmest);
                sajda.setTextColor(dimmest);
                brightnesstitle.setTextColor(dimmest);*/
                SQLSharing.mydb.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        } else if(progress<=69){
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement){
                brightness.setThumb(dark_thumb);
                brightness.setProgressDrawable(dark_volume_control);
                scheme = 1;
                /*raka3at.setTextColor(dark);
                sajda.setTextColor(dark);
                brightnesstitle.setTextColor(dark);*/
                SQLSharing.mydb.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(dimmer_thumb);
                brightness.setProgressDrawable(dimmer_volume_control);
                scheme_light_mode = 1;
                /*raka3at.setTextColor(dimmer);
                sajda.setTextColor(dimmer);
                brightnesstitle.setTextColor(dimmer);*/
                SQLSharing.mydb.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        } else {
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
                brightness.setThumb(thumb);
                brightness.setProgressDrawable(volume_control);
                scheme = 2;
                /*raka3at.setTextColor(white);
                sajda.setTextColor(white);
                brightnesstitle.setTextColor(white);*/
                SQLSharing.mydb.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(dimm_thumb);
                brightness.setProgressDrawable(dimm_volume_control);
                scheme_light_mode = 2;
                /*raka3at.setTextColor(dimm);
                sajda.setTextColor(dimm);
                brightnesstitle.setTextColor(dimm);*/
                SQLSharing.mydb.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        }
    }

    private void load_previous_light_mode_settings() {
        if(getContext()!=null) {
            Drawable simpelbackground = getContext().getResources().getDrawable(R.drawable.simpelbackground);
            settings.setBackground(simpelbackground);
            delaytitle.setTextColor(dimmest);
            arrow.setBackground(arrowbutton);
            if (scheme_light_mode == 0) {
                brightness.setProgress(10);
                brightness.setThumb(dimmest_thumb);
                brightness.setProgressDrawable(dimmest_volume_control);
                brightnesstitle.setTextColor(dimmest);
            } else if (scheme_light_mode == 1) {
                brightness.setProgress(50);
                brightness.setThumb(dimmer_thumb);
                brightness.setProgressDrawable(dimmer_volume_control);
                brightnesstitle.setTextColor(dimmer);
            } else {
                brightness.setProgress(80);
                brightness.setThumb(dimm_thumb);
                brightness.setProgressDrawable(dimm_volume_control);
                brightnesstitle.setTextColor(dimm);
            }
        }
    }

    private void load_previous_dark_mode_settings() {
        if(getContext()!=null) {
            Drawable darksimpelbackground = getContext().getResources().getDrawable(R.drawable.darksimpelbackground);
            settings.setBackground(darksimpelbackground);
            delaytitle.setTextColor(white);
            arrow.setBackground(darkarrowbutton);
            if (scheme == 0) {
                brightness.setProgress(10);
                brightness.setThumb(darkest_thumb);
                brightness.setProgressDrawable(darkest_volume_control);
                brightnesstitle.setTextColor(darkest);
            } else if (scheme == 1) {
                brightness.setProgress(50);
                brightness.setThumb(dark_thumb);
                brightness.setProgressDrawable(dark_volume_control);
                brightnesstitle.setTextColor(dark);
            } else {
                brightness.setProgress(80);
                brightness.setThumb(thumb);
                brightness.setProgressDrawable(volume_control);
                brightnesstitle.setTextColor(white);
            }
        }
    }

    int white;
    int dark;
    int darkest;
    private void prepare_variables() {
        brightness = v.findViewById(R.id.brightness);
        five = v.findViewById(R.id.five);
        three = v.findViewById(R.id.three);
        zero = v.findViewById(R.id.zero);
        arrow = v.findViewById(R.id.arrow);
        brightnesstitle = v.findViewById(R.id.brightnesstitle);
        delaytitle = v.findViewById(R.id.delaytitle);

        if(getContext()!=null) {
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
        }

        settings = v.findViewById(R.id.settings);
        Resources resources = getResources();
        white = resources.getColor(R.color.white);
        dark = resources.getColor(R.color.dark);
        darkest = resources.getColor(R.color.darkest);

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
        if(language.equals("ar"))
            Toast.makeText(getContext(), getString(R.string.saved_arabe), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();

        Intent restart = new Intent(getContext(), slat.class);
        startActivity(restart);
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text); // can pass anything back
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            BottomSheetListener mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

}
