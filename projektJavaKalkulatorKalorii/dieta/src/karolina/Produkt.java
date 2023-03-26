package karolina;

public class Produkt {

    private int idProduktu;
    private String nazwaProduktu;
    private double wartoscEnergetyczna;
    private double bialka;
    private double tluszcze;
    private double weglowodany;
    private double ilosc;
    private String jednostkaMiary;

    public Produkt() {}
    public Produkt(int id) {
        this(id, "", 0, 0,0,0,0,"");
    }
    public Produkt(int id, String nazwaProduktu) {
        this(id, nazwaProduktu, 0, 0,0,0,0,"");
    }

    public Produkt(int idProduktu, String nazwaProduktu,
                   double wartoscEnergetyczna, double bialka, double tluszcze, double weglowodany,
                   double ilosc, String jednostkaMiary) {
        this.idProduktu = idProduktu;
        this.nazwaProduktu = nazwaProduktu;
        this.wartoscEnergetyczna = wartoscEnergetyczna;
        this.bialka = bialka;
        this.tluszcze = tluszcze;
        this.weglowodany = weglowodany;
        this.ilosc = ilosc;
        this.jednostkaMiary = jednostkaMiary;
    }

    public int getIdProduktu() {
        return idProduktu;
    }

    public void setIdProduktu(int idProduktu) {
        this.idProduktu = idProduktu;
    }

    public String getNazwaProduktu() {
        return nazwaProduktu;
    }

    public void setNazwaProduktu(String nazwaProduktu) {
        this.nazwaProduktu = nazwaProduktu;
    }

    public double getWartoscEnergetyczna() {
        return wartoscEnergetyczna;
    }

    public void setWartoscEnergetyczna(double wartoscEnergetyczna) {
        this.wartoscEnergetyczna = wartoscEnergetyczna;
    }

    public double getBialka() {
        return bialka;
    }

    public void setBialka(double bialka) {
        this.bialka = bialka;
    }

    public double getTluszcze() {
        return tluszcze;
    }

    public void setTluszcze(double tluszcze) {
        this.tluszcze = tluszcze;
    }

    public double getWeglowodany() {
        return weglowodany;
    }

    public void setWeglowodany(double weglowodany) {
        this.weglowodany = weglowodany;
    }

    public double getIlosc() {
        return ilosc;
    }

    public void setIlosc(double ilosc) {
        this.ilosc = ilosc;
    }

    public String getJednostkaMiary() {
        return jednostkaMiary;
    }

    public void setJednostkaMiary(String jednostkaMiary) {
        this.jednostkaMiary = jednostkaMiary;
    }

    @Override
    public String toString() {
        return nazwaProduktu;
    }
}
