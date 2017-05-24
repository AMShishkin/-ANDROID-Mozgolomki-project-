package com.example.ams.golovolomki.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ams.golovolomki.Assistants.DatabaseHelper;
import com.example.ams.golovolomki.Assistants.DisplayMetricsHelper;
import com.example.ams.golovolomki.R;
import com.example.ams.golovolomki.apprater.AppRater;
import java.util.concurrent.atomic.AtomicInteger;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        // disable overdraws
        getWindow().setBackgroundDrawable(null);
        // set orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AppRater.app_launched(SplashScreenActivity.this);

        // initialize display metrics
        DisplayMetricsHelper.setDisplayMetrics(getWindowManager().getDefaultDisplay());

        // initialize db
        MainActivity.bdh = new DatabaseHelper(getApplicationContext());
        MainActivity.db = MainActivity.bdh.getReadableDatabase();

        // get db settings
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);
        DatabaseHelper.settingsCursor.moveToFirst();

        AtomicInteger _appStart = new AtomicInteger(Integer.parseInt(DatabaseHelper.settingsCursor.getString(2)));
        _appStart.getAndIncrement();
        MainActivity.bdh.UpdateSettingsAppStart(MainActivity.db, 1, Integer.toString(_appStart.get()));

        startActivity(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(6)) ? new Intent(this, PuzzleActivity.class) : new Intent(this, MainActivity.class));

        MainActivity.db.close();
        finish();
    }
}