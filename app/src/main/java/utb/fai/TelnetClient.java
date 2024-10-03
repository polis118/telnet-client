package utb.fai;

import java.io.*;
import java.net.*;

public class TelnetClient {

    private String serverIp;
    private int port;

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try {
            Socket socket = new Socket(serverIp, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Thread for reading from the server and writing to System.out
                Thread readerThread = new Thread(() -> {
                    try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
                        String userInput;
                        while (userInput = stdIn.readLine()) != null) {
                            if ("/QUIT".equalsIgnoreCase(userInput)) {
                                break;
                            }
                            out.println(userInput);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                // Thread for reading from System.in and sending to the server
                Thread writerThread = new Thread(() -> {
                    try {
                        String response;
                        while ((response = in.readLine()) != null) {
                            System.out.println(response);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                readerThread.start();
                writerThread.start();

                try {
                    readerThread.join();
                } catch (InterruptedException e) {
                    System.out.println("Reader thread was interrupted");
                }
                out.close();
                in.close();
                socket.close();

                // readerThread.join();
                // writerThread.join();
            // Implementation of receiving and sending data
            // Implement processing of input from the user and sending data to the server
            // Implement response processing from the server and output to the console
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
