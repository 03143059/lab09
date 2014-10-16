import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Werner on 10/15/2014.
 */
public class UDPServer implements Runnable {

    //static variables
    static boolean verbose = true;

    //instance variables
    DatagramSocket connect;

    //constructor
    public UDPServer(DatagramSocket connect) {
        this.connect = connect;
    }

    public static void start() throws Exception {

        ExecutorService threadPoolExecutor =
                new ThreadPoolExecutor(
                        5, //corePoolSize,
                        10, //maxPoolSize,
                        5000, //keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );

        try {
            DatagramSocket serverSocket = new DatagramSocket(9080);
            System.out.println("\nListening for connections on port 9080...\n");
            while (true) //listen until user halts execution
            {
                UDPServer server = new UDPServer(serverSocket); //instantiate HttpServer
                if (verbose) {
                    System.out.println("Connection opened. (" + new Date() + ")");
                }
                //create new thread using a thread pool
                threadPoolExecutor.execute(server);
            }
        } catch (IOException e) {
            threadPoolExecutor.shutdown();
            System.err.println("Server error: " + e);
        }
    }

    @Override
    public void run() {
        try {
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            connect.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            System.out.println("Received by UDP: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            connect.send(sendPacket);
        } catch (Exception e) {

        }
    }
}
