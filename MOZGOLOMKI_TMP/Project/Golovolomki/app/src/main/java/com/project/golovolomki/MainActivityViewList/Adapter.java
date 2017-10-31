package com.project.golovolomki.MainActivityViewList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.golovolomki.Activities.MainActivity;
import com.project.golovolomki.Assistants.DatabaseHelper;
import com.project.golovolomki.R;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<ViewListItem> objects;
    private final float TEXT_SIZE_MAX = 2.0f;

    public Adapter(Context context, ArrayList<ViewListItem> listItem) {
        ctx = context;
        objects = listItem;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        InitializeDataBase(context);
    }

    private void InitializeDataBase(Context context) {
        // INITIALIZE D.B.
        MainActivity.bdh = new DatabaseHelper(context);
        MainActivity.db = MainActivity.bdh.getReadableDatabase();

        // GET D.B. SETTINGS
        DatabaseHelper.settingsCursor = MainActivity.db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_SETTINGS, null);
        DatabaseHelper.settingsCursor.moveToFirst();
        MainActivity.db.close();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View _view = convertView;

        if (_view == null)
            _view = lInflater.inflate(R.layout.item, parent, false);

        ViewListItem _listItem = getViewItem(position);

        InitializeText(_view, _listItem);
        InitializeTextSize(_view);
        InitizlizeFont(_view);

        return _view;
    }

    private void InitizlizeFont(View view) {
        ((TextView) view.findViewById(R.id.titleItem)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/titleItem.ttf"));
        ((TextView) view.findViewById(R.id.item_main_number)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/titleItem.ttf"));
        ((TextView) view.findViewById(R.id.descriptionItem)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/cavia_puzzle.ttf"));
        ((TextView) view.findViewById(R.id.state_item)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/titleItem.ttf"));
        ((TextView) view.findViewById(R.id.complexityItem)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/titleItem.ttf"));
    }

    private void InitializeText(View view, ViewListItem listItem) {
        ((TextView) view.findViewById(R.id.item_main_number)).setText(listItem.index + ".");
        ((TextView) view.findViewById(R.id.titleItem)).setText(listItem.name);
        ((TextView) view.findViewById(R.id.complexityItem)).setText(listItem.complexity);
        ((TextView) view.findViewById(R.id.descriptionItem)).setText(listItem.description);

        ((TextView) view.findViewById(R.id.descriptionItem)).setGravity(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? Gravity.CENTER : Gravity.START);
        ((TextView) view.findViewById(R.id.itemFavorite)).setText(listItem.favorite);
        ((TextView) view.findViewById(R.id.state_item)).setText(listItem.state);
    }

    private void InitializeTextSize(View view) {
        ((TextView) view.findViewById(R.id.titleItem)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ctx.getResources().getDimension(R.dimen.action_bar_buttons_text_size) + getSettingsTextSize());
        ((TextView) view.findViewById(R.id.item_main_number)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ctx.getResources().getDimension(R.dimen.action_bar_buttons_text_size) + getSettingsTextSize());
        // ((TextView) view.findViewById(R.id.complexityItem)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ctx.getResources().getDimension(R.dimen.action_bar_buttons_text_size) + getSettingsTextSize());
        ((TextView) view.findViewById(R.id.descriptionItem)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ctx.getResources().getDimension(R.dimen.action_bar_buttons_text_size) + getSettingsTextSize());
    }

    private float getSettingsTextSize() {
        return Boolean.valueOf(DatabaseHelper.settingsCursor.getString(10)) ?  TEXT_SIZE_MAX : 0;
    }

    ViewListItem getViewItem(int position) {
        return ((ViewListItem) getItem(position));
    }
}