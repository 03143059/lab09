import protocol.DVClient;
import protocol.DVServer;
import protocol.DistanceVectorMessage;
import protocol.Target;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by Werner on 10/15/2014.
 */
public class Lab09 {

    public static InetAddress address = null;
    public static int PORT = 9080; //default port

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("-i")) {
            int num = 0;
            if (args.length == 2)
                try {
                    num = Integer.parseInt(args[1]);
                } catch (NumberFormatException nfe) {
                }
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

            // Start server thread
            (new Thread() {
                public void run() {
                    DVServer.start(Lab09.address, Lab09.PORT); // iniciar servidor de mensajes
                }
            }).start();

            Thread.sleep(500); // esperar un momento

            DistanceVectorMessage message = new DistanceVectorMessage();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Cuantos destinos son? ");
            int n = Integer.parseInt(in.readLine());
            for (int i = 1; i <= n; i++) {
                System.out.print("Ingrese el destino #" + i + ": ");
                String ip = in.readLine();
                System.out.print("Ingrese el DV #" + i + ": ");
                int dv = Integer.parseInt(in.readLine());
                System.out.print("Es adyacente [S/N]? ");
                String op = in.readLine();
                message.add(new Target(ip, dv, op.equals("S") || op.equals("s")));
            }
            in.close();
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
            System.out.println("Uso: java Ruteador -i [interface]");
            System.exit(1);
        }
    }
}

