package karolina;

public class Uzytkownik {
    private int idUzytkownika;
    private String nazwaUzytkownika;
    private String plec;
    private int wiek;
    private double waga;
    private double wzrost;
    private int poziomRuchu;
    private int cel;
    private double wartoscEnergetyczna;
    private double bialka;
    private double tluszcze;
    private double weglowodany;

    public Uzytkownik() {}

    public Uzytkownik(int idUzytkownika, String nazwaUzytkownika) {
        this.idUzytkownika = idUzytkownika;
        this.nazwaUzytkownika = nazwaUzytkownika;
    }

    public Uzytkownik(int idUzytkownika, String nazwaUzytkownika, String plec, int wiek, double waga, double wzrost,
                        int poziomRuchu, int cel,
                        double wartoscEnergetyczna, double bialka, double tluszcze, double weglowodany) {
        this.idUzytkownika = idUzytkownika;
        this.nazwaUzytkownika = nazwaUzytkownika;
        this.plec = plec;
        this.wiek = wiek;
        this.waga = waga;
        this.wzrost = wzrost;
        this.poziomRuchu = poziomRuchu;
        this.cel = cel;
        this.wartoscEnergetyczna = wartoscEnergetyczna;
        this.bialka = bialka;
        this.tluszcze = tluszcze;
        this.weglowodany = weglowodany;
    }

    public int getIdUzytkownika() {
        return idUzytkownika;
    }

    public void setIdUzytkownika(int idUzytkownika) {
        this.idUzytkownika = idUzytkownika;
    }

    public String getNazwaUzytkownika() {
        return nazwaUzytkownika;
    }

    public void setNazwaUzytkownika(String nazwaUzytkownika) {
        this.nazwaUzytkownika = nazwaUzytkownika;
    }

    public String getPlec() {
        return plec;
    }

    public void setPlec(String plec) {
        this.plec = plec;
    }

    public int getWiek() {
        return wiek;
    }

    public void setWiek(int wiek) {
        this.wiek = wiek;
    }

    public double getWaga() {
        return waga;
    }

    public void setWaga(double waga) {
        this.waga = waga;
    }

    public double getWzrost() {
        return wzrost;
    }

    public void setWzrost(double wzrost) {
        this.wzrost = wzrost;
    }

    public int getPoziomRuchu() {
        return poziomRuchu;
    }

    public void setPoziomRuchu(int poziomRuchu) {
        this.poziomRuchu = poziomRuchu;
    }

    public int getCel() {
        return cel;
    }

    public void setCel(int cel) {
        this.cel = cel;
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

    @Override
    public String toString() {
        return nazwaUzytkownika;
    }
}
