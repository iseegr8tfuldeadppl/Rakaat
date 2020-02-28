package com.krimzon.scuffedbots.raka3at;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
    private boolean darkmode = true, once2 = true, once = true, audioisplaying = false;
    private String[] selections;
    private Resources resources;
    private TextView adantitle, selectiontitle;
    private List<TextView> adans, titles, adansselection;
    private List<ImageView> ringmodes, audioplayer;
    private List<FrameLayout> ringmodesbackground;
    private int selectedadan = -1, currentlyplayingadan = 0;

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
        onClickListeners();
        plusminusonclicklisteners();
        dark_light_mode();
        load_plusminusbitmaps();



        arrow.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            if(!mouadineselectionpageison){
                dismiss();
            } else {
                mouadineselectionpageison = false;
                adansselection.get(selectedadan).setBackground(getResources().getDrawable(R.drawable.selectorresposive));
                settingsmenu.setVisibility(View.VISIBLE);
                selectionmenu.setVisibility(View.GONE);
                apply_adanSelectionsshort();
                if(language.equals("ar"))
                    arrow.setText(resources.getString(R.string.save_arabe));
                else if(language.equals("en"))
                    arrow.setText(resources.getString(R.string.save));
            }
        }});
    }

    private void apply_previous_delays() {
        String[] delays_split_to_five = delays.split(" ");
        String[] current_prayer_delays_split = delays_split_to_five[0].split(",");
        fajrbeforecounterdisplay.setText(current_prayer_delays_split[0]);
        fajrbeforecounter = Integer.valueOf(current_prayer_delays_split[0]);
        fajraftercounterdisplay.setText(current_prayer_delays_split[1]);
        fajraftercounter = Integer.valueOf(current_prayer_delays_split[1]);
        current_prayer_delays_split = delays_split_to_five[1].split(",");
        dhuhrbeforecounterdisplay.setText(current_prayer_delays_split[0]);
        dhuhrbeforecounter = Integer.valueOf(current_prayer_delays_split[0]);
        dhuhraftercounterdisplay.setText(current_prayer_delays_split[1]);
        dhuhraftercounter = Integer.valueOf(current_prayer_delays_split[1]);
        current_prayer_delays_split = delays_split_to_five[2].split(",");
        asrbeforecounterdisplay.setText(current_prayer_delays_split[0]);
        asrbeforecounter = Integer.valueOf(current_prayer_delays_split[0]);
        asraftercounterdisplay.setText(current_prayer_delays_split[1]);
        asraftercounter = Integer.valueOf(current_prayer_delays_split[1]);
        current_prayer_delays_split = delays_split_to_five[3].split(",");
        maghrebbeforecounterdisplay.setText(current_prayer_delays_split[0]);
        maghrebbeforecounter = Integer.valueOf(current_prayer_delays_split[0]);
        maghrebaftercounterdisplay.setText(current_prayer_delays_split[1]);
        maghrebaftercounter = Integer.valueOf(current_prayer_delays_split[1]);
        current_prayer_delays_split = delays_split_to_five[4].split(",");
        ishabeforecounterdisplay.setText(current_prayer_delays_split[0]);
        ishabeforecounter = Integer.valueOf(current_prayer_delays_split[0]);
        ishaaftercounterdisplay.setText(current_prayer_delays_split[1]);
        ishaaftercounter = Integer.valueOf(current_prayer_delays_split[1]);
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
        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.fajrbeforeplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.fajrbeforeplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.fajrafterplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.fajrafterplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.fajrbeforeminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.fajrbeforeminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.fajrafterminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.fajrafterminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }




        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.dhuhrbeforeplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.dhuhrbeforeplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.dhuhrafterplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.dhuhrafterplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.dhuhrbeforeminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.dhuhrbeforeminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.dhuhrafterminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.dhuhrafterminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }



        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.asrbeforeplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.asrbeforeplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.asrafterplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.asrafterplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.asrbeforeminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.asrbeforeminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.asrafterminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.asrafterminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }



        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.maghrebbeforeplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.maghrebbeforeplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.maghrebafterplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.maghrebafterplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.maghrebbeforeminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.maghrebbeforeminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.maghrebafterminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.maghrebafterminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }



        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.ishabeforeplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.ishabeforeplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.plus).into((ImageView) findViewById(R.id.ishaafterplus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.ishaafterplus);
            h.setImageDrawable(resources.getDrawable(R.drawable.plus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.ishabeforeminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.ishabeforeminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }
        try {
            Glide.with(context).load(R.drawable.minus).into((ImageView) findViewById(R.id.ishaafterminus));
        } catch (Exception ignored) {
            ImageView h = findViewById(R.id.ishaafterminus);
            h.setImageDrawable(resources.getDrawable(R.drawable.minus));
        }
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

    private void make_view_adaptively_longer(View viewer) {
        try{
            LinearLayout gestureLayout = viewer.findViewById(R.id.settings);
            BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(viewer);
            int height = gestureLayout.getMeasuredHeight();
            sheetBehavior.setPeekHeight(height);
        } catch(Exception e){
            e.printStackTrace();
        }
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

    private boolean mouadineselectionpageison = false;
    private void apply_selected_language() {
        if(language.equals("en")){
            fajrmutebeforeadantitle.setText(resources.getString(R.string.minutestomutebeforeadan));
            fajrmutebeforeadantitle.setText(resources.getString(R.string.minutestomuteafteradan));

            dhuhrmutebeforeadantitle.setText(resources.getString(R.string.minutestomutebeforeadan));
            dhuhrafteradantitle.setText(resources.getString(R.string.minutestomuteafteradan));

            asrbeforeadantitle.setText(resources.getString(R.string.minutestomutebeforeadan));
            asrafteradantitle.setText(resources.getString(R.string.minutestomuteafteradan));

            maghrebbeforeadantitle.setText(resources.getString(R.string.minutestomutebeforeadan));
            maghrebafteradantitle.setText(resources.getString(R.string.minutestomuteafteradan));

            ishabeforeadantitle.setText(resources.getString(R.string.minutestomutebeforeadan));
            ishaafteradantitle.setText(resources.getString(R.string.minutestomuteafteradan));

            titles.get(0).setText(resources.getString(R.string.fajrtitle));
            titles.get(1).setText(resources.getString(R.string.rise));
            titles.get(2).setText(resources.getString(R.string.dohrtitle));
            titles.get(3).setText(resources.getString(R.string.asrtitle));
            titles.get(4).setText(resources.getString(R.string.maghrebtitle));
            titles.get(5).setText(resources.getString(R.string.ishatitle));
            arrow.setText(resources.getString(R.string.save));
            adantitle.setText(resources.getString(R.string.adan));
            mutetitle.setText(resources.getString(R.string.mutephonetitle));
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                settingstitle.setText(resources.getString(R.string.settings));
                notitext.setText(resources.getString(R.string.main_noti));
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
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
        SQLSharing.TABLE_NAME_INPUTER = table;
        SQLSharing.mydb = new SQL(context);
        switch (table) {
            case "slat":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateslat();
                break;
            case "force":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateforce();
                break;
            case "force3":
                SQLSharing.mycursor = SQLSharing.mydb.getAllDateforce3();
                break;
        }
    }

    private boolean once22 = true;
    private void update_switch_setting_in_sql(String switch_setting){
        sql("slat");
        if(once22){
            once22 = false;
            SQLSharing.mycursor.moveToPosition(8);
            SWITCHSETTINGID = SQLSharing.mycursor.getString(0);
        }
        SQLSharing.mydb.updateData(switch_setting, SWITCHSETTINGID);
        SQLSharing.mydb.close();
    }

    private void protected_apps_request() {
        try {
            if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                protected_apps_request request = new protected_apps_request(force_settings.this, darkmode, language);
                request.show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
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

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            RestartServiceBroadcastReceiver.scheduleJob(context);
                        } else {
                            ProcessMainClass bck = new ProcessMainClass();
                            bck.launchService(context);
                        }

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

    private void launch_selection(final int prayertobemodified) {
        settingsmenu.setVisibility(View.GONE);
        selectionmenu.setVisibility(View.VISIBLE);
        mouadineselectionpageison = true;


        if(language.equals("en")){
            adansselection.get(0).setText(resources.getString(R.string.adan1));
            adansselection.get(1).setText(resources.getString(R.string.adan2));
            adansselection.get(2).setText(resources.getString(R.string.adan3));
            adansselection.get(3).setText(resources.getString(R.string.adan4));
            adansselection.get(4).setText(resources.getString(R.string.adan5));
            adansselection.get(5).setText(resources.getString(R.string.adan6));
            selectiontitle.setText(resources.getString(R.string.select_an_adan));
        }

        if(language.equals("ar"))
            arrow.setText(resources.getString(R.string.back_arabe));
        else if(language.equals("en"))
            arrow.setText(resources.getString(R.string.back));

        variables_of_selection();
        apply_font_to_selection();
        if(prayertobemodified>0)
            apply_already_selected_mouadin(prayertobemodified+1);
        else
            apply_already_selected_mouadin(prayertobemodified);

        adansselection.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(0, prayertobemodified); }});
        adansselection.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(1, prayertobemodified); }});
        adansselection.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(2, prayertobemodified); }});
        adansselection.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(3, prayertobemodified); }});
        adansselection.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(4, prayertobemodified); }});
        adansselection.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { applyselectedadan(5, prayertobemodified); }});

        audioplayer.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(0);
            }
        });
        audioplayer.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(1);
            }
        });
        audioplayer.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(2);
            }
        });
        audioplayer.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(3);
            }
        });
        audioplayer.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(4);
            }
        });
        audioplayer.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playcorrespondingaudio(5);
            }
        });

        apply_lightmode_if_found_to_play_buttons();
    }

    private void apply_lightmode_if_found_to_play_buttons() {
        for(ImageView audioplayers:audioplayer){
            try {
                Glide.with(context).load(R.drawable.playlightmode).into(audioplayers);
            } catch (Exception ignored) {
                audioplayers.setImageDrawable(resources.getDrawable(R.drawable.playlightmode));
            }
        }
    }

    private void apply_already_selected_mouadin(int prayertobemodified) {
        selectedadan = Integer.valueOf(selections[prayertobemodified].split(",")[0]) - 1;
        adansselection.get(selectedadan).setBackground(getResources().getDrawable(R.drawable.selectionreponsivereverse));
    }

    private void playcorrespondingaudio(int i){
        if(!audioisplaying || currentlyplayingadan!=i){
            audioisplaying = true;
            if(darkmode) {
                try {
                    Glide.with(context).load(R.drawable.play).into(audioplayer.get(currentlyplayingadan));
                } catch (Exception ignored) {
                    audioplayer.get(currentlyplayingadan).setImageDrawable(resources.getDrawable(R.drawable.play));
                }
                try {
                    Glide.with(context).load(R.drawable.pause).into(audioplayer.get(i));
                } catch (Exception ignored) {
                    audioplayer.get(i).setImageDrawable(resources.getDrawable(R.drawable.pause));
                }
            } else {
                try {
                    Glide.with(context).load(R.drawable.playlightmode).into(audioplayer.get(currentlyplayingadan));
                } catch (Exception ignored) {
                    audioplayer.get(currentlyplayingadan).setImageDrawable(resources.getDrawable(R.drawable.playlightmode));
                }
                try {
                    Glide.with(context).load(R.drawable.pauselightmode).into(audioplayer.get(i));
                } catch (Exception ignored) {
                    audioplayer.get(i).setImageDrawable(resources.getDrawable(R.drawable.pauselightmode));
                }
            }
            currentlyplayingadan = i;
            playadan(currentlyplayingadan);
        } else {
            audioisplaying = false;
            stopadan();
            try {
                Glide.with(context).load(R.drawable.play).into(audioplayer.get(i));
            } catch (Exception ignored) {
                audioplayer.get(i).setImageDrawable(resources.getDrawable(R.drawable.play));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopadan();
        save_delays_of_changed();
    }

    private void save_delays_of_changed() {
        sql("slat");
        SQLSharing.mycursor.moveToPosition(10);
        ID = SQLSharing.mycursor.getString(0);
        SQLSharing.mydb.updateData(fajrbeforecounter+","+fajraftercounter + " " + dhuhrbeforecounter+","+dhuhraftercounter + " " + asrbeforecounter+","+asraftercounter + " " + maghrebbeforecounter+","+maghrebaftercounter + " " + ishabeforecounter+","+ishaaftercounter ,ID);
    }

    private void stopadan(){
        try{
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        } catch(Exception ignored){}
    }
    private SimpleExoPlayer simpleExoPlayer;
    private void playadan(int adantag) {
        try{
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        } catch(Exception ignored){}
        String adan = "";
        switch(adantag){
            case 0:
                adan = "amine.mp3";
                break;
            case 1:
                adan = "altazi.mp3";
                break;
            case 2:
                adan = "karim.mp3";
                break;
            case 3:
                adan = "ismail.mp3";
                break;
            case 4:
                adan = "afasi.mp3";
                break;
            case 5:
                adan = "madani.mp3";
        }
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(
                this,
                null,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        );
        TrackSelector trackSelector = new DefaultTrackSelector();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                renderersFactory,
                trackSelector
        );
        String userAgent = Util.getUserAgent(this, getResources().getString(R.string.adanner));
        try {
            ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(getResources().getString(R.string.idek) + adan), // file audio ada di folder assets
                    new DefaultDataSourceFactory(this, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null
            );
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        } catch(Exception ignored){}
    }

    private void applyselectedadan(int i, int prayertobemodified) {
        if(selectedadan!=i){
            adansselection.get(i).setBackground(getResources().getDrawable(R.drawable.selectionreponsivereverse));
            if(selectedadan!=-1)
                adansselection.get(selectedadan).setBackground(getResources().getDrawable(R.drawable.selectorresposive));
            selectedadan = i;
            applyselectedadantoadanselections(prayertobemodified, selectedadan + 1);
            update_sql();
        }
    }

    private void apply_font_to_selection() {
        try{
            Typeface arabic_typeface = Typeface.createFromAsset(this.getAssets(), "Tajawal-Light.ttf");
            selectiontitle.setTypeface(arabic_typeface);
            for(int i=0; i<6; i++)
                adansselection.get(i).setTypeface(arabic_typeface);
        } catch(Exception ignored){ print("applying fonts failed"); }
    }

    private void variables_of_selection() {
        adansselection = new ArrayList<>();
        adansselection.add((TextView) findViewById(R.id.adan1));
        adansselection.add((TextView) findViewById(R.id.adan2));
        adansselection.add((TextView) findViewById(R.id.adan3));
        adansselection.add((TextView) findViewById(R.id.adan4));
        adansselection.add((TextView) findViewById(R.id.adan5));
        adansselection.add((TextView) findViewById(R.id.adan6));

        audioplayer = new ArrayList<>();
        audioplayer.add((ImageView) findViewById(R.id.adan1audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan2audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan3audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan4audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan5audioplayer));
        audioplayer.add((ImageView) findViewById(R.id.adan6audioplayer));

        selectiontitle = findViewById(R.id.selectiontitle);
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

    private void applyselectedadantoadanselections(int prayedtobemodified, int selectedmouadin) {
        String[] yes;
        if(prayedtobemodified>0)
            prayedtobemodified++;
        yes = selections[prayedtobemodified].split(",");
        selections[prayedtobemodified] = String.valueOf(selectedmouadin) + "," + yes[1];
        StringBuilder temp = new StringBuilder();
        for(int j=0; j<6; j++){
            temp.append(selections[j]);
            temp.append(" ");
        }
        adanSelections = temp.toString();
    }

    private void update_bitmap(int i) {
        String[] yes;
        yes = selections[i].split(",");
        if(once2){
            switch (yes[1]) {
                case "0":
                    // no ring no vibrate
                    if(darkmode) {
                        try {
                            Glide.with(this).load(R.drawable.soundsoff).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsoff));
                        }
                    } else {
                        try {
                            Glide.with(this).load(R.drawable.soundsofflightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsofflightmode));
                        }
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "1":
                    // vibrate only
                    if(darkmode){
                        try {
                            Glide.with(this).load(R.drawable.vibrate).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.vibrate));
                        }
                    } else {
                        try {
                            Glide.with(this).load(R.drawable.vibratelightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.vibratelightmode));
                        }
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "2":
                    // ring only
                    if(darkmode){
                        try {
                            Glide.with(this).load(R.drawable.soundson).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundson));
                        }
                    }else {
                        try {
                            Glide.with(this).load(R.drawable.soundsonlightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsonlightmode));
                        }
                    }
            }
        } else {
            switch (yes[1]) {
                case "0":
                    // no ring no vibrate
                    if(darkmode) {
                        try {
                            Glide.with(this).load(R.drawable.soundsoff).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsoff));
                        }
                    } else {
                        try {
                            Glide.with(this).load(R.drawable.soundsofflightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsofflightmode));
                        }
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "1":
                    // vibrate only
                    if(darkmode){
                        try {
                            Glide.with(this).load(R.drawable.vibrate).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.vibrate));
                        }
                    } else {
                        try {
                            Glide.with(this).load(R.drawable.vibratelightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.vibratelightmode));
                        }
                    }
                    adans.get(i).setVisibility(View.INVISIBLE);
                    break;
                case "2":
                    // ring only
                    if(darkmode){
                        try {
                            Glide.with(this).load(R.drawable.soundson).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundson));
                        }
                    }else {
                        try {
                            Glide.with(this).load(R.drawable.soundsonlightmode).into(ringmodes.get(i));
                        } catch (Exception ignored) {
                            ringmodes.get(i).setImageDrawable(resources.getDrawable(R.drawable.soundsonlightmode));
                        }
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
                        adans.get(i).setText(resources.getString(R.string.adan1_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan1));
                    break;
                case "2":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan2_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan2));
                    break;
                case "3":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan3_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan3));
                    break;
                case "4":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan4_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan4));
                    break;
                case "5":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan5_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan5));
                    break;
                case "6":
                    if(language.equals("ar"))
                        adans.get(i).setText(resources.getString(R.string.adan6_arabe));
                    else if(language.equals("en"))
                        adans.get(i).setText(resources.getString(R.string.adan6));
            }
        }
    }

    private String delays;
    private void sql_work() {
        sql("slat");
        SQLSharing.mycursor.moveToPosition(1);
        darkmode = SQLSharing.mycursor.getString(1).equals("yes");
        SQLSharing.mycursor.moveToPosition(6);
        language = SQLSharing.mycursor.getString(1);
        SQLSharing.mycursor.moveToNext();
        adanSelections = SQLSharing.mycursor.getString(1);
        selections = adanSelections.split(" ");
        SQLSharing.mycursor.moveToNext();
        main_notification_sql = SQLSharing.mycursor.getString(1);

        SQLSharing.mycursor.moveToNext();
        request_protected_menu = SQLSharing.mycursor.getString(1).equals("yes");
        SQLSharing.mycursor.moveToNext();
        delays = SQLSharing.mycursor.getString(1);

        SQLSharing.mycursor.close();
        SQLSharing.mydb.close();
    }

    private void close_sql() {
        if(SQLSharing.mycursor!=null)
            SQLSharing.mycursor.close();
        if(SQLSharing.mydb!=null)
            SQLSharing.mydb.close();
    }

    private boolean request_protected_menu;
    private String main_notification_sql = "yes";
    private void update_sql() {
        sql("slat");
        if(once){
            once = false;
            SQLSharing.mycursor.moveToPosition(7);
            ID = SQLSharing.mycursor.getString(0);
        }
        SQLSharing.mydb.updateData(adanSelections, ID);
        SQLSharing.mydb.close();
    }

    private LinearLayout selectionmenu, settingsmenu;
    private Switch notiswitch;
    private TextView settingstitle, notitext, mutetitle;
    private Context context;
    private TextView fajrmutebeforeadantitle, fajrmuteafteradantitle, dhuhrmutebeforeadantitle, dhuhrafteradantitle, asrbeforeadantitle, asrafteradantitle;
    private TextView maghrebbeforeadantitle, maghrebafteradantitle, ishabeforeadantitle, ishaafteradantitle;
    private TextView mutefajrtitle, mutedhuhrtitle, muteasrtitle, mutemaghrebtitle, muteishatitle;
    private TextView fajrbeforecounterdisplay, fajraftercounterdisplay, dhuhrbeforecounterdisplay, dhuhraftercounterdisplay, asrbeforecounterdisplay, asraftercounterdisplay, maghrebbeforecounterdisplay, maghrebaftercounterdisplay, ishabeforecounterdisplay, ishaaftercounterdisplay;
    private void prepare_variables() {
        context = this;

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
        selectionmenu = findViewById(R.id.selectionmenu);
        settingsmenu = findViewById(R.id.settingsmenu);
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

        resources = getResources();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopadan();
        close_sql();
        save_delays_of_changed();
        close_sql();
        if(language.equals("ar"))
            Toast.makeText(this, getString(R.string.saved_arabe), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
    }
}
