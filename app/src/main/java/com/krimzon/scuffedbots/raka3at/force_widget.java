package com.krimzon.scuffedbots.raka3at;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
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

import net.time4j.PlainDate;
import net.time4j.calendar.HijriCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.abs;

public class force_widget extends AppWidgetProvider {

    private CalculationParameters params;
    private int i = 0;
    private Date old_date;
    private Coordinates coordinates;
    private DateComponents date;
    private List<Integer> prayers;
    private String fajr;
    private String rise;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;
    private List<String> praytimesregularform;
    private int positifise=0;
    private int negatifise = 0;
    private boolean still_scoping_on_previous_adan = false;
    private int slider = 0;
    private int rightnowcomparable = 1;
    private RemoteViews widgetViews;
    private boolean change_happened = true;

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
                boolean darkmode = SQLSharing.mycursorslater.getString(1).equals("yes");
                SQLSharing.mycursorslater.moveToPosition(6);
                String language = SQLSharing.mycursorslater.getString(1);
                SQLSharing.mycursorslater.moveToPosition(10);
                close_sql();

                launch_prayer_processing(context);

                if(language.equals("en")){
                    if(darkmode)
                        widgetViews = new RemoteViews(context.getPackageName(), R.layout.force_widget);
                    else
                        widgetViews = new RemoteViews(context.getPackageName(), R.layout.force_widget_lightmode); // TODO make a light mode
                } else if(language.equals("ar")){
                    if(darkmode)
                        widgetViews = new RemoteViews(context.getPackageName(), R.layout.force_widget_gravity);
                    else
                        widgetViews = new RemoteViews(context.getPackageName(), R.layout.force_widget_lightmode_gravity); // TODO make a light mode
                }

                Intent launchingforce = new Intent(context, force.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchingforce, 0);
                widgetViews.setOnClickPendingIntent(R.id.main, pendingIntent);

                prepare_and_apply_hijri_date(language, context);
                widgetViews.setTextViewText(R.id.hijridisplay, hijri);
                widgetViews.setTextViewText(R.id.miladidisplay, datin);

                widgetViews.setImageViewResource(R.id.widgetfajrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetrisearrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetdhuhrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetasrarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetmaghrebarrow, R.drawable.arrowdown);
                widgetViews.setImageViewResource(R.id.widgetishaarrow, R.drawable.arrowdown);

                if(language.equals("en")){
                    widgetViews.setTextViewText(R.id.widgetfajrtitle, context.getResources().getString(R.string.fajrtitle));
                    widgetViews.setTextViewText(R.id.widgetrisetitle, context.getResources().getString(R.string.rise));
                    if(friday)
                        widgetViews.setTextViewText(R.id.widgetdhuhrtitle, context.getResources().getString(R.string.Jamo3a));
                    else
                        widgetViews.setTextViewText(R.id.widgetdhuhrtitle, context.getResources().getString(R.string.dohrtitle));
                    widgetViews.setTextViewText(R.id.widgetasrtitle, context.getResources().getString(R.string.asrtitle));
                    widgetViews.setTextViewText(R.id.widgetmaghribtitle, context.getResources().getString(R.string.maghrebtitle));
                    widgetViews.setTextViewText(R.id.widgetishatitle, context.getResources().getString(R.string.ishatitle));
                } else if(language.equals("ar")){
                    if(friday)
                        widgetViews.setTextViewText(R.id.widgetdhuhrtitle, context.getResources().getString(R.string.friarabe));
                    else
                        widgetViews.setTextViewText(R.id.widgetdhuhrtitle, context.getResources().getString(R.string.dohrtitle_arabe));
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

    private int hijri_month = 0, hijri_year = 0, hijri_day = 0;
    private int miladi_month = 0;
    private String hijri = "";
    private boolean friday = false;
    private String[] temptoday;
    private String todaycomparable = "";
    private void prepare_and_apply_hijri_date(String language, Context context) {
        Calendar cal = Calendar.getInstance(Locale.US);
        Date today = new Date(cal.getTimeInMillis());
        temptoday = today.toString().split(" ");
        todaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];
        work_on_date(language, context);
    }

    private String datin = "";
    private void work_on_date(String language, Context context) {
        datin = "";
        String tempdatin;
        if(language.equals(context.getResources().getString(R.string.en))) {
            tempdatin = temptoday[0];


            if (tempdatin.equals(context.getResources().getString(R.string.satu))) {
                datin += context.getResources().getString(R.string.sat);
                friday = false;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.sunu))) {
                datin += context.getResources().getString(R.string.sun);
                friday = false;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.monu))) {
                datin += context.getResources().getString(R.string.mon);
                friday = false;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.tueu))) {
                datin += context.getResources().getString(R.string.tue);
                friday = false;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.wedu))) {
                datin += context.getResources().getString(R.string.wed);
                friday = false;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.thuru))) {
                datin += context.getResources().getString(R.string.thu);
                friday = false;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.fridu))) {
                datin += context.getResources().getString(R.string.fri);
                friday = true;
            }


            // add week day to hijri date
            //hijri += datin + " ";

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals(context.getResources().getString(R.string.jan))) {
                datin += context.getResources().getString(R.string.january);
                miladi_month = 1;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.feb))) {
                datin += context.getResources().getString(R.string.february);
                miladi_month = 2;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.mar))) {
                datin += context.getResources().getString(R.string.march);
                miladi_month = 3;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.apr))) {
                datin += context.getResources().getString(R.string.april);
                miladi_month = 4;
            }
            else if (tempdatin.contains(context.getResources().getString(R.string.mao))) {
                datin += context.getResources().getString(R.string.may);
                miladi_month = 5;
            }
            else if (tempdatin.contains(context.getResources().getString(R.string.june))) {
                datin += context.getResources().getString(R.string.junee);
                miladi_month = 6;
            }
            else if (tempdatin.contains(context.getResources().getString(R.string.july))) {
                datin += context.getResources().getString(R.string.julyy);

                miladi_month = 7;
            }
            else if (tempdatin.equals(context.getResources().getString(R.string.aug))) {
                miladi_month = 8;
                datin += context.getResources().getString(R.string.august);
            }
            else if (tempdatin.equals("Sep")) {
                miladi_month = 9;
                datin += context.getResources().getString(R.string.september);
            }
            else if (tempdatin.equals("Oct")) {
                miladi_month = 10;
                datin += context.getResources().getString(R.string.october);
            }
            else if (tempdatin.equals("Nov")) {
                miladi_month = 11;
                datin += context.getResources().getString(R.string.november);
            }
            else if (tempdatin.equals("Dec")) {
                miladi_month = 12;
                datin += context.getResources().getString(R.string.december);
            }

            tempdatin = temptoday[2];
            int temper = Integer.parseInt(tempdatin);
            datin += " " + temper;
            if (temper==2 || temper==22)
                datin += context.getResources().getString(R.string.nd);
            else if (temper==3 || temper==23)
                datin += context.getResources().getString(R.string.rd);
            else if (temper==1 || temper==21)
                datin += context.getResources().getString(R.string.st);
            else
                datin += context.getResources().getString(R.string.th);

        } else if(language.equals(context.getResources().getString(R.string.ar))){
            tempdatin = temptoday[0];


            switch (tempdatin) {
                case "Sat":
                    datin += context.getResources().getString(R.string.satarabe);
                    friday = false;
                    break;
                case "Sun":
                    datin += context.getResources().getString(R.string.sunarabe);
                    friday = false;
                    break;
                case "Mon":
                    datin += context.getResources().getString(R.string.monarabe);
                    friday = false;

                    break;
                case "Tue":
                    datin += context.getResources().getString(R.string.tuearabe);
                    friday = false;
                    break;
                case "Wed":
                    datin += context.getResources().getString(R.string.wedarabe);
                    friday = false;
                    break;
                case "Thu":
                    datin += context.getResources().getString(R.string.thurarabe);
                    friday = false;
                    break;
                case "Fri":
                    datin += context.getResources().getString(R.string.friarabe);
                    friday = true;
                    break;
            }

            //hijri += datin + " ";

            tempdatin = temptoday[2];
            int temper = Integer.parseInt(tempdatin);
            datin += " " + temper;

            datin += " ";
            tempdatin = temptoday[1];
            if (tempdatin.equals("Jan")) {
                miladi_month = 1;
                datin += context.getResources().getString(R.string.janarabe);
            }
            else if (tempdatin.equals("Feb")) {
                miladi_month = 2;
                datin += context.getResources().getString(R.string.febarabe);
            }
            else if (tempdatin.equals("Mar")) {
                miladi_month = 3;
                datin += context.getResources().getString(R.string.mararabe);
            }
            else if (tempdatin.equals("Apr")) {
                miladi_month = 4;
                datin += context.getResources().getString(R.string.aprarabe);
            }
            else if (tempdatin.contains("Ma")) {
                miladi_month = 5;
                datin += context.getResources().getString(R.string.maarabe);
            }
            else if (tempdatin.contains("Jun")) {
                miladi_month = 6;
                datin += context.getResources().getString(R.string.junarabe);
            }
            else if (tempdatin.contains("Jul")) {
                miladi_month = 7;
                datin += context.getResources().getString(R.string.jularabe);
            }
            else if (tempdatin.equals("Aug")) {
                miladi_month = 8;
                datin += context.getResources().getString(R.string.augusarabe);
            }
            else if (tempdatin.equals("Sep")) {
                miladi_month = 9;
                datin += context.getResources().getString(R.string.separabe);
            }
            else if (tempdatin.equals("Oct")) {
                miladi_month = 10;
                datin += context.getResources().getString(R.string.octarabe);
            }
            else if (tempdatin.equals("Nov")) {
                miladi_month = 11;
                datin += context.getResources().getString(R.string.novarabe);
            }
            else if (tempdatin.equals("Dec")) {
                miladi_month = 12;
                datin += context.getResources().getString(R.string.decarabe);
            }

        }
        datin += " " + temptoday[5];


        hijri_date_setup(language, context);
    }

    private void hijri_date_setup(String language, Context context) {
        if(miladi_month!=0) {
            String[] lel = todaycomparable.split(" ");
            String[] t = PlainDate.of(Integer.parseInt(temptoday[5]), miladi_month, Integer.parseInt(lel[1])) // TODO: fix me Integer.parseInt(lel[5]) + 2000
                    .transform(HijriCalendar.class, HijriCalendar.VARIANT_UMALQURA).toString().split("-");
            t[3] = t[3].replace("[islamic", "");
            hijri_year = Integer.parseInt(t[1]);
            hijri_month = Integer.parseInt(t[2]);
            hijri_day = Integer.parseInt(t[3]);
            convert_hijri_to_cute(language, context);
        }
    }

    private void convert_hijri_to_cute(String language, Context context) {
        if(language.equals(context.getResources().getString(R.string.ar))){
            hijri = "";
            hijri += hijri_day + " ";
            switch(hijri_month) {
                case 1:
                    hijri += context.getResources().getString(R.string.muharram_arabe);
                    break;
                case 2:
                    hijri += context.getResources().getString(R.string.safar_arabe);
                    break;
                case 3:
                    hijri += context.getResources().getString(R.string.rabialawwal_arabe);
                    break;
                case 4:
                    hijri += context.getResources().getString(R.string.rabialthani_arabe);
                    break;
                case 5:
                    hijri += context.getResources().getString(R.string.jumadialawwal_arabe);
                    break;
                case 6:
                    hijri += context.getResources().getString(R.string.jumadialthani_arabe);
                    break;
                case 7:
                    hijri += context.getResources().getString(R.string.rajab_arabe);
                    break;
                case 8:
                    hijri += context.getResources().getString(R.string.chaaban_arabe);
                    break;
                case 9:
                    hijri += context.getResources().getString(R.string.ramadhan_arabe);
                    break;
                case 10:
                    hijri += context.getResources().getString(R.string.shawwal_arabe);
                    break;
                case 11:
                    hijri += context.getResources().getString(R.string.dhualqaada_arabe);
                    break;
                case 12:
                    hijri += context.getResources().getString(R.string.dhualhijja_arabe);
                    break;
            }

            hijri += " " + hijri_year;
        } else {
            hijri = "";
            switch(hijri_month) {
                case 1:
                    hijri += context.getResources().getString(R.string.muharram);
                    break;
                case 2:
                    hijri += context.getResources().getString(R.string.safar);
                    break;
                case 3:
                    hijri += context.getResources().getString(R.string.rabialawwal);
                    break;
                case 4:
                    hijri += context.getResources().getString(R.string.rabialthani);
                    break;
                case 5:
                    hijri += context.getResources().getString(R.string.jumadialawwal);
                    break;
                case 6:
                    hijri += context.getResources().getString(R.string.jumadialthani);
                    break;
                case 7:
                    hijri += context.getResources().getString(R.string.rajab);
                    break;
                case 8:
                    hijri += context.getResources().getString(R.string.chaaban);
                    break;
                case 9:
                    hijri += context.getResources().getString(R.string.ramadhan);
                    break;
                case 10:
                    hijri += context.getResources().getString(R.string.shawwal);
                    break;
                case 11:
                    hijri += context.getResources().getString(R.string.dhualqaada);
                    break;
                case 12:
                    hijri += context.getResources().getString(R.string.dhualhijja);
                    break;
            }

            hijri += " " + hijri_day;
            if (hijri_day==2 || hijri_day==22)
                hijri += "nd";
            else if (hijri_day==3 || hijri_day==23)
                hijri += "rd";
            else if (hijri_day==1 || hijri_day==21)
                hijri += "st";
            else
                hijri += "th";

            hijri += " " + hijri_year;
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

            Calendar cal = Calendar.getInstance(Locale.US);
            old_date = new Date(cal.getTimeInMillis());
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

    private void use(double longitude, double latitude, Date today, Context context) {
        prayers = new ArrayList<>();

        /*String[] temptoday = today.toString().split(" ");
        String todaycomparable = temptoday[1] + " " + temptoday[2] + " " + temptoday[5];
        */
        Calendar cal = Calendar.getInstance(Locale.US);
        old_date = new Date(cal.getTimeInMillis());

        pull_date_and_shape_it(longitude, latitude, today);
        pull_prayer_times_and_shape_them();
        convert_prayertimes_into_milliseconds(context);
    }

    private void convert_prayertimes_into_milliseconds(Context context) {
        String pm = context.getResources().getString(R.string.pm);

        int fajrtemp = Integer.parseInt(fajr.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(fajr.split(" ")[0].split(":")[1]);
        if(fajr.split(" ")[1].equals(context.getResources().getString(R.string.pmer))|| fajr.split(" ")[1].equals(pm))
            fajrtemp += 720; //12*60
        int risetemp = Integer.parseInt(rise.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(rise.split(" ")[0].split(":")[1]);
        if(rise.split(" ")[1].equals("PM") || rise.split(" ")[1].equals(pm))
            risetemp += 720; //12*60
        //Integer risetemp = Integer.parseInt(rise.split(" ")[0].split(":")[0])*3600 + Integer.parseInt(rise.split(" ")[0].split(":")[1])*60;
        int dhuhrtemp = Integer.parseInt(dhuhr.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(dhuhr.split(" ")[0].split(":")[1]);
        if((dhuhr.split(" ")[1].equals(context.getResources().getString(R.string.pmer)) || dhuhr.split(" ")[1].equals(pm)) && !dhuhr.split(":")[0].equals("12"))
            dhuhrtemp += 720; //12*60
        int asrtemp = Integer.parseInt(asr.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(asr.split(" ")[0].split(":")[1]);
        if(asr.split(" ")[1].equals(context.getResources().getString(R.string.pmer)) || asr.split(" ")[1].equals(pm))
            asrtemp += 720; //12*60
        int maghribtemp = Integer.parseInt(maghrib.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(maghrib.split(" ")[0].split(":")[1]);
        if(maghrib.split(" ")[1].equals(context.getResources().getString(R.string.pmer)) || maghrib.split(" ")[1].equals(pm))
            maghribtemp += 720; //12*60
        int ishatemp = Integer.parseInt(isha.split(" ")[0].split(":")[0]) * 60 + Integer.parseInt(isha.split(" ")[0].split(":")[1]);
        if(isha.split(" ")[1].equals(context.getResources().getString(R.string.pmer)) || isha.split(" ")[1].equals(pm))
            ishatemp += 720; //12*60



        /*// TODO:  for testing purposes
        temptime = String.valueOf(old_date).split(" ")[3];
        rightnowcomparable = Integer.parseInt(temptime.split(":")[0]) * 3600 + Integer.parseInt(temptime.split(":")[1]) * 60 + Integer.parseInt(temptime.split(":")[2]);
        fajrtemp = rightnowcomparable + 10;*/


        prayers.add(fajrtemp);
        prayers.add(risetemp);
        prayers.add(dhuhrtemp);
        prayers.add(asrtemp);
        prayers.add(maghribtemp);
        prayers.add(ishatemp);

        praytimesregularform = new ArrayList<>();

        if(fajr.split(" ")[1].equals("PM")) {
            if(Integer.parseInt(fajr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + fajr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.parseInt(fajr.split(" ")[0].split(":")[0]) + 12) + ":" + fajr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(fajr.split(" ")[0]);

        if(rise.split(" ")[1].equals("PM")){
            if(Integer.parseInt(rise.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + rise.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.parseInt(rise.split(" ")[0].split(":")[0])+12) + ":" + rise.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(rise.split(" ")[0]);

        if(dhuhr.split(" ")[1].equals("PM")){
            if(Integer.parseInt(dhuhr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + dhuhr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.parseInt(dhuhr.split(" ")[0].split(":")[0])+12) + ":" + dhuhr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(dhuhr.split(" ")[0]);

        if(asr.split(" ")[1].equals("PM")){
            if(Integer.parseInt(asr.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + asr.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.parseInt(asr.split(" ")[0].split(":")[0])+12) + ":" + asr.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(asr.split(" ")[0]);

        if(maghrib.split(" ")[1].equals("PM")){
            if(Integer.parseInt(maghrib.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + maghrib.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.parseInt(maghrib.split(" ")[0].split(":")[0])+12) + ":" + maghrib.split(" ")[0].split(":")[1]);
        } else
            praytimesregularform.add(maghrib.split(" ")[0]);

        if(isha.split(" ")[1].equals("PM")){
            if(Integer.parseInt(isha.split(" ")[0].split(":")[0])==12)
                praytimesregularform.add("00" + ":" + isha.split(" ")[0].split(":")[1]);
            else
                praytimesregularform.add(String.valueOf(Integer.parseInt(isha.split(" ")[0].split(":")[0])+12) + ":" + isha.split(" ")[0].split(":")[1]);
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
        double longitude = Double.parseDouble(SQLSharing.mycursorforceer.getString(1));
        double latitude = Double.parseDouble(SQLSharing.mycursorforceer.getString(2));
        use(longitude, latitude, date, context);
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
            still_scoping_on_previous_adan = negatifise < SQLSharing.minute_limit_to_display_negatifise;
    }

    private void if_change_needed_for_slider_apply_it() {
        int lol = -3;
        if(still_scoping_on_previous_adan)
            lol = i-1;
        else
            lol = i;
        hide_all_sliders_fuck_it();
        if(still_scoping_on_previous_adan){
            change_happened = true;
            find_slider(lol);
            widgetViews.setTextViewText(slider, "+ " + String.valueOf(negatifise));
            widgetViews.setViewVisibility(slider, View.VISIBLE);
        }
        else if(positifise<SQLSharing.minute_limit_to_display_positifise){
            change_happened = true;
            find_slider(lol);
            widgetViews.setTextViewText(slider, "- " + String.valueOf(positifise));
            widgetViews.setViewVisibility(slider, View.VISIBLE);
        } else {
            if(change_happened){ change_happened = false;
                hide_all_sliders_fuck_it();
            }
        }

        //update_widget_ui();
    }

    private void hide_all_sliders_fuck_it() {
        widgetViews.setViewVisibility(R.id.widgetsliderfajr, View.INVISIBLE);
        widgetViews.setViewVisibility(R.id.widgetsliderrise, View.INVISIBLE);
        widgetViews.setViewVisibility(R.id.widgetsliderdhuhr, View.INVISIBLE);
        widgetViews.setViewVisibility(R.id.widgetsliderasr, View.INVISIBLE);
        widgetViews.setViewVisibility(R.id.widgetslidermaghreb, View.INVISIBLE);
        widgetViews.setViewVisibility(R.id.widgetsliderisha, View.INVISIBLE);
    }

    private void find_slider(final int next_adaner) {
        switch (next_adaner) {
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
            rightnowcomparable = Integer.parseInt(temptime.split(":")[0]) * 60 + Integer.parseInt(temptime.split(":")[1]);

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
            boolean end_of_day = false;
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

