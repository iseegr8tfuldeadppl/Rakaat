package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
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
import android.widget.Toast;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;

public class protected_apps_request extends Dialog {


    private Activity c;
    private boolean darkmode;
    private String language;
    private TextView title;
    private LinearLayout background;
    private TextView gotoprotectedd, dismiss, idid;

    public protected_apps_request(Activity a, boolean darkmode, String language) {
        super(a);
        this.c = a;
        this.darkmode = darkmode;
        this.language = language;
    }

    private void language_setup() {
        title.setText(c.getResources().getString(R.string.request_text));
        gotoprotectedd.setText(c.getResources().getString(R.string.go_to_protected));
        dismiss.setText(c.getResources().getString(R.string.nothanks));
        idid.setText(c.getResources().getString(R.string.idid));
    }

    private void typeface_setup() {
        Typeface arabic_typeface = Typeface.createFromAsset(c.getAssets(), "Tajawal-Light.ttf");
        title.setTypeface(arabic_typeface);
        gotoprotectedd.setTypeface(arabic_typeface);
        dismiss.setTypeface(arabic_typeface);
        idid.setTypeface(arabic_typeface);

    }

    private void variables_setup() {
        background = findViewById(R.id.background);
        title = findViewById(R.id.title);
        gotoprotectedd = findViewById(R.id.gotoprotectedd);
        dismiss = findViewById(R.id.dismiss);
        idid = findViewById(R.id.idid);
    }

    private void light_mode(){
        background.setBackground(c.getResources().getDrawable(R.drawable.simpelbackground));
        title.setTextColor(Color.BLACK);

        gotoprotectedd.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        dismiss.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        idid.setBackground(c.getResources().getDrawable(R.drawable.buttons));
    }



    private void sql(String table) {
        SQLSharing.TABLE_NAME_INPUTER = table;
        switch (table) {
            case "slat":
                SQLSharing.mydbslat = new SQL(getContext());
                SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
                break;
            case "force":
                SQLSharing.mydbforce = new SQL(getContext());
                SQLSharing.mycursorforce = SQLSharing.mydbforce.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mydbforce3 = new SQL(getContext());
                SQLSharing.mycursorforce3 = SQLSharing.mydbforce3.getAllDateforce3();
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.protected_apps_request);


        variables_setup();
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        gotoprotectedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(c.getResources().getString(R.string.huaweisource), c.getResources().getString(R.string.huaweiactivity)));
                c.startActivity(intent);}
                catch(Exception ignored){
                    sql("slat");
                    SQLSharing.mycursorslat.moveToPosition(9);
                    SQLSharing.mydbslat.updateData("no", SQLSharing.mycursorslat.getString(0));
                    close_sql();
                }
            }
        });
        idid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql("slat");
                SQLSharing.mycursorslat.moveToPosition(9);
                SQLSharing.mydbslat.updateData("no", SQLSharing.mycursorslat.getString(0));
                close_sql();
                dismiss();
            }
        });
        typeface_setup();
        if(language.equals("en"))
            language_setup();
        if(!darkmode)
            light_mode();

    }

    private void languageshet() {
        if(language.equals("en")){
        }
    }



    private void print(Object dumps) {
        Toast.makeText(c.getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }

    private void close_sql() {
    }


}