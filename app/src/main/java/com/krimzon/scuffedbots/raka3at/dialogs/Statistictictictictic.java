package com.krimzon.scuffedbots.raka3at.dialogs;

        import android.app.Activity;
        import android.app.Dialog;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.view.View;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.TextView;
        import com.krimzon.scuffedbots.raka3at.MainActivity;
        import com.krimzon.scuffedbots.raka3at.R;
        import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
        import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
        import com.krimzon.scuffedbots.raka3at.slat;

        import java.util.ArrayList;
        import java.util.List;

public class Statistictictictictic extends Dialog implements
        android.view.View.OnClickListener {

    private Intent slatIntent;
    public Activity c;
    private boolean MainActivitys;
    public Dialog dialog;
    public Button yes, never;
    public TextView fixbig, fixsmall1, fixsmall2, fixsmall3, fixsmall4;
    private String noshow;
    private String ok;
    private String ff;
    private String d;
    private String f;
    private String a;
    private String howtouse;

    public Statistictictictictic(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.statistictictictictic);
        display_of_prayed_prayers_of_all_time = findViewById(R.id.prayed_prayers_of_all_time);
        title_prayed_prayers_of_all_time = findViewById(R.id.title_prayed_prayers_of_all_time);
        statstitle = findViewById(R.id.statstitle);
        arabic_typeface = Typeface.createFromAsset(c.getAssets(),  "Tajawal-Light.ttf");

        statstitle.setTypeface(arabic_typeface);
        title_prayed_prayers_of_all_time.setTypeface(arabic_typeface);
        display_of_prayed_prayers_of_all_time.setTypeface(arabic_typeface);

        sql("force2");
        count_prayed_prayers_of_all_time();
        display_of_prayed_prayers_of_all_time.setText(String.valueOf(prayed_prayers_of_all_time));

        /*yes.setOnClickListener(this);
        never.setOnClickListener(this);*/
    }

    private Typeface arabic_typeface;
    private TextView display_of_prayed_prayers_of_all_time;
    private TextView statstitle;
    private TextView title_prayed_prayers_of_all_time;
    private int prayed_prayers_of_all_time = 0;
    private String temper;
    private void count_prayed_prayers_of_all_time() {
        if(SQLSharing.mycursor.getCount()>0){
                while(SQLSharing.mycursor.moveToNext()) {
                        temper = SQLSharing.mycursor.getString(2);
                        for(int i=0;i<temper.length();i++)
                            if(String.valueOf(temper.charAt(i)).equals("1"))
                                prayed_prayers_of_all_time += 1;
                }
        } else
            prayed_prayers_of_all_time = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                break;
            case R.id.btn_never:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}