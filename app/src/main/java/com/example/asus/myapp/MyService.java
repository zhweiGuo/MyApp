package com.example.asus.myapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

import com.example.asus.myapp.CommitTeacher;

/**
 * Created by asus on 2016/9/6.
 */
public class MyService extends Service {

    private final String url = "http://115.28.80.81/app/check.php";
    private CommitTeacher commitTeacher = new CommitTeacher();
    private UserTeacherActivity userTeacherActivity = new UserTeacherActivity();
    private UserActivity userActivity = new UserActivity();
    private static String groupId = null;
    private SimpleDateFormat simpleDateFormat = null;
    private Date date = null;


    @Override
    public void onCreate() {

        final Intent intent = new Intent();
        intent.setAction(CommitTeacher.ACTION_UPDATEUI);

            new Thread(new Runnable() {
                boolean i = true;
                int sleepTime = 3000;

                @Override
                public void run() {
                    while (i) {
                        System.out.println("运行中。。。。。。");
                        try {
                            Thread.sleep(sleepTime);

                            HttpPost request = new HttpPost(url);

                            List<NameValuePair> params = new ArrayList<>();
                            params.add(new BasicNameValuePair("action", "request"));
                            params.add(new BasicNameValuePair("userName", userTeacherActivity.getUsername()));
                            params.add(new BasicNameValuePair("groupId", getGroupId()));

                            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                            HttpResponse response = new DefaultHttpClient().execute(request);
                            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                String str = EntityUtils.toString(response.getEntity());
                                System.out.println("第一个服务的" + str);
                                JSONArray jsonArray = new JSONArray(str);
                                for (int x = 0; x < jsonArray.length(); x++) {
                                    //System.out.println("return = ------->" + jsonObject.getBoolean("status"));
                                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                                    intent.putExtra("msgBody", jsonObject.getString("msgBody"));
                                    intent.putExtra("hour", jsonObject.getString("hour"));
                                    intent.putExtra("min", jsonObject.getString("min"));
                                    intent.putExtra("name", jsonObject.getString("name"));
                                /*commitTeacher.setShowMsg(jsonObject.getString("msgBody"));
                                commitTeacher.setShowHour(jsonObject.getString("hour"));
                                commitTeacher.setShowMin(jsonObject.getString("min"));
                                commitTeacher.setShowUser(jsonObject.getString("name"));
                                */
                                    String result = formatData("yyyy-MM-dd HH:mm:ss", jsonObject.getLong("time"));

                                    intent.putExtra("time", result);
                                    //commitTeacher.setShowDate(result);
                                    intent.putExtra("groupId", jsonObject.getString("groupId"));
                                    sendBroadcast(intent);
                                    System.out.println("显示 " + "内容: " + jsonObject.getString("msgBody") + " "
                                            + jsonObject.getString("hour") + ":" + jsonObject.getString("min") + " 发件人："
                                            + jsonObject.getString("name") + " " + result);


                                }
                            }


                            sleepTime += 1000;
                            if (sleepTime == 6000) {
                                sleepTime = 3000;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            }).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(MyService.this, "Myserver", Toast.LENGTH_SHORT).show();




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

    public  String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }

    public void setGroupId(String groupId){
        this.groupId = groupId;
    }

    public String getGroupId(){
        return this.groupId;
    }

}
