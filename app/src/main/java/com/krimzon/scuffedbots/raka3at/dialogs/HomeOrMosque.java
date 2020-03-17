package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.slat;

public class HomeOrMosque extends Dialog {

    private Activity c;
    private boolean at_home = false;
    private boolean friday;
    private String prayed;
    private String verified;
    private String todaycomparable;
    private String athome;
    private int prayerer;
    private LinearLayout selectionbackground;
    private TextView selectiontitle;
    private Button selectionmosque, selectionhome;
    private boolean darkmode;
    private String language;
    private boolean found_prayed_history_in_sql = false;
    private boolean all_day;

    public HomeOrMosque(Activity a, boolean friday, String prayed, String todaycomparable, int prayerer, boolean darkmode, String language, String verified, String athome, boolean all_day) {
        super(a);
        this.c = a;
        this.friday = friday;
        this.prayed = prayed;
        this.todaycomparable = todaycomparable;
        this.prayerer = prayerer;
        this.darkmode = darkmode;
        this.language = language;
        this.verified = verified;
        this.athome = athome;
        this.all_day = all_day;
    }

    private Button dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.homeormosque);

        // Step 1: initialize variables
        selectionbackground = findViewById(R.id.selectionbackground);
        selectiontitle = findViewById(R.id.selectiontitle);
        selectionmosque = findViewById(R.id.selectionmosque);
        selectionhome = findViewById(R.id.selectionhome);
        dismiss = findViewById(R.id.dismiss);

        // Step 2: Fonts
        Typeface arabic_typeface = Typeface.createFromAsset(c.getAssets(), "Tajawal-Light.ttf");
        selectionmosque.setTypeface(arabic_typeface);
        selectionhome.setTypeface(arabic_typeface);
        selectiontitle.setTypeface(arabic_typeface);
        dismiss.setTypeface(arabic_typeface);

        // Step 3: display mode
        if(!darkmode)
            light_mode();
        
        // Step 4: language
        if(language.equals("en"))
            english();

        // Wait for selection
        selectionhome.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            at_home = true;

            if(all_day)
                set_all_prayers_and_reload();
            else
                detectorornot();
        }});
        selectionmosque.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            at_home = false;

            if(all_day)
                set_all_prayers_and_reload();
            else
                detectorornot();
        }});
        dismiss.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            dismiss();
        }});
    }

    private void set_all_prayers_and_reload() {
        set_all_prayers();

        c.finish();
        c.getIntent().putExtra("todaycomparable", todaycomparable);
        c.getIntent().putExtra("light_alert", "no");
        c.startActivity(c.getIntent());
        dismiss();
    }

    private void detectorornot() {
        setContentView(R.layout.detectorornot);

        // Step 1: initialize variables
        selectionbackground = findViewById(R.id.selectionbackground);
        selectiontitle = findViewById(R.id.selectiontitle);
        selectionmosque = findViewById(R.id.selectionmosque);
        selectionhome = findViewById(R.id.selectionhome);
        dismiss = findViewById(R.id.dismiss);

        // Step 2: Fonts
        Typeface arabic_typeface = Typeface.createFromAsset(c.getAssets(), "Tajawal-Light.ttf");
        selectionmosque.setTypeface(arabic_typeface);
        selectionhome.setTypeface(arabic_typeface);
        selectiontitle.setTypeface(arabic_typeface);
        dismiss.setTypeface(arabic_typeface);

        // Step 3: display mode
        if(!darkmode)
            light_mode2();

        // Step 4: language
        if(language.equals("en"))
            english2();

        // Wait for selection
        selectionhome.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            send(prayerer);
        }});
        selectionmosque.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {

            // TODO: after adding a new collumn for confirmed prayers or not, add the check here
            set_prayer_without_using_detector();
            c.finish();
            c.getIntent().putExtra("todaycomparable", todaycomparable);
            c.getIntent().putExtra("light_alert", "no");
            c.startActivity(c.getIntent());
            dismiss();
        }});
        dismiss.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            dismiss();
        }});
    }

    private void sql() {
        SQLSharing.TABLE_NAME_INPUTER = "force3";
        SQLSharing.mydbforce3 = SQL.getInstance(c);
        SQLSharing.mycursorforce3 = SQLSharing.mydbforce3.getAllDateforce3();
    }

    private void set_prayer_without_using_detector() {

        // set if at home or not
        if(at_home){
            StringBuilder athome_stringbuilder = new StringBuilder(athome);
            athome_stringbuilder.setCharAt(prayerer, '1');
            athome = String.valueOf(athome_stringbuilder);
        }

        sql();
        StringBuilder strinkbilder = new StringBuilder(prayed);
        strinkbilder.setCharAt(prayerer, '1');
        String temper = String.valueOf(strinkbilder);
        check_if_prayed_exists_in_sql();

        if(found_prayed_history_in_sql)
            SQLSharing.mydbforce3.updatePrayed(todaycomparable, temper, verified, athome);
        else
            SQLSharing.mydbforce3.insertPrayed(todaycomparable, temper, verified, athome);
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

    private void set_all_prayers() {

        // set if at home or not

        if(at_home)
            athome = "11111";
        else
            athome = "00000";

        sql();
        check_if_prayed_exists_in_sql();
        if(found_prayed_history_in_sql)
            SQLSharing.mydbforce3.updatePrayed(todaycomparable, "11111", verified, athome);
        else
            SQLSharing.mydbforce3.insertPrayed(todaycomparable, "11111", verified, athome);
        close_sql();
    }

    private void check_if_prayed_exists_in_sql() {
        if(SQLSharing.mycursorforce3.getCount()<=0)
            found_prayed_history_in_sql = false;
        else {
            while(SQLSharing.mycursorforce3.moveToNext()) {
                if (todaycomparable.equals(SQLSharing.mycursorforce3.getString(1))){
                    found_prayed_history_in_sql = true;
                    break;
                }
            }
        }
    }

    private void english2() {
        selectiontitle.setText(c.getString(R.string.needdetector));
        selectionhome.setText(c.getString(R.string.detectoryes));
        selectionmosque.setText(c.getString(R.string.detectorno));
        dismiss.setText(c.getResources().getString(R.string.closenglish));
    }

    private void light_mode2(){
        selectionbackground.setBackground(c.getResources().getDrawable(R.drawable.simpelbackground));
        selectiontitle.setTextColor(Color.BLACK);

        selectionmosque.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        selectionhome.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        dismiss.setBackground(c.getResources().getDrawable(R.drawable.buttons));
    }

    private void send(int prayer){
        Intent slatIntent = new Intent(c.getApplicationContext(), slat.class);
        slatIntent.putExtra("sender", c.getResources().getString(R.string.justforce));
        slatIntent.putExtra("prayer", String.valueOf(prayer));
        slatIntent.putExtra("todaycomparable", todaycomparable);
        slatIntent.putExtra("prayed", prayed);
        slatIntent.putExtra("verified", verified);
        slatIntent.putExtra("friday", String.valueOf(friday));
        slatIntent.putExtra("at_home", String.valueOf(at_home));
        slatIntent.putExtra("athome", String.valueOf(athome));
        c.startActivity(slatIntent);
        c.finish();
    }

    private void english() {
        selectiontitle.setText(c.getString(R.string.selectiontitle));
        selectionhome.setText(c.getString(R.string.selectionhome));
        selectionmosque.setText(c.getString(R.string.selectionmosque));
        dismiss.setText(c.getResources().getString(R.string.closenglish));
    }

    private void light_mode(){
        dismiss.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        selectionbackground.setBackground(c.getResources().getDrawable(R.drawable.simpelbackground));
        selectiontitle.setTextColor(Color.BLACK);

        selectionmosque.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        selectionhome.setBackground(c.getResources().getDrawable(R.drawable.buttons));
    }

}