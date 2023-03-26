package karolina;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Posilek {
    private int idPosilku;
    private Timestamp godzinaPosilku;
    private String nazwaPosilku;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public Posilek(int idPosilku, Timestamp godzinaPosilku) {
        this(idPosilku, godzinaPosilku, Posilek.formatujDate(godzinaPosilku));
    }

    public Posilek(int idPosilku, Timestamp godzinaPosilku, String nazwaPosilku) {
        this.idPosilku = idPosilku;
        this.godzinaPosilku = godzinaPosilku;
        this.nazwaPosilku = nazwaPosilku;
    }

    public int getIdPosilku() {
        return idPosilku;
    }

    public void setIdPosilku(int idPosilku) {
        this.idPosilku = idPosilku;
    }

    public Timestamp getGodzinaPosilku() {
        return godzinaPosilku;
    }

    public void setGodzinaPosilku(Timestamp godzinaPosilku) {
        this.godzinaPosilku = godzinaPosilku;
    }

    public String getNazwaPosilku() {
        return nazwaPosilku;
    }

    public void setNazwaPosilku(String nazwaPosilku) {
        this.nazwaPosilku = nazwaPosilku;
    }

    private static String formatujDate(Timestamp timestamp) {
        return sdf.format(timestamp);
    }

    @Override
    public String toString() {
        return nazwaPosilku;
    }
}
