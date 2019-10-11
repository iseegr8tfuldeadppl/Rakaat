package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.krimzon.scuffedbots.raka3at.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    public Dialog d;
    private Button yes;
    private TextView fixbig, fixsmall1, fixsmall2, fixsmall3;

    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customdialog);
        yes = (Button) findViewById(R.id.btn_yes);
        fixbig = findViewById(R.id.fixbig);
        fixsmall1 = findViewById(R.id.fixsmall1);
        fixsmall2 = findViewById(R.id.fixsmall2);
        fixsmall3 = findViewById(R.id.fixsmall3);

        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "Tajawal-Light.ttf");
        yes.setTypeface(custom_font);
        //fixbig.setTypeface(custom_font);
        fixsmall1.setTypeface(custom_font);
        fixsmall2.setTypeface(custom_font);
        fixsmall3.setTypeface(custom_font);

        yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                //c.finish();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}