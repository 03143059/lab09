package protocol;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Werner on 10/15/2014.
 */
public class DVServer implements Runnable {

    //static variables
    static boolean verbose = true;

    //instance variables
    Socket connect;

    //constructor
    public DVServer(Socket connect) {
        this.connect = connect;
    }

    public static void start(InetAddress address, int port) {
        ExecutorService threadPoolExecutor =
                new ThreadPoolExecutor(
                        5, //corePoolSize,
                        10, //maxPoolSize,
                        5000, //keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );

        try {
            ServerSocket serverConnect = new ServerSocket(port, 0, address);
            System.out.println("\nListening for connections on port 9080...\n");
            while (true) //listen until user halts execution
            {
                DVServer server = new DVServer(serverConnect.accept()); //instantiate HttpServer
                if (verbose) {
                    System.out.println("Connection opened from: " +
                            serverConnect.getInetAddress() + " (" + new Date() + ")");
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
        BufferedReader in = null;

        try {
            // set timeout to 10 seconds so the clients don't block the server for ever
            connect.setSoTimeout(10000);
            //get character input stream from client
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));

            //get first line of request from client
            String input = in.readLine();
            if (input == null || !input.equals("Type:DV")) throw new Exception("Invalid request");
            input = in.readLine(); // get next line: Len:#
            //create StringTokenizer to parse request
            StringTokenizer parse = new StringTokenizer(input, ":");
            parse.nextToken(); // ignore Len
            //parse out length
            int len = Integer.parseInt(parse.nextToken());
            if (verbose) {
                System.out.println("Expecting " + len + " targets");
            }
            for (int i = 1; i <= len; i++) {
                //get first line of request from client
                input = in.readLine();
                if (input == null) throw new Exception("Invalid request");
                parse = new StringTokenizer(input, ":");
                String ip = parse.nextToken();
                int dv = Integer.parseInt(parse.nextToken());
                Target t = new Target(ip, dv, false);
                if (verbose) {
                    System.out.println("Received target #" + i + ": " + t);
                }
            }
        } catch (SocketTimeoutException ste) {
            System.out.println("Connection timeout");
        } catch (Exception ioe) {
            System.err.println("Server Error: " + ioe);
        } finally {
            close(in); //close character input stream
            close(connect); //close socket connection

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }

    /**
     * close method closes the given stream.
     *
     * @param stream
     */
    public void close(Object stream) {
        if (stream == null) return;

        try {
            if (stream instanceof Reader) {
                ((Reader) stream).close();
            } else if (stream instanceof Writer) {
                ((Writer) stream).close();
            } else if (stream instanceof InputStream) {
                ((InputStream) stream).close();
            } else if (stream instanceof OutputStream) {
                ((OutputStream) stream).close();
            } else if (stream instanceof Socket) {
                ((Socket) stream).close();
            } else {
                System.err.println("Unable to close object: " + stream);
            }
        } catch (Exception e) {
            System.err.println("Error closing stream: " + e);
        }
    }
}
