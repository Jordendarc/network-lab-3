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
    static public String lastServerText = "";

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
        lastServerText = in.readLine();
        System.out.println(lastServerText);

        BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        while((userInput = userInputBR.readLine()) != null){
            //IF LAST SERVER ACTION WAS A SEND
            if(lastServerText.contains(":") && !lastServerText.contains("Server")){
                if (validateUserInput(userInput)) {
                    out.println(userInput);
                    try {
                        lastServerText = in.readLine();
                        System.out.println(lastServerText);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Enter an accepted command please");
                }
            }
            //INPUT THAT DOES NOT NEED TO BE ONE OF THE COMMANDS
            else if(!lastServerText.contains("Accepted Commands") && !lastServerText.isEmpty() || lastServerText.contains("welcome")){
                out.println(userInput);
                lastServerText = in.readLine();
                System.out.println(lastServerText);
            }
            //INPUT THAT NEEDS TO BE ONE OF THE FEW COMMANDS
            else {
                if (validateUserInput(userInput)) {
                    out.println(userInput);
                    try {
                        lastServerText = in.readLine();
                        System.out.println(lastServerText);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Client: Enter an accepted command please");
                }
            }
            if(lastServerText.contains("Goodbye")){
                userInputBR.close();
                in.close();
                out.close();
                socket.close();
                break;
            }
        }

    }
    public static boolean validateUserInput(String input){
        if(!input.contains("send") ){
            if(!input.contains("newuser"))
                if( !input.contains("login"))
                    if( !input.equals("logout"))
                        return false;
        }
        return true;
    }
}
