package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;

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