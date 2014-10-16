package protocol;

public class Target {
    private String ip;
    private int dv;

    public Target(String ip, int dv) {
        this.ip = ip;
        this.dv = dv;
    }

    public String getIp() {
        return ip;
    }

    public int getDv() {
        return dv;
    }

    @Override
    public String toString() {
        return ip + ":" + dv;
    }

}
