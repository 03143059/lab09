package protocol;

import java.util.ArrayList;

public class DistanceVectorMessage {
    private ArrayList<Target> list = new ArrayList<Target>();
    public static final int INFINITO = Integer.MAX_VALUE;

    public void add(Target t) {
        getList().add(t);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Type:DV");
        res.append(System.lineSeparator());
        res.append("Len:");
        res.append(getList().size());
        res.append(System.lineSeparator());
        for (Target t : getList()) {
            res.append(t);
            res.append(System.lineSeparator());
        }
        return res.toString();
    }

    public ArrayList<Target> getList() {
        return list;
    }
}
