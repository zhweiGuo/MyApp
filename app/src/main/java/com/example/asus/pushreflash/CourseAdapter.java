package com.example.asus.pushreflash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.myapp.R;

import java.util.List;

/**
 * Created by asus on 2016/9/27.
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;

    public CourseAdapter(Context context, int resource, List<Course> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.courseHead = (ImageView) view.findViewById(R.id.course_head);
            viewHolder.courseTeacher = (TextView) view.findViewById(R.id.course_teacher);
            viewHolder.courseName = (TextView) view.findViewById(R.id.course_name);
            viewHolder.courseId = (TextView)view.findViewById(R.id.course_id);
            viewHolder.courseFlag = (TextView) view.findViewById(R.id.course_Flag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.courseHead.setImageResource(course.getCourseHead());
        viewHolder.courseTeacher.setText(course.getCourseTeacher());
        viewHolder.courseName.setText(course.getCourseName());
        viewHolder.courseId.setText(course.getCourseID());
        viewHolder.courseFlag.setText(course.getCourseFlag());
        return view;
    }

    class ViewHolder {
        public ImageView courseHead;
        public TextView courseTeacher;
        public TextView courseName;
        public TextView courseId;
        public TextView courseFlag;
    }
}
