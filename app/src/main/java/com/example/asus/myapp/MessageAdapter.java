package com.example.asus.myapp;

import android.os.Message;
import com.example.asus.myapp.MyMessage;
import android.widget.ArrayAdapter;

/**
 * Created by asus on 2016/9/17.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * MessageAdapter
 *
 * @author: Allen
 * @time: 2016/9/14 0:37
 */
public class MessageAdapter extends ArrayAdapter<MyMessage> {

    private int resourceId;
    public MessageAdapter(Context context, int resource, List<MyMessage> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyMessage msg = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
/*
            viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
            viewHolder.dateMsg = (TextView) view.findViewById(R.id.date_msg);
            viewHolder.leftHead = (ImageView) view.findViewById(R.id.left_head);
            viewHolder.rightHead = (ImageView) view.findViewById(R.id.right_head);
*/
            viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout1);
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout1);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg1);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg1);
            viewHolder.dateMsgLeft = (TextView) view.findViewById(R.id.date_msg_left);
            viewHolder.dateMsgRight = (TextView) view.findViewById(R.id.date_msg_right);
            viewHolder.leftHead = (ImageView) view.findViewById(R.id.left_head1);
            viewHolder.rightHead = (ImageView) view.findViewById(R.id.right_head1);

            viewHolder.textView = (TextView) view.findViewById(R.id.textView);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.textView2);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (msg.getType() == MyMessage.RECEIVE) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftHead.setVisibility(View.VISIBLE);
            viewHolder.rightHead.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
            viewHolder.dateMsgRight.setVisibility(View.GONE);
            viewHolder.textView2.setVisibility(View.GONE);
            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.dateMsgLeft.setText(new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                    .format(msg.getMsgDate()));

            viewHolder.textView.setText(msg.getStudentId_left());
        } else if (msg.getType() == MyMessage.SEND) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.leftHead.setVisibility(View.GONE);
            viewHolder.rightHead.setVisibility(View.VISIBLE);
            viewHolder.rightMsg.setText(msg.getContent());
            viewHolder.dateMsgLeft.setVisibility(View.GONE);
            viewHolder.dateMsgRight.setText(new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                    .format(msg.getMsgDate()));

            viewHolder.textView.setVisibility(View.GONE);
            viewHolder.textView2.setVisibility(View.VISIBLE);

            viewHolder.textView2.setText(msg.getStudentId_right());
        }

        return view;
    }

    class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        //        TextView dateMsg;
        TextView dateMsgLeft;
        TextView dateMsgRight;
        ImageView leftHead;
        ImageView rightHead;

        TextView textView;
        TextView textView2;
    }
}
