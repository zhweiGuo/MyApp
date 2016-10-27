package com.example.asus.myapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
 * Created by asus on 2016/9/6.
 */
public class MyService2 extends Service {

    private final String url = "http://115.28.80.81/app/check.php";
    private CommitTeacher commitTeacher = new CommitTeacher();
    private UserTeacherActivity userTeacherActivity = new UserTeacherActivity();
    private static String groupId = null;
    private SimpleDateFormat simpleDateFormat = null;
    private Date date = null;

    private static String name = null;


    @Override
    public void onCreate() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Intent intent2 = intent;

        intent2.setAction(Commit.ACTION_UPDATEUI2);
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = new Date(System.currentTimeMillis() / 1000);


        new Thread(new Runnable() {
            boolean i = true;
            int sleepTime = 3000;

            @Override
            public void run() {
                while (i) {
                    try {
                        String time = simpleDateFormat.format(date);
                        String unix = simpleDateFormat.parse(time).getTime() + "";

                        System.out.println("这里是服务2");

                        Thread.sleep(sleepTime);

                        System.out.println("循环接收消息");

                        HttpPost request = new HttpPost(url);

                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("action", "commit"));
                        params.add(new BasicNameValuePair("userName", getName()));
                        params.add(new BasicNameValuePair("discuss", ""));
                        params.add(new BasicNameValuePair("teacherName", "1"));
                        params.add(new BasicNameValuePair("courseId", getGroupId()));
                        params.add(new BasicNameValuePair("time", unix));
                        request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));


                        HttpResponse response = new DefaultHttpClient().execute(request);

                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            String str = EntityUtils.toString(response.getEntity());

                            System.out.println("这里是服务2内" + str);
                            JSONArray jsonArray = new JSONArray(str);
                            for (int x = 0; x < jsonArray.length(); x++) {
                                //System.out.println("return = ------->" + jsonObject.getBoolean("status"));
                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                intent2.putExtra("discuss", jsonObject.getString("discuss"));
                                intent2.putExtra("userName", jsonObject.getString("userName"));

                                String result = formatData("yyyy-MM-dd HH:mm:ss", jsonObject.getLong("time"));

                                intent2.putExtra("time", result);
                                //commitTeacher.setShowDate(result);
                                intent2.putExtra("groupId", jsonObject.getString("groupId"));
                                sendBroadcast(intent2);
                                System.out.println("Commit " + "内容: " + jsonObject.getString("discuss") + " 发件人："
                                        + jsonObject.getString("userName"));


                            }
                        }

                        sleepTime += 1000;
                        if (sleepTime == 5000) {
                            sleepTime = 1000;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
