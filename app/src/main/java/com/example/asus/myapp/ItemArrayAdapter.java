package com.example.asus.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 2016/9/22.
 */
public class ItemArrayAdapter extends ArrayAdapter<SettingItem> {
    private int resourceId;
    public ItemArrayAdapter(Context context, int resource, List<SettingItem> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingItem item = getItem(position);
        View view;
        view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView settingKey = (TextView) view.findViewById(R.id.setting_key);
        TextView settingValue = (TextView) view.findViewById(R.id.setting_value);
        settingKey.setText(item.getKey());
        settingValue.setText(item.getValue());
        return view;
    }
}
