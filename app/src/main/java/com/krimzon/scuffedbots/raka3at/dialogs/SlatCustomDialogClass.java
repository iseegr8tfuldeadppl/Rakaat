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
import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.slat;

public class SlatCustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    private Intent slatIntent;
    private Activity c;
    private boolean MainActivitys;
    private Button yes, never;
    private TextView fixbig, fixsmall1, fixsmall2, fixsmall3, fixsmall4;
    private String noshow;
    private String ok;
    private String ff;
    private String d;
    private String f;
    private String a;
    private String howtouse;
    private String language;

    public SlatCustomDialogClass(Activity a, boolean MainActivity, String language) {
        super(a);
        this.c = a;
        this.MainActivitys = MainActivity;
        this.language = language;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slatcustomdialog);
        yes = (Button) findViewById(R.id.btn_yes);
        never = (Button) findViewById(R.id.btn_never);
        fixbig = findViewById(R.id.fixbig);
        fixsmall1 = findViewById(R.id.fixsmall1);
        fixsmall2 = findViewById(R.id.fixsmall2);
        fixsmall3 = findViewById(R.id.fixsmall3);
        fixsmall4 = findViewById(R.id.fixsmall4);
        slatIntent = new Intent(this.c, slat.class);

        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "Tajawal-Light.ttf");
        yes.setTypeface(custom_font);
        fixsmall1.setTypeface(custom_font);
        fixsmall2.setTypeface(custom_font);
        fixsmall3.setTypeface(custom_font);
        never.setTypeface(custom_font);

        check_language();

        yes.setOnClickListener(this);
        never.setOnClickListener(this);
    }

    private void check_language() {
        getStrings();
        if(language.equals("en"))
            english();
    }

    private void getStrings(){
        Resources resources = this.c.getResources();
        if(language.equals("en")){
            howtouse = resources.getString(R.string.howtouse);
            a = resources.getString(R.string.a);
            f = resources.getString(R.string.f);
            d = resources.getString(R.string.d);
            ff = resources.getString(R.string.ff);
            ok = resources.getString(R.string.ok);
            noshow = resources.getString(R.string.noshow);
        }
    }

    private void english(){
        fixbig.setText(howtouse);
        fixsmall1.setText(a);
        fixsmall2.setText(f);
        fixsmall3.setText(d);
        fixsmall4.setText(ff);
        yes.setText(ok);
        never.setText(noshow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                //c.finish();
                if(this.MainActivitys){
                    this.c.startActivity(slatIntent);
                    this.c.finish();
                }
                dismiss();
                break;
            case R.id.btn_never:
                SQLSharing.mydb.updateData("1", "1");
                if(this.MainActivitys){
                    this.c.startActivity(slatIntent);
                    this.c.finish();
                }
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}