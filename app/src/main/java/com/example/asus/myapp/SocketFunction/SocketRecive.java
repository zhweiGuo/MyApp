package com.example.asus.myapp.SocketFunction;

import java.net.Socket;
import java.util.Scanner;

/**
 * Created by asus on 2016/11/6.
 */
public class SocketRecive extends Thread {
    private SocketFunction socketFunction = null;
    private String msg = null;
    private Socket socket = null;
    @Override
    public void run() {
        this.socketFunction.ReciveSocket(socket);
        System.out.println("收消息" + getMsg());
    }

    public SocketRecive(SocketFunction socketFunction,Socket socket){
        this.socketFunction = socketFunction;
        this.socket = socket;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
