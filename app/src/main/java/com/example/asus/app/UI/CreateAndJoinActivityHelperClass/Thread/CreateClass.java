package com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread;

import android.app.Activity;
import android.content.Intent;

import com.example.asus.myapp.UserTeacherActivity;

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

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * CreateClass
 *
 * @author: Allen
 * @time: 2016/10/23 12:09
 */

public class CreateClass extends Thread {
    private static final String url = "http://115.28.80.81/app/check.php";
    private String classname = null;
    private String teachername = null;
    private Activity currentActivity;

    public CreateClass(String classname, String teachername, Activity activity) {
        this.classname = classname;
        this.teachername = teachername;
        this.currentActivity = activity;
    }

    @Override
    public void run() {
        try {


            HttpPost request = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("action", "setCourse"));
            params.add(new BasicNameValuePair("courseName", classname));
            params.add(new BasicNameValuePair("teacherName", teachername));

            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("JSON1-------->" + str);
                JSONObject jsonObject = new JSONObject(str);
                System.out.println("STATUS------------------->" + jsonObject.getInt("status"));
                //ClassnumberActivity.this.flag = jsonObject.getBoolean("status");
                //setFlag(jsonObject.getInt("status"));
//                    flag = jsonObject.getInt("status");
                if (jsonObject.getInt("status") != 0) {

//                        UserTeacherActivity.this.handler.sendEmptyMessage(1);
                    Intent intent = new Intent();
                    intent.putExtra(CREATECOURSENAME, classname);
                    intent.putExtra(CLASSTEACHERNAME, teachername);
                    intent.putExtra(CLASSNUMBER, jsonObject.getInt("status"));
                    currentActivity.setResult(Activity.RESULT_OK, intent);
                    currentActivity.finish();
                } else {
//                        UserTeacherActivity.this.handler.sendEmptyMessage(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final String CREATECOURSENAME = "createCourseName";
    public static final String CLASSTEACHERNAME = "classTeacherName";
    public static final String CLASSNUMBER = "classNumber";
}

