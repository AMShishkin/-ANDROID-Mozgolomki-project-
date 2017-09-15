package com.project.golovolomki.MainActivityViewList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.project.golovolomki.Assistants.DatabaseHelper;
import com.project.golovolomki.R;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<ViewListItem> objects;

    public Adapter(Context context, ArrayList<ViewListItem> listItem) {
        ctx = context;
        objects = listItem;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        View view = convertView;
        if (view == null) view = lInflater.inflate(R.layout.item, parent, false);

        ViewListItem p = getViewItem(position);

        ((TextView) view.findViewById(R.id.item_main_number)).setText(p.index + ".");
        ((TextView) view.findViewById(R.id.titleItem)).setText(p.name);
        ((TextView) view.findViewById(R.id.complexityItem)).setText(p.complexity);
        ((TextView) view.findViewById(R.id.descriptionItem)).setText(p.description);
        ((TextView) view.findViewById(R.id.descriptionItem)).setGravity(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? Gravity.CENTER : Gravity.START);
        ((TextView) view.findViewById(R.id.itemFavorite)).setText(p.favorite);
        ((TextView) view.findViewById(R.id.state_item)).setText(p.state);

        ((TextView) view.findViewById(R.id.titleItem)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/titleItem.ttf"));
        ((TextView) view.findViewById(R.id.item_main_number)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/titleItem.ttf"));
        ((TextView) view.findViewById(R.id.descriptionItem)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/cavia_puzzle.ttf"));
        ((TextView) view.findViewById(R.id.state_item)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/titleItem.ttf"));
        ((TextView) view.findViewById(R.id.complexityItem)).setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/titleItem.ttf"));

        return view;
    }

    ViewListItem getViewItem(int position) {
        return ((ViewListItem) getItem(position));
    }
}