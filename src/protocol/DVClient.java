package protocol;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by Werner on 10/15/2014.
 */
public class DVClient {

    private final int port;

    public DVClient(int port) {
        this.port = port;
    }

    public void send(DistanceVectorMessage message) {
        String result = "";
        boolean hayError = false;
        for (Target t : message.getList()) {
            try {
                Socket clientSocket = new Socket(t.getIp(), port);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(message.toString() + '\n');
                clientSocket.close();
                System.out.print("*");
            } catch (Exception e) {
                hayError = true;
                System.out.print("X");
                result += System.lineSeparator() + "ERROR " + t.getIp() + ": " + e.getMessage();
            }
        }
        if (!hayError) result = System.lineSeparator() + "OK. Mensaje enviado";
        System.out.println(result);
    }
}
