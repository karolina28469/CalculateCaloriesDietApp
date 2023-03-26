package karolina;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML private BorderPane startBorderPane;
    @FXML private Label nazwaUzytkownikaLabel;

    @FXML private Label dzienTygodniaLabel;
    @FXML private Label dataLabel;
    @FXML private Label statusLabel;
    @FXML private TextField searchField;

    @FXML private Label zjedzoneKalorieLabel;
    @FXML private Label maxKaloriiLabel;
    @FXML private ProgressBar kalorieProgressBar;
    @FXML private Label zjedzoneBialkaLabel;
    @FXML private Label maxBialkaLabel;
    @FXML private ProgressBar bialkaProgressBar;
    @FXML private Label zjedzoneTluszczeLabel;
    @FXML private Label maxTluszczyLabel;
    @FXML private ProgressBar tluszczeProgressBar;
    @FXML private Label zjedzoneWeglowodanyLabel;
    @FXML private Label maxWeglowodanowLabel;
    @FXML private ProgressBar weglowodanyProgressBar;

    @FXML private ListView<Produkt> produktyListView;
    private ObservableList<Produkt> produktyOList;
    @FXML private ListView<Produkt> zjedzoneListView;
    private ObservableList<Produkt> zjedzoneOList;
    @FXML private ListView<Posilek> posilkiListView;
    private ObservableList<Posilek> posilkiOList;

    @FXML private TextField nazwaProduktuTextField;
    @FXML private TextField iloscTextField;
    @FXML private TextField wartoscEnergetycznaTextField;
    @FXML private TextField bialkaTextField;
    @FXML private TextField tluszczeTextField;
    @FXML private TextField weglowodanyTextField;
    @FXML private Label jednostkaMiaryLabel;
    @FXML private TextField porcjaTextField;
    @FXML private TextField razyTextField;
    @FXML private TextField podzielTextField;

    private int idBiezacegoProduktu = 0;
    @FXML private Label iloscLabel;
    @FXML private Label wartoscEnergetycznaLabel;
    @FXML private Label bialkaLabel;
    @FXML private Label tluszczeLabel;
    @FXML private Label weglowodanyLabel;

    @FXML private Button edytujProduktButton;
    @FXML private Button dodajProduktButton;
    @FXML private Button usunProduktButton;

    @FXML private TextField dzisTextField;

    DietaDb db = null;
    Uzytkownik uzytkownik; // = new Uzytkownik(1, "Karolina");

    private Boolean edycjaProduktow = true;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        otworzDb();

        produktyOList = FXCollections.observableArrayList();
        produktyListView.setItems(produktyOList);
        produktyListView.getSelectionModel().selectedIndexProperty().addListener(new ProduktyListViewListener());
        pokazProdukty("");

        zjedzoneOList = FXCollections.observableArrayList();
        zjedzoneListView.setItems(zjedzoneOList);
        zjedzoneListView.getSelectionModel().selectedIndexProperty().addListener(new ZjedzoneListViewListener());

        posilkiOList = FXCollections.observableArrayList();
        posilkiListView.setItems(posilkiOList);
        posilkiListView.getSelectionModel().selectedIndexProperty().addListener(new PosilkiListViewChangeListener());

        edytujProdukty();
        wlaczSzczegolyProduktuChangeListener(true);

        pokazDateIGodzine();
    }

    public void ustawUzytkownika(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
        nazwaUzytkownikaLabel.setText(uzytkownik.getNazwaUzytkownika());
        sumujWartosciOdzywcze();
        ustawMaxWartosciDlaWartosciOdzywczych();

        pokazPosilki();
        // pokazZjedzone();
        dzisTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            pokazPosilki();
        });
    }

    private void ustawMaxWartosciDlaWartosciOdzywczych() {
        maxKaloriiLabel.setText(Double.toString(uzytkownik.getWartoscEnergetyczna()));
        maxBialkaLabel.setText(Double.toString(uzytkownik.getBialka()));
        maxTluszczyLabel.setText(Double.toString(uzytkownik.getTluszcze()));
        maxWeglowodanowLabel.setText(Double.toString(uzytkownik.getWeglowodany()));
    }

    private void otworzDb() {
        try {
            db = Singleton.initialize();
        }
        catch(CommunicationsException e) {
            System.out.println("### Brak dostępu do bazy danych ###");
            System.exit(0);
        }
        catch(SQLException e) {
            Utils.displayException(e);
        }
    }

    private Timestamp terazGodzina() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        long p = ((long) zamienNaLiczbe(dzisTextField.getText())) * 24 * 60 * 60 * 1000;
        time.setTime(time.getTime() + p);
        return time;
    }

    private void pokazDateIGodzine() {
        Timestamp timestamp = terazGodzina();

        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        dataLabel.setText(date.format(timestamp));

//        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
//        dzienTygodniaLabel.setText(time.format(timestamp));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dzienTygodnia = sdf.format(d);
        dzienTygodniaLabel.setText(dzienTygodnia);
    }

    private void pokazProdukty(String maska) {
        try {
            List<Produkt> list = db.dajProdukty(maska);
            produktyOList.clear();
            produktyOList.addAll(list);
        }
        catch (SQLException e) {
            Utils.displayException(e);
        }
    }

    private void pokazZjedzone(Posilek posilek) {
        String komunikat = "";

        if (posilek == null) {
            zjedzoneOList.clear();
            return;
        }

        try {
            List<Produkt> list = db.dajZjedzone(posilek);
            zjedzoneOList.clear();
            zjedzoneOList.addAll(list);
            komunikat = Komunikaty.OK;
        }
        catch (SQLException e) {
            komunikat = Komunikaty.UPS;
            Utils.displayException(e);
        }
        statusLabel.setText(komunikat);
    }

    private void sumujWartosciOdzywcze() {
        String komunikat = "";

        DecimalFormat liczba = new DecimalFormat("###.##");

        double wartoscEnergetyczna = 0;
        double bialka = 0, tluszcze = 0, weglowodany = 0;

        try {
            Timestamp time = terazGodzina();
            time = Utils.poczatekDnia(time, 0);
            Timestamp time2 = Utils.poczatekDnia(time, 1);
            List<Produkt> list = db.dajDzisiajZjedzone(uzytkownik, time, time2);
            for (Produkt p: list) {
                wartoscEnergetyczna += p.getWartoscEnergetyczna();
                bialka += p.getBialka();
                tluszcze += p.getTluszcze();
                weglowodany += p.getWeglowodany();
            }

            zjedzoneKalorieLabel.setText(liczba.format(wartoscEnergetyczna));
            double d1 = Double.parseDouble(maxKaloriiLabel.getText());
            kalorieProgressBar.setProgress(wartoscEnergetyczna / d1);

            zjedzoneBialkaLabel.setText(liczba.format(bialka));
            double d2 = Double.parseDouble(maxBialkaLabel.getText());
            bialkaProgressBar.setProgress(bialka/ d2);

            zjedzoneTluszczeLabel.setText(liczba.format(tluszcze));
            double d3 = Double.parseDouble(maxTluszczyLabel.getText());
            tluszczeProgressBar.setProgress(tluszcze / d3);

            zjedzoneWeglowodanyLabel.setText(liczba.format(weglowodany));
            double d4 = Double.parseDouble(maxWeglowodanowLabel.getText());
            weglowodanyProgressBar.setProgress(weglowodany / d4);


            if (wartoscEnergetyczna > d1) {
                zjedzoneKalorieLabel.setTextFill(Color.RED);
            }
            else {
                zjedzoneKalorieLabel.setTextFill(Color.web("#587f5a"));
            }
            if (bialka > d2) {
                zjedzoneBialkaLabel.setTextFill(Color.RED);
            }
            else {
                zjedzoneBialkaLabel.setTextFill(Color.web("#587f5a"));
            }
            if (tluszcze > d3) {
                zjedzoneTluszczeLabel.setTextFill(Color.RED);
            }
            else {
                zjedzoneTluszczeLabel.setTextFill(Color.web("#587f5a"));
            }
            if (weglowodany > d4) {
                zjedzoneWeglowodanyLabel.setTextFill(Color.RED);
            }
            else {
                zjedzoneWeglowodanyLabel.setTextFill(Color.web("#587f5a"));
            }
        }
        catch (SQLException e) {
            komunikat = Komunikaty.UPS;
            Utils.displayException(e);
        }
        statusLabel.setText(komunikat);
    }
    
    @FXML
    private void addProduct(){
 //       zjedzoneOList.add("Big");
        try {
            DietaDb db = Singleton.initialize();
            Produkt selectedItem = produktyListView.getSelectionModel().getSelectedItem();
            zjedzoneOList.add(selectedItem);
            pokazSzczegoloweInformacjeOProdukcie(selectedItem);

           // String i = db.getProductsInfo(selectedItem);
          //  info.setText(i);
            //db.getProductsInfo("ananas");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dodaje produkt do bazy produktów
     */
    @FXML
    private void dodajProdukt() {
        Produkt produkt = przeliczPorcje();
        String komunikat = "";
        // +++ sprawdzic poprawnosc danych
        if (produkt.getIlosc() <= 0) {
            komunikat = Komunikaty.BLEDNE_DANE;
        }
        else {
            try {
                int id = db.dodajProdukt(produkt);
                produkt.setIdProduktu(id);
                produktyOList.add(produkt);
                komunikat = String.format(Komunikaty.PRODUKT_ZOSTAL_DODANY, produkt.getNazwaProduktu());
                sumujWartosciOdzywcze();
            }
            catch (SQLException e) {
                komunikat = Komunikaty.UPS;
                Utils.displayException(e);
            }
        }
        statusLabel.setText(komunikat);

        produktyListView.scrollTo(produkt);
    }

    @FXML
    private void usunProdukt() {
        Produkt zaznaczony = produktyListView.getSelectionModel().getSelectedItem();
        String komunikat = "";
        if (zaznaczony == null) {
            komunikat = Komunikaty.WYBIERZ_PRODUKT;
        }
        else {
            try {
                db.usunProdukt(zaznaczony);
                komunikat = String.format(Komunikaty.PRODUKT_ZOSTAL_USUNIETY, zaznaczony.getNazwaProduktu());
            }
            catch (SQLException e) {
                komunikat = Komunikaty.UPS;
                Utils.displayException(e);
            }
        }
        statusLabel.setText(komunikat);
        pokazProdukty("");
    }

    //################################################
    //##########          POSILKI          ##########
    //################################################

    private void pokazPosilki() {
        String komunikat = "";
        try {
            Timestamp time = terazGodzina();
            time = Utils.poczatekDnia(time, 0);
            Timestamp time2 = Utils.poczatekDnia(time, 1);

            List<Posilek> list = db.dajPosilki(uzytkownik, time, time2);
            posilkiOList.setAll(list);
            posilkiListView.getSelectionModel().selectLast();
            komunikat = Komunikaty.OK;
        }
        catch (SQLException e) {
            komunikat = Komunikaty.UPS;
            Utils.displayException(e);
        }
        statusLabel.setText(komunikat);
    }



    //################################################
    //##########          ZJEDZONE          ##########
    //################################################

    /**
     * Dodaje zjedzony produkt do listy zjedzonych i do bazy danych
     */
    @FXML
    private void dodajZjedzonyProdukt() {
        String komunikat = "";

        Timestamp timestamp = terazGodzina();
        Posilek posilek = new Posilek(0, timestamp);
        Produkt produkt = przeliczPorcje();

        // +++ mozna sprawdzic dokladniej poprawnosc danych
        if (produkt.getIlosc() <= 0) {
            komunikat = Komunikaty.BLEDNE_DANE;
        }
        else {
            try {
                Timestamp time = terazGodzina();
                int id  = db.dodajZjedzonyProdukt(produkt, posilek, uzytkownik, time);
                produkt.setIdProduktu(id);
                zjedzoneOList.add(produkt);
                komunikat = String.format(Komunikaty.PRODUKT_ZOSTAL_DODANY, produkt.getNazwaProduktu());
            }
            catch (SQLException e) {
                komunikat = Komunikaty.UPS;
                Utils.displayException(e);
            }
        }
        zjedzoneListView.scrollTo(produkt);

        sumujWartosciOdzywcze();
        pokazPosilki();

        statusLabel.setText(komunikat);
    }

    @FXML
    private void usunZjedzonyProdukt() {
        Produkt zaznaczony = zjedzoneListView.getSelectionModel().getSelectedItem();
        String komunikat = "";
        if (zaznaczony == null) {
            komunikat = Komunikaty.WYBIERZ_PRODUKT;
        }
        else {
            try {
                db.usunZjedzonyProdukt(zaznaczony);
                komunikat = String.format(Komunikaty.PRODUKT_ZOSTAL_USUNIETY, zaznaczony.getNazwaProduktu());
            }
            catch (SQLException e) {
                komunikat = Komunikaty.UPS;
                Utils.displayException(e);
            }
        }
//+++ wybrac ten posilek z ktorego ellement usuwasz
        pokazZjedzone(null);
        sumujWartosciOdzywcze();
        pokazPosilki();
        statusLabel.setText(komunikat);
    }





    /**
     * Wyszukuje produkty w bazie danych wedlug maski.
     * Jeżeli podano jedną lub dwie litery - wyszukuje produkty, których nazwa zaczyna się tymi literami.
     * Jeżeli podano więcej niż dwie litery - szuka produktów zawierających ten tekst w dowolnym miejscu.
     */
    @FXML
    private void searchProduct(){
        String product = searchField.getText();
        pokazProdukty(product);
    }

    /**
     * Wypełnia formularz szczegółowymi informacjami danego produktu.
     * @param wybranyProdukt - wyswietlany produkt
     */
    @FXML
    private void pokazSzczegoloweInformacjeOProdukcie(Produkt wybranyProdukt) {
        wlaczSzczegolyProduktuChangeListener(false);

        idBiezacegoProduktu = wybranyProdukt.getIdProduktu();

        nazwaProduktuTextField.setText(wybranyProdukt.getNazwaProduktu());
        iloscTextField.setText(Double.toString(wybranyProdukt.getIlosc()));
        wartoscEnergetycznaTextField.setText(Double.toString(wybranyProdukt.getWartoscEnergetyczna()));
        bialkaTextField.setText(Double.toString(wybranyProdukt.getBialka()));
        tluszczeTextField.setText(Double.toString(wybranyProdukt.getTluszcze()));
        weglowodanyTextField.setText(Double.toString(wybranyProdukt.getWeglowodany()));
        jednostkaMiaryLabel.setText(wybranyProdukt.getJednostkaMiary());
        porcjaTextField.setText(Double.toString(wybranyProdukt.getIlosc()));
        razyTextField.setText("1");
        podzielTextField.setText("1");

        przeliczPorcje();
        wlaczSzczegolyProduktuChangeListener(true);
    }

    /**
     * Zamienia string na liczbe lub zero dla stringu pustego.
     * @param s - badany string
     * @return
     */
    private double zamienNaLiczbe(String s) {
        double ret = 0;
        s = s.trim();
//        if (!s.isEmpty()) {
//            ret = Double.parseDouble(s);
//        }
        try {
            ret = Double.parseDouble(s);
        }
        catch (NumberFormatException | NullPointerException nfe) {
        }
        return ret;
    }

    /**
     * Oblicza wielkość porcji na podstawie trzech podanych ilosci
     * @return
     */
    @FXML
    private Produkt przeliczPorcje() {
        double temp;
        Produkt porcja = new Produkt();

        porcja.setIdProduktu(idBiezacegoProduktu);
        porcja.setNazwaProduktu(nazwaProduktuTextField.getText());
        porcja.setJednostkaMiary(jednostkaMiaryLabel.getText());

        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat separatorTysiecy = new DecimalFormat("###,###.#");

        double wielkoscPorcji = zamienNaLiczbe(porcjaTextField.getText());
        wielkoscPorcji *= zamienNaLiczbe(razyTextField.getText());
        temp = zamienNaLiczbe(podzielTextField.getText());
        wielkoscPorcji = temp == 0 ? 0 : wielkoscPorcji / temp;

        iloscLabel.setText(separatorTysiecy.format(wielkoscPorcji));
        porcja.setIlosc(wielkoscPorcji);

        temp = zamienNaLiczbe(iloscTextField.getText());
        double sto = temp == 0 ? 1 : temp;

        temp = wielkoscPorcji * zamienNaLiczbe(wartoscEnergetycznaTextField.getText()) / sto;
        wartoscEnergetycznaLabel.setText(separatorTysiecy.format(temp));
        porcja.setWartoscEnergetyczna(temp);

        temp = wielkoscPorcji * zamienNaLiczbe(bialkaTextField.getText()) / sto;
        bialkaLabel.setText(separatorTysiecy.format(temp));
        porcja.setBialka(temp);

        temp = wielkoscPorcji * zamienNaLiczbe(tluszczeTextField.getText()) / sto;
        tluszczeLabel.setText(separatorTysiecy.format(temp));
        porcja.setTluszcze(temp);

        temp = wielkoscPorcji * zamienNaLiczbe(weglowodanyTextField.getText()) / sto;
        weglowodanyLabel.setText(separatorTysiecy.format(temp));
        porcja.setWeglowodany(temp);

        return porcja;
    }

    private void wlaczSzczegolyProduktuChangeListener(boolean wlacz) {
        SzczegolyProduktuChangeListener szczegolyProduktuChangeListener = new SzczegolyProduktuChangeListener();
        if (wlacz) {
            iloscTextField.textProperty().addListener(szczegolyProduktuChangeListener);
            wartoscEnergetycznaTextField.textProperty().addListener(szczegolyProduktuChangeListener);
            bialkaTextField.textProperty().addListener(szczegolyProduktuChangeListener);
            tluszczeTextField.textProperty().addListener(szczegolyProduktuChangeListener);
            weglowodanyTextField.textProperty().addListener(szczegolyProduktuChangeListener);

            porcjaTextField.textProperty().addListener(szczegolyProduktuChangeListener);
            razyTextField.textProperty().addListener(szczegolyProduktuChangeListener);
            podzielTextField.textProperty().addListener(szczegolyProduktuChangeListener);
        }
        else {
            iloscTextField.textProperty().removeListener(szczegolyProduktuChangeListener);
            wartoscEnergetycznaTextField.textProperty().removeListener(szczegolyProduktuChangeListener);
            bialkaTextField.textProperty().removeListener(szczegolyProduktuChangeListener);
            tluszczeTextField.textProperty().removeListener(szczegolyProduktuChangeListener);
            weglowodanyTextField.textProperty().removeListener(szczegolyProduktuChangeListener);

            porcjaTextField.textProperty().removeListener(szczegolyProduktuChangeListener);
            razyTextField.textProperty().removeListener(szczegolyProduktuChangeListener);
            podzielTextField.textProperty().removeListener(szczegolyProduktuChangeListener);
        }
    }

    private class ProduktyListViewListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldIndeks, Number newIndeks) {
            if (newIndeks.intValue() < 0) {
                return;
            }
            Produkt wybrany = produktyListView.getSelectionModel().getSelectedItem();
            pokazSzczegoloweInformacjeOProdukcie(wybrany);
        }
    }

    private class ZjedzoneListViewListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Produkt wybrany = zjedzoneListView.getSelectionModel().getSelectedItem();
            if (wybrany != null) {
                pokazSzczegoloweInformacjeOProdukcie(wybrany);
            }
        }
    }

    private class PosilkiListViewChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Posilek wybrany = posilkiListView.getSelectionModel().getSelectedItem();
            if (wybrany != null) {
                pokazZjedzone(wybrany);
            }
        }
    }

    private class SzczegolyProduktuChangeListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (oldValue.equals(newValue)) {
                return;
            }
            przeliczPorcje();
        }
    }


    @FXML
    private void edytujProdukty() {
        Boolean ustaw = !edycjaProduktow;

        nazwaProduktuTextField.setDisable(!ustaw);
        iloscTextField.setDisable(!ustaw);
        wartoscEnergetycznaTextField.setDisable(!ustaw);
        bialkaTextField.setDisable(!ustaw);
        tluszczeTextField.setDisable(!ustaw);
        weglowodanyTextField.setDisable(!ustaw);

        dodajProduktButton.setVisible(ustaw);
        dodajProduktButton.setManaged(ustaw);

        edytujProduktButton.setText(ustaw ? "WRÓĆ" : "EDYTUJ DANE PRODUKTU");

        edycjaProduktow = ustaw;
    }

    //##########################


    @FXML
    private void przejdzDoPaneluStatystyk() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("statystyki.fxml"));
        Parent b = loader.load();
        StatystykiController statystykiController = loader.getController();
        Uzytkownik user = uzytkownik;
        statystykiController.ustawUzytkownika(user);

        startBorderPane.getChildren().setAll(b);
    }

    @FXML
    private void cofnijDoPaneluStartowego() throws Exception{
        BorderPane b = FXMLLoader.load(getClass().getResource("scenaStartowa.fxml"));
        startBorderPane.getChildren().setAll(b);
    }

}