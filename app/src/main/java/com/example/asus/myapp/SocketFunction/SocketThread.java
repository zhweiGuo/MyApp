package com.example.asus.myapp.SocketFunction;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.net.Socket;

/**
 * Created by asus on 2016/11/5.
 */
public class SocketThread extends Thread{
    private String info = null;
    private SocketFunction socketFunction = null;
    private Socket socket = null;
    private String URL = null;
    private int pro ;
    @Override
    public void run() {
        socketFunction = new SocketFunction();
        System.out.println("SocketThread++++++" + info);
        socketFunction.setSendInfo(info);
        socketFunction.LinkServer(socket);

    }


    public void setInfo(String info){
        this.info = info;
    }
    public String getInfo(){
        return this.info;
    }

    public SocketThread(String URL ,int pro){

        this.URL = URL;
        this.pro = pro;
        try {
            this.socket = new Socket(URL,pro);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Socket getSocket(){
        return this.socket;
    }
}