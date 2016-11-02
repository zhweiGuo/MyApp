package com.example.asus.app.UI;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.myapp.Check;
import com.example.asus.myapp.R;
import com.example.asus.myapp.UserActivity;
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

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.app.UI.FunctionActivity.USER_NAME;

public class Login2Activity extends AppCompatActivity implements View.OnClickListener {

    public static final String STUDENT = "0";
    public static final String TEACHER = "1";
    public static final int SHOW_ERROR_INFORMATION = 1;
    private String mPeople = TEACHER;

    private EditText mUserNameEditText;
    private EditText mPassWordEditText;
    private Button mLoginBtn;
    private TextView mRegisterTextView;
    private RadioGroup mRadioGroup;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ERROR_INFORMATION:
                    Toast.makeText(Login2Activity.this, "用户名、密码或身份错误",Toast.LENGTH_SHORT)
                            .show();
                    mLoginBtn.setText("登陆");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mUserNameEditText = (EditText) findViewById(R.id.user_name_edit);
        mPassWordEditText = (EditText) findViewById(R.id.password_edit);
        mLoginBtn = (Button) findViewById(R.id.login2_btn);
        mRegisterTextView = (TextView) findViewById(R.id.register_text_view);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRegisterTextView = (TextView) findViewById(R.id.register_text_view);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.teacher_radio_btn) {
                    mPeople = TEACHER;
                } else if (checkedId == R.id.student_radio_btn) {
                    mPeople = STUDENT;
                }
            }
        });
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            if (v.getId() == R.id.login2_btn) {
                String name = mUserNameEditText.getText().toString();
                String passWord = mPassWordEditText.getText().toString();
                new PostThread(name, passWord, mPeople).start();
                //让键盘消失
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mPassWordEditText.getWindowToken(), 0);

                mLoginBtn.setText("登陆中···");
            }

            if (v.getId() == R.id.register_text_view) {

            }
    }

    private class PostThread extends Thread {
        public static final String url = "http://115.28.80.81/app/check.php";
        private String name;
        private String pass;
        private String people;
        public PostThread(String name, String pass, String people) {
            this.name = name;
            this.pass = pass;
            this.people = people;
        }

        @Override
        public void run() {
            boolean canLogin = true;
            try {
                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action","sign"));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("password", pass));
                params.add(new BasicNameValuePair("flag",people));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(str);
                    canLogin = jsonObject.getBoolean("status");
                    Log.e("////////线程/////////", str);

                }
                Log.e("////////线程/////////", String.valueOf(canLogin));
                if (canLogin == true) {
                    Intent intent = new Intent(Login2Activity.this, FunctionActivity.class);
                    intent.putExtra(USER_NAME, name);
                    startActivity(intent);
//                    finish();
                } else {
                    mHandler.sendEmptyMessage(SHOW_ERROR_INFORMATION);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
