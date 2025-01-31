package com.project.golovolomki.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.project.golovolomki.Assistants.DatabaseHelper;
import com.project.golovolomki.Assistants.DisplayMetricsHelper;
import com.project.golovolomki.R;
import com.project.golovolomki.apprater.AppRater;

import java.util.concurrent.atomic.AtomicInteger;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppRater.app_launched(SplashScreenActivity.this);
        // INITIALIZE DISPLAY METRICS
        DisplayMetricsHelper.setDisplayMetrics(getWindowManager().getDefaultDisplay());
        // INITIALIZE D.B.
        MainActivity.bdh = new DatabaseHelper(getApplicationContext());
        MainActivity.db = MainActivity.bdh.getReadableDatabase();
        // GET D.B. SETTINGS
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