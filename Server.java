

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    PrintWriter wr;

    BufferedReader in;
    private boolean done;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(9999);
            while (!done) {
                Socket socketToReceive = serverSocket.accept();
                 in = new BufferedReader(new InputStreamReader(socketToReceive.getInputStream()));
                 wr = new PrintWriter(socketToReceive.getOutputStream(), true);
                Scanner sc = new Scanner(System.in);

                //This thread is created to handle incoming requests asynchronously.
                //This thread will handle incoming requests implemented using the lambda expressions
                //so that the main thread can handle other task(sending messages)
                Thread readThread = new Thread(() -> {
                    String messageToBeDisplayed;
                    try {
                        while ((messageToBeDisplayed = in.readLine()) != null) {
                            System.out.println("Client: " + messageToBeDisplayed);
                        }
                    } catch (IOException e) {
                            if (!done) {
                                System.out.println("Connection with client lost: " + e.getMessage());
                            }
                    }
                });

                readThread.start();

                String msg;
                while (true) {
                    msg = sc.nextLine();
                    wr.println(msg);
                    if (msg.equalsIgnoreCase("exit")) {
                        System.exit(0);
                    }
                }
            }
        } catch (IOException e) {
            shutdownServer();
        }
    }

    public void shutdownServer() {
        done = true;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            wr.write(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server).start();
    }
}


