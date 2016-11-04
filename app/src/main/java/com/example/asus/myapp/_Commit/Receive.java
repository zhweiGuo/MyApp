package com.example.asus.myapp._Commit;

import android.os.Handler;

import com.example.asus.myapp.Commit;

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
 * Created by asus on 2016/11/4.
 */
public class Receive extends Thread{
    private SimpleDateFormat simpleDateFormat = null;
    private Date date = null;
    private final String url = "http://115.28.80.81/app/check.php";
    private String date1 = null;
    private Date _date = null;
    private String name = null;
    private Handler handler = null;

    private Commit commit = new Commit();
    @Override
    public void run() {


        try {
            String date = simpleDateFormat.format(_date);
            String unix = simpleDateFormat.parse(date).getTime() + "";

            while (true) {

                Thread.sleep(1000);

                System.out.println("循环接收消息");

                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "commit"));
                params.add(new BasicNameValuePair("userName", name));
                params.add(new BasicNameValuePair("discuss", ""));
                params.add(new BasicNameValuePair("teacherName", "1"));
                params.add(new BasicNameValuePair("courseId", commit.getGroupId()));
                params.add(new BasicNameValuePair("time", unix));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("str = " + str);
                    if (!str.equals(null)) {
                        JSONArray jsonArray = new JSONArray(str);

                        for (int x = 0; x < jsonArray.length(); x++) {


                            JSONObject jsonObject = jsonArray.getJSONObject(x);

                            String discuss = jsonObject.getString("discuss");
                            String show_name = jsonObject.getString("userName");
                            String time = formatData("yyyy-MM-dd HH:mm:ss", jsonObject.getLong("time"));

                            if (commit.getIsWho() == 0) {
                                commit.setDiscuss(discuss);
                                handler.sendEmptyMessage(0);
                            } else if (commit.getIsWho() == 1) {
                                commit.setDiscuss(discuss);
                                commit.setShow_name(show_name);
                                handler.sendEmptyMessage(1);
                            }
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Receive(Date _date,String name,SimpleDateFormat simpleDateFormat ,Date date,Handler handler) {
        this._date = _date;
        this.name = name;
        this.simpleDateFormat = simpleDateFormat;
        this.date = date;
        this.handler = handler;
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
