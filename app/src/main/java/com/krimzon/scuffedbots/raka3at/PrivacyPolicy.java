package com.krimzon.scuffedbots.raka3at;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
    }

    public void backClicked(View view) {
        exit();
    }

    private void exit() {
        Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}
