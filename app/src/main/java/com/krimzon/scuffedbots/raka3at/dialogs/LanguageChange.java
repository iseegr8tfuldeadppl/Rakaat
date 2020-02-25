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

import com.krimzon.scuffedbots.raka3at.MainActivity;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;


public class LanguageChange extends Dialog implements android.view.View.OnClickListener {


    private Activity c;
    private boolean darkmode;
    private String language;

    public LanguageChange(Activity a, boolean darkmode, String language) {
        super(a);
        this.c = a;
        this.darkmode = darkmode;
        this.language = language;
    }


    private void sql() {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydb = new SQL(c.getApplicationContext());
        SQLSharing.mycursor = SQLSharing.mydb.getAllDateslat();
    }


    private Button english, arabic;
    private TextView languagetitle;
    private LinearLayout languagebackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.languagechange);

        english = findViewById(R.id.english);
        arabic = findViewById(R.id.arabe);
        languagebackground = findViewById(R.id.languagebackground);
        languagetitle = findViewById(R.id.languagetitle);

        Typeface font = Typeface.createFromAsset(c.getAssets(), "Tajawal-Light.ttf");
        english.setTypeface(font);
        arabic.setTypeface(font);
        languagetitle.setTypeface(font);

        if(!darkmode)
            light_mode();

        english.setOnClickListener(this);
        arabic.setOnClickListener(this);
    }

    private void light_mode() {
        Drawable simpelbackground = c.getResources().getDrawable(R.drawable.simpelbackground);
        Drawable buttons = c.getResources().getDrawable(R.drawable.buttons);
        Drawable buttons2 = c.getResources().getDrawable(R.drawable.buttons);
        languagebackground.setBackground(simpelbackground);
        languagetitle.setTextColor(Color.BLACK);

        arabic.setTextColor(Color.WHITE);
        english.setTextColor(Color.WHITE);

        arabic.setBackground(buttons);
        english.setBackground(buttons2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.english:
                if(language.equals("en"))
                    dismiss();
                else {
                    sql();
                    SQLSharing.mycursor.moveToFirst();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mydb.updateData("en", SQLSharing.mycursor.getString(0));
                    SQLSharing.mycursor.close();
                    SQLSharing.mydb.close();
                    dismiss();
                    Intent main = new Intent(c.getApplicationContext(), MainActivity.class);
                    c.startActivity(main);
                    c.finish();
                }
                break;
            case R.id.arabe:
                if(language.equals("ar"))
                    dismiss();
                else {
                    sql();
                    SQLSharing.mycursor.moveToFirst();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mycursor.moveToNext();
                    SQLSharing.mydb.updateData("ar", SQLSharing.mycursor.getString(0));
                    SQLSharing.mycursor.close();
                    SQLSharing.mydb.close();
                    dismiss();
                    Intent mainer = new Intent(c.getApplicationContext(), MainActivity.class);
                    c.startActivity(mainer);
                    c.finish();
                }
                break;
            default:
                break;
        }
    }

}