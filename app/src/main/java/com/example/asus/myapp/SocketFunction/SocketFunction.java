package com.example.asus.myapp.SocketFunction;

/**
 * Created by asus on 2016/10/31.
 */

import android.os.Handler;

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

public class SocketFunction {
    //private Socket socket = null;
    private OutputStream output= null;
    private BufferedReader bufferedReader = null;
    private String sendInfo = null;
    private String reciveInfo = null;
    private SocketRecive socketRecive = null;


    //接收数据
    public void LinkServer(Socket socket){

        try{

            //获取输出流

            output = socket.getOutputStream();
            output.write(getSendInfo().getBytes("UTF-8"));
            output.flush();

            System.out.println("Function + " + getSendInfo());

            output.close();

            //socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //接收消息
    public void ReciveSocket(Socket socket){
        try {

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String buff = "";
            buff = bufferedReader.readLine();
            socketRecive.setMsg(buff);

            bufferedReader.close();
       }catch (Exception e){
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

    //设置接收的消息
    public void setReciveInfo(String reciveInfo){
        this.reciveInfo = reciveInfo;
    }
    public String getReciveInfo(){
        return this.reciveInfo;
    }
}
