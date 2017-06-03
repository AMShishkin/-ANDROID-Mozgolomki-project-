package com.example.ams.golovolomki.Activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ams.golovolomki.Assistants.DatabaseHelper;
import com.example.ams.golovolomki.BuildConfig;
import com.example.ams.golovolomki.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
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




        Picasso.with(rootView.getContext())
                .load(DatabaseHelper.listImages.get(PuzzleActivity.currentPuzzleNumber))
                .resize(384, 256)
                .placeholder(R.drawable.img0) // заглушка до загрузки изображения
                .into((ImageView)rootView.findViewById(R.id.imageView2));


        if (textViewSection == null) textViewSection = (TextView)rootView.findViewById(R.id.section_label);
        textViewSection.setText(DatabaseHelper.valueCursor.getString(1));
        textViewSection.setTypeface(Typefaces.get(inflater.getContext(), "fonts/title_puzzle.ttf"));

        // text view fragment puzzle <- main puzzle text
        if (textViewFragmentPuzzle == null) textViewFragmentPuzzle = (TextView)rootView.findViewById(R.id.textViewFragmentPuzzle);
        rootView.findViewById(R.id.textViewFragmentPuzzle).setTextAlignment(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_VIEW_START);
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
        if (DatabaseHelper.favoriteCursor.getString(2).equals("")) textViewState.setText("ЕЩЕ НЕ ПРОЧИТАНО");
        else textViewState.setText(DatabaseHelper.favoriteCursor.getString(2));


        // text favorite
        TextView textView = (TextView)rootView.findViewById(R.id.puzzle_favorite);
        if (DatabaseHelper.favoriteCursor.getString(1).equals("★")) textView.setText("★");
        else textView.setText("☆");
        textView.setTypeface(Typefaces.get(inflater.getContext(), "fonts/cavia_puzzle.ttf"));

        if (BuildConfig.DEBUG) {
            AdView adContainer = (AdView) rootView.findViewById(R.id.adView);
            AdView mAdView = new AdView(rootView.getContext());
            mAdView.setAdSize(adSize);
            mAdView.setAdUnitId("ca-app-pub-9188008799728984/3543487752");
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
        else
        {
            AdView mPublisherAdView = (AdView) rootView.findViewById(R.id.adView);
            mPublisherAdView.setAdUnitId("ca-app-pub-9188008799728984/3543487752");
            AdRequest adRequest = new  AdRequest.Builder().build();
            mPublisherAdView.loadAd(adRequest);
        }

        return rootView;
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