import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Werner on 10/15/2014.
 */
public class Lab09 {
    public static final int INFINITO = Integer.MAX_VALUE;

    static class Target {
        private String ip;
        private int dv;
        private boolean es_adyacente;

        public Target(String ip, int dv, boolean ea){
            this.ip = ip;
            this.dv = dv;
            this.es_adyacente = ea;
        }

        public String getIp() {
            return ip;
        }

        public int getDv() {
            return dv;
        }

        public boolean esAdyacente() {
            return es_adyacente;
        }

        @Override
        public String toString(){
            return ip + ":" + dv;
        }

    }

    static class Mensaje extends ArrayList<Target> {
        @Override
        public String toString(){
            StringBuilder res = new StringBuilder();
            res.append("Type:DV");
            res.append(System.lineSeparator());
            res.append("Len:");
            res.append(this.size());
            res.append(System.lineSeparator());
            for (Target t : this){
                res.append(t);
                res.append(System.lineSeparator());
            }
            return res.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        Mensaje list = new Mensaje();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Cuantos destinos son? ");
        int n = Integer.parseInt(in.readLine());
        for (int i = 1; i <= n; i++){
            System.out.print("Ingrese el destino #" + i + ": ");
            String ip = in.readLine();
            System.out.print("Ingrese el DV #" + i + ": ");
            int dv = Integer.parseInt(in.readLine());
            System.out.print("Es adyacente [S/N]? ");
            String op = in.readLine();
            list.add(new Target(ip, dv, op.equals("S") || op.equals("s")));
        }
        System.out.println(list);
        in.close();
    }
}
