package protocol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by Werner on 10/15/2014.
 */
public class DVClient {

    private final int port;

    public DVClient(int port) {
        this.port = port;
    }

    public void send(DistanceVectorMessage message) {
        try {
            for(Target t: message.getList()) {
                Socket clientSocket = new Socket(t.getIp(), port);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(message.toString() + '\n');
                clientSocket.close();
                System.out.print("*");
            }
            System.out.println("OK");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
