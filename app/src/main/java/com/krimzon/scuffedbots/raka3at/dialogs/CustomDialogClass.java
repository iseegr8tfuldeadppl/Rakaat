package com.krimzon.scuffedbots.raka3at.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.krimzon.scuffedbots.raka3at.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private Button yes;
    private TextView fixbig, fixsmall1, fixsmall2, fixsmall3;
    private String language;
    private ImageView imageView;

    public CustomDialogClass(Activity a, String language) {
        super(a);
        this.c = a;
        this.language = language;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customdialog);
        yes = findViewById(R.id.btn_yes);
        fixbig = findViewById(R.id.fixbig);
        fixsmall1 = findViewById(R.id.fixsmall1);
        fixsmall2 = findViewById(R.id.fixsmall2);
        fixsmall3 = findViewById(R.id.fixsmall3);
        imageView = findViewById(R.id.imageView);

        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "Tajawal-Medium.ttf");
        yes.setTypeface(custom_font);
        fixbig.setTypeface(custom_font);
        fixsmall1.setTypeface(custom_font);
        fixsmall2.setTypeface(custom_font);
        fixsmall3.setTypeface(custom_font);


        try {
            Glide.with(c.getApplicationContext()).load(R.drawable.orientation).into(imageView);
        } catch (Exception ignored) {
            imageView.setImageDrawable(c.getApplicationContext().getResources().getDrawable(R.drawable.orientation));
        }

        yes.setOnClickListener(this);

        if(language.equals("en"))
            english();
    }

    private void english() {
        String lmfao = c.getApplicationContext().getResources().getString(R.string.lmfao);
        String lol = c.getApplicationContext().getResources().getString(R.string.lol);
        String yesoyes = c.getApplicationContext().getResources().getString(R.string.yesoyes);
        String cbon = c.getApplicationContext().getResources().getString(R.string.cbon);
        String done = c.getApplicationContext().getResources().getString(R.string.done);

        fixbig.setText(lmfao);
        fixsmall1.setText(lol);
        fixsmall2.setText(yesoyes);
        fixsmall3.setText(cbon);
        yes.setText(done);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {//c.finish();
            dismiss();
        }
        dismiss();
    }
}