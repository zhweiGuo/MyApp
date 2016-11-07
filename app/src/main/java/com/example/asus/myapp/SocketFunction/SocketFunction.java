package com.example.asus.myapp.SocketFunction;

/**
 * Created by asus on 2016/10/31.
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.asus.myapp.Commit;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

public class SocketFunction extends Thread{
    private Socket socket = null;
    private OutputStream output= null;
    private BufferedReader buff = null;
    private String sendInfo = null;
    public Handler rehandler = null;
    private Handler handler = null;
    private String URL = null;
    private int pro ;

    public SocketFunction(Handler handler,String URL ,int pro){
        this.handler = handler;
        this.URL = URL;
        this.pro = pro;
    }

    @Override
    public void run() {
        try{

            this.socket = new Socket(URL,pro);
            this.socket.setSoTimeout(5000);
            //获取输出流
            output = socket.getOutputStream();
            buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //启动另一条线程去接受Socket数据
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String context = null;

                    try{
                        while((context = buff.readLine()) != null){
                            System.out.println("不断接受的消息" + context);
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = context;
                            handler.sendMessage(msg);
                        }


                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }).start();

            Looper.prepare();
            rehandler = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x123) {
                        //向服务器发送数据
                        try {
                            System.out.println(msg.obj.toString());
                            output.write((msg.obj.toString()+"\r\n").getBytes("UTF-8"));
                            output.flush();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            };

            Looper.loop();



            //System.out.println("Function + " + getSendInfo());
            buff.close();
            output.close();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //设置发送的消息
    public void setSendInfo(String sendInfo){
        this.sendInfo = sendInfo;
    }
    public String getSendInfo(){
        return this.sendInfo;
    }

}
