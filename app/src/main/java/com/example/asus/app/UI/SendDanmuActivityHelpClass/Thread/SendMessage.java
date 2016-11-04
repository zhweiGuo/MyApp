package com.example.asus.app.UI.SendDanmuActivityHelpClass.Thread;

import android.app.Activity;
import android.util.Log;

import com.example.asus.app.UI.SendDanmuActivity;
import com.example.asus.myapp.Commit;
import com.example.asus.myapp.MyMessage;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.asus.app.UI.SendDanmuActivity.UPDATE_MESSAGE_LIST;

/**
 * SendMessage
 *
 * @author: Allen
 * @time: 2016/10/29 8:04
 */

public class SendMessage extends Thread {

    private final String url = "http://115.28.80.81/app/check.php";
    private String messageContent = null;
    private String dateString = null;
    private SendDanmuActivity currentActivity;
    private String userName;
    private String groupNumber;

    public SendMessage(String messageContent, String date, SendDanmuActivity currentActivity,
                       String userName, String groupNumber) {
        this.messageContent = messageContent;
        this.dateString = date;
        this.currentActivity = currentActivity;
        this.userName = userName;
        this.groupNumber = groupNumber;
    }

    @Override
    public void run() {

        try {

            HttpPost request = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("action", "commit"));
            params.add(new BasicNameValuePair("userName", userName));
            params.add(new BasicNameValuePair("discuss", messageContent));
            params.add(new BasicNameValuePair("teacherName", "1"));
            params.add(new BasicNameValuePair("courseId", groupNumber));
            params.add(new BasicNameValuePair("time", dateString));
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("JSON1-------->" + str);
                JSONObject jsonObject = new JSONObject(str);
                System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));

                Log.i("STATUS------>", String.valueOf(jsonObject.getBoolean("status")));
                Log.i("/////发送弹幕的Thread", groupNumber);
                Log.i("/////发送弹幕的Thread名字", userName);
//                MyMessage message = new MyMessage(messageContent, MyMessage.SEND,
//                        date, "", userName);
                MyMessage myMessage = new MyMessage(MyMessage.SEND, messageContent, dateString, "", userName);
                currentActivity.mMessageList.add(myMessage);
                currentActivity.updateListViewHandler.sendEmptyMessage(UPDATE_MESSAGE_LIST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
