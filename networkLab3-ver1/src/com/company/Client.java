package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    static public Socket socket;
    final static int portNumber = 19642;
    final static String host = "localhost";
    static public String userName = "";

    public static void main(String args[]) throws IOException {
        System.out.println("Creating socket to '" + host + "' on port " + portNumber);
        startClient();
    }
    public static void startClient() throws IOException {
        socket = new Socket(host, portNumber);
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println(in.readLine());
        System.out.println(in.readLine());

        BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        while((userInput = userInputBR.readLine()) != null){
            out.println(userInput);
            String text = in.readLine();
            System.out.println(text);
            if(text.contains("Goodbye")){
                userInputBR.close();
                in.close();
                out.close();
                socket.close();
                break;
            }
        }

    }
}
