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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class Statistictictictictic extends Dialog implements android.view.View.OnClickListener {


    private Activity c;
    private boolean darkmode;
    private String language;

    public Statistictictictictic(Activity a, boolean darkmode, String language) {
        super(a);
        this.c = a;
        this.darkmode = darkmode;
        this.language = language;
    }


    private void sql() {
        SQLSharing.TABLE_NAME_INPUTER = "force3";
        SQLSharing.mydbforce3 = new SQL(c.getApplicationContext());
        SQLSharing.mycursorforce3 = SQLSharing.mydbforce3.getAllDateforce3();
    }


    private Button dismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.statistictictictictic);

        ImageView statslogo = findViewById(R.id.statslogo);
        try {
            Glide.with(getContext()).load(R.drawable.stats).into(statslogo);
        } catch (Exception ignored) {
            statslogo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.stats));
        }

        TextView display_of_prayed_prayers_of_all_time = findViewById(R.id.prayed_prayers_of_all_time);
        title_prayed_prayers_of_all_time = findViewById(R.id.title_prayed_prayers_of_all_time);

        TextView display_prayed_prayers_past_week = findViewById(R.id.prayed_prayers_past_week);
        title_prayed_prayers_past_week = findViewById(R.id.title_prayed_prayers_past_week);

        TextView display_prayed_prayers_past_month = findViewById(R.id.prayed_prayers_past_month);
        title_prayed_prayers_past_month = findViewById(R.id.title_prayed_prayers_past_month);

        title_prayed_prayers_past_six_months = findViewById(R.id.title_prayed_prayers_past_six_months);
        TextView display_prayed_prayers_past_six_months = findViewById(R.id.prayed_prayers_past_six_months);

        title_prayed_prayers_past_year = findViewById(R.id.title_prayed_prayers_past_year);
        TextView display_prayed_prayers_past_year = findViewById(R.id.prayed_prayers_past_year);

        title_prayed_prayers_today = findViewById(R.id.title_prayed_prayers_today);
        TextView display_prayed_prayers_today = findViewById(R.id.prayed_prayers_today);

        title = findViewById(R.id.title);
        statstitle = findViewById(R.id.statstitle);
        dismiss = findViewById(R.id.dismiss);

        languageshet();

        Typeface arabic_typeface = Typeface.createFromAsset(c.getAssets(), "Tajawal-Light.ttf");

        statstitle.setTypeface(arabic_typeface);
        title.setTypeface(arabic_typeface);
        title_prayed_prayers_of_all_time.setTypeface(arabic_typeface);
        display_of_prayed_prayers_of_all_time.setTypeface(arabic_typeface);
        title_prayed_prayers_past_week.setTypeface(arabic_typeface);
        display_prayed_prayers_past_week.setTypeface(arabic_typeface);
        display_prayed_prayers_past_month.setTypeface(arabic_typeface);
        title_prayed_prayers_past_month.setTypeface(arabic_typeface);
        display_prayed_prayers_past_six_months.setTypeface(arabic_typeface);
        title_prayed_prayers_past_six_months.setTypeface(arabic_typeface);
        title_prayed_prayers_past_year.setTypeface(arabic_typeface);
        display_prayed_prayers_past_year.setTypeface(arabic_typeface);
        title_prayed_prayers_today.setTypeface(arabic_typeface);
        display_prayed_prayers_today.setTypeface(arabic_typeface);
        dismiss.setTypeface(arabic_typeface);

        if(!darkmode) {
            findViewById(R.id.fulle).setBackground(c.getResources().getDrawable(R.drawable.simpelbackground));
            title.setTextColor(Color.BLACK);
            statstitle.setTextColor(Color.BLACK);
            title_prayed_prayers_today.setTextColor(Color.BLACK);
            display_prayed_prayers_today.setTextColor(Color.BLACK);
            title_prayed_prayers_past_week.setTextColor(Color.BLACK);
            display_prayed_prayers_past_week.setTextColor(Color.BLACK);
            title_prayed_prayers_past_month.setTextColor(Color.BLACK);
            display_prayed_prayers_past_month.setTextColor(Color.BLACK);
            title_prayed_prayers_past_six_months.setTextColor(Color.BLACK);
            display_prayed_prayers_past_six_months.setTextColor(Color.BLACK);
            title_prayed_prayers_past_year.setTextColor(Color.BLACK);
            display_prayed_prayers_past_year.setTextColor(Color.BLACK);
            title_prayed_prayers_of_all_time.setTextColor(Color.BLACK);
            display_of_prayed_prayers_of_all_time.setTextColor(Color.BLACK);
            dismiss.setBackground(c.getResources().getDrawable(R.drawable.buttons));
        }


        sql();
        count_prayed_prayers_today();
        display_prayed_prayers_today.setText(String.valueOf(prayed_prayers));
        prayed_prayers = 0;

        sql();
        count_prayed_prayers_of_all_time();
        display_of_prayed_prayers_of_all_time.setText(String.valueOf(prayed_prayers));
        prayed_prayers = 0;

        count_prayed_prayers(7);
        display_prayed_prayers_past_week.setText(String.valueOf(prayed_prayers));
        prayed_prayers = 0;

        count_prayed_prayers(30);
        display_prayed_prayers_past_month.setText(String.valueOf(prayed_prayers));
        prayed_prayers = 0;

        count_prayed_prayers(182);
        display_prayed_prayers_past_six_months.setText(String.valueOf(prayed_prayers));
        prayed_prayers = 0;

        count_prayed_prayers(365);
        display_prayed_prayers_past_year.setText(String.valueOf(prayed_prayers));
        prayed_prayers = 0;

        dismiss.setOnClickListener(this);
    }

    private void languageshet() {
        if(language.equals("en")){
            title_prayed_prayers_today.setText(getContext().getString(R.string.today));
            title_prayed_prayers_past_year.setText(getContext().getString(R.string.pastyear));
            title_prayed_prayers_past_six_months.setText(getContext().getString(R.string.past6months));
            title_prayed_prayers_past_month.setText(getContext().getString(R.string.pastmonth));
            title_prayed_prayers_past_week.setText(getContext().getString(R.string.past7days));
            title_prayed_prayers_of_all_time.setText(getContext().getString(R.string.alltime));
            statstitle.setText(getContext().getString(R.string.stats));
            title.setText(getContext().getString(R.string.yourprayers));
            dismiss.setText(c.getResources().getString(R.string.closenglish));
        }
    }

    private void count_prayed_prayers_today() {
        Calendar cal = Calendar.getInstance(Locale.US);
        day = new Date(cal.getTimeInMillis());
        tempday = day.toString().split(" ");
        daycomparable = tempday[1] + " " + tempday[2] + " " + tempday[5];

        if(SQLSharing.mycursorforce3.getCount()>0){
            while(SQLSharing.mycursorforce3.moveToNext()) {
                if (daycomparable.equals(SQLSharing.mycursorforce3.getString(1))) {
                    temper = SQLSharing.mycursorforce3.getString(2);
                    for (int i = 0; i < temper.length(); i++)
                        if (String.valueOf(temper.charAt(i)).equals("1"))
                            prayed_prayers += 1;
                    break;
                }
            }
        } else
            prayed_prayers = 0;
    }


    private void count_prayed_prayers(int days) {


        sql();
        if(SQLSharing.mycursorforce3.getCount()>0){
            // we do one day on its own since it's today and can't be looped like others look below
            GregorianCalendar gc = new GregorianCalendar();
            day = gc.getTime();
            tempday = day.toString().split(" ");
            daycomparable = tempday[1] + " " + tempday[2] + " " + tempday[5];
            count_prayed_prayers2();


            for(int i=0; i < days - 1; i++){ // will only do 6
                gc = new GregorianCalendar();
                gc.add(Calendar.DATE, -(i+1));
                day = gc.getTime();
                tempday = day.toString().split(" ");
                daycomparable = tempday[1] + " " + tempday[2] + " " + tempday[5];
                count_prayed_prayers2();
            }

        } else
            prayed_prayers = 0;

    }


    private void count_prayed_prayers2() {

        SQLSharing.mycursorforce3.moveToFirst();
        if (daycomparable.equals(SQLSharing.mycursorforce3.getString(1))){
            temper = SQLSharing.mycursorforce3.getString(2);
            for(int i=0;i<temper.length();i++)
                if(String.valueOf(temper.charAt(i)).equals("1"))
                    prayed_prayers += 1;
        }

        while(SQLSharing.mycursorforce3.moveToNext()) {
            if (daycomparable.equals(SQLSharing.mycursorforce3.getString(1))){
                temper = SQLSharing.mycursorforce3.getString(2);
                for(int i=0;i<temper.length();i++)
                    if(String.valueOf(temper.charAt(i)).equals("1"))
                        prayed_prayers += 1;
            }
        }
    }


    private void print(Object dumps) {
        Toast.makeText(c.getApplicationContext(), String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }



    private Date day;
    private String[] tempday;
    private String daycomparable;
    private TextView title_prayed_prayers_of_all_time;
    private TextView title_prayed_prayers_past_month;
    private TextView title_prayed_prayers_past_week;
    private TextView title_prayed_prayers_past_six_months;
    private TextView title_prayed_prayers_past_year;
    private TextView title_prayed_prayers_today;
    private TextView statstitle;
    private TextView title;
    private int prayed_prayers = 0;
    private String temper;

    private void count_prayed_prayers_of_all_time() {
        if(SQLSharing.mycursorforce3.getCount()>0){
                while(SQLSharing.mycursorforce3.moveToNext()) {
                        temper = SQLSharing.mycursorforce3.getString(2);
                        for(int i=0;i<temper.length();i++)
                            if(String.valueOf(temper.charAt(i)).equals("1"))
                                prayed_prayers += 1;
                }
        } else
            prayed_prayers = 0;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dismiss) {
            dismiss();
        }
    }


}