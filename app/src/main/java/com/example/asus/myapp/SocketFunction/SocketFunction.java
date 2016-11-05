package com.example.asus.myapp.SocketFunction;

/**
 * Created by asus on 2016/10/31.
 */

import android.os.Handler;

import com.example.asus.myapp.Commit;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketFunction {
    private Socket socket = null;
    private OutputStream out = null;
    private InputStream input = null;
    private String sendInfo = null;
    private Commit commit = new Commit();
    public void LinkServer(String URL, int pro){
        String buff = null;
        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(URL, pro), 3000);

            //获取输入流和输出流
            out = socket.getOutputStream();
            input = socket.getInputStream();

            int line;
            while ((line = input.read()) != -1){
                buff = line + buff;
            }
            System.out.println("服务器字节流输入" + buff);
            commit.setDiscuss(buff);


            System.out.println("Function + " + getSendInfo());
            out.write(getSendInfo().getBytes("gbk"));

            out.close();
            input.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setSendInfo(String sendInfo){
        this.sendInfo = sendInfo;
    }
    public String getSendInfo(){
        return this.sendInfo;
    }
}
