package com.example.asus.myapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.app.UI.Login2Activity;

public class Welcome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setContentView(R.layout.welcome);

//        Resources resources = getResources();
//        Drawable drawable = resources.getDrawable(R.drawable.welcomeColor);
//        this.getWindow().setBackgroundDrawable(drawable);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1500);

//                    Intent intent = new Intent(Welcome.this,MainActivity.class);
                    Intent intent = new Intent(Welcome.this,Login2Activity.class);
                    startActivity(intent);
                    finish();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
