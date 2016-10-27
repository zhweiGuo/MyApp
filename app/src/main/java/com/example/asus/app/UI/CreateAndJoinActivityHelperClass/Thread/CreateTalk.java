package com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

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
 * CreateTalk
 *
 * @author: Allen
 * @time: 2016/10/23 15:42
 */

public class CreateTalk extends Thread {
    private static final String url = "http://115.28.80.81/app/check.php";
    private String talkname = null;
    private Activity currentActivity;
    private String teachername = null;

    public CreateTalk(String talkname, String teachername, Activity activity) {
        this.talkname = talkname;
        this.teachername = teachername;
        this.currentActivity = activity;
    }

    @Override
    public void run() {
        try {


            HttpPost request = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("action", "addSession"));
            params.add(new BasicNameValuePair("userName", teachername));
            params.add(new BasicNameValuePair("groupName", talkname));

            System.out.println("user-------name = " + teachername);
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("JSON1-------->" + str);
                JSONObject jsonObject = new JSONObject(str);
                //System.out.println("STATUS------------------->" + jsonObject.getInt("status"));
                //ClassnumberActivity.this.flag = jsonObject.getBoolean("status");
                //setFlag(jsonObject.getInt("status"));
//                talkflag = jsonObject.getInt("groupId");
                if (jsonObject.getInt("groupId") != 0) {
//                    mainActivity.setHash_info(1);
//                    UserTeacherActivity.this.handler.sendEmptyMessage(2);
                    Intent intent = new Intent();
                    intent.putExtra(CREATETALKNAME, talkname);
                    intent.putExtra(TALKTEACHERNAME, teachername);
                    intent.putExtra(TALKNUMBER, jsonObject.getInt("groupId"));
                    currentActivity.setResult(Activity.RESULT_OK, intent);
                    Log.e("\\\\\\\\会话\\\\\\\\\\", talkname+" "+teachername+" " +
                            jsonObject.getInt("groupId"));
                    currentActivity.finish();

                } else {
//                    UserTeacherActivity.this.handler.sendEmptyMessage(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final String CREATETALKNAME = "createTalkName";
    public static final String TALKTEACHERNAME = "talkTeacherName";
    public static final String TALKNUMBER = "talkNumber";
}
