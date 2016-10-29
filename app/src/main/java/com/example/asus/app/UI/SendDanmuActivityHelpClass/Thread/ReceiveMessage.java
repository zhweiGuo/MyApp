package com.example.asus.app.UI.SendDanmuActivityHelpClass.Thread;

import android.app.Activity;

import com.example.asus.app.UI.SendDanmuActivity;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.asus.app.UI.SendDanmuActivity.UPDATE_MESSAGE_LIST;

/**
 * ReceiveMessage
 *
 * @author: Allen
 * @time: 2016/10/28 18:12
 */

public class ReceiveMessage extends Thread {
    private final String url = "http://115.28.80.81/app/check.php";
    private String dateString;
    private SendDanmuActivity currentActivity;
    private String userName;
    private String lessonNumber;

    public ReceiveMessage(String lessonNumber,
                          String date, SendDanmuActivity currentActivity, String userName) {
        this.lessonNumber = lessonNumber;
        this.dateString = date;
        this.currentActivity = currentActivity;
        this.userName = userName;
    }

    @Override
    public void run() {

        try {

            while (true) {

                Thread.sleep(3000);
                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "commit"));
                params.add(new BasicNameValuePair("userName", userName));
                params.add(new BasicNameValuePair("discuss", ""));
                params.add(new BasicNameValuePair("teacherName", "1"));
                params.add(new BasicNameValuePair("courseId", lessonNumber));
                params.add(new BasicNameValuePair("time", dateString));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("str = " + str);
                    if (!str.equals(null)) {
                        JSONArray jsonArray = new JSONArray(str);

                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(x);

                            Date date = new SimpleDateFormat("MM-dd EEE HH:mm",
                                    new Locale("ZH", "CN")).parse(dateString);
                            String discuss = jsonObject.getString("discuss");
                            String show_name = jsonObject.getString("userName");
                            MyMessage message = new MyMessage(discuss, MyMessage.RECEIVE,
                                    date, show_name, "");

                            currentActivity.mMessageList.add(message);
                        }

                        currentActivity.updateListViewHandler.sendEmptyMessage(UPDATE_MESSAGE_LIST);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


