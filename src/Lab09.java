import protocol.DVClient;
import protocol.DVServer;
import protocol.DistanceVectorMessage;
import protocol.Target;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * Created by Werner on 10/15/2014.
 */
public class Lab09 {

    public static InetAddress address = null;
    public static int PORT = 9080; //default port
    public static boolean verbose = false;

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            int num = 0; // numero de interface
            FileInputStream fin = null; // archivo de entrada
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-v") || args[i].equals("-verbose")) {
                    verbose = true; //print status to standard out
                } else if (args[i].equals("-?") || args[i].equals("-help") || args[i].equals("-h")) {
                    showHelp();
                } else if (args[i].equals("-i")) {
                    if (args.length > (i + 1) && !args[i+1].startsWith("-") ){
                        // hay interface seleccionada
                        try {
                            num = Integer.parseInt(args[i+1]);
                        } catch (NumberFormatException nfe) {
                        }
                        i++;
                    }
                } else if (args[i].equals("-f")) {
                    if (args.length > (i + 1) && !args[i+1].startsWith("-") ){
                        // hay archivo de entrada
                        try {
                            fin = new FileInputStream(args[i+1]);
                        } catch (IOException nfe) {
                        }
                        i++;
                    } else showHelp();
                } else showHelp();
            } // end-for

            selectInterface(num); // mostrar interfaces o la interfaz seleccionada

            // Start server thread
            (new Thread() {
                public void run() {
                    DVServer.start(Lab09.address, Lab09.PORT); // iniciar servidor de mensajes
                }
            }).start();

            Thread.sleep(500); // esperar un momento

            DistanceVectorMessage message = new DistanceVectorMessage();
            BufferedReader in = new BufferedReader(new InputStreamReader((fin == null)?System.in:fin));
            if (fin == null) {
                System.out.println("Ingrese los destinos y los distance vectors en la forma: IP_ADDRESS:DV");
                System.out.println("Ingrese una linea en blanco para terminar.");
            } else {
                System.out.println("Leyendo destinos del archivo de entrada.");
            }
            String l;
            while ((l = in.readLine()) != null && l.length() > 0) {
                StringTokenizer st = new StringTokenizer(l, ":");
                String ip = st.nextToken();
                int dv = Integer.parseInt(st.nextToken());
                message.add(new Target(ip, dv));
            }
            in.close();
            if (fin != null) fin.close();
            System.out.println();
            System.out.println("Mensaje a enviar:");
            System.out.println("------------------");
            System.out.println(message);
            DVClient client = new DVClient(PORT);
            System.out.print("Enviando mensaje a los adyacentes: ");
            client.send(message);
            System.out.println();
            System.out.println("El servidor sigue ejecutandose.");
            System.out.println("Presione Ctrl+C para interrumpir el programa.");
            System.out.println();

        } else {
            showHelp();
        }
    }

    private static void showHelp() {
        System.out.println("Uso: java Lab09 -i [interface] [-h | -? | -help] [-f archivo_de_destinos]");
        System.exit(1);
    }

    private static void selectInterface(int num) throws SocketException {
        Enumeration nifEnm = NetworkInterface.getNetworkInterfaces();
        while (nifEnm.hasMoreElements()) {
            NetworkInterface nif = (NetworkInterface) nifEnm.nextElement();
            if (!nif.isLoopback() && nif.getInterfaceAddresses().size() > 0) {
                Enumeration addrEnum = nif.getInetAddresses();
                int i = 0;
                while (addrEnum.hasMoreElements()) {
                    InetAddress a = (InetAddress) addrEnum.nextElement();
                    if (a instanceof Inet4Address) {
                        if (i == 0) {
                            if (num == 0)// mostrar interfaces?
                                System.out.println(String.format("%d\t%s\t%s",
                                        nif.getIndex(), nif.getName(), nif.getDisplayName()));
                        }
                        i++;
                        // mostrar interfaces?
                        if (num == 0)
                            System.out.println("\t" + a.getHostAddress());
                        else if (num == nif.getIndex())
                            address = a;
                    }
                } // end-while address
            }
        } // end-while interfaces
        if (address == null) {
            if (num > 0)
                System.err.println("El numero de interfaz es invalido!");
            System.exit(2);
        }

        System.out.println("Utilizando IP: " + address.getHostAddress());
    }
}

