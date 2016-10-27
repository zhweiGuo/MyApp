package com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread;

import com.example.asus.myapp.MainActivity;
import com.example.asus.myapp.R;
import com.example.asus.pushreflash.Course;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LoadingInfo
 *
 * @author: Allen
 * @time: 2016/10/22 16:27
 */

public class LoadingInfo extends Thread {
    private String url = "http://115.28.80.81/app/check.php";

    @Override
    public void run() {
        try {

            HttpPost request = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("action", "loadSession"));
//            params.add(new BasicNameValuePair("userName", getDatabaseName()));


            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("JSONLoding-------->" + str);
                JSONArray jsonArray = new JSONArray(str);
                System.out.println("STATUS lodingInfo------------------->" + 2);

                List<Map<String, String>> userInformations = new ArrayList<>();
                List<Course> courseInformations = new ArrayList<>();

                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                    String name = jsonObject.getString(USERNAME);
                    String groupName = jsonObject.getString(GROUPNAME);
                    String groupId = jsonObject.getString(GROUPID);
                    int tempFlag = jsonObject.getInt(SAVEFLAG); //0 会话；1 班课
//                    Course courseItem = new Course(name, groupName, R.drawable.file_pic, );
                    Map<String, String> itemStringStringMap = new HashMap<>();
                    itemStringStringMap.put(USERNAME, name);
                    itemStringStringMap.put(GROUPNAME, groupName);
                    itemStringStringMap.put(GROUPID, groupId);
                    itemStringStringMap.put(SAVEFLAG, String.valueOf(tempFlag));
                    userInformations.add(itemStringStringMap);
                    System.out.println("name = " + name + "group = " + groupName);
//                    MainActivity.this.setCourseName(groupName);
//                    MainActivity.this.setTeacherName(name);
//                    MainActivity.this.setGroupId(groupId);
//                    MainActivity.this.setSaveFlag(String.valueOf(tempFlag));
//                    MainActivity.this.handler.sendEmptyMessage(4);
                    
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final String USERNAME = "userName";
    public static final String GROUPNAME = "groupName";
    public static final String GROUPID = "gid";
    public static final String SAVEFLAG = "flag";
}
