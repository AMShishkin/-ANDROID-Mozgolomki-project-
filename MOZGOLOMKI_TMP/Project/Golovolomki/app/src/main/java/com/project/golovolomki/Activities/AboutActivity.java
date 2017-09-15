package com.project.golovolomki.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.project.golovolomki.Assistants.DatabaseHelper;
import com.project.golovolomki.R;
import com.project.golovolomki.apprater.AppRater;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_about);

        InitializeInterface();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void InitializeInterface() {
        // TEXT VIEW FONT
        ((TextView) findViewById(R.id.about_title)).setTypeface(Typefaces.get(getBaseContext(), "fonts/mainFont.ttf"));
        ((TextView) findViewById(R.id.about_main_text_top)).setTypeface(Typefaces.get(getBaseContext(), "fonts/cavia_puzzle.ttf"));
        ((TextView) findViewById(R.id.about_main_text_middle)).setTypeface(Typefaces.get(getBaseContext(), "fonts/cavia_puzzle.ttf"));
        ((TextView) findViewById(R.id.about_main_text_copyright)).setTypeface(Typefaces.get(getBaseContext(), "fonts/cavia_puzzle.ttf"));
        // TEXT VIEW ALIGNMENT
        ((TextView) findViewById(R.id.about_main_text_top)).setGravity(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? Gravity.CENTER : Gravity.START);
        ((TextView) findViewById(R.id.about_main_text_middle)).setGravity(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? Gravity.CENTER : Gravity.START);
        // BUTTON FONT
        ((Button) findViewById(R.id.about_button_email)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button) findViewById(R.id.about_button_app)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button) findViewById(R.id.about_button_rate)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        // BUTTON LISTENER
        View.OnClickListener _oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.about_button_rate:
                        AppRater.setDarkTheme();
                        AppRater.showRateDialog(AboutActivity.this);
                        break;

                    case R.id.about_button_app:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        }
                        catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        break;

                    case R.id.about_button_email:
                        Intent _emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "AMShishkin.vrn@gmail.com", null));
                        _emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[MOZGOLOMKI]");
                        startActivity(Intent.createChooser(_emailIntent, "Написать автору"));
                        break;
                }
            }
        };

        findViewById(R.id.about_button_rate).setOnClickListener(_oclBtn);
        findViewById(R.id.about_button_app).setOnClickListener(_oclBtn);
        findViewById(R.id.about_button_email).setOnClickListener(_oclBtn);
    }
}