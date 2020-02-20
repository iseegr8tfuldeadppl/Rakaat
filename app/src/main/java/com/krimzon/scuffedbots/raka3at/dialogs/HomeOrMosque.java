package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
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

            DetectorOrNot detectorOrNot=new DetectorOrNot(c, friday, prayed, todaycomparable, prayerer, darkmode, language, at_home, verified, athome);
            detectorOrNot.show();
            dismiss();
        }});
        selectionmosque.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            selectionhome.setEnabled(false);
            selectionmosque.setEnabled(false);
            at_home = false;
            DetectorOrNot detectorOrNot=new DetectorOrNot(c, friday, prayed, todaycomparable, prayerer, darkmode, language, at_home, verified, athome);
            detectorOrNot.show();
            dismiss();
        }});
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