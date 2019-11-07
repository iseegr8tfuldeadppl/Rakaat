package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.krimzon.scuffedbots.raka3at.MainActivity;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;


public class LanguageChange extends Dialog implements android.view.View.OnClickListener {


    private Activity c;

    public LanguageChange(Activity a) {
        super(a);
        this.c = a;
    }


    private void sql(String table) {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = table;
        SQLSharing.mydb = new SQL(c.getApplicationContext());
        SQLSharing.mycursor = SQLSharing.mydb.getAllDate();
    }


    protected Button english, arabic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.languagechange);

        english = findViewById(R.id.english);
        arabic = findViewById(R.id.arabic);


        english.setOnClickListener(this);
        arabic.setOnClickListener(this);
    }

    private void print(Object dumps) {
        Toast.makeText(c.getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.english:
                sql("slat");
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
                break;
            case R.id.arabic:
                sql("slat");
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
                break;
            default:
                break;
        }
    }

}