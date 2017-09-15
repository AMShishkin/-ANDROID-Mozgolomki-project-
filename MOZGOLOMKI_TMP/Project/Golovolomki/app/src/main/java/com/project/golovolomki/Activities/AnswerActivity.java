package com.project.golovolomki.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.project.golovolomki.Assistants.DatabaseHelper;
import com.project.golovolomki.BuildConfig;
import com.project.golovolomki.R;
import com.project.golovolomki.apprater.AppRater;

public class AnswerActivity extends AppCompatActivity {
    private final AdSize ADSIZE = AdSize.SMART_BANNER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_answer);

        InitializeInterface();

        // SHOW RATE DIALOG ON LAST PAGE
        if (!Boolean.valueOf(DatabaseHelper.settingsCursor.getString(7)) && (PuzzleActivity.mViewPager.getCurrentItem() + 1) == DatabaseHelper.valueCursor.getCount()) {
            AppRater.setDarkTheme();
            AppRater.showRateDialog(AnswerActivity.this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (com.project.golovolomki.Activities.PuzzleActivity.isAnswerAdvise) InitializeAnswer();
        else InitializeAdvice();
    }

    @Override
    public void onBackPressed() {
        this.getBaseContext().startActivity(new Intent(Intent.ACTION_MAIN).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(AnswerActivity.this, PuzzleActivity.class));
    }

    private void InitializeInterface() {
        // TEXT VIEW FONT
        ((TextView)findViewById(R.id.answer_title)).setTypeface(Typefaces.get(getBaseContext(), "fonts/mainFont.ttf"));
        ((TextView)findViewById(R.id.answer_text_view)).setTypeface(Typefaces.get(getBaseContext(), "fonts/cavia_puzzle.ttf"));
        // BUTTON FONT
        ((Button)findViewById(R.id.answer_button_share)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button)findViewById(R.id.answer_button_wiki)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        // TEXT VIEW ALIGNMENT
        ((TextView) findViewById(R.id.answer_text_view)).setGravity(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? Gravity.CENTER : Gravity.START);
        // BUTTONS LISTENER
        View.OnClickListener _oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.answer_button_share:
                        DatabaseHelper.valueCursor.moveToPosition(PuzzleActivity.mViewPager.getCurrentItem());
                        Intent _sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        _sharingIntent.setType("text/plain");
                        String _shareBody, _sharedTitle;
                        if (PuzzleActivity.isAnswerAdvise) {
                            _sharedTitle = "Поделиться ответом";
                            _shareBody = "• [ " + DatabaseHelper.valueCursor.getString(1) + " ] •\n• [ ответ ] •\n\n" + DatabaseHelper.valueCursor.getString(4).trim();
                        }
                        else {
                            _shareBody = "• [ " + DatabaseHelper.valueCursor.getString(1) + " ] •\n• [ подсказка ] •\n\n" + DatabaseHelper.valueCursor.getString(5).trim();
                            _sharedTitle ="Поделиться подсказкой";
                        }

                        _sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, DatabaseHelper.valueCursor.getString(1));
                        _sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, _shareBody);
                        startActivity(Intent.createChooser(_sharingIntent, _sharedTitle));
                        break;

                    case R.id.answer_button_wiki:
                        if (PuzzleActivity.isAnswerAdvise) {
                            DatabaseHelper.valueCursor.moveToPosition(PuzzleActivity.mViewPager.getCurrentItem());
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://ru.wikipedia.org/wiki/" + DatabaseHelper.valueCursor.getString(7))));
                        }
                        else {
                            AnswerActivity.this.startActivity(new Intent(AnswerActivity.this, AnswerActivity.class));
                            PuzzleActivity.isAnswerAdvise = true;
                        }
                        break;
                }
            }
        };

        findViewById(R.id.answer_button_share).setOnClickListener(_oclBtn);
        findViewById(R.id.answer_button_wiki).setOnClickListener(_oclBtn);
        // ADMOB BLOCK
        InitializeAdmobBlock();
    }

    private void InitializeAdmobBlock() {
        AdView _adContainer = (AdView) findViewById(R.id.adViewAnswer);
        AdView _mAdView = new AdView(getApplicationContext());
        _mAdView.setAdSize(ADSIZE);
        _mAdView.setAdUnitId("ca-app-pub-9188008799728984/1523306959");
        _adContainer.addView(_mAdView);
        AdRequest _adRequest;

        if (BuildConfig.DEBUG) _adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("BEA3544ED9561600F831EE6B5A414F49") // Galaxy A3 2016
                .build();
        else _adRequest = new AdRequest.Builder().build();

        _mAdView.loadAd(_adRequest);
    }

    private void InitializeAnswer() {
        ((TextView)findViewById(R.id.answer_title)).setText("||||||||ОТВЕТ||||||||");

        DatabaseHelper.valueCursor.moveToFirst();
        DatabaseHelper.valueCursor.move(PuzzleActivity.mViewPager.getCurrentItem());

        ((TextView) findViewById(R.id.answer_text_view)).setText(DatabaseHelper.valueCursor.getString(4));
        ((Button) findViewById(R.id.answer_button_wiki)).setText("ВИКИПЕДИЯ");

        MainActivity.db = MainActivity.bdh.getReadableDatabase();
        MainActivity.bdh.UpdatePuzzleState(MainActivity.db, DatabaseHelper.valueCursor.getInt(0), "ПРОЧИТАНО");
        MainActivity.db.close();
    }

    private void InitializeAdvice() {
        ((TextView) findViewById(R.id.answer_title)).setText("|||||||ПОДСКАЗКА|||||||");

        DatabaseHelper.valueCursor.moveToFirst();
        DatabaseHelper.valueCursor.move((PuzzleActivity.mViewPager.getCurrentItem()));

        ((TextView) findViewById(R.id.answer_text_view)).setText(DatabaseHelper.valueCursor.getString(5));
        ((Button) findViewById(R.id.answer_button_wiki)).setText("◦ОТВЕТ◦");
    }
}