package com.example.ams.golovolomki.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.ams.golovolomki.Alert.Alert;
import com.example.ams.golovolomki.Alert.Alerter;
import com.example.ams.golovolomki.Assistants.DatabaseHelper;
import com.example.ams.golovolomki.R;
import com.example.ams.golovolomki.apprater.AppRater;
import com.google.android.gms.ads.MobileAds;

public class PuzzleActivity extends AppCompatActivity {
    private static PagerAdapter mSectionsPagerAdapter;
    public static int dataBaseCount = 1;
    public static int currentPuzzleNumber = 0;
    public static ViewPager mViewPager;
    public static Button buttonFavorite;
    public static boolean isAnswerAdvise = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(null);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_puzzle);

        InitializeFonts();
        InitializeMenuButtons();

        // initialize db
        MainActivity.bdh = new DatabaseHelper(getApplicationContext());
        MainActivity.db = MainActivity.bdh.getReadableDatabase();
        // return db settings
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);

        // return db favorite
        DatabaseHelper.favoriteCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_FAVORITE, null);

        DatabaseHelper.valueCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_PUZZLE, null);

        dataBaseCount = DatabaseHelper.valueCursor.getCount();
        MainActivity.db = MainActivity.bdh.getReadableDatabase();
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);
        DatabaseHelper.settingsCursor.moveToFirst();

        // set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // select current item
        mViewPager.setCurrentItem(Integer.parseInt(DatabaseHelper.settingsCursor.getString(1)));

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9188008799728984~8113288152");

        if (Integer.parseInt(DatabaseHelper.settingsCursor.getString(2)) >= Integer.parseInt(DatabaseHelper.settingsCursor.getString(8)) && !Boolean.valueOf(DatabaseHelper.settingsCursor.getString(7))) {
            AppRater.setDarkTheme();
            AppRater.showRateDialog(PuzzleActivity.this);
        }



    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.db = MainActivity.bdh.getReadableDatabase();
        // get db settings
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);
        DatabaseHelper.settingsCursor.moveToFirst();

        DatabaseHelper.favoriteCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_FAVORITE, null);
        DatabaseHelper.favoriteCursor.moveToFirst();

        InitializeFonts();
    }

    @Override
    public void onBackPressed() {
        SaveIndexSelectedPuzzle();
        Alert.isShowing = false;
        startActivity(new Intent(PuzzleActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void InitializeFonts() {
        // buttons
        ((Button)findViewById(R.id.button_puzzle_answer)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button)findViewById(R.id.button_puzzle_advice)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button)findViewById(R.id.puzzle_button_share)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button)findViewById(R.id.button_puzzle_favorite)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
    }

    private void InitializeMenuButtons() {
        Button _buttonAnswer, _buttonAdvice, _buttonShare, _buttonFavorite;

        _buttonAnswer = (Button)findViewById(R.id.button_puzzle_answer);
        _buttonAdvice = (Button)findViewById(R.id.button_puzzle_advice);
        _buttonShare = (Button)findViewById(R.id.puzzle_button_share);
        _buttonFavorite = buttonFavorite = (Button)findViewById(R.id.button_puzzle_favorite);

        View.OnClickListener _oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.button_puzzle_answer:
                        PuzzleActivity.this.startActivity(new Intent(PuzzleActivity.this, AnswerActivity.class));
                        isAnswerAdvise = true;
                        SaveIndexSelectedPuzzle();
                        break;

                    case R.id.button_puzzle_advice:
                        PuzzleActivity.this.startActivity(new Intent(PuzzleActivity.this, AnswerActivity.class));
                        isAnswerAdvise = false;
                        SaveIndexSelectedPuzzle();
                        break;

                    case R.id.puzzle_button_share:
                        DatabaseHelper.valueCursor.moveToPosition(mViewPager.getCurrentItem());
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "• [ " + DatabaseHelper.valueCursor.getString(1) + " ] •\n• [ головоломка ] •\n\n" + DatabaseHelper.valueCursor.getString(3);
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "[МОЗГОЛОМКИ]");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        PuzzleActivity.this.startActivity(Intent.createChooser(sharingIntent, "Поделиться головоломкой"));
                        break;

                    case R.id.button_puzzle_favorite:
                        if (!Alerter.getStateShowing()) PuzzleActivity.this.ResetFavoriteView();
                        break;
                }
            }
        };

        _buttonAnswer.setOnClickListener(_oclBtn);
        _buttonAdvice.setOnClickListener(_oclBtn);
        _buttonShare.setOnClickListener(_oclBtn);
        _buttonFavorite.setOnClickListener(_oclBtn);
    }

    private void ResetFavoriteView() {
        SaveIndexSelectedPuzzle();

        dataBaseCount = DatabaseHelper.valueCursor.getCount();
        MainActivity.db = MainActivity.bdh.getReadableDatabase();
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);
        DatabaseHelper.settingsCursor.moveToFirst();

        DatabaseHelper.valueCursor.moveToPosition(mViewPager.getCurrentItem());

        if (buttonFavorite.getText().equals("ДОБАВИТЬ")) {
            Alerter.create(this)
                    .setTitle("ДОБАВЛЕНО")
                    .setText("Головоломка №" + DatabaseHelper.valueCursor.getString(0) + " «" + DatabaseHelper.valueCursor.getString(1) + "»\nдобавлена в список избранного")
                    .setBackgroundColor(R.color.alertAdd)
                    .setDuration(2200)
                    .show();

            MainActivity.bdh.UpdatePuzzleFavorite(MainActivity.db, mViewPager.getCurrentItem() + 1, "★");
        } else {
            Alerter.create(this)
                    .setTitle("УДАЛЕНО")
                    .setText("Головоломка №" + DatabaseHelper.valueCursor.getString(0) + " «" + DatabaseHelper.valueCursor.getString(1) + "»\nудалена из списка избранного")
                    .setBackgroundColor(R.color.alertRemove)
                    .setDuration(2200)
                    .show();

            MainActivity.bdh.UpdatePuzzleFavorite(MainActivity.db, mViewPager.getCurrentItem() + 1, "");
        }

        MainActivity.db.close();

        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(Integer.parseInt(DatabaseHelper.settingsCursor.getString(1)));
    }

    private void SaveIndexSelectedPuzzle() {
        MainActivity.db = MainActivity.bdh.getReadableDatabase();
        MainActivity.bdh.UpdateSettingsState(MainActivity.db, 1, "" +  mViewPager.getCurrentItem());
        MainActivity.db.close();
    }
}