package com.example.asus.app.UI.SendDanmuActivityHelpClass.Thread;

import com.example.asus.myapp.CommitTeacher;

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

import java.util.ArrayList;
import java.util.List;

/**
 * SendNotification
 *
 * @author: Allen
 * @time: 2016/10/30 20:31
 */

public class SendNotification extends Thread {
    private class SentProm extends Thread {

        private final String url = "http://115.28.80.81/app/check.php";
        private String promble = null;
        private String info_hour = null;
        private String info_min = null;
        private String date = null;
        private String userName;
        private String groupId;

        public SentProm(String promble, String date, String info_hour, String info_min) {
            this.promble = promble;
            this.date = date;
            this.info_hour = info_hour;
            this.info_min = info_min;

        }

        @Override
        public void run() {


            try {

                System.out.println("date----->" + date);
                System.out.println("prom----->" + promble);

                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "msgSend"));
                params.add(new BasicNameValuePair("name", userName));
                params.add(new BasicNameValuePair("msgBody", promble));
                params.add(new BasicNameValuePair("hour", info_hour));
                params.add(new BasicNameValuePair("min", info_min));
                params.add(new BasicNameValuePair("time", date));
                params.add(new BasicNameValuePair("groupId", groupId));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
                    if (jsonObject.getBoolean("status")) {
//
//                        CommitTeacher.this.setShowMsg(promble);
//                        CommitTeacher.this.setShowHour(info_hour);
//                        CommitTeacher.this.setShowMin(info_min);
//                        CommitTeacher.this.setShowUser(userTeacherActivity.getUsername());

//                        String info = "内容: " + getShowMsg() + " " + getShowHour() + ":" + getShowMin() + " 发件人：" + getShowUser();
//                        setTempInfo(info);


//                        CommitTeacher.this.handler.sendEmptyMessage(1);
                    } else {
//                        CommitTeacher.this.handler.sendEmptyMessage(0);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
