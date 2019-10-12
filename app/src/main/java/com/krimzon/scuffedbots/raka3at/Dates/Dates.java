package com.krimzon.scuffedbots.raka3at.Dates;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krimzon.scuffedbots.raka3at.R;


public class Dates extends Fragment {

    protected TextView dateerr;
    protected Typeface arabic_typeface;

    public Dates() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates, container, false);
        dateerr = view.findViewById(R.id.daterr);
        arabic_typeface = Typeface.createFromAsset(view.getContext().getAssets(),  "Tajawal-Light.ttf");
        assert getArguments() != null;
        String date = getArguments().getString("date");
        dateerr.setTypeface(arabic_typeface);
        dateerr.setText(date);
        return view;
    }
}
