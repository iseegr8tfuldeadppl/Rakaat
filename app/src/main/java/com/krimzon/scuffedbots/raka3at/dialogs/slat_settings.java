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
    private TextView brightnesstitle, delaytitle;
    private LinearLayout settings;
    private View v;
    private String SCHEME_ID, SCHEME_LIGHT_MODE_ID;
    private Button five, three, zero;
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
        if(language.equals("en"))
            english();
    }


    private void english(){
        arrow.setText(getResources().getString(R.string.save));
        brightnesstitle.setText(getResources().getString(R.string.setting1));
        zero.setText(getResources().getString(R.string.zeroseconds));
        three.setText(getResources().getString(R.string.threeseconds));
        five.setText(getResources().getString(R.string.fiveseconds));
        delaytitle.setText(getResources().getString(R.string.delaybeforecounting));
    }

    private void select_three() {
        delaybeforecounting = 3;
        SQLSharing.mydbslat.updateData(String.valueOf(delaybeforecounting), String.valueOf(5));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
            zero.setBackground(getResources().getDrawable(R.drawable.darkzerodelayunselected));
            three.setBackground(getResources().getDrawable(R.drawable.darkthreedelayselected));
            five.setBackground(getResources().getDrawable(R.drawable.darkfivedelayunselected));
        } else {
            zero.setBackground(getResources().getDrawable(R.drawable.zerodelayunselected));
            three.setBackground(getResources().getDrawable(R.drawable.threedelayselected));
            five.setBackground(getResources().getDrawable(R.drawable.fivedelayunselected));
        }
    }

    private void select_five() {
        delaybeforecounting = 5;
        SQLSharing.mydbslat.updateData(String.valueOf(delaybeforecounting), String.valueOf(5));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
            zero.setBackground(getResources().getDrawable(R.drawable.darkzerodelayunselected));
            three.setBackground(getResources().getDrawable(R.drawable.darkthreedelayunselected));
            five.setBackground(getResources().getDrawable(R.drawable.darkfivedelayselected));
        } else {
            zero.setBackground(getResources().getDrawable(R.drawable.zerodelayunselected));
            three.setBackground(getResources().getDrawable(R.drawable.threedelayunselected));
            five.setBackground(getResources().getDrawable(R.drawable.fivedelayselected));
        }
    }

    private void select_zero() {
        delaybeforecounting = 0;
        SQLSharing.mydbslat.updateData(String.valueOf(delaybeforecounting), String.valueOf(5));
        if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
            zero.setBackground(getResources().getDrawable(R.drawable.darkzerodelayselected));
            three.setBackground(getResources().getDrawable(R.drawable.darkthreedelayunselected));
            five.setBackground(getResources().getDrawable(R.drawable.darkfivedelayunselected));
        } else {
            zero.setBackground(getResources().getDrawable(R.drawable.zerodelayselected));
            three.setBackground(getResources().getDrawable(R.drawable.threedelayunselected));
            five.setBackground(getResources().getDrawable(R.drawable.fivedelayunselected));
        }
    }

    private void sql_work() {
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydbslat = SQL.getInstance(getContext());
        SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
        SQLSharing.mycursorslat.moveToFirst();
        SQLSharing.mycursorslat.moveToNext();
        it_is_nightmode_since_lightmode_shines_and_ruins_measurement = SQLSharing.mycursorslat.getString(1).equals("yes");
        SQLSharing.mycursorslat.moveToNext();
        SCHEME_ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mycursorslat.moveToNext();
        SCHEME_LIGHT_MODE_ID = SQLSharing.mycursorslat.getString(0);
        SQLSharing.mycursorslat.moveToNext();
        SQLSharing.mycursorslat.moveToNext();
        SQLSharing.mycursorslat.moveToNext();
        language = SQLSharing.mycursorslat.getString(1);
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

    private void update_brightness(int progress) {
        if(progress<=30){
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
                brightness.setThumb(getResources().getDrawable(R.drawable.darkest_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.darkest_volume_control));
                scheme = 0;
                /*raka3at.setTextColor(darkest);
                sajda.setTextColor(darkest);
                brightnesstitle.setTextColor(darkest);*/
                SQLSharing.mydbslat.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(getResources().getDrawable(R.drawable.dimmest_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.dimmest_volume_control));
                scheme_light_mode = 0;
                /*raka3at.setTextColor(dimmest);
                sajda.setTextColor(dimmest);
                brightnesstitle.setTextColor(dimmest);*/
                SQLSharing.mydbslat.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        } else if(progress<=69){
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement){
                brightness.setThumb(getResources().getDrawable(R.drawable.dark_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.dark_volume_control));
                scheme = 1;
                /*raka3at.setTextColor(dark);
                sajda.setTextColor(dark);
                brightnesstitle.setTextColor(dark);*/
                SQLSharing.mydbslat.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(getResources().getDrawable(R.drawable.dimmer_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.dimmer_volume_control));
                scheme_light_mode = 1;
                /*raka3at.setTextColor(dimmer);
                sajda.setTextColor(dimmer);
                brightnesstitle.setTextColor(dimmer);*/
                SQLSharing.mydbslat.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        } else {
            if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement) {
                brightness.setThumb(getResources().getDrawable(R.drawable.thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.volume_control));
                scheme = 2;
                /*raka3at.setTextColor(white);
                sajda.setTextColor(white);
                brightnesstitle.setTextColor(white);*/
                SQLSharing.mydbslat.updateData(String.valueOf(scheme), SCHEME_ID);
            } else {
                brightness.setThumb(getResources().getDrawable(R.drawable.dimm_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.dimm_volume_control));
                scheme_light_mode = 2;
                /*raka3at.setTextColor(dimm);
                sajda.setTextColor(dimm);
                brightnesstitle.setTextColor(dimm);*/
                SQLSharing.mydbslat.updateData(String.valueOf(scheme_light_mode), SCHEME_LIGHT_MODE_ID);
            }
        }
    }

    private void load_previous_light_mode_settings() {
        if(getContext()!=null) {
            settings.setBackground(getContext().getResources().getDrawable(R.drawable.simpelbackground));
            delaytitle.setTextColor(getResources().getColor(R.color.dimmest));
            arrow.setBackground(getResources().getDrawable(R.drawable.arrowbutton));
            if (scheme_light_mode == 0) {
                brightness.setProgress(10);
                brightness.setThumb(getResources().getDrawable(R.drawable.dimmest_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.dimmest_volume_control));
                brightnesstitle.setTextColor(getResources().getColor(R.color.dimmest));
            } else if (scheme_light_mode == 1) {
                brightness.setProgress(50);
                brightness.setThumb(getResources().getDrawable(R.drawable.dimmer_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.dimmer_volume_control));
                brightnesstitle.setTextColor(getResources().getColor(R.color.dimmer));
            } else {
                brightness.setProgress(80);
                brightness.setThumb(getResources().getDrawable(R.drawable.dimm_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.dimm_volume_control));
                brightnesstitle.setTextColor(getResources().getColor(R.color.dimm));
            }
        }
    }

    private void load_previous_dark_mode_settings() {
        if(getContext()!=null) {
            Drawable darksimpelbackground = getContext().getResources().getDrawable(R.drawable.darksimpelbackground);
            settings.setBackground(darksimpelbackground);
            delaytitle.setTextColor(getResources().getColor(R.color.white));
            arrow.setBackground(getResources().getDrawable(R.drawable.darkarrowbutton));
            if (scheme == 0) {
                brightness.setProgress(10);
                brightness.setThumb(getResources().getDrawable(R.drawable.darkest_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.darkest_volume_control));
                brightnesstitle.setTextColor(getResources().getColor(R.color.darkest));
            } else if (scheme == 1) {
                brightness.setProgress(50);
                brightness.setThumb(getResources().getDrawable(R.drawable.dark_thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.dark_volume_control));
                brightnesstitle.setTextColor(getResources().getColor(R.color.dark));
            } else {
                brightness.setProgress(80);
                brightness.setThumb(getResources().getDrawable(R.drawable.thumb));
                brightness.setProgressDrawable(getResources().getDrawable(R.drawable.volume_control));
                brightnesstitle.setTextColor(getResources().getColor(R.color.white));
            }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            BottomSheetListener mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

}
