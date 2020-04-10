package com.krimzon.scuffedbots.raka3at.SQLite;

import android.database.Cursor;

public class SQLSharing {
    public static SQL mydbslat;
    public static SQL mydbforce;
    public static SQL mydbforce3;
    public static Cursor mycursorslat;
    public static Cursor mycursorforce;
    public static Cursor mycursorforce3;

    public static SQL servicemydbslat;
    public static SQL servicemydbforce;
    public static SQL servicemydbforce3;
    public static Cursor servicemycursorslat;
    public static Cursor servicemycursorforce;
    public static Cursor servicemycursorforce3;

    // for widget
    public static SQL mydbslater;
    public static SQL mydbforceer;
    public static SQL mydbforce3er;
    public static Cursor mycursorslater;
    public static Cursor mycursorforceer;
    public static Cursor mycursorforce3er;

    public static String TABLE_NAME_INPUTER;

    public static int params_adjustments_fajr = 0;
    public static double fajrangle = 18.1; // correct calculation for algeria
    public static double ishaangle = 17.1; // 17.2 for dar el beida

    public static int minute_limit_to_display_positifise = 100;
    public static int minute_limit_to_display_negatifise = 30;
}