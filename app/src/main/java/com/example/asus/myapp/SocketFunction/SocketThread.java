package com.example.asus.myapp.SocketFunction;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by asus on 2016/11/5.
 */
public class SocketThread extends Thread{
    private String info = null;
    private SocketFunction socketFunction = null;
    private String URL;
    private int pro;
    private Handler handler = null;
    @Override
    public void run() {

        this.socketFunction = new SocketFunction();

        System.out.println("SocketThread++++++" + info);
        socketFunction.setSendInfo(info);
        socketFunction.LinkServer(URL,pro);

        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("key",socketFunction.getReciveInfo());
        handler.sendMessage(msg);
    }


    public void setInfo(String info){
        this.info = info;
    }
    public String getInfo(){
        return this.info;
    }
    public void setURL(String URL){
        this.URL = URL;
    }
    public String getURL(){
        return this.URL;
    }
    public void setPro(int pro){
        this.pro = pro;
    }

    public SocketThread(Handler handler){
        this.handler = handler;
    }
}