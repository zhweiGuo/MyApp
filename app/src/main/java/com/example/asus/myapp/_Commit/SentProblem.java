package com.example.asus.myapp._Commit;

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
 * Created by asus on 2016/10/25.
 */

public class SentProblem extends Thread{
    private final String url = "http://115.28.80.81/app/check.php";
    private String promble = null;
    private String date = null;
    private String groupId =null;
    private String sendname = null;




    public SentProblem(String promble, String date,String groupId,String sendname) {
        this.promble = promble;
        this.date = date;
        this.groupId = groupId;
        this.sendname = sendname;
    }

    @Override
    public void run() {


        try {

            System.out.println("date----->" + date);
            System.out.println("prom----->" + promble);

            HttpPost request = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("action", "commit"));
            params.add(new BasicNameValuePair("userName",sendname));
            params.add(new BasicNameValuePair("discuss", promble));
            params.add(new BasicNameValuePair("teacherName", "1"));
            params.add(new BasicNameValuePair("courseId",groupId));
            params.add(new BasicNameValuePair("time", date));
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("JSON1-------->" + str);
                JSONObject jsonObject = new JSONObject(str);
                System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
