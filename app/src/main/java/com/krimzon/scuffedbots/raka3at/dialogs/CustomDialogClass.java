package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.krimzon.scuffedbots.raka3at.R;
import com.krimzon.scuffedbots.raka3at.kibla;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    protected Activity c;
    public Dialog d;
    protected Button yes;
    protected TextView fixbig, fixsmall1, fixsmall2, fixsmall3;

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

        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "Tajawal-Medium.ttf");
        yes.setTypeface(custom_font);
        fixbig.setTypeface(custom_font);
        fixsmall1.setTypeface(custom_font);
        fixsmall2.setTypeface(custom_font);
        fixsmall3.setTypeface(custom_font);

        yes.setOnClickListener(this);

        if(kibla.language.equals("en"))
            english();
    }

    private void english() {
        Resources resources = c.getResources();
        String lmfao = resources.getString(R.string.lmfao);
        String lol = resources.getString(R.string.lol);
        String yesoyes = resources.getString(R.string.yesoyes);
        String cbon = resources.getString(R.string.cbon);
        String done = resources.getString(R.string.done);

        fixbig.setText(lmfao);
        fixsmall1.setText(lol);
        fixsmall2.setText(yesoyes);
        fixsmall3.setText(cbon);
        yes.setText(done);
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