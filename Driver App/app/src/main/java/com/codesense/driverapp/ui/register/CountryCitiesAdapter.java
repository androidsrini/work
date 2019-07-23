package com.codesense.driverapp.ui.register;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.codesense.driverapp.R;

import com.codesense.driverapp.net.Constant;

public class CountryCitiesAdapter extends CursorAdapter {

    private CountryCitiesSelectionActivity.ActivityType activityType;

    public CountryCitiesAdapter(Context context, Cursor cursor, int flags, CountryCitiesSelectionActivity.ActivityType activityType) {
        super(context, cursor, flags);
        this.activityType = activityType;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.country_cities_selection_adapter_screen, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dataTextView = view.findViewById(R.id.dataTextView);
        if (CountryCitiesSelectionActivity.ActivityType.CITIES == activityType) {
            dataTextView.setText(cursor.getString(cursor.getColumnIndex(Constant.CITY_NAME)));
        } else {
            dataTextView.setText(cursor.getString(cursor.getColumnIndex(Constant.COUNTRY_NAME)));
        }
    }
}
