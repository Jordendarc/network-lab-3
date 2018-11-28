package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static public ServerSocket serverSocket;
    static public Socket socket;
    final static int portNumber = 19642;
    static public boolean loggedIn = false;
    static public String loggedInUser = "";
    static boolean exit = false;

    public static void main(String args[]) throws IOException {
        System.out.println("Creating socket on port " + portNumber);
        startServer();
    }

    public static void startServer() throws IOException {
        serverSocket = new ServerSocket(portNumber);
        socket = serverSocket.accept();
        PrintWriter pw;
        OutputStream os;
        os = socket.getOutputStream();
        pw = new PrintWriter(os, true);
        System.out.println("**********Chatroom server begins**********");
        pw.println("Server: ********** Welcome to the Chatroom! **********");
        pw.println("Server: Accepted Commands: send, login, newuser, and logout");

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String str;
        while((str = br.readLine()) != null) {
            if(validateUserInput(str)) {
                if (str.contains("login")) {
                    if (str.split(" ").length != 3) {
                        pw.println("Server: ERROR invalid login format");
                    } else if (login(str)) {
                        pw.println("Server: Welcome " + loggedInUser);
                    } else
                        pw.println("Server: ERROR cannot find username");
                } else if (str.equals("logout")) {
                    if (loggedIn) {
                        pw.println("Goodbye " + loggedInUser);
                        logout();
                    } else {
                        pw.println("System: ERROR cannot logout user not logged in");
                    }
                } else if (str.split(" ", 2)[0].contains("send")) {
                    if (loggedIn)
                        pw.println(send(str));
                    else
                        pw.println("Server: ERROR not logged in");
                } else if (str.contains("newuser")) {
                    if (str.split(" ").length != 3) {
                        pw.println("Server: ERROR invalid create user format");
                    } else if (str.split(" ")[1].length() > 32) {
                        pw.println("Server: ERROR username must be under 32 characters");
                    } else if (str.split(" ")[2].length() > 8 || str.split(" ")[2].length() < 4) {
                        pw.println("Server: ERROR password must be between 4 and 8 characters long");
                    } else {
                        if (newuser(str)) {
                            pw.println("Server: account created");
                        } else {
                            pw.println("Server: ERROR account could not be created");
                        }
                    }
                }
            }
            else {
                pw.println("Server: please enter valid commands");
            }

            if (exit) {
                pw.close();
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

    private static boolean login(String input){
        try{
            String line = null;
            FileReader fileReader = new FileReader("C:\\Users\\jorwh\\IdeaProjects\\networkLab3\\src\\com\\company\\logins.txt");

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                String[] usrAndPwd = line.split(",");
                String[] inputs = input.split(" ");
                if(inputs[1].strip().equals(usrAndPwd[0].strip()) && inputs[2].strip().equals(usrAndPwd[1].strip()) ){
                    loggedIn = true;
                    loggedInUser = inputs[1];
                    System.out.println(loggedInUser + " login");
                    break;
                }
            }
            bufferedReader.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return loggedIn;
    }
    private static void logout(){
        System.out.println(loggedInUser + " logout");
        loggedInUser = "";
        loggedIn = false;
        exit = true;

    }
    private static String send(String input){
        String[] splitInput = input.split(" ", 2);
        System.out.println(loggedInUser + ": " + splitInput[1]);
        return loggedInUser + ": " + splitInput[1];
    }
    private static boolean newuser(String input) {
        boolean userCreated = true;
        try {
            String[] splitInputs = input.split(" ");
            String line = "";
            FileReader fileReader = new FileReader("C:\\Users\\jorwh\\IdeaProjects\\networkLab3\\src\\com\\company\\logins.txt");
            String possibleFound = "";
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                String[] usrAndPwd = line.split(",");
                if (splitInputs[1].strip().equals(usrAndPwd[0].strip())) {
                    possibleFound = usrAndPwd[1];
                    userCreated = false;
                    break;
                }
            }
            bufferedReader.close();
            if(userCreated){
                System.out.println("dont worry, " + splitInputs[1] + "does not equal " + possibleFound);
                FileWriter fileWriter = new FileWriter("C:\\Users\\jorwh\\IdeaProjects\\networkLab3\\src\\com\\company\\logins.txt", true);
                fileWriter.write("\n" + splitInputs[1]+","+splitInputs[2]);
                fileWriter.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userCreated;
    }
}
