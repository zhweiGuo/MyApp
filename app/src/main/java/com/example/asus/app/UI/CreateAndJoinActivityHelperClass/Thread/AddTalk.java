package com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ProviderInfo;

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
 * AddTalk
 *
 * @author: Allen
 * @time: 2016/10/22 16:21
 */

public class AddTalk extends Thread {
    private String inputkey = null;
    private static final String url = "http://115.28.80.81/app/check.php";
    private Activity currentActivity;
    public static final String GROUPNAME = "groupName";
    public static final String USERNAME = "userName";
    public static final String TALKNUMBER  = "talkNumber";


    public AddTalk(String inputkey, Activity activity) {
        this.inputkey = inputkey;
        this.currentActivity = activity;
    }

    @Override
    public void run() {

        try {

            HttpPost request = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("action", "addTalk"));
            params.add(new BasicNameValuePair("gid", inputkey));

            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("JSON1-------->" + str);
                JSONArray jsonArray = new JSONArray(str);
                //System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
                if (str != null) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    System.out.println("talkName------------------->" + jsonObject.getString("groupName"));
                    System.out.println("teacherName------------------->" + jsonObject.getString("userName"));
//                    setCourseName(jsonObject.getString("groupName"));
//                    setTeacherName(jsonObject.getString("userName"));

//                    MainActivity.this.handler.sendEmptyMessage(1);
                    Intent intent = new Intent();
                    intent.putExtra(GROUPNAME, jsonObject.getString("groupName"));
                    intent.putExtra(USERNAME, jsonObject.getString("userName"));
                    intent.putExtra(TALKNUMBER, inputkey);
                    currentActivity.setResult(Activity.RESULT_OK, intent);
                }
                currentActivity.finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
