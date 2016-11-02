package com.example.asus.app.UI.SendDanmuActivityHelpClass.Thread;

import com.example.asus.myapp.CommitTeacher;
import com.example.asus.pushreflash.TalkNotifacatio;

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

/**
 * ReceiveNotification
 *
 * @author: Allen
 * @time: 2016/10/30 19:26
 */

public class ReceiveNotification extends Thread {
    private final String url = "http://115.28.80.81/app/check.php";

    @Override
    public void run() {
        try {
            HttpPost request = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("action", "msgGet"));

            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("JSON1-------->" + str);
                //JSONObject jsonObject = new JSONObject(str);
                //System.out.println("RECIVE------------------->" + jsonObject.getBoolean("status"));
                JSONArray jsonArray = new JSONArray(str);
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
//                    CommitTeacher.this.setShowMsg(jsonObject.getString("msgBody"));
//                    CommitTeacher.this.setShowHour(jsonObject.getString("hour"));
//                    CommitTeacher.this.setShowMin(jsonObject.getString("min"));
//                    CommitTeacher.this.setShowUser(jsonObject.getString("name"));
                    String content = jsonObject.getString("msgBody");
                    String name = jsonObject.getString("name");
                    String hour = jsonObject.getString("hour");
                    String minute = jsonObject.getString("min");
                    TalkNotifacatio receiveNotification = new TalkNotifacatio(content,name, hour, minute);
                    String result = formatData("yyyy-MM-dd HH:mm:ss", jsonObject.getLong("time"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }

}
