package com.example.ams.golovolomki.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.ams.golovolomki.Assistants.DatabaseHelper;
import com.example.ams.golovolomki.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AnswerActivity extends AppCompatActivity {
    private final AdSize ADSIZE = AdSize.SMART_BANNER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_answer);

        InitializeMenuButtons();
        InitializeFonts();

        this.findViewById(R.id.answer_text_view).setTextAlignment(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_VIEW_START);

        // initialize ads RELEASE
        //AdView mPublisherAdView = (PublisherAdView)rootView.findViewById(R.id.adView);
        //  PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        //  mPublisherAdView.loadAd(adRequest);

        // TEST ADS
        AdView adContainer = (AdView)findViewById(R.id.adViewAnswer);

        AdView mAdView = new AdView(getApplicationContext());
        mAdView.setAdSize(ADSIZE);
        mAdView.setAdUnitId("ca-app-pub-9188008799728984/1523306959");

        adContainer.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("BEA3544ED9561600F831EE6B5A414F49") // Galaxy A3 2016
                .build();

        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (PuzzleActivity.isAnswerAdvise) InitializeAnswer();
        else InitializeAdvice();
    }

    @Override
    public void onBackPressed() {
        this.getBaseContext().startActivity(new Intent(Intent.ACTION_MAIN).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(AnswerActivity.this, PuzzleActivity.class));
    }

    private void InitializeFonts() {
        ((TextView)findViewById(R.id.answer_title)).setTypeface(Typefaces.get(getBaseContext(), "fonts/mainFont.ttf"));
        ((TextView)findViewById(R.id.answer_text_view)).setTypeface(Typefaces.get(getBaseContext(), "fonts/cavia_puzzle.ttf"));

        ((Button)findViewById(R.id.answer_button_share)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button)findViewById(R.id.answer_button_wiki)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
    }

    private void InitializeMenuButtons() {
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
    }

    private void InitializeAnswer() {
        InitializeAnswerAdvice();

        DatabaseHelper.valueCursor.moveToFirst();
        DatabaseHelper.valueCursor.move(PuzzleActivity.mViewPager.getCurrentItem());

        ((TextView)findViewById(R.id.answer_text_view)).setText(DatabaseHelper.valueCursor.getString(4));
        ((Button)findViewById(R.id.answer_button_wiki)).setText("ВИКИПЕДИЯ");

        MainActivity.db = MainActivity.bdh.getReadableDatabase();

        MainActivity.bdh.UpdatePuzzleState(MainActivity.db, DatabaseHelper.valueCursor.getInt(0), "ПРОЧИТАНО");
        MainActivity.db.close();
    }

    private void InitializeAdvice() {
        InitializeAnswerAdvice();

        DatabaseHelper.valueCursor.moveToFirst();
        DatabaseHelper.valueCursor.move((PuzzleActivity.mViewPager.getCurrentItem()));
        ((TextView)findViewById(R.id.answer_text_view)).setText(DatabaseHelper.valueCursor.getString(5));
        ((Button)findViewById(R.id.answer_button_wiki)).setText("◦ОТВЕТ◦");
    }

    private void InitializeAnswerAdvice() {
        if (PuzzleActivity.isAnswerAdvise) ((TextView)findViewById(R.id.answer_title)).setText("||||||ОТВЕТ||||||");
        else ((TextView)findViewById(R.id.answer_title)).setText("||||ПОДСКАЗКА||||");
    }
}