package com.project.golovolomki.Activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.project.golovolomki.Assistants.DatabaseHelper;
import com.project.golovolomki.BuildConfig;
import com.project.golovolomki.R;
import com.project.golovolomki.apprater.AppRater;
import com.squareup.picasso.Picasso;

public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private TextView textViewSection, textViewFragmentPuzzle, textViewComplexity, textViewState;
    private AdSize adSize = AdSize.SMART_BANNER;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PuzzleActivity.currentPuzzleNumber = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        FavoriteState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FavoriteState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (PuzzleActivity.currentPuzzleNumber <= DatabaseHelper.valueCursor.getCount() && PuzzleActivity.currentPuzzleNumber >= 0)
            DatabaseHelper.valueCursor.moveToPosition(PuzzleActivity.currentPuzzleNumber);

        if (PuzzleActivity.currentPuzzleNumber <= DatabaseHelper.favoriteCursor.getCount() && PuzzleActivity.currentPuzzleNumber >= 0)
            DatabaseHelper.favoriteCursor.moveToPosition(PuzzleActivity.currentPuzzleNumber);

        rootView = inflater.inflate(R.layout.fragment_puzzle, null);

        // set image
        Picasso.with(rootView.getContext())
                .load(DatabaseHelper.listImages.get(PuzzleActivity.currentPuzzleNumber))

                .placeholder(R.drawable.img99999) // заглушка до загрузки изображения
                .into((ImageView)rootView.findViewById(R.id.imageView2));


        if (textViewSection == null) textViewSection = (TextView)rootView.findViewById(R.id.section_label);
        textViewSection.setText(DatabaseHelper.valueCursor.getString(1));
        textViewSection.setTypeface(Typefaces.get(inflater.getContext(), "fonts/title_puzzle.ttf"));

        // text view fragment puzzle <- main puzzle text
        if (textViewFragmentPuzzle == null) textViewFragmentPuzzle = (TextView)rootView.findViewById(R.id.textViewFragmentPuzzle);
        ((TextView) rootView.findViewById(R.id.textViewFragmentPuzzle)).setGravity(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? Gravity.CENTER : Gravity.START);
        textViewFragmentPuzzle.setText(DatabaseHelper.valueCursor.getString(3));
        textViewFragmentPuzzle.setTypeface(Typefaces.get(inflater.getContext(), "fonts/cavia_puzzle.ttf"));

        // text complexity
        if (textViewComplexity == null) textViewComplexity = (TextView)rootView.findViewById(R.id.puzzle_complexity);
        textViewComplexity.setTypeface(Typefaces.get(inflater.getContext(), "fonts/cavia_puzzle.ttf"));
        textViewComplexity.setTypeface(textViewComplexity.getTypeface(), Typeface.BOLD);
        textViewComplexity.setText(DatabaseHelper.valueCursor.getString(6));

        // text state
        if (textViewState == null) textViewState = (TextView)rootView.findViewById(R.id.puzzle_state);
        textViewState.setTypeface(Typefaces.get(inflater.getContext(), "fonts/cavia_puzzle.ttf"));
        textViewState.setTypeface(textViewState.getTypeface(), Typeface.BOLD);
        if (DatabaseHelper.favoriteCursor.getString(2).equals("")) textViewState.setText("НОВАЯ");
        else textViewState.setText(DatabaseHelper.favoriteCursor.getString(2));

        // text favorite
        TextView textView = (TextView)rootView.findViewById(R.id.puzzle_favorite);
        if (DatabaseHelper.favoriteCursor.getString(1).equals("★")) textView.setText("★");
        else textView.setText("☆");
        textView.setTypeface(Typefaces.get(inflater.getContext(), "fonts/cavia_puzzle.ttf"));

        InitializeAdmobBlcok(rootView, adSize);

        return rootView;
    }

    private static void InitializeAdmobBlcok(View rootView, AdSize adSize) {
        AdView _adContainer = (AdView) rootView.findViewById(R.id.adView);
        AdView _mAdView = new AdView(rootView.getContext());
        _mAdView.setAdSize(adSize);
        _mAdView.setAdUnitId("ca-app-pub-9188008799728984/3543487752");
        _adContainer.addView(_mAdView);
        AdRequest _adRequest;

        if (BuildConfig.DEBUG) _adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("BEA3544ED9561600F831EE6B5A414F49") // Galaxy A3 2016
                .build();
        else _adRequest = new AdRequest.Builder().build();

        _mAdView.loadAd(_adRequest);
    }

    private void FavoriteState() {
        if (MainActivity.bdh.getReadableDatabase() != null) {
            MainActivity.db = MainActivity.bdh.getReadableDatabase();
            DatabaseHelper.favoriteCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_FAVORITE, null);
            DatabaseHelper.favoriteCursor.move((PuzzleActivity.mViewPager.getCurrentItem() + 1));
        }

        if (DatabaseHelper.favoriteCursor.getString(1).trim().equals("★")) PuzzleActivity.buttonFavorite.setText("УДАЛИТЬ");
        else PuzzleActivity.buttonFavorite.setText("ДОБАВИТЬ");

        MainActivity.db.close();
    }

    public static PlaceholderFragment newInstance(int sectionNumber) {
        Bundle _args = new Bundle();
        _args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        PlaceholderFragment _fragment = new PlaceholderFragment();
        _fragment.setArguments(_args);
        return _fragment;
    }
}

class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PuzzleActivity.dataBaseCount;
    }

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
}