package com.example.asus.pushreflash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.myapp.R;

import java.util.List;

/**
 * LessonOrConversionAdapter
 *
 * @author: Allen
 * @time: 2016/10/26 23:19
 */

public class LessonOrConversionAdapter extends ArrayAdapter<LessonOrConversation> {
    private int resourceId;

    public LessonOrConversionAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LessonOrConversation lessonOrConversation = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder.lessonPicture = (ImageView) view.findViewById(R.id.lesson_picture);
            viewHolder.name = (TextView) view.findViewById(R.id.lesson_conversation_name);
            viewHolder.creator = (TextView) view.findViewById(R.id.creator_name);
            viewHolder.number = (TextView) view.findViewById(R.id.lesson_conversation_number);
            view.setTag(viewHolder);
        }
        viewHolder.lessonPicture.setImageResource(lessonOrConversation.getResourceId());
        viewHolder.name.setText(lessonOrConversation.getName());
        viewHolder.creator.setText(lessonOrConversation.getCreator());
        viewHolder.number.setText(String.valueOf(lessonOrConversation.getNumber()));
        return view;
    }

    class ViewHolder {
        ImageView lessonPicture;
        TextView name;
        TextView creator;
        TextView number;

    }
}
