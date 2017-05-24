package com.example.ams.golovolomki.Activities;

/*
values-sw720dp          10.1” tablet 1280x800 mdpi

values-sw600dp          7.0”  tablet 1024x600 mdpi

values-sw480dp          5.4”  480x854 mdpi
values-sw480dp          5.1”  480x800 mdpi

values-xxhdpi           5.5"  1080x1920 xxhdpi
values-xxhdpi           5.5"  1440x2560 xxhdpi

values-xhdpi            4.7”   1280x720 xhdpi
values-xhdpi            4.65”  720x1280 xhdpi

values-hdpi             4.0” 480x800 hdpi
values-hdpi             3.7” 480x854 hdpi

values-mdpi             3.2” 320x480 mdpi

values-ldpi             3.4” 240x432 ldpi
values-ldpi             3.3” 240x400 ldpi
values-ldpi             2.7” 240x320 ldpi
 */

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.Toast;
import com.example.ams.golovolomki.MainActivityViewList.Adapter;
import com.example.ams.golovolomki.Assistants.DatabaseHelper;
import com.example.ams.golovolomki.R;
import com.example.ams.golovolomki.MainActivityViewList.ViewListItem;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private final String TOAST_EXIT_MSG = "НАЖМИТЕ ЕЩЕ РАЗ ДЛЯ ВЫХОДА",
                         TOAST_ERROR_SHOW_FAVORITE_MSG = "ДАННОЕ ДЕЙСТВИЕ НЕВОЗМОЖНО",
                         TOAST_LAST_PUZZLE_MSG = "ВЫ ОСТАНОВИЛИСЬ НА № ",
                         TOAST_FAVORITE_LIST_EMPTY = "СПИСОК ИЗБРАННОГО ПУСТ",
                         FAVORITE_BUTTON_TEXT_TRUE = "СПИСОК",
                         FAVORITE_BUTTON_TEXT_FALSE = "ИЗБРАННОЕ";
    private boolean isFavorite = false;
    private long back_pressed = 0;
    // main view list
    private ArrayList<ViewListItem> listItems = new ArrayList<ViewListItem>(50);
    private ListView listPuzzles, listPuzzleItem;

    private int scrollSpeed = 15;


    // !!!! ВОЗМОЖНО ВЫНЕСТИ СТАТИКУ В ДР КЛАСС
    public static SQLiteDatabase db;
    public static DatabaseHelper bdh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_main);
        // set orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // initialize db
        MainActivity.bdh = new DatabaseHelper(getApplicationContext());
        MainActivity.db = MainActivity.bdh.getReadableDatabase();

        // return db settings
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);
        while (DatabaseHelper.settingsCursor.moveToNext()) MainActivity.bdh.onUpgrade(MainActivity.db, DatabaseHelper.settingsCursor.getInt(3), DatabaseHelper.SCHEMA);

        // return db favorite
        DatabaseHelper.favoriteCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_FAVORITE, null);
        MainActivity.bdh.close();

        InitializeMenuButtons();
        InitializeButtonFavoriteState();
        InitializeFonts();
    }

    @Override
    public void onBackPressed() {
        if (isFavorite) {
            isFavorite = !isFavorite;
            InitializeMainListView();
            InitializeButtonFavoriteState();
        }
        else {
            // double click
            if (back_pressed + 2000 > System.currentTimeMillis())
                startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            else Toasty.info(getBaseContext(), TOAST_EXIT_MSG, Toast.LENGTH_SHORT, true).show();

            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.db = MainActivity.bdh.getReadableDatabase();
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);
        DatabaseHelper.settingsCursor.moveToFirst();
        db.close();

        if (isFavorite) InitializeMainFavoriteListView();
        else InitializeMainListView();

        InitializeButtonFavoriteState();
    }

    private void InitializeFonts() {
        ((TextView) findViewById(R.id.main_title)).setTypeface(Typefaces.get(getBaseContext(), "fonts/mainFont.ttf"));
        ((Button) findViewById(R.id.button_settings)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button) findViewById(R.id.button_about)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button) findViewById(R.id.button_favorite)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
        ((Button) findViewById(R.id.button_tolastindex)).setTypeface(Typefaces.get(getBaseContext(), "fonts/titleItem.ttf"));
    }

    private void InitializeMainFavoriteListView() {
        AdapterView.OnItemClickListener _onItemClickListener;

        listItems.clear();
        db = bdh.getReadableDatabase();
        DatabaseHelper.valueCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_PUZZLE, null);

        // set db favorite
        DatabaseHelper.favoriteCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_FAVORITE, null);
        DatabaseHelper.favoriteCursor.moveToFirst();

        CreatingFavoriteAdapter();

        db.close();

        listItems.trimToSize();

        // set adapter
        listPuzzles = (ListView) findViewById(R.id.mainList);
        listPuzzles.setAdapter(new Adapter(this, listItems));

        // set scroll speed
        listPuzzleItem.setFriction(ViewConfiguration.getScrollFriction() * scrollSpeed);

        _onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // save index item
                db = bdh.getReadableDatabase();

                bdh.UpdateSettingsState(db, 1, "" + (listItems.get((int) (id)).index -1));
                db.close();

                Intent _intent = new Intent();
                _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _intent.setClass(MainActivity.this, PuzzleActivity.class);
                MainActivity.this.getBaseContext().startActivity(_intent);
            }
        };

        // initialize items click
        listPuzzleItem.setOnItemClickListener(_onItemClickListener);

        // show toast
        if (listPuzzles.getCount() == 0) Toasty.info(getBaseContext(), TOAST_FAVORITE_LIST_EMPTY, Toast.LENGTH_SHORT, true).show();
    }

    private void InitializeMainListView() {
        AdapterView.OnItemClickListener _onItemClickListener;

        listItems.clear();

        db = bdh.getReadableDatabase();
        DatabaseHelper.valueCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_PUZZLE, null);

        // get db settings
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);
        DatabaseHelper.settingsCursor.moveToFirst();

        // get db favorite
        DatabaseHelper.favoriteCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_FAVORITE, null);
        DatabaseHelper.favoriteCursor.moveToFirst();

        // initialize list view (true = first to last)
        if (DatabaseHelper.settingsCursor.getString(4).equals("true")) CreateAssortedAdapter();
        else CreatingAnAssortedAdapter();

        db.close();

        listItems.trimToSize();

        // set current adapter
        listPuzzleItem = (ListView) findViewById(R.id.mainList);
        listPuzzleItem.setAdapter(new Adapter(this, listItems));

        // scrolling list view to index last puzzle
        if (DatabaseHelper.settingsCursor.getString(4).equals("true")) listPuzzleItem.setSelection(Integer.parseInt(DatabaseHelper.settingsCursor.getString(1)));
        else listPuzzleItem.setSelection(listPuzzleItem.getCount() - Integer.parseInt(DatabaseHelper.settingsCursor.getString(1)) - 1);

        // set scroll speed
        listPuzzleItem.setFriction(ViewConfiguration.getScrollFriction() * scrollSpeed);

        _onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // save index item
                MainActivity.db = MainActivity.bdh.getReadableDatabase();

                if (DatabaseHelper.settingsCursor.getString(4).equals("true")) MainActivity.bdh.UpdateSettingsState(MainActivity.db, 1, String.valueOf(id));
                else MainActivity.bdh.UpdateSettingsState(MainActivity.db, 1, String.valueOf(listPuzzleItem.getCount() - id - 1));

                MainActivity.db.close();
                MainActivity.this.getBaseContext().startActivity(new Intent(Intent.ACTION_MAIN).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(MainActivity.this, PuzzleActivity.class));
            }
        };

        // initialize items click
        listPuzzleItem.setOnItemClickListener(_onItemClickListener);
    }

    private void CreatingFavoriteAdapter() {
        while (DatabaseHelper.valueCursor.moveToNext()) {
            DatabaseHelper.favoriteCursor.moveToPosition(DatabaseHelper.valueCursor.getPosition());
            if (DatabaseHelper.favoriteCursor.getString(1).equals("★")) {
                listItems.add(new ViewListItem(
                        DatabaseHelper.valueCursor.getInt(0),
                        DatabaseHelper.valueCursor.getString(1),
                        DatabaseHelper.valueCursor.getString(2),
                        DatabaseHelper.valueCursor.getString(6),
                        DatabaseHelper.favoriteCursor.getString(2),
                        DatabaseHelper.favoriteCursor.getString(1)));
            }
        }
    }

    private void CreatingAnAssortedAdapter() {
        DatabaseHelper.valueCursor.moveToPosition(DatabaseHelper.valueCursor.getCount());
        while (DatabaseHelper.valueCursor.moveToPrevious())
            if (listItems.size() == (DatabaseHelper.valueCursor.getCount() - Integer.parseInt(DatabaseHelper.settingsCursor.getString(1)) - 1)) {
                DatabaseHelper.favoriteCursor.moveToPosition(DatabaseHelper.valueCursor.getCount() - (listItems.size() + 1));
                listItems.add(new ViewListItem(DatabaseHelper.valueCursor.getInt(0),
                        "<-- " + DatabaseHelper.valueCursor.getString(1) + " -->",
                        DatabaseHelper.valueCursor.getString(2),
                        DatabaseHelper.valueCursor.getString(6),
                        DatabaseHelper.favoriteCursor.getString(2),
                        DatabaseHelper.favoriteCursor.getString(1)));
            } else {
                DatabaseHelper.favoriteCursor.moveToPosition(DatabaseHelper.valueCursor.getCount() - (listItems.size() + 1));
                listItems.add(new ViewListItem(DatabaseHelper.valueCursor.getInt(0),
                        DatabaseHelper.valueCursor.getString(1),
                        DatabaseHelper.valueCursor.getString(2),
                        DatabaseHelper.valueCursor.getString(6),
                        DatabaseHelper.favoriteCursor.getString(2),
                        DatabaseHelper.favoriteCursor.getString(1)));
            }
    }

    private void CreateAssortedAdapter() {
        int _lastItemIndex = DatabaseHelper.settingsCursor.getInt(1);
        ViewListItem _item;
        DatabaseHelper.valueCursor.moveToPosition(-1);

        while (DatabaseHelper.valueCursor.moveToNext()) {
            _item = new ViewListItem();

            if (listItems.size() == _lastItemIndex) _item.name = "<-- " + DatabaseHelper.valueCursor.getString(1) + " -->";
            else _item.name = DatabaseHelper.valueCursor.getString(1);

            _item.index = DatabaseHelper.valueCursor.getInt(0);
            _item.description = DatabaseHelper.valueCursor.getString(2);
            _item.complexity = DatabaseHelper.valueCursor.getString(6);
            _item.state = DatabaseHelper.favoriteCursor.getString(2);
            _item.favorite = DatabaseHelper.favoriteCursor.getString(1);
            listItems.add(_item);
            DatabaseHelper.favoriteCursor.moveToPosition(listItems.size());
        }
    }

    private void InitializeMenuButtons() {
        Button _buttonSettings, _buttonFavorite, _buttonToLastIndex, _buttonAbout;

        _buttonSettings = (Button)findViewById(R.id.button_settings);
        _buttonFavorite = (Button)findViewById(R.id.button_favorite);
        _buttonToLastIndex = (Button)findViewById(R.id.button_tolastindex);
        _buttonAbout = (Button)findViewById(R.id.button_about);

        View.OnClickListener _oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.button_settings:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;

                    case R.id.button_favorite:
                        isFavorite = !isFavorite;
                        if (isFavorite) MainActivity.this.InitializeMainFavoriteListView();
                        else MainActivity.this.InitializeMainListView();
                        MainActivity.this.InitializeButtonFavoriteState();
                        break;

                    case R.id.button_tolastindex:
                        if (isFavorite) {
                            Toasty.error(MainActivity.this.getBaseContext(), TOAST_ERROR_SHOW_FAVORITE_MSG, Toast.LENGTH_SHORT, true).show();
                            return;
                        }

                        if (DatabaseHelper.settingsCursor.getString(4).equals("true")) listPuzzleItem.setSelection(Integer.parseInt(DatabaseHelper.settingsCursor.getString(1).toString()));
                        else listPuzzleItem.setSelection(listPuzzleItem.getCount() - Integer.parseInt(DatabaseHelper.settingsCursor.getString(1)) - 1);

                        Toasty.info(MainActivity.this.getBaseContext(), TOAST_LAST_PUZZLE_MSG + (Integer.parseInt(DatabaseHelper.settingsCursor.getString(1)) + 1), Toast.LENGTH_LONG, true).show();
                        break;

                    case R.id.button_about:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, AboutActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                }
            }
        };

        _buttonSettings.setOnClickListener(_oclBtn);
        _buttonFavorite.setOnClickListener(_oclBtn);
        _buttonToLastIndex.setOnClickListener(_oclBtn);
        _buttonAbout.setOnClickListener(_oclBtn);
    }

    private void InitializeButtonFavoriteState() {
        ((Button) findViewById(R.id.button_favorite)).setText(isFavorite ? FAVORITE_BUTTON_TEXT_TRUE : FAVORITE_BUTTON_TEXT_FALSE);
    }
}