package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;

import java.util.ArrayList;
import java.util.List;

public class AdanSelection extends Dialog {

    private Context c;
    private boolean darkmode;
    private String language;
    private int prayer;

    AdanSelection(Context c, boolean darkmode, String language, int prayer) {
        super(c); // TODO: this might cause a crash idk how to get activity to i renamed to Context fix pls
        // TODO: this might cause a crash idk how to get activity to i renamed to Context fix pls
        // TODO: this might cause a crash idk how to get activity to i renamed to Context fix pls
        // TODO: this might cause a crash idk how to get activity to i renamed to Context fix pls
        // TODO: this might cause a crash idk how to get activity to i renamed to Context fix pls
        // TODO: this might cause a crash idk how to get activity to i renamed to Context fix pls
        // TODO: this might cause a crash idk how to get activity to i renamed to Context fix pls
        this.c = c;
        this.darkmode = darkmode;
        this.language = language;
        this.prayer = prayer;

        variables();
        applyfont();
    }

    private TextView selectiontitle;
    private Button selectionmosque;
    private List<Button> adans;
    private void variables() {
        adans = new ArrayList<>();
        adans.add((Button) findViewById(R.id.adan1));
        adans.add((Button) findViewById(R.id.adan2));
        adans.add((Button) findViewById(R.id.adan3));
        adans.add((Button) findViewById(R.id.adan4));
        adans.add((Button) findViewById(R.id.adan5));
        adans.add((Button) findViewById(R.id.adan6));

        selectionmosque = findViewById(R.id.selectionmosque);
        selectiontitle = findViewById(R.id.selectiontitle);
    }

    private void applyfont() {
        try{
            Typeface arabic_typeface = Typeface.createFromAsset(getContext().getAssets(), "Tajawal-Light.ttf");
            selectionmosque.setTypeface(arabic_typeface);
            selectiontitle.setTypeface(arabic_typeface);
            for(int i=0; i<6; i++)
                adans.get(i).setTypeface(arabic_typeface);
        } catch(Exception ignored){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.adanselection);

        // Step 2: Fonts
        Typeface arabic_typeface = Typeface.createFromAsset(c.getAssets(), "Tajawal-Light.ttf");
        // TODO: finish adding click listeners for adan selection list
        // Step 3: display mode
        if(!darkmode)
            light_mode();

        // Step 4: language
        if(language.equals("en"))
            english();

        // Wait for selection
        /*c.finish();
        c.startActivity(c.getIntent());
        dismiss();*/
    }

    private void sql() {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(c);
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
    }

    private void english() {
        /*selectiontitle.setText(c.getString(R.string.needdetector));*/

    }

    private void light_mode(){
        Drawable simpelbackground = c.getResources().getDrawable(R.drawable.simpelbackground);
        Drawable buttons = c.getResources().getDrawable(R.drawable.buttons);
        Drawable buttons2 = c.getResources().getDrawable(R.drawable.buttons);
    }


}