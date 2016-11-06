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

public class SocketFunction {
    private Socket socket = null;
    private BufferedWriter out = null;
    private BufferedReader input = null;


    private OutputStream output= null;


    private String sendInfo = null;
    private String reciveInfo = null;
    public void LinkServer(String URL, int pro){
        String buff = null;
        try{

            socket = new Socket(URL,pro);

            //获取输入流和输出流

            output = socket.getOutputStream();
            output.write("hello".getBytes());
            output.flush();
            //out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //out.write(sendInfo + "\n");
            //out.flush();    //刷新缓存区，发送数据

            System.out.println("Function + " + getSendInfo());

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            buff = input.readLine();

            System.out.println("服务器发回来的消息" + buff);
            setReciveInfo(buff);


            output.close();

            out.close();
            input.close();
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

    //设置接收的消息
    public void setReciveInfo(String reciveInfo){
        this.reciveInfo = reciveInfo;
    }
    public String getReciveInfo(){
        return this.reciveInfo;
    }
}
