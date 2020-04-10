package com.krimzon.scuffedbots.raka3at;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.krimzon.scuffedbots.raka3at.SQLite.SQL;
import com.krimzon.scuffedbots.raka3at.SQLite.SQLSharing;
import com.krimzon.scuffedbots.raka3at.background.ProcessMainClass;
import com.krimzon.scuffedbots.raka3at.background.restarter.RestartServiceBroadcastReceiver;
import com.krimzon.scuffedbots.raka3at.dialogs.protected_apps_request;
import java.util.ArrayList;
import java.util.List;

public class force_settings extends AppCompatActivity {

    private Button arrow;
    private View v;
    private String language = "ar", ID = "", adanSelections = "";
    private boolean darkmode = true, once2 = true, once = true;
    private String[] selections;
    private TextView adantitle;
    private List<TextView> adans, titles;
    private List<ImageView> ringmodes;
    private List<FrameLayout> ringmodesbackground;

    private void print(Object dumps) {
        Toast.makeText(this, String.valueOf(dumps), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.force_settings);

        prepare_variables();
        applyfont();
        sql_work();
        apply_selected_language();
        apply_previous_settings();
        apply_adanSelections();
        apply_previous_delays();
        apply_enabled_and_disabled_on_delays();
        onClickListeners();
        plusminusonclicklisteners();
        dark_light_mode();
        load_plusminusbitmaps();



        arrow.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
                dismiss();
        }});
    }

    private void apply_enabled_and_disabled_on_delays() {
        if(fajrmuteenabled){
            mutefajrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            mutefajrtitle.setTextColor(Color.BLACK);
        } else {
            mutefajrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            mutefajrtitle.setTextColor(Color.WHITE);
            fajrmenu.setVisibility(View.GONE);
        }
        if(dhuhrmuteenabled){
            mutedhuhrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            mutedhuhrtitle.setTextColor(Color.BLACK);
        } else {
            mutedhuhrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            mutedhuhrtitle.setTextColor(Color.WHITE);
            dhuhrmenu.setVisibility(View.GONE);
        }
        if(asrmuteenabled){
            muteasrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            muteasrtitle.setTextColor(Color.BLACK);
        } else {
            muteasrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            muteasrtitle.setTextColor(Color.WHITE);
            asrmenu.setVisibility(View.GONE);
        }
        if(maghrebmuteenabled) {
            mutemaghrebtitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            mutemaghrebtitle.setTextColor(Color.BLACK);
        } else {
            mutemaghrebtitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            mutemaghrebtitle.setTextColor(Color.WHITE);
            maghrebmenu.setVisibility(View.GONE);
        }
        if(ishamuteenabled) {
            muteishatitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            muteishatitle.setTextColor(Color.BLACK);
        } else {
            muteishatitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            muteishatitle.setTextColor(Color.WHITE);
            ishamenu.setVisibility(View.GONE);
        }
    }

    private void apply_previous_delays() {
        String[] delays_split_to_five = delays.split(" ");
        String[] current_prayer_delays_split = delays_split_to_five[0].split(",");
        fajrbeforecounterdisplay.setText(current_prayer_delays_split[0]);
        fajrbeforecounter = Integer.parseInt(current_prayer_delays_split[0]);
        fajraftercounterdisplay.setText(current_prayer_delays_split[1]);
        fajraftercounter = Integer.parseInt(current_prayer_delays_split[1]);
        fajrmuteenabled = current_prayer_delays_split[2].equals("1");
        current_prayer_delays_split = delays_split_to_five[1].split(",");
        dhuhrbeforecounterdisplay.setText(current_prayer_delays_split[0]);
        dhuhrbeforecounter = Integer.parseInt(current_prayer_delays_split[0]);
        dhuhraftercounterdisplay.setText(current_prayer_delays_split[1]);
        dhuhraftercounter = Integer.parseInt(current_prayer_delays_split[1]);
        dhuhrmuteenabled = current_prayer_delays_split[2].equals("1");
        current_prayer_delays_split = delays_split_to_five[2].split(",");
        asrbeforecounterdisplay.setText(current_prayer_delays_split[0]);
        asrbeforecounter = Integer.parseInt(current_prayer_delays_split[0]);
        asraftercounterdisplay.setText(current_prayer_delays_split[1]);
        asraftercounter = Integer.parseInt(current_prayer_delays_split[1]);
        asrmuteenabled = current_prayer_delays_split[2].equals("1");
        current_prayer_delays_split = delays_split_to_five[3].split(",");
        maghrebbeforecounterdisplay.setText(current_prayer_delays_split[0]);
        maghrebbeforecounter = Integer.parseInt(current_prayer_delays_split[0]);
        maghrebaftercounterdisplay.setText(current_prayer_delays_split[1]);
        maghrebaftercounter = Integer.parseInt(current_prayer_delays_split[1]);
        maghrebmuteenabled = current_prayer_delays_split[2].equals("1");
        current_prayer_delays_split = delays_split_to_five[4].split(",");
        ishabeforecounterdisplay.setText(current_prayer_delays_split[0]);
        ishabeforecounter = Integer.parseInt(current_prayer_delays_split[0]);
        ishaaftercounterdisplay.setText(current_prayer_delays_split[1]);
        ishaaftercounter = Integer.parseInt(current_prayer_delays_split[1]);
        ishamuteenabled = current_prayer_delays_split[2].equals("1");
    }

    private boolean delays_modified = false;
    private int fajrbeforecounter=5, fajraftercounter=35, dhuhrbeforecounter=5, dhuhraftercounter=35, asrbeforecounter=5, asraftercounter=35;
    private int maghrebbeforecounter=5, maghrebaftercounter=20, ishabeforecounter=5, ishaaftercounter=35;
    private void plusminusonclicklisteners() {
        findViewById(R.id.fajrbeforeminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fajrbeforecounter!=0) {
                    delays_modified = true;
                    fajrbeforecounter--;
                    fajrbeforecounterdisplay.setText(String.valueOf(fajrbeforecounter));
                }
            }
        });
        findViewById(R.id.fajrminusbeforebackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fajrbeforecounter!=0) {
                    delays_modified = true;
                    fajrbeforecounter--;
                    fajrbeforecounterdisplay.setText(String.valueOf(fajrbeforecounter));
                }
            }
        });
        findViewById(R.id.fajrbeforeplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fajrbeforecounter!=99) {
                    delays_modified = true;
                    fajrbeforecounter++;
                    fajrbeforecounterdisplay.setText(String.valueOf(fajrbeforecounter));
                }
            }
        });
        findViewById(R.id.fajrbeforeplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fajrbeforecounter!=99) {
                    delays_modified = true;
                    fajrbeforecounter++;
                    fajrbeforecounterdisplay.setText(String.valueOf(fajrbeforecounter));
                }
            }
        });
        findViewById(R.id.fajrafterminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fajraftercounter!=0) {
                    delays_modified = true;
                    fajraftercounter--;
                    fajraftercounterdisplay.setText(String.valueOf(fajraftercounter));
                }
            }
        });
        findViewById(R.id.fajrafterminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fajraftercounter!=0) {
                    delays_modified = true;
                    fajraftercounter--;
                    fajraftercounterdisplay.setText(String.valueOf(fajraftercounter));
                }
            }
        });
        findViewById(R.id.fajrafterplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fajraftercounter!=99) {
                    delays_modified = true;
                    fajraftercounter++;
                    fajraftercounterdisplay.setText(String.valueOf(fajraftercounter));
                }
            }
        });
        findViewById(R.id.fajrafterplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fajraftercounter!=99) {
                    delays_modified = true;
                    fajraftercounter++;
                    fajraftercounterdisplay.setText(String.valueOf(fajraftercounter));
                }
            }
        });











        findViewById(R.id.dhuhrbeforeminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dhuhrbeforecounter!=0) {
                    delays_modified = true;
                    dhuhrbeforecounter--;
                    dhuhrbeforecounterdisplay.setText(String.valueOf(dhuhrbeforecounter));
                }
            }
        });
        findViewById(R.id.dhuhrbeforeminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dhuhrbeforecounter!=0) {
                    delays_modified = true;
                    dhuhrbeforecounter--;
                    dhuhrbeforecounterdisplay.setText(String.valueOf(dhuhrbeforecounter));
                }
            }
        });
        findViewById(R.id.dhuhrbeforeplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dhuhrbeforecounter!=99) {
                    delays_modified = true;
                    dhuhrbeforecounter++;
                    dhuhrbeforecounterdisplay.setText(String.valueOf(dhuhrbeforecounter));
                }
            }
        });
        findViewById(R.id.dhuhrbeforeplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dhuhrbeforecounter!=99) {
                    delays_modified = true;
                    dhuhrbeforecounter++;
                    dhuhrbeforecounterdisplay.setText(String.valueOf(dhuhrbeforecounter));
                }
            }
        });
        findViewById(R.id.dhuhrafterminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dhuhraftercounter!=0) {
                    delays_modified = true;
                    dhuhraftercounter--;
                    dhuhraftercounterdisplay.setText(String.valueOf(dhuhraftercounter));
                }
            }
        });
        findViewById(R.id.dhuhrafterminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dhuhraftercounter!=0) {
                    delays_modified = true;
                    dhuhraftercounter--;
                    dhuhraftercounterdisplay.setText(String.valueOf(dhuhraftercounter));
                }
            }
        });
        findViewById(R.id.dhuhrafterplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dhuhraftercounter!=99) {
                    delays_modified = true;
                    dhuhraftercounter++;
                    dhuhraftercounterdisplay.setText(String.valueOf(dhuhraftercounter));
                }
            }
        });
        findViewById(R.id.dhuhrafterplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dhuhraftercounter!=99) {
                    delays_modified = true;
                    dhuhraftercounter++;
                    dhuhraftercounterdisplay.setText(String.valueOf(dhuhraftercounter));
                }
            }
        });








        findViewById(R.id.asrbeforeminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asrbeforecounter!=0) {
                    delays_modified = true;
                    asrbeforecounter--;
                    asrbeforecounterdisplay.setText(String.valueOf(asrbeforecounter));
                }
            }
        });
        findViewById(R.id.asrbeforeminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asrbeforecounter!=0) {
                    delays_modified = true;
                    asrbeforecounter--;
                    asrbeforecounterdisplay.setText(String.valueOf(asrbeforecounter));
                }
            }
        });
        findViewById(R.id.asrbeforeplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asrbeforecounter!=99) {
                    delays_modified = true;
                    asrbeforecounter++;
                    asrbeforecounterdisplay.setText(String.valueOf(asrbeforecounter));
                }
            }
        });
        findViewById(R.id.asrbeforeplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asrbeforecounter!=99) {
                    delays_modified = true;
                    asrbeforecounter++;
                    asrbeforecounterdisplay.setText(String.valueOf(asrbeforecounter));
                }
            }
        });
        findViewById(R.id.asrafterminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asraftercounter!=0) {
                    delays_modified = true;
                    asraftercounter--;
                    asraftercounterdisplay.setText(String.valueOf(asraftercounter));
                }
            }
        });
        findViewById(R.id.asrafterminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asraftercounter!=0) {
                    delays_modified = true;
                    asraftercounter--;
                    asraftercounterdisplay.setText(String.valueOf(asraftercounter));
                }
            }
        });
        findViewById(R.id.asrafterplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asraftercounter!=99) {
                    delays_modified = true;
                    asraftercounter++;
                    asraftercounterdisplay.setText(String.valueOf(asraftercounter));
                }
            }
        });
        findViewById(R.id.asrafterplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asraftercounter!=99) {
                    delays_modified = true;
                    asraftercounter++;
                    asraftercounterdisplay.setText(String.valueOf(asraftercounter));
                }
            }
        });








        findViewById(R.id.maghrebbeforeminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maghrebbeforecounter!=0) {
                    delays_modified = true;
                    maghrebbeforecounter--;
                    maghrebbeforecounterdisplay.setText(String.valueOf(maghrebbeforecounter));
                }
            }
        });
        findViewById(R.id.maghrebbeforeminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maghrebbeforecounter!=0) {
                    delays_modified = true;
                    maghrebbeforecounter--;
                    maghrebbeforecounterdisplay.setText(String.valueOf(maghrebbeforecounter));
                }
            }
        });
        findViewById(R.id.maghrebbeforeplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maghrebbeforecounter!=99) {
                    delays_modified = true;
                    maghrebbeforecounter++;
                    maghrebbeforecounterdisplay.setText(String.valueOf(maghrebbeforecounter));
                }
            }
        });
        findViewById(R.id.maghrebbeforeplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maghrebbeforecounter!=99) {
                    delays_modified = true;
                    maghrebbeforecounter++;
                    maghrebbeforecounterdisplay.setText(String.valueOf(maghrebbeforecounter));
                }
            }
        });
        findViewById(R.id.maghrebafterminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maghrebaftercounter!=0) {
                    delays_modified = true;
                    maghrebaftercounter--;
                    maghrebaftercounterdisplay.setText(String.valueOf(maghrebaftercounter));
                }
            }
        });
        findViewById(R.id.maghrebafterminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maghrebaftercounter!=0) {
                    delays_modified = true;
                    maghrebaftercounter--;
                    maghrebaftercounterdisplay.setText(String.valueOf(maghrebaftercounter));
                }
            }
        });
        findViewById(R.id.maghrebafterplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maghrebaftercounter!=99) {
                    delays_modified = true;
                    maghrebaftercounter++;
                    maghrebaftercounterdisplay.setText(String.valueOf(maghrebaftercounter));
                }
            }
        });
        findViewById(R.id.maghrebafterplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maghrebaftercounter!=99) {
                    delays_modified = true;
                    maghrebaftercounter++;
                    maghrebaftercounterdisplay.setText(String.valueOf(maghrebaftercounter));
                }
            }
        });









        findViewById(R.id.ishabeforeminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ishabeforecounter!=0) {
                    ishabeforecounter--;
                    ishabeforecounterdisplay.setText(String.valueOf(ishabeforecounter));
                }
            }
        });
        findViewById(R.id.ishabeforeminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ishabeforecounter!=0) {
                    ishabeforecounter--;
                    ishabeforecounterdisplay.setText(String.valueOf(ishabeforecounter));
                }
            }
        });
        findViewById(R.id.ishabeforeplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ishabeforecounter!=99) {
                    ishabeforecounter++;
                    ishabeforecounterdisplay.setText(String.valueOf(ishabeforecounter));
                }
            }
        });
        findViewById(R.id.ishabeforeplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ishabeforecounter!=99) {
                    ishabeforecounter++;
                    ishabeforecounterdisplay.setText(String.valueOf(ishabeforecounter));
                }
            }
        });









        findViewById(R.id.ishaafterminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ishaaftercounter!=0) {
                    ishaaftercounter--;
                    ishaaftercounterdisplay.setText(String.valueOf(ishaaftercounter));
                }
            }
        });
        findViewById(R.id.ishaafterminusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ishaaftercounter!=0) {
                    ishaaftercounter--;
                    ishaaftercounterdisplay.setText(String.valueOf(ishaaftercounter));
                }
            }
        });
        findViewById(R.id.ishaafterplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ishaaftercounter!=99) {
                    ishaaftercounter++;
                    ishaaftercounterdisplay.setText(String.valueOf(ishaaftercounter));
                }
            }
        });
        findViewById(R.id.ishaafterplusbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ishaaftercounter!=99) {
                    ishaaftercounter++;
                    ishaaftercounterdisplay.setText(String.valueOf(ishaaftercounter));
                }
            }
        });
    }

    private void load_plusminusbitmaps() {
            ImageView h = findViewById(R.id.fajrbeforeplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.fajrafterplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.fajrbeforeminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));
            h = findViewById(R.id.fajrafterminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));
            h = findViewById(R.id.dhuhrbeforeplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.dhuhrafterplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.dhuhrbeforeminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));
            h = findViewById(R.id.dhuhrafterminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));

            h = findViewById(R.id.asrbeforeplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.asrafterplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.asrbeforeminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));
            h = findViewById(R.id.asrafterminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));
            h = findViewById(R.id.maghrebbeforeplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.maghrebafterplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.maghrebbeforeminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));
            h = findViewById(R.id.maghrebafterminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));

            h = findViewById(R.id.ishabeforeplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.ishaafterplus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            h = findViewById(R.id.ishabeforeminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));
            h = findViewById(R.id.ishaafterminus);
            h.setImageDrawable(getResources().getDrawable(R.drawable.minus));
    }

    private void dismiss() {
        Intent backtoforce = new Intent(this, force.class);
        startActivity(backtoforce);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        save_delays_of_changed();
        dismiss();
    }

    private void apply_previous_settings() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (main_notification_sql.equals("yes")) {
                notiswitch.setChecked(true);
            }
        }
    }

    private void dark_light_mode() {
        if(!darkmode){
            findViewById(R.id.settings).setBackground(getResources().getDrawable(R.drawable.simpelbackground));
            findViewById(R.id.arrow).setBackground(getResources().getDrawable(R.drawable.arrowbutton));
            for(TextView adan:adans)
                adan.setBackgroundColor(getResources().getColor(R.color.typicallightbuttoncolors));

            adantitle.setTextColor(getResources().getColor(R.color.dimmest));
            mutetitle.setTextColor(getResources().getColor(R.color.dimmest));
            settingstitle.setTextColor(getResources().getColor(R.color.dimmest));
            notitext.setTextColor(getResources().getColor(R.color.dimmest));
        }
    }

    private void apply_selected_language() {
        if(language.equals("en")){
            fajrmutebeforeadantitle.setText(getResources().getString(R.string.minutestomutebeforeadan));
            fajrmuteafteradantitle.setText(getResources().getString(R.string.minutestomuteafteradan));

            dhuhrmutebeforeadantitle.setText(getResources().getString(R.string.minutestomutebeforeadan));
            dhuhrafteradantitle.setText(getResources().getString(R.string.minutestomuteafteradan));

            asrbeforeadantitle.setText(getResources().getString(R.string.minutestomutebeforeadan));
            asrafteradantitle.setText(getResources().getString(R.string.minutestomuteafteradan));

            maghrebbeforeadantitle.setText(getResources().getString(R.string.minutestomutebeforeadan));
            maghrebafteradantitle.setText(getResources().getString(R.string.minutestomuteafteradan));

            ishabeforeadantitle.setText(getResources().getString(R.string.minutestomutebeforeadan));
            ishaafteradantitle.setText(getResources().getString(R.string.minutestomuteafteradan));

            titles.get(0).setText(getResources().getString(R.string.fajrtitle));
            titles.get(1).setText(getResources().getString(R.string.rise));
            titles.get(2).setText(getResources().getString(R.string.dohrtitle));
            titles.get(3).setText(getResources().getString(R.string.asrtitle));
            titles.get(4).setText(getResources().getString(R.string.maghrebtitle));
            titles.get(5).setText(getResources().getString(R.string.ishatitle));
            arrow.setText(getResources().getString(R.string.save));
            adantitle.setText(getResources().getString(R.string.adan));
            mutetitle.setText(getResources().getString(R.string.mutephonetitle));
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                settingstitle.setText(getResources().getString(R.string.settings));
                notitext.setText(getResources().getString(R.string.main_noti));
            }
        }

    }

    private void applyfont() {
        try{
            Typeface arabic_typeface = Typeface.createFromAsset(this.getAssets(), "Tajawal-Light.ttf");
            arrow.setTypeface(arabic_typeface);
            adantitle.setTypeface(arabic_typeface);
            notitext.setTypeface(arabic_typeface);
            settingstitle.setTypeface(arabic_typeface);
            mutetitle.setTypeface(arabic_typeface);

            mutefajrtitle.setTypeface(arabic_typeface);
            mutedhuhrtitle.setTypeface(arabic_typeface);
            muteasrtitle.setTypeface(arabic_typeface);
            mutemaghrebtitle.setTypeface(arabic_typeface);
            muteishatitle.setTypeface(arabic_typeface);

            fajrmutebeforeadantitle.setTypeface(arabic_typeface);
            fajrmuteafteradantitle.setTypeface(arabic_typeface);

            dhuhrmutebeforeadantitle.setTypeface(arabic_typeface);
            dhuhrafteradantitle.setTypeface(arabic_typeface);

            asrbeforeadantitle.setTypeface(arabic_typeface);
            asrafteradantitle.setTypeface(arabic_typeface);

            maghrebbeforeadantitle.setTypeface(arabic_typeface);
            maghrebafteradantitle.setTypeface(arabic_typeface);

            ishabeforeadantitle.setTypeface(arabic_typeface);
            ishaafteradantitle.setTypeface(arabic_typeface);

            for(int i=0; i<6; i++){
                if(i!=1) {
                    adans.get(i).setTypeface(arabic_typeface);
                }
                titles.get(i).setTypeface(arabic_typeface);
            }
        } catch(Exception ignored){}
    }

    private void sql(String table) {
        SQLSharing.TABLE_NAME_INPUTER = table;
        switch (table) {
            case "slat":
                SQLSharing.mydbslat = SQL.getInstance(this);
                SQLSharing.mycursorslat = SQLSharing.mydbslat.getAllDateslat();
                break;
            case "force":
                SQLSharing.mydbforce = SQL.getInstance(this);
                SQLSharing.mycursorforce = SQLSharing.mydbforce.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mydbforce3 = SQL.getInstance(this);
                SQLSharing.mycursorforce3 = SQLSharing.mydbforce3.getAllDateforce3();
                break;
        }
    }

    private boolean once22 = true;
    private void update_switch_setting_in_sql(String switch_setting){
        sql("slat");
        if(once22){
            once22 = false;
            SQLSharing.mycursorslat.moveToPosition(8);
            SWITCHSETTINGID = SQLSharing.mycursorslat.getString(0);
        }
        SQLSharing.mydbslat.updateData(switch_setting, SWITCHSETTINGID);
        close_sql();
    }

    private void protected_apps_request() {
        try {
            if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                protected_apps_request request = new protected_apps_request(force_settings.this, darkmode, language);
                request.show();
            }
        }
        catch(Exception ignored){
        }
    }

    private String SWITCHSETTINGID = "";
    private void onClickListeners() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notiswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        update_switch_setting_in_sql("yes");

                        load_service();

                        protected_apps_request();
                    }else
                        update_switch_setting_in_sql("no");
                }
            });
        }

        adans.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(0);
            }
        });
        // we ignored get(1) as fajr has no adan
        adans.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(1);
            }
        });
        adans.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(2);
            }
        });
        adans.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(3);
            }
        });
        adans.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_selection(4);
            }
        });


        ringmodes.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(0);
            }
        });
        ringmodes.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(1);
            }
        });
        ringmodes.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(2);
            }
        });
        ringmodes.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(3);
            }
        });
        ringmodes.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(4);
            }
        });
        ringmodes.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(5);
            }
        });
        ringmodesbackground.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(0);
            }
        });
        ringmodesbackground.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(1);
            }
        });
        ringmodesbackground.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(2);
            }
        });
        ringmodesbackground.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(3);
            }
        });
        ringmodesbackground.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(4);
            }
        });
        ringmodesbackground.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { rotate_sound_flags(5);
            }
        });
    }

    private void load_service() {
        // adan service
        if (Build.VERSION.SDK_INT < 28) {
            try {
                close_sql();
                sql("force");
                if (SQLSharing.mycursorforce.getCount() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
                    } else {
                        ProcessMainClass bck = new ProcessMainClass();
                        bck.launchService(getApplicationContext());
                    }
                }
                close_sql();
            }
            catch(Exception ignored){}
        }
    }
    private boolean opening_selection = false;
    private void launch_selection(int prayertobemodified) {
        opening_selection = true;
        Intent openselectionforcurrentprayer = new Intent(context, force_settings_adan_selection.class);
        openselectionforcurrentprayer.putExtra("prayertobemodified", String.valueOf(prayertobemodified));
        startActivity(openselectionforcurrentprayer);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        save_delays_of_changed();

        close_sql();
    }


    private void save_delays_of_changed() {
        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(10);
        ID = SQLSharing.mycursorslat.getString(0);
        String one, two, three, four, five;
        if(fajrmuteenabled)
            one = "1";
        else
            one = "0";
        if(dhuhrmuteenabled)
            two = "1";
        else
            two = "0";
        if(asrmuteenabled)
            three = "1";
        else
            three = "0";
        if(maghrebmuteenabled)
            four = "1";
        else
            four = "0";
        if(ishamuteenabled)
            five = "1";
        else
            five = "0";
        SQLSharing.mydbslat.updateData(fajrbeforecounter+","+fajraftercounter+","+one + " " + dhuhrbeforecounter+","+dhuhraftercounter+","+two + " " + asrbeforecounter+","+asraftercounter+","+three + " " + maghrebbeforecounter+","+maghrebaftercounter+","+four + " " + ishabeforecounter+","+ishaaftercounter+","+five ,ID);
        close_sql();
    }

    private void rotate_sound_flags(int i) {

        String[] yes;
        yes = selections[i].split(",");

        if(yes[1].equals(String.valueOf(0))){
            selections[i] = yes[0] + "," + String.valueOf(1);
            StringBuilder temp = new StringBuilder();
            for(int j=0; j<6; j++){
                temp.append(selections[j]);
                temp.append(" ");
            }
            adanSelections = temp.toString();
        }

        else if(yes[1].equals(String.valueOf(1))){
            selections[i] = yes[0] + "," + String.valueOf(2);

            if(i==1) // fajr will not have a sound, only vibrate/no sound
                selections[i] = yes[0] + "," + String.valueOf(0);
            else
                adans.get(i).setVisibility(View.VISIBLE);

            StringBuilder temp = new StringBuilder();
            for(int j=0; j<6; j++){
                temp.append(selections[j]);
                temp.append(" ");
            }
            adanSelections = temp.toString();
        }

        else if(yes[1].equals(String.valueOf(2))){
            selections[i] = yes[0] + "," + String.valueOf(0);
            StringBuilder temp = new StringBuilder();
            for(int j=0; j<6; j++){
                temp.append(selections[j]);
                temp.append(" ");
            }
            adanSelections = temp.toString();

            adans.get(i).setVisibility(View.INVISIBLE);
        }

        update_bitmap(i);
        update_sql();
    }

    private void update_bitmap(int i) {
        String[] yes;
        yes = selections[i].split(",");
        if(once2){
            switch (yes[1]) {
                case "0":
                    // no ring no vibrate
                    if(darkmode) {
                        ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.soundsoff));
                    } else {
                        ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.soundsofflightmode));
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "1":
                    // vibrate only
                    if(darkmode){
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.vibrate));
                    } else {
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.vibratelightmode));
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "2":
                    // ring only
                    if(darkmode){
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.soundson));
                    }else {
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.soundsonlightmode));
                    }
            }
        } else {
            switch (yes[1]) {
                case "0":
                    // no ring no vibrate
                    if(darkmode) {
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.soundsoff));
                    } else {
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.soundsofflightmode));
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "1":
                    // vibrate only
                    if(darkmode){
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.vibrate));
                    } else {
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.vibratelightmode));
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "2":
                    // ring only
                    if(darkmode){
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.soundson));
                    }else {
                            ringmodes.get(i).setImageDrawable(getResources().getDrawable(R.drawable.soundsonlightmode));
                    }
            }
        }

    }

    private void apply_adanSelections() {
        for(int i=0; i<6;i++){
            update_adan(i);
            update_bitmap(i);
        }
        once2 = false;
    }

    private void apply_adanSelectionsshort() {
        for(int i=0; i<6;i++)
            update_adan(i);
    }

    private void update_adan(int i) {
        String[] yes = selections[i].split(",");
        if(i!=1) {
            // applied currently selected adan
            switch (yes[0]) {
                case "1":
                    if(language.equals("ar"))
                        adans.get(i).setText(getResources().getString(R.string.adan1_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(getResources().getString(R.string.adan1));
                    break;
                case "2":
                    if(language.equals("ar"))
                        adans.get(i).setText(getResources().getString(R.string.adan2_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(getResources().getString(R.string.adan2));
                    break;
                case "3":
                    if(language.equals("ar"))
                        adans.get(i).setText(getResources().getString(R.string.adan3_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(getResources().getString(R.string.adan3));
                    break;
                case "4":
                    if(language.equals("ar"))
                        adans.get(i).setText(getResources().getString(R.string.adan4_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(getResources().getString(R.string.adan4));
                    break;
                case "5":
                    if(language.equals("ar"))
                        adans.get(i).setText(getResources().getString(R.string.adan5_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(getResources().getString(R.string.adan5));
                    break;
                case "6":
                    if(language.equals("ar"))
                        adans.get(i).setText(getResources().getString(R.string.adan6_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(getResources().getString(R.string.adan6));
            }
        }
    }

    private String delays;
    private void sql_work() {
        sql("slat");
        SQLSharing.mycursorslat.moveToPosition(1);
        darkmode = SQLSharing.mycursorslat.getString(1).equals("yes");
        SQLSharing.mycursorslat.moveToPosition(6);
        language = SQLSharing.mycursorslat.getString(1);
        SQLSharing.mycursorslat.moveToNext();
        adanSelections = SQLSharing.mycursorslat.getString(1);
        selections = adanSelections.split(" ");
        SQLSharing.mycursorslat.moveToNext();
        main_notification_sql = SQLSharing.mycursorslat.getString(1);

        SQLSharing.mycursorslat.moveToNext();
        boolean request_protected_menu = SQLSharing.mycursorslat.getString(1).equals("yes");
        SQLSharing.mycursorslat.moveToNext();
        delays = SQLSharing.mycursorslat.getString(1);
        close_sql();

    }

    private void close_sql() {
        if(SQLSharing.mydbforce!=null)
            SQLSharing.mydbforce.close();
        if(SQLSharing.mydbslat!=null)
            SQLSharing.mydbslat.close();
        if(SQLSharing.mydbforce3!=null)
            SQLSharing.mydbforce3.close();
    }

    private String main_notification_sql = "yes";
    private void update_sql() {
        sql("slat");
        if(once){
            once = false;
            SQLSharing.mycursorslat.moveToPosition(7);
            ID = SQLSharing.mycursorslat.getString(0);
        }
        SQLSharing.mydbslat.updateData(adanSelections, ID);
        close_sql();
    }

    private Switch notiswitch;
    private TextView settingstitle, notitext, mutetitle;
    private Context context;
    private LinearLayout fajrmenu, dhuhrmenu, asrmenu, maghrebmenu, ishamenu;
    private TextView fajrmutebeforeadantitle, fajrmuteafteradantitle, dhuhrmutebeforeadantitle, dhuhrafteradantitle, asrbeforeadantitle, asrafteradantitle;
    private TextView maghrebbeforeadantitle, maghrebafteradantitle, ishabeforeadantitle, ishaafteradantitle;
    private TextView mutefajrtitle, mutedhuhrtitle, muteasrtitle, mutemaghrebtitle, muteishatitle;
    private TextView fajrbeforecounterdisplay, fajraftercounterdisplay, dhuhrbeforecounterdisplay, dhuhraftercounterdisplay, asrbeforecounterdisplay, asraftercounterdisplay, maghrebbeforecounterdisplay, maghrebaftercounterdisplay, ishabeforecounterdisplay, ishaaftercounterdisplay;
    private void prepare_variables() {
        context = this;

        fajrmenu = findViewById(R.id.fajrmenu);
        dhuhrmenu = findViewById(R.id.dhuhrmenu);
        asrmenu = findViewById(R.id.asrmenu);
        maghrebmenu = findViewById(R.id.maghrebmenu);
        ishamenu = findViewById(R.id.ishamenu);

        fajrbeforecounterdisplay = findViewById(R.id.fajrbeforecounter);
        fajraftercounterdisplay= findViewById(R.id.fajraftercounter);
        dhuhrbeforecounterdisplay= findViewById(R.id.beforecounterdhuhr);
        dhuhraftercounterdisplay= findViewById(R.id.aftercounterdhuhr);
        asrbeforecounterdisplay = findViewById(R.id.beforecounterasr);
        asraftercounterdisplay = findViewById(R.id.aftercounterasr);
        maghrebbeforecounterdisplay = findViewById(R.id.beforecountermaghreb);
        maghrebaftercounterdisplay = findViewById(R.id.aftercountermaghreb);
        ishabeforecounterdisplay= findViewById(R.id.beforecounterisha);
        ishaaftercounterdisplay = findViewById(R.id.aftercounterisha);

        mutefajrtitle = findViewById(R.id.mutefajrtitle);
        mutedhuhrtitle = findViewById(R.id.mutedhuhrtitle);
        muteasrtitle = findViewById(R.id.muteasrtitle);
        mutemaghrebtitle = findViewById(R.id.mutemaghrebtitle);
        muteishatitle = findViewById(R.id.muteishatitle);


        fajrmutebeforeadantitle = findViewById(R.id.fajrmutebeforeadantitle);
        fajrmuteafteradantitle = findViewById(R.id.fajrmuteafteradantitle);
        dhuhrmutebeforeadantitle = findViewById(R.id.dhuhrmutebeforeadantitle);
        dhuhrafteradantitle = findViewById(R.id.dhuhrmuteafteradantitle);
        asrbeforeadantitle = findViewById(R.id.asrmutebeforeadantitle);
        asrafteradantitle = findViewById(R.id.asrmuteafteradantitle);
        maghrebbeforeadantitle = findViewById(R.id.maghrebmutebeforeadantitle);
        maghrebafteradantitle = findViewById(R.id.maghrebmuteafteradantitle);
        ishabeforeadantitle = findViewById(R.id.ishamutebeforeadantitle);
        ishaafteradantitle = findViewById(R.id.ishamuteafteradantitle);

        mutetitle = findViewById(R.id.mutetitle);
        arrow = findViewById(R.id.arrow);
        adantitle = findViewById(R.id.adantitle);

        notiswitch = findViewById(R.id.notiswitch);
        settingstitle = findViewById(R.id.settingstitle);
        notitext = findViewById(R.id.notitext);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notiswitch.setVisibility(View.GONE);
            settingstitle.setVisibility(View.GONE);
            notitext.setVisibility(View.GONE);
        }

        adans = new ArrayList<>();
        adans.add((TextView) findViewById(R.id.fajradan));
        adans.add((TextView) findViewById(R.id.riseadan));
        adans.add((TextView) findViewById(R.id.dhuhradan));
        adans.add((TextView) findViewById(R.id.asradan));
        adans.add((TextView) findViewById(R.id.maghribadan));
        adans.add((TextView) findViewById(R.id.ishaadan));

        titles = new ArrayList<>();
        titles.add((TextView) findViewById(R.id.fajrtitle));
        titles.add((TextView) findViewById(R.id.risetitle));
        titles.add((TextView) findViewById(R.id.dhuhrtitle));
        titles.add((TextView) findViewById(R.id.asrtitle));
        titles.add((TextView) findViewById(R.id.maghribtitle));
        titles.add((TextView) findViewById(R.id.ishatitle));

        ringmodes = new ArrayList<>();
        ringmodes.add((ImageView) findViewById(R.id.fajrringmode));
        ringmodes.add((ImageView) findViewById(R.id.riseringmode));
        ringmodes.add((ImageView) findViewById(R.id.dhuhrringmode));
        ringmodes.add((ImageView) findViewById(R.id.asrringmode));
        ringmodes.add((ImageView) findViewById(R.id.maghribringmode));
        ringmodes.add((ImageView) findViewById(R.id.isharingmode));

        ringmodesbackground = new ArrayList<>();
        ringmodesbackground.add((FrameLayout) findViewById(R.id.fajrringmodebackground));
        ringmodesbackground.add((FrameLayout) findViewById(R.id.riseringmodebackground));
        ringmodesbackground.add((FrameLayout) findViewById(R.id.dhuhrringmodebackground));
        ringmodesbackground.add((FrameLayout) findViewById(R.id.asrringmodebackground));
        ringmodesbackground.add((FrameLayout) findViewById(R.id.maghribringmodebackground));
        ringmodesbackground.add((FrameLayout) findViewById(R.id.isharingmodebackground));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        close_sql();
        save_delays_of_changed();

        if(!opening_selection) {
            if (language.equals("ar"))
                Toast.makeText(this, getString(R.string.saved_arabe), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        }

        close_sql();
    }

    private boolean fajrmuteenabled = true, dhuhrmuteenabled = true, asrmuteenabled = true, maghrebmuteenabled = true, ishamuteenabled = true;
    public void fajrmuteClicked(View view) {
        if(fajrmuteenabled){
            mutefajrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            mutefajrtitle.setTextColor(Color.WHITE);
            fajrmenu.setVisibility(View.GONE);
            fajrmuteenabled = false;
        } else {
            mutefajrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            mutefajrtitle.setTextColor(Color.BLACK);
            fajrmenu.setVisibility(View.VISIBLE);
            fajrmuteenabled = true;
        }
    }

    public void dhuhrmuteClicked(View view) {
        if(dhuhrmuteenabled){
            mutedhuhrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            mutedhuhrtitle.setTextColor(Color.WHITE);
            dhuhrmenu.setVisibility(View.GONE);
            dhuhrmuteenabled = false;
        } else {
            mutedhuhrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            mutedhuhrtitle.setTextColor(Color.BLACK);
            dhuhrmenu.setVisibility(View.VISIBLE);
            dhuhrmuteenabled = true;
        }
    }

    public void asrmuteClicked(View view) {
        if(asrmuteenabled){
            muteasrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            muteasrtitle.setTextColor(Color.WHITE);
            asrmenu.setVisibility(View.GONE);
            asrmuteenabled = false;
        } else {
            muteasrtitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            muteasrtitle.setTextColor(Color.BLACK);
            asrmenu.setVisibility(View.VISIBLE);
            asrmuteenabled = true;
        }
    }

    public void maghrebmuteClicked(View view) {
        if(maghrebmuteenabled){
            mutemaghrebtitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            mutemaghrebtitle.setTextColor(Color.WHITE);
            maghrebmenu.setVisibility(View.GONE);
            maghrebmuteenabled = false;
        } else {
            mutemaghrebtitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            mutemaghrebtitle.setTextColor(Color.BLACK);
            maghrebmenu.setVisibility(View.VISIBLE);
            maghrebmuteenabled = true;
        }
    }

    public void ishamuteClicked(View view) {
        if(ishamuteenabled){
            muteishatitle.setBackground(getResources().getDrawable(R.drawable.mutetitleoff));
            muteishatitle.setTextColor(Color.WHITE);
            ishamenu.setVisibility(View.GONE);
            ishamuteenabled = false;
        } else {
            muteishatitle.setBackground(getResources().getDrawable(R.drawable.mutetitle));
            muteishatitle.setTextColor(Color.BLACK);
            ishamenu.setVisibility(View.VISIBLE);
            ishamuteenabled = true;
        }
    }

}
