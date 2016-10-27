package com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.asus.app.UI.FunctionActivity;

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

import java.util.ArrayList;
import java.util.List;

/**
 * AddClass
 *
 * @author: Allen
 * @time: 2016/10/22 16:24
 */

public class AddClass extends Thread {
    private AppCompatActivity currentActivity;
    private String inputkey = null;
    private static final String url = "http://115.28.80.81/app/check.php";

    public static final int JOINCOURSE = 0;
    public static final int JOINCONVERSATION = 1;
    public static final String COURSENAME = "courseName";
    public static final String TEACHERNAME = "teacherName";
    public static final String CLASSNUMBER  = "classNumber";


    public AddClass(String inputkey, AppCompatActivity activity) {
        this.inputkey = inputkey;
        this.currentActivity = activity;
    }

    @Override
    public void run() {

        try {

            HttpPost request = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("action", "getCourse"));
            params.add(new BasicNameValuePair("courseId", inputkey));

            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("JSON1-------->" + str);
                JSONArray jsonArray = new JSONArray(str);
                //System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
                if (str != null) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    System.out.println("courseName------------------->" + jsonObject.getString("courseName"));
                    System.out.println("teacherName------------------->" + jsonObject.getString("teacherName"));
//                    setCourseName(jsonObject.getString("courseName"));
//                    setTeacherName(jsonObject.getString("teacherName"));
//                    Intent intent = new Intent(currentActivity, FunctionActivity.class);
                    Intent intent = new Intent();
                    intent.putExtra(COURSENAME, jsonObject.getString("courseName"));
                    intent.putExtra(TEACHERNAME, jsonObject.getString("teacherName"));
                    intent.putExtra(CLASSNUMBER, inputkey);
                    currentActivity.setResult(Activity.RESULT_OK, intent);
                }
                currentActivity.finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
