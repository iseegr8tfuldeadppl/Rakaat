package com.krimzon.scuffedbots.raka3at.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krimzon.scuffedbots.raka3at.MainActivity;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class force_settings extends BottomSheetDialogFragment {

    private Button arrow;
    private LinearLayout settings;
    private View v;
    private String language = "ar";
    private boolean darkmode = true;

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
        apply_adanSelections();
        onClickListeners();
        /*if(it_is_nightmode_since_lightmode_shines_and_ruins_measurement)
            load_previous_dark_mode_settings();
        else
            load_previous_light_mode_settings();*/

        /*work_on_language();*/

        arrow.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            dismiss();
        }});
        return v;
    }

    private void applyfont() {
        try{
            Typeface arabic_typeface = Typeface.createFromAsset(getContext().getAssets(), "Tajawal-Light.ttf");
            arrow.setTypeface(arabic_typeface);
            adantitle.setTypeface(arabic_typeface);
            for(int i=0; i<6; i++){
                if(i!=1) {
                    adans.get(i).setTypeface(arabic_typeface);
                }
                titles.get(i).setTypeface(arabic_typeface);
            }
        } catch(Exception ignored){}
    }

    private void onClickListeners() {
        adans.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdanSelection adanSelection = new AdanSelection(v.getContext(), darkmode, language, 0);
                adanSelection.show();
            }
        });
        // we ignored get(1) as fajr has no adan
        adans.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdanSelection adanSelection = new AdanSelection(v.getContext(), darkmode, language, 2);
                adanSelection.show();
            }
        });
        adans.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdanSelection adanSelection = new AdanSelection(v.getContext(), darkmode, language, 3);
                adanSelection.show();
            }
        });
        adans.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdanSelection adanSelection = new AdanSelection(v.getContext(), darkmode, language, 4);
                adanSelection.show();
            }
        });
        adans.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdanSelection adanSelection = new AdanSelection(v.getContext(), darkmode, language, 5);
                adanSelection.show();
            }
        });
    }

    private void apply_adanSelections() {
        String[] selections = adanSelections.split(" ");
        String[] yes;
        for(int i=0; i<6;i++){
            yes = selections[i].split(",");
            if(i!=1) {
                // applied currently selected adan
                switch (yes[0]) {
                    case "1":
                        adans.get(i).setText(resources.getString(R.string.adan1_arabe));
                        break;
                    case "2":
                        adans.get(i).setText(resources.getString(R.string.adan2_arabe));
                        break;
                    case "3":
                        adans.get(i).setText(resources.getString(R.string.adan3));
                        break;
                    case "4":
                        adans.get(i).setText(resources.getString(R.string.adan4));
                        break;
                    case "5":
                        adans.get(i).setText(resources.getString(R.string.adan5));
                        break;
                    case "6":
                        adans.get(i).setText(resources.getString(R.string.adan6));
                        break;
                }
            }

            // TODO: Apply ringmode bitmaps using glide
            switch (yes[1]) {
                case "0":
                    // no ring no vibrate
                    try {
                        Glide.with(this).load(R.drawable.soundsoff).into(ringmodes.get(i));
                    } catch (Exception ignored) {
                        ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsoff));
                    }
                    break;
                case "1":
                    // vibrate only
                    break;
                case "2":
                    // ring only
                    break;
                case "3":
                    // ring n vibrate
            }
        }
    }

    private void work_on_language() {
        if(language.equals("en"))
            english();
    }


    private void english(){
        /*arrow.setText(save);*/
        /*brightnesstitle.setText(setting1);
        zero.setText(zeroseconds);
        three.setText(threeseconds);
        five.setText(fiveseconds);
        delaytitle.setText(delaybeforecountingg);*/
    }

    private String adanSelections = "";
    private void sql_work() {
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(getContext());
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
        SQLSharing.mycursor.moveToFirst();
        SQLSharing.mycursor.moveToNext();
        darkmode = SQLSharing.mycursor.getString(1).equals("yes");
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        SQLSharing.mycursor.moveToNext();
        language = SQLSharing.mycursor.getString(1);
        SQLSharing.mycursor.moveToNext();
        adanSelections = SQLSharing.mycursor.getString(1);
    }



    private Resources resources;
    private TextView adantitle;
    private List<TextView> adans;
    private List<ImageView> ringmodes;
    private List<TextView> titles;
    private void prepare_variables() {
        arrow = v.findViewById(R.id.arrow);
        settings = v.findViewById(R.id.settings);
        adantitle = v.findViewById(R.id.adantitle);

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
