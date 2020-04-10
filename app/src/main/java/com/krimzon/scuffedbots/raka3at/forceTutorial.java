package com.krimzon.scuffedbots.raka3at;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;

public class forceTutorial extends AppCompatActivity {

    private int page = 1;
    private ImageView display1, display4;
    private LinearLayout explanation1holder, explanation4holder;
    private FrameLayout fourthpagebackground;
    private TextView next, previous;
    private String language;

    private void sql() {
        SQLSharing.TABLE_NAME_INPUTER = "slat";
        SQLSharing.mydbslat = SQL.getInstance(this);
        SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_tutorial);

        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);

        ImageView arrowleft = findViewById(R.id.arrowleft);
        ImageView arrowright = findViewById(R.id.arrowright);

        TextView explanation1 = findViewById(R.id.explanation1);
        TextView explanation4 = findViewById(R.id.explanation4);

        fourthpagebackground = findViewById(R.id.fourthpagebackground);

        explanation1holder = findViewById(R.id.explanation1holder);
        display1 = findViewById(R.id.display1);
        explanation4holder = findViewById(R.id.explanation4holder);
        display4 = findViewById(R.id.display4);

        Typeface arabic_typeface2 = Typeface.createFromAsset(getAssets(), "Tajawal-Regular.ttf");
        explanation1.setTypeface(arabic_typeface2);
        explanation4.setTypeface(arabic_typeface2);

        next.setTypeface(arabic_typeface2);
        previous.setTypeface(arabic_typeface2);

        sql();
        SQLSharing.mycursorslat.moveToPosition(6);
        language = SQLSharing.mycursorslat.getString(1);
        close_sql();

        if(language.equals("ar")){
            explanation1.setText(getResources().getString(R.string.explanation1force_arabe));
            explanation4.setText(getResources().getString(R.string.explanation4force_arabe));
            previous.setText(getResources().getString(R.string.back_arabe));
            next.setText(getResources().getString(R.string.next_arabe));
        }

            arrowright.setImageDrawable(getResources().getDrawable(R.drawable.arrowright));

            arrowleft.setImageDrawable(getResources().getDrawable(R.drawable.arrowleft));

        try {
            Glide.with(this).load(R.drawable.one).into(display1);
        } catch (Exception ignored) {
            display1.setImageDrawable(getResources().getDrawable(R.drawable.one)); }
        try {
            Glide.with(this).load(R.drawable.onea).into(display4);
        } catch (Exception ignored) {
            display4.setImageDrawable(getResources().getDrawable(R.drawable.onea)); }

        mapActivity();
    }

    private void close_sql() {
        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    private void mapActivity() {
        Intent dd = new Intent(this, MapActivity.class);
        startActivity(dd);
    }

    public void nextClicked(View view) {
        int totalpages = 2;
        if(page!= totalpages && !clicked) {
            page++;

            if(language.equals("ar"))
                previous.setText(getResources().getString(R.string.previous_arabe));
            else if(language.equals("en"))
                previous.setText(getResources().getString(R.string.previous));

            if(language.equals("ar"))
                next.setText(getResources().getString(R.string.enter_arabe));
            else if(language.equals("en"))
                next.setText(getResources().getString(R.string.enter));
            clicked = true;
            slide_page_one_out_to_left();
            slide_page_four_in_from_right();
        } else if(page== totalpages){
            sql();
            SQLSharing.mycursorslat.moveToPosition(11);
            SQLSharing.mydbslat.updateData("1", SQLSharing.mycursorslat.getString(0));
            close_sql();


            Intent forceer = new Intent(this, com.krimzon.scuffedbots.raka3at.force.class);
            startActivity(forceer);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent MainActivityer = new Intent(this, com.krimzon.scuffedbots.raka3at.MainActivity.class);
        startActivity(MainActivityer);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    public void previousClicked(View view) {
        if(page!=1 && !clicked) {
            page--;

            clicked = true;
            if(language.equals("ar")) {
                next.setText(getResources().getString(R.string.next_arabe));
                previous.setText(getResources().getString(R.string.back_arabe));
            } else if(language.equals("en")) {
                next.setText(getResources().getString(R.string.next));
                previous.setText(getResources().getString(R.string.back));
            }

            slide_page_four_out_to_right();
            slide_page_one_in_from_left();
        } else if(page==1){
            Intent MainActivityer = new Intent(this, com.krimzon.scuffedbots.raka3at.MainActivity.class);
            startActivity(MainActivityer);
            finish();
        }
    }


    private boolean clicked = false;
    private void slide_page_one_in_from_left() {
        Animation slide_in_from_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tutorial_slide_in_from_left);
        Animation slide_in_from_left2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tutorial_slide_in_from_left);
        explanation1holder.startAnimation(slide_in_from_left);
        slide_in_from_left.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            explanation1holder.setVisibility(View.VISIBLE);
            clicked = false;

            Animation fade_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            fourthpagebackground.startAnimation(fade_out);
            fade_out.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                fourthpagebackground.setVisibility(View.INVISIBLE);
            }});
        }});
        display1.startAnimation(slide_in_from_left2);
        slide_in_from_left2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            display1.setVisibility(View.VISIBLE);
            clicked = false;
        }});
    }
    private void slide_page_one_out_to_left() {
        Animation slide_out_to_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tutorial_slide_out_to_left);
        Animation slide_out_to_left2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tutorial_slide_out_to_left);
        explanation1holder.startAnimation(slide_out_to_left);
        slide_out_to_left.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            explanation1holder.setVisibility(View.INVISIBLE);
        }});
        display1.startAnimation(slide_out_to_left2);
        slide_out_to_left2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            display1.setVisibility(View.INVISIBLE);
        }});
    }

    private void slide_page_four_in_from_right() {
        Animation slide_in_from_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tutorial_slide_in_from_right);
        Animation slide_in_from_right2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tutorial_slide_in_from_right);
        explanation4holder.startAnimation(slide_in_from_right);
        slide_in_from_right.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            explanation4holder.setVisibility(View.VISIBLE);
            clicked = false;

            Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            fourthpagebackground.startAnimation(fade_in);
            fade_in.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                fourthpagebackground.setVisibility(View.VISIBLE);
            }});
        }});
        display4.startAnimation(slide_in_from_right2);
        slide_in_from_right2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            display4.setVisibility(View.VISIBLE);
            clicked = false;
        }});
    }
    private void slide_page_four_out_to_right() {
        Animation slide_out_from_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tutorial_slide_out_from_right);
        Animation slide_out_from_right2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tutorial_slide_out_from_right);
        explanation4holder.startAnimation(slide_out_from_right);
        slide_out_from_right.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            explanation4holder.setVisibility(View.INVISIBLE);
        }});
        display4.startAnimation(slide_out_from_right2);
        slide_out_from_right2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            display4.setVisibility(View.INVISIBLE);
        }});
    }

}
