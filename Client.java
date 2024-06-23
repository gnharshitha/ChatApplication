import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    private Socket client;
    private boolean done;
    BufferedReader br;
    PrintWriter pr;

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 9999);
             br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             pr = new PrintWriter(client.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);

            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = br.readLine()) != null) {
                        System.out.println("Server: " + serverMessage);
                    }
                } catch (IOException e) {
                    shutdown();
                }
            }).start();

            while (!done) {
                String message = sc.nextLine();
                if(message.equalsIgnoreCase("exit")){
                    System.exit(0);
                }
                pr.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        done = true;
        try {
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        new Thread(client).start();
    }
}
























