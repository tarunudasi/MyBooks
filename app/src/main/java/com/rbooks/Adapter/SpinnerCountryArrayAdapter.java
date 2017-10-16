package com.rbooks.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.rbooks.Model.Country;
import com.rbooks.R;

public class SpinnerCountryArrayAdapter extends ArrayAdapter<Country> {

    private List<Country> objects;
    private Context context;

    public SpinnerCountryArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Country> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row = inflater.inflate(R.layout.spinner_country_code, parent, false);

        TextView countryName=(TextView)row.findViewById(R.id.country_name);
        countryName.setText(objects.get(position).getCountryName());

        //TextView countryCode = (TextView) row.findViewById(R.id.country_code);
        //countryCode.setText(objects.get(position).getCountryCode());

        return row;
    }
}
