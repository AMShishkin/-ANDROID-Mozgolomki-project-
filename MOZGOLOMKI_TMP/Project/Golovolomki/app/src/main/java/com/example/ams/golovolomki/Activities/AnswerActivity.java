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
    AdSize adSize = AdSize.SMART_BANNER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(null);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_answer);

        InitializeMenuButtons();



        // initialize ads RELEASE
        //AdView mPublisherAdView = (PublisherAdView)rootView.findViewById(R.id.adView);
        //  PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        //  mPublisherAdView.loadAd(adRequest);


        this.findViewById(R.id.answer_text_view).setTextAlignment(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_VIEW_START);


        // TEST ADS
        AdView adContainer = (AdView)findViewById(R.id.adViewAnswer);

        AdView mAdView = new AdView(getApplicationContext());
        mAdView.setAdSize(adSize);
        mAdView.setAdUnitId("ca-app-pub-9188008799728984/1523306959");

        adContainer.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("05B14F908C488B9820E02532746B28A1") // [Genymotion] Nexus 7
                .addTestDevice("DCB08892F1089BCA2AB568F6009342A7") // [Genymotion] Galaxy S5
                .addTestDevice("EE9FA0F13B20B55ADD88A77A9FBECC3F") // [Genymotion] CP 768x1280
                .addTestDevice("3F7F5FE726961D6E2600EF0E7D278EC2") // [Genymotion] CP 480x800
                .addTestDevice("AA0714F5CF37C6C8EA09E8E5E8BFF156") // [Genymotion] Nexus 7 800x1280
                .addTestDevice("91AC0C17F5D5838404785E3EBA501184") // Galaxy Tab E
                .addTestDevice("616AF26EDDBBEBAF18659E076AD01080") // Galaxy s3
                .addTestDevice("2677F1E886AFD81B7E78E0B243422EF0") // Nexus 6
                .addTestDevice("BEA3544ED9561600F831EE6B5A414F49") // Galaxy A3 2016
                .addTestDevice("27E76A584DAB315A77C658037558DDDA") // Fly iq4410
                .addTestDevice("7CADE54B631E24530684B71F6B699170") // Nexus 7
                .addTestDevice("B2DD43156D4EC31C1224559A0D77CFAB") // Mi4
                .addTestDevice("5D3F192EF9029C015917D33F89F2A99A") // Shield
                .addTestDevice("328CA476D709D83BFFCF8E03B9AF0584") // LG Optimus L70
                .build();

        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

        InitializeFonts();

        InitializeAnswerAdvice();

        if (PuzzleActivity.isAnswerAdvise) InitializeAnswer();
        else InitializeAdvice();
    }

    @Override
    public void onBackPressed() {
        this.getBaseContext().startActivity(new Intent(Intent.ACTION_MAIN).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(AnswerActivity.this, PuzzleActivity.class));
    }

    private void InitializeFonts() {
        // text views
        ((TextView)findViewById(R.id.answer_title)).setTypeface(Typefaces.get(getBaseContext(), "fonts/mainFont.ttf"));
        ((TextView)findViewById(R.id.answer_text_view)).setTypeface(Typefaces.get(getBaseContext(), "fonts/cavia_puzzle.ttf"));
        // buttons
        ((Button)findViewById(R.id.answer_button_share)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button)findViewById(R.id.answer_button_wiki)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
    }

    private void InitializeMenuButtons() {
        Button _buttonShare, _buttonViki;

        _buttonShare = (Button)findViewById(R.id.answer_button_share);
        _buttonViki = (Button)findViewById(R.id.answer_button_wiki);

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

        _buttonShare.setOnClickListener(_oclBtn);
        _buttonViki.setOnClickListener(_oclBtn);
    }

    private void InitializeAnswer() {
        DatabaseHelper.valueCursor.moveToFirst();
        DatabaseHelper.valueCursor.move(PuzzleActivity.mViewPager.getCurrentItem());

        ((TextView)findViewById(R.id.answer_text_view)).setText(DatabaseHelper.valueCursor.getString(4));
        ((Button)findViewById(R.id.answer_button_wiki)).setText("ВИКИПЕДИЯ");

        MainActivity.db = MainActivity.bdh.getReadableDatabase();

        MainActivity.bdh.UpdatePuzzleState(MainActivity.db, DatabaseHelper.valueCursor.getInt(0), "ПРОЧИТАНО");
        MainActivity.db.close();
    }

    private void InitializeAdvice() {
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