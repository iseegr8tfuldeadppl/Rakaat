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

public class protected_apps_request extends Dialog implements android.view.View.OnClickListener {


    private Activity c;
    private boolean darkmode;
    private String language;
    private TextView title;
    private LinearLayout background;
    private Button gotoprotected, dismiss, idid;

    public protected_apps_request(Activity a, boolean darkmode, String language) {
        super(a);
        this.c = a;
        this.darkmode = darkmode;
        this.language = language;
        variables_setup();
        typeface_setup();
        if(language.equals("en"))
            language_setup();
        if(!darkmode)
            light_mode();
    }

    private void language_setup() {
        title.setText(c.getResources().getString(R.string.request_text));
        gotoprotected.setText(c.getResources().getString(R.string.go_to_protected));
        dismiss.setText(c.getResources().getString(R.string.nothanks));
        idid.setText(c.getResources().getString(R.string.idid));
    }

    private void typeface_setup() {
        Typeface arabic_typeface = Typeface.createFromAsset(c.getAssets(), "Tajawal-Light.ttf");
        title.setTypeface(arabic_typeface);
        gotoprotected.setTypeface(arabic_typeface);
        dismiss.setTypeface(arabic_typeface);
        idid.setTypeface(arabic_typeface);

    }

    private void variables_setup() {
        background = c.findViewById(R.id.background);
        title = c.findViewById(R.id.title);
        gotoprotected = c.findViewById(R.id.gotoprotected);
        dismiss = c.findViewById(R.id.dismiss);
        idid = c.findViewById(R.id.idid);
    }

    private void light_mode(){
        background.setBackground(c.getResources().getDrawable(R.drawable.simpelbackground));
        title.setTextColor(Color.BLACK);

        gotoprotected.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        dismiss.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        idid.setBackground(c.getResources().getDrawable(R.drawable.buttons));
    }



    private void sql(String table) {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = table;
        SQLSharing.mydb = new SQL(c.getApplicationContext());
        switch (table) {
            case "slat":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateslat();
                break;
            case "force":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateforce3();
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.protected_apps_request);

    }

    private void languageshet() {
        if(language.equals("en")){
        }
    }



    private void print(Object dumps) {
        Toast.makeText(c.getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.dismiss:
                dismiss();
                break;
            case R.id.gotoprotected:
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(c.getResources().getString(R.string.huaweisource), c.getResources().getString(R.string.huaweiactivity)));
                c.startActivity(intent);
                break;
            case R.id.idid:
                sql("slat");
                SQLSharing.mycursor.moveToPosition(9);
                SQLSharing.mydb.updateData("no", SQLSharing.mycursor.getString(0));
                close_sql();
        }
    }

    private void close_sql() {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
    }


}