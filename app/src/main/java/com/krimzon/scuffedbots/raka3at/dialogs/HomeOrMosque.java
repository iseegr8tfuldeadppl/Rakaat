package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
    private Typeface arabic_typeface;
    private boolean darkmode;
    private String language;
    private boolean found_prayed_history_in_sql = false;

    public HomeOrMosque(Activity a, boolean friday, String prayed, String todaycomparable, int prayerer, boolean darkmode, String language, String verified, String athome) {
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
    }

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

        // Step 2: Fonts
        arabic_typeface = Typeface.createFromAsset(c.getAssets(),  "Tajawal-Light.ttf");
        selectionmosque.setTypeface(arabic_typeface);
        selectionhome.setTypeface(arabic_typeface);
        selectiontitle.setTypeface(arabic_typeface);

        // Step 3: display mode
        if(!darkmode)
            light_mode();
        
        // Step 4: language
        if(language.equals("en"))
            english();

        // Wait for selection
        selectionhome.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            selectionhome.setEnabled(false);
            selectionmosque.setEnabled(false);
            at_home = true;

            /*DetectorOrNot detectorOrNot=new DetectorOrNot(c, friday, prayed, todaycomparable, prayerer, darkmode, language, at_home, verified, athome);
            detectorOrNot.show();*/
            detectorornot();
            //dismiss();
        }});
        selectionmosque.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            selectionhome.setEnabled(false);
            selectionmosque.setEnabled(false);
            at_home = false;
            /*DetectorOrNot detectorOrNot=new DetectorOrNot(c, friday, prayed, todaycomparable, prayerer, darkmode, language, at_home, verified, athome);
            detectorOrNot.show();*/
            detectorornot();
            //dismiss();
        }});
    }

    private void detectorornot() {
        setContentView(R.layout.detectorornot);

        // Step 1: initialize variables
        selectionbackground = findViewById(R.id.selectionbackground);
        selectiontitle = findViewById(R.id.selectiontitle);
        selectionmosque = findViewById(R.id.selectionmosque);
        selectionhome = findViewById(R.id.selectionhome);

        // Step 2: Fonts
        Typeface arabic_typeface = Typeface.createFromAsset(c.getAssets(), "Tajawal-Light.ttf");
        selectionmosque.setTypeface(arabic_typeface);
        selectionhome.setTypeface(arabic_typeface);
        selectiontitle.setTypeface(arabic_typeface);

        // Step 3: display mode
        if(!darkmode)
            light_mode2();

        // Step 4: language
        if(language.equals("en"))
            english2();

        // Wait for selection
        selectionhome.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            selectionhome.setEnabled(false);
            selectionmosque.setEnabled(false);
            send(prayerer);
        }});
        selectionmosque.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            selectionhome.setEnabled(false);
            selectionmosque.setEnabled(false);

            // TODO: after adding a new collumn for confirmed prayers or not, add the check here
            set_prayer_without_using_detector();
            c.finish();
            c.startActivity(c.getIntent());
            dismiss();
        }});
    }

    private void sql() {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = "force3";
        SQLSharing.mydb = new SQL(c);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
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
            SQLSharing.mydb.updatePrayed(todaycomparable, temper, verified, athome);
        else
            SQLSharing.mydb.insertPrayed(todaycomparable, temper, verified, athome);
    }

    private void check_if_prayed_exists_in_sql() {
        if(SQLSharing.mycursor.getCount()<=0)
            found_prayed_history_in_sql = false;
        else {
            while(SQLSharing.mycursor.moveToNext()) {
                if (todaycomparable.equals(SQLSharing.mycursor.getString(1))){
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
    }

    private void light_mode2(){
        Drawable simpelbackground = c.getResources().getDrawable(R.drawable.simpelbackground);
        Drawable buttons = c.getResources().getDrawable(R.drawable.buttons);
        Drawable buttons2 = c.getResources().getDrawable(R.drawable.buttons);
        selectionbackground.setBackground(simpelbackground);
        selectiontitle.setTextColor(Color.BLACK);

        selectionmosque.setTextColor(Color.WHITE);
        selectionhome.setTextColor(Color.WHITE);

        selectionmosque.setBackground(buttons);
        selectionhome.setBackground(buttons2);
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
    }

    private void light_mode(){
        Drawable simpelbackground = c.getResources().getDrawable(R.drawable.simpelbackground);
        Drawable buttons = c.getResources().getDrawable(R.drawable.buttons);
        Drawable buttons2 = c.getResources().getDrawable(R.drawable.buttons);
        selectionbackground.setBackground(simpelbackground);
        selectiontitle.setTextColor(Color.BLACK);

        selectionmosque.setTextColor(Color.WHITE);
        selectionhome.setTextColor(Color.WHITE);

        selectionmosque.setBackground(buttons);
        selectionhome.setBackground(buttons2);
    }

}