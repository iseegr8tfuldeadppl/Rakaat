package com.krimzon.scuffedbots.raka3at;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RemoteViews;
import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

public class force_widget extends AppWidgetProvider {

    private boolean darkmode  = true;
    private String language;
    private CalculationParameters params;
    private int i = 0;
    private Date old_date;
    private boolean recent_adan = false;
    private Coordinates coordinates;
    private DateComponents date;
    private List<Integer> prayers;
    private String fajr;
    private String rise;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;
    private boolean end_of_day = false;
    private int current_adding_playing = 0;
    private List<String> praytimesregularform;
    private Context c;
    private int positifise=0;
    private int negatifise = 0;
    private boolean still_scoping_on_previous_adan = false;
    private int current_displayed_slider = -1;
    private int slider = 0;
    private int rightnowcomparable = 1;
    private RemoteViews widgetViews;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.force_widget);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                close_sql();
                sql("slat", context);
                SQLSharing.mycursorslater.moveToPosition(1);
                darkmode = SQLSharing.mycursorslater.getString(1).equals("yes");
                SQLSharing.mycursorslater.moveToPosition(6);
                language = SQLSharing.mycursorslater.getString(1);
                SQLSharing.mycursorslater.moveToPosition(10);
                close_sql();

                launch_prayer_processing(context);

                if(darkmode)
                    widgetViews = new RemoteViews(context.getPackageName(), R.layout.force_widget);
                else
                    widgetViews = new RemoteViews(context.getPackageName(), R.layout.force_widget_lightmode); // TODO make a light mode

                widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetrisearrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);

                if(language.equals("en")){
                    widgetViews.setTextViewText(R.id.widgetfajrtitle, context.getResources().getString(R.string.fajrtitle));
                    widgetViews.setTextViewText(R.id.widgetrisetitle, context.getResources().getString(R.string.rise));
                    widgetViews.setTextViewText(R.id.widgetdhuhrtitle, context.getResources().getString(R.string.dohrtitle));
                    widgetViews.setTextViewText(R.id.widgetasrtitle, context.getResources().getString(R.string.asrtitle));
                    widgetViews.setTextViewText(R.id.widgetmaghribtitle, context.getResources().getString(R.string.maghrebtitle));
                    widgetViews.setTextViewText(R.id.widgetishatitle, context.getResources().getString(R.string.ishatitle));
                }


                widgetViews.setTextViewText(R.id.widgetfajrtime, praytimesregularform.get(0));
                widgetViews.setTextViewText(R.id.widgetrisetime, praytimesregularform.get(1));
                widgetViews.setTextViewText(R.id.widgetdhuhrtime, praytimesregularform.get(2));
                widgetViews.setTextViewText(R.id.widgetasrtime, praytimesregularform.get(3));
                widgetViews.setTextViewText(R.id.widgetmaghrebtime, praytimesregularform.get(4));
                widgetViews.setTextViewText(R.id.widgetishatime, praytimesregularform.get(5));


                find_next_adan();
                if(i!=-1) {
                    calculate_negatifise_and_positifise();
                    check_negatifise();
                    if_change_needed_for_slider_apply_it();
                    find_currently_displayed_adans_id(i);
                }


                /*if(!end_of_day) {
                    // Check if we reached the adan, if so, then switch i to the next adan
                    if (prayers.get(i) == rightnowcomparable) {
                        if (!recent_adan) {
                            recent_adan = true;
                            current_adding_playing = i;
                            i++;
                            if (i >= 5) i = 0;
                        }
                    } else recent_adan = false;

                }*/

                // Widget
                switch (i) {
                    case 0:
                        if(language.equals("en"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.fajrtitle));
                        else if(language.equals("ar"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.fajrtitle_arabe));

                        widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(0));

                        widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.greenarrowdown);
                        widgetViews.setImageViewResource(R.id.widgetrisearrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                        break;
                    case 1:
                        if(language.equals("en"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.rise));
                        else if(language.equals("ar"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.rise_arabe));

                        widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(1));

                        widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetrisearrow, R.drawable.greenarrowdown);
                        widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                        break;
                    case 2:
                        if(language.equals("en"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.dohrtitle));
                        else if(language.equals("ar"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.dohrtitle_arabe));

                        widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(2));

                        widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetrisearrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.greenarrowdown);
                        widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                        break;
                    case 3:
                        if(language.equals("en"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.asrtitle));
                        else if(language.equals("ar"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.asrtitle_arabe));

                        widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(3));

                        widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetrisearrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.greenarrowdown);
                        widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                        break;
                    case 4:
                        if(language.equals("en"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.maghrebtitle));
                        else if(language.equals("ar"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.maghrebtitle_arabe));

                        widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(4));

                        widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetrisearrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.greenarrowdown);
                        widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);
                        break;
                    case 5:
                        if(language.equals("en"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.ishatitle));
                        else if(language.equals("ar"))
                            widgetViews.setTextViewText(R.id.nextprayertitle, context.getResources().getString(R.string.ishatitle_arabe));

                        widgetViews.setTextViewText(R.id.nextprayertime, praytimesregularform.get(5));

                        widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetrisearrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                        widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.greenarrowdown);
                        break;
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(new ComponentName(context.getPackageName(), force_widget.class.getName()), widgetViews);
                Bundle extras = intent.getExtras();
                if(extras!=null) {
                    appWidgetManager = AppWidgetManager.getInstance(context);
                    ComponentName thisAppWidget = new ComponentName(context.getPackageName(), force_widget.class.getName());
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

                    onUpdate(context, appWidgetManager, appWidgetIds);
                }

            }
        super.onReceive(context, intent);
    }
    private void find_currently_displayed_adans_id(final int next_adaner) {
        int temp;
        if (still_scoping_on_previous_adan)
            temp = next_adaner - 1;
        else
            temp = next_adaner;
        switch (temp) {
            case -1:
                current_displayed_slider = -1;
                break;
            case 0:
            case 6:
                current_displayed_slider = 0;
                break;
            case 1:
                current_displayed_slider = 1;
                break;
            case 2:
                current_displayed_slider = 2;
                break;
            case 3:
                current_displayed_slider = 3;
                break;
            case 4:
                current_displayed_slider = 4;
                break;
            case 5:
                current_displayed_slider = 5;
        }
    }

    private void launch_prayer_processing(Context context) {
        sql("force", context);
        if(SQLSharing.mycursorforceer.getCount()>0) {

            params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
            params = CalculationMethod.EGYPTIAN.getParameters();
            params.madhab = Madhab.SHAFI; // SHAFI made 95% accuracy, HANAFI had 1hour different for l'3asr
            params.adjustments.fajr = SQLSharing.params_adjustments_fajr; //2 TODO change this one aswell
            params.fajrAngle = SQLSharing.fajrangle;
            params.ishaAngle = SQLSharing.ishaangle;
            //String pattern = "dd-MMM-yyyy";
            //SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            old_date = new Date();
            location_shit(old_date, context);

            find_next_adan();
        }
    }

    private void location_shit(Date date, Context context) {
        if_theres_previous_info_load_it_n_display(date, context);

    }

    private void pull_date_and_shape_it(double longitude, double latitude, Date today) {
        coordinates = new Coordinates(latitude, longitude);
        date = DateComponents.from(today);
    }

    private void use(double longitude, double latitude, Date today) {
        prayers = new ArrayList<>();

        /*String[] temptoday = today.toString().split(" ");
        String todaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];
        */old_date = new Date();

        pull_date_and_shape_it(longitude, latitude, today);
        pull_prayer_times_and_shape_them();
        convert_prayertimes_into_milliseconds();
    }

    private void convert_prayertimes_into_milliseconds() {
        int fajrtemp = Integer.valueOf(fajr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(fajr.split(" ")[0].split(":")[1]);
        if(fajr.split(" ")[1].equals("PM"))
            fajrtemp += 720; //12*60
        int risetemp = Integer.valueOf(rise.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(rise.split(" ")[0].split(":")[1]);
        if(rise.split(" ")[1].equals("PM"))
            risetemp += 720; //12*60
        int dhuhrtemp = Integer.valueOf(dhuhr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(dhuhr.split(" ")[0].split(":")[1]);
        if(dhuhr.split(" ")[1].equals("PM") && !dhuhr.split(":")[0].equals("12"))
            dhuhrtemp += 720; //12*60
        int asrtemp = Integer.valueOf(asr.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(asr.split(" ")[0].split(":")[1]);
        if(asr.split(" ")[1].equals("PM"))
            asrtemp += 720; //12*60
        int maghribtemp = Integer.valueOf(maghrib.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(maghrib.split(" ")[0].split(":")[1]);
        if(maghrib.split(" ")[1].equals("PM"))
            maghribtemp += 720; //12*60
        int ishatemp = Integer.valueOf(isha.split(" ")[0].split(":")[0]) * 60 + Integer.valueOf(isha.split(" ")[0].split(":")[1]);
        if(isha.split(" ")[1].equals("PM"))
            ishatemp += 720; //12*60



        /*// TODO:  for testing purposes
        temptime = String.valueOf(old_date).split(" ")[3];
        rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 3600 + Integer.valueOf(temptime.split(":")[1]) * 60 + Integer.valueOf(temptime.split(":")[2]);
        fajrtemp = rightnowcomparable + 10;*/


        prayers.add(fajrtemp);
        prayers.add(risetemp);
        prayers.add(dhuhrtemp);
        prayers.add(asrtemp);
        prayers.add(maghribtemp);
        prayers.add(ishatemp);

        praytimesregularform = new ArrayList<>();

        if(fajr.split(" ")[1].equals("PM")) {
            if(Integer.valueOf(fajr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + fajr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(fajr.split(" ")[0].split(":")[0]) + 12) + ":" + fajr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(fajr.split(" ")[0]);

        if(rise.split(" ")[1].equals("PM")){
            if(Integer.valueOf(rise.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + rise.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(rise.split(" ")[0].split(":")[0])+12) + ":" + rise.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(rise.split(" ")[0]);

        if(dhuhr.split(" ")[1].equals("PM")){
            if(Integer.valueOf(dhuhr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + dhuhr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(dhuhr.split(" ")[0].split(":")[0])+12) + ":" + dhuhr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(dhuhr.split(" ")[0]);

        if(asr.split(" ")[1].equals("PM")){
            if(Integer.valueOf(asr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + asr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(asr.split(" ")[0].split(":")[0])+12) + ":" + asr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(asr.split(" ")[0]);

        if(maghrib.split(" ")[1].equals("PM")){
            if(Integer.valueOf(maghrib.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + asr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(maghrib.split(" ")[0].split(":")[0])+12) + ":" + maghrib.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(maghrib.split(" ")[0]);

        if(isha.split(" ")[1].equals("PM")){
            if(Integer.valueOf(maghrib.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + isha.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.valueOf(isha.split(" ")[0].split(":")[0])+12) + ":" + isha.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(isha.split(" ")[0]);
    }

    private void pull_prayer_times_and_shape_them() {
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, date, params);
        try {
            String timeshape = "hh:mm a";
            fajr = DateFormat.format(timeshape, new Date(prayerTimes.fajr.getTime())).toString();
            rise = DateFormat.format(timeshape, new Date(prayerTimes.sunrise.getTime())).toString();
            dhuhr = DateFormat.format(timeshape, new Date(prayerTimes.dhuhr.getTime())).toString();
            asr = DateFormat.format(timeshape, new Date(prayerTimes.asr.getTime())).toString();
            maghrib = DateFormat.format(timeshape, new Date(prayerTimes.maghrib.getTime())).toString();
            isha = DateFormat.format(timeshape, new Date(prayerTimes.isha.getTime())).toString();

        } catch(Exception ignored){}

    }

    private void if_theres_previous_info_load_it_n_display(Date date, Context context) {
        sql("force", context);
        SQLSharing.mycursorforceer.moveToFirst();
        double longitude = Double.valueOf(SQLSharing.mycursorforceer.getString(1));
        double latitude = Double.valueOf(SQLSharing.mycursorforceer.getString(2));
        use(longitude, latitude, date);
    }

    private void calculate_negatifise_and_positifise() {
        if (i != 0)
            negatifise = Math.round(abs((rightnowcomparable - prayers.get(i - 1))));
        else
            still_scoping_on_previous_adan = false;

        positifise = Math.round(abs((prayers.get(i) - rightnowcomparable)));
    }

    private void check_negatifise() {
        if(i!=0)
            still_scoping_on_previous_adan = negatifise <= SQLSharing.minute_limit_to_display_negatifise;
    }

    private boolean change_happened = true;
    private void if_change_needed_for_slider_apply_it() {
        int lol = -3;
        if(still_scoping_on_previous_adan)
            lol = i-1;
        else
            lol = i;
        hide_slider_for_given_slat_number(current_displayed_slider);
        if(still_scoping_on_previous_adan){
            change_happened = true;
            find_slider(i);
                widgetViews.setTextViewText(slider, "+ " + String.valueOf(negatifise));
                widgetViews.setViewVisibility(slider, View.VISIBLE);
        }
        else if(positifise<=SQLSharing.minute_limit_to_display_positifise){
            change_happened = true;
            find_slider(i);
            widgetViews.setTextViewText(slider, "- " + String.valueOf(positifise));
            widgetViews.setViewVisibility(slider, View.VISIBLE);
        } else {
            if(change_happened){ change_happened = false;
                hide_slider_for_given_slat_number(current_displayed_slider);
            }
        }

        //update_widget_ui();
    }

    private void hide_slider_for_given_slat_number(int lol) {
        switch(lol){
            case 0:
                widgetViews.setViewVisibility(R.id.widgetsliderfajr, View.INVISIBLE);
                break;
            case 1:
                widgetViews.setViewVisibility(R.id.widgetsliderrise, View.INVISIBLE);
                break;
            case 2:
                widgetViews.setViewVisibility(R.id.widgetsliderdhuhr, View.INVISIBLE);
                break;
            case 3:
                widgetViews.setViewVisibility(R.id.widgetsliderasr, View.INVISIBLE);
                break;
            case 4:
                widgetViews.setViewVisibility(R.id.widgetslidermaghreb, View.INVISIBLE);
                break;
            case 5:
                widgetViews.setViewVisibility(R.id.widgetsliderisha, View.INVISIBLE);
        }
    }

    private void find_slider(final int next_adaner) {
        int temp;
        if (still_scoping_on_previous_adan)
            temp = next_adaner - 1;
        else
            temp = next_adaner;
        switch (temp) {
            case -1:
                break;
            case 0:
            case 6:
                slider = R.id.widgetsliderfajr;
                break;
            case 1:
                slider = R.id.widgetsliderrise;
                break;
            case 2:
                slider = R.id.widgetsliderdhuhr;
                break;
            case 3:
                slider = R.id.widgetsliderasr;
                break;
            case 4:
                slider = R.id.widgetslidermaghreb;
                break;
            case 5:
                slider = R.id.widgetsliderisha;
                break;
        }
    }

    private void find_next_adan() {
        try {
            String temptime = String.valueOf(old_date).split(" ")[3];
            rightnowcomparable = Integer.valueOf(temptime.split(":")[0]) * 60 + Integer.valueOf(temptime.split(":")[1]);

            /*if(rightnowcomparable_old!=rightnowcomparable){
            }*/

            //if(rightnowcomparable_old!=rightnowcomparable)
            // displayed rightnowcomparable
            for (int j = 0; j < 6; j++) {
                if(rightnowcomparable<prayers.get(0)) {
                    i = 0;
                    break;
                }
                if (rightnowcomparable > prayers.get(j)){
                    i = j + 1;
                }
            }
            if(i>=6){
                i = 0;
                end_of_day = true;
            } else
                end_of_day = false;

        } catch(Exception ignored){}
    }

    private void sql(String table,  Context context) {
        SQLSharing.TABLE_NAME_INPUTER = table;
        switch (table) {
            case "slat":
                SQLSharing.mydbslater = new SQL(context);
                SQLSharing.mycursorslater = SQLSharing.mydbslater.getAllDateslat();
                break;
            case "force":
                SQLSharing.mydbforceer = new SQL(context);
                SQLSharing.mycursorforceer = SQLSharing.mydbforceer.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mydbforce3er = new SQL(context);
                SQLSharing.mycursorforce3er = SQLSharing.mydbforce3er.getAllDateforce3();
                break;
        }
    }

    private void close_sql() {
        if(SQLSharing.mydbforceer!=null)
            SQLSharing.mydbforceer.close();
        if(SQLSharing.mydbslater!=null)
            SQLSharing.mydbslater.close();
        if(SQLSharing.mydbforce3er!=null)
            SQLSharing.mydbforce3er.close();
    }
}

