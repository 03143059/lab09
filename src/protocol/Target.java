package protocol;

public class Target {
    private String ip;
    private int dv;
    private boolean es_adyacente;

    public Target(String ip, int dv, boolean ea) {
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
    public String toString() {
        return ip + ":" + dv;
    }

}
