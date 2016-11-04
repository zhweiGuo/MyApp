package com.example.asus.myapp.SocketFunction;

/**
 * Created by asus on 2016/10/31.
 */

import java.net.Socket;

public class SocketFunction {

    public void LinkServer(){
        try{
            Socket client = new Socket("localhost",8888);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
