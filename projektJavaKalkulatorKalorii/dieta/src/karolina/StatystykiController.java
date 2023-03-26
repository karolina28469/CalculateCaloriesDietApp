package karolina;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatystykiController implements Initializable {

    @FXML private BorderPane startBorderPane;
    @FXML private Label nazwaUzytkownikaLabel;

    @FXML private Label dzienTygodniaLabel;
    @FXML private Label dataLabel;
    @FXML private Label statusLabel;

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

    @FXML private Button tydzienButton;
    @FXML private Button miesiacButton;
    private int liczbaDniHistorii = -7 + 1;
    @FXML private Label podsumowanieLabel;

    @FXML private BarChart kalorieBarChart;
    @FXML private Button kalorieButton;
    @FXML private Button bialkaButton;
    @FXML private Button tluszczeButton;
    @FXML private Button weglowodanyButton;


    DietaDb db = null;
    Uzytkownik uzytkownik;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        otworzDb();
        pokazDateIGodzine();
    }

    public void ustawUzytkownika(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
        nazwaUzytkownikaLabel.setText(uzytkownik.getNazwaUzytkownika());

        ustawMaxWartosciDlaWartosciOdzywczych();
        pokazWykresWartosciEnergetycznej();

        tydzienButton.setOnAction(event -> {
            liczbaDniHistorii = -7 + 1;
            pokazWykresWartosciEnergetycznej();
            podsumowanieLabel.setText("Podsumowanie ostatniego tygodnia");

        });
        miesiacButton.setOnAction(event -> {
            liczbaDniHistorii = -3 + 1;
            pokazWykresWartosciEnergetycznej();
            podsumowanieLabel.setText("Podsumowanie ostatniego miesiąca");
        });

        kalorieButton.setOnAction(event -> pokazWykresWartosciEnergetycznej());
        bialkaButton.setOnAction(event -> pokazWykresBiałka());
        tluszczeButton.setOnAction(event -> pokazWykresTluszczow());
        weglowodanyButton.setOnAction(event -> pokazWykresWeglowodanow());
    }

    private void ustawMaxWartosciDlaWartosciOdzywczych() {
        maxKaloriiLabel.setText(Double.toString(uzytkownik.getWartoscEnergetyczna()));
        maxBialkaLabel.setText(Double.toString(uzytkownik.getBialka()));
        maxTluszczyLabel.setText(Double.toString(uzytkownik.getTluszcze()));
        maxWeglowodanowLabel.setText(Double.toString(uzytkownik.getWeglowodany()));
        System.out.println("aaa " + uzytkownik.getWartoscEnergetyczna());
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

    private void pokazDateIGodzine() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        dataLabel.setText(date.format(timestamp));

//        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
//        dzienTygodniaLabel.setText(time.format(timestamp));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dzienTygodnia = sdf.format(d);
        dzienTygodniaLabel.setText(dzienTygodnia);
    }


    private void sumujWartosciOdzywcze(List<Punkt> list) {
        String komunikat = "";
        DecimalFormat liczba = new DecimalFormat("###.##");

        double wartoscEnergetyczna = 0;
        double bialka = 0, tluszcze = 0, weglowodany = 0;

        for (Punkt p: list) {
            wartoscEnergetyczna += p.getY1();
            bialka += p.getY2();
            tluszcze += p.getY3();
            weglowodany += p.getY4();
        }
        int size = list.size() == 0 ? 1 : list.size();
        wartoscEnergetyczna /= size;
        bialka /= size;
        tluszcze /= size;
        weglowodany /= size;


        zjedzoneKalorieLabel.setText(liczba.format(wartoscEnergetyczna));
        double d1 = zamienNaLiczbe(maxKaloriiLabel.getText());
        kalorieProgressBar.setProgress(wartoscEnergetyczna / d1);

        zjedzoneBialkaLabel.setText(liczba.format(bialka));
        double d2 = zamienNaLiczbe(maxBialkaLabel.getText());
        bialkaProgressBar.setProgress(bialka/ d2);

        zjedzoneTluszczeLabel.setText(liczba.format(tluszcze));
        double d3 = zamienNaLiczbe(maxTluszczyLabel.getText());
        tluszczeProgressBar.setProgress(tluszcze / d3);

        zjedzoneWeglowodanyLabel.setText(liczba.format(weglowodany));
        double d4 = zamienNaLiczbe(maxWeglowodanowLabel.getText());
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

        statusLabel.setText(komunikat);
    }


    private double zamienNaLiczbe(String s) {
        double ret = 0;
        s = s.trim();
        if (!s.isEmpty()) {
            ret = Double.parseDouble(s);
        }
        return ret;
    }

    //########################################

    private void pokazWykresWartosciEnergetycznej() {
        int liczbaDni = liczbaDniHistorii;
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName("Wartość energetyczna");
        List<Punkt> dane = new ArrayList<>();
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            dane = db.dajWartosciHistoryczne(uzytkownik, Utils.poczatekDnia(time, liczbaDni), Utils.poczatekDnia(time, 1) );
            System.out.println( Utils.poczatekDnia(time, liczbaDni));
            System.out.println( Utils.poczatekDnia(time, 1));
            for (Punkt p: dane) {
                dataSeries.getData().add(new XYChart.Data(p.getX(), p.getY1()));
            }
        }
        catch (SQLException e) {
            Utils.displayException(e);
        }
        kalorieBarChart.getData().clear();
        kalorieBarChart.getData().add(dataSeries);

        sumujWartosciOdzywcze(dane);
    }

    private void pokazWykresBiałka() {
        int liczbaDni = liczbaDniHistorii;
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName("Białka");
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            List<Punkt> dane = db.dajWartosciHistoryczne(uzytkownik, Utils.poczatekDnia(time,liczbaDni), Utils.poczatekDnia(time, 1) );
            for (Punkt p: dane) {
                dataSeries.getData().add(new XYChart.Data(p.getX(), p.getY2()));
            }
        }
        catch (SQLException e) {
            Utils.displayException(e);
        }

        kalorieBarChart.getData().clear();
        kalorieBarChart.getData().add(dataSeries);
    }

    private void pokazWykresTluszczow() {
        int liczbaDni = liczbaDniHistorii;
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName("Tłuszcze");
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            List<Punkt> dane = db.dajWartosciHistoryczne(uzytkownik, Utils.poczatekDnia(time,liczbaDni), Utils.poczatekDnia(time, 1) );
            for (Punkt p: dane) {
                dataSeries.getData().add(new XYChart.Data(p.getX(), p.getY3()));
            }
        }
        catch (SQLException e) {
            Utils.displayException(e);
        }
        kalorieBarChart.getData().clear();
        kalorieBarChart.getData().add(dataSeries);
    }

    private void pokazWykresWeglowodanow() {
        int liczbaDni = liczbaDniHistorii;
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName("Węglowodany");
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            List<Punkt> dane = db.dajWartosciHistoryczne(uzytkownik, Utils.poczatekDnia(time,liczbaDni), Utils.poczatekDnia(time, 1) );
            for (Punkt p: dane) {
                dataSeries.getData().add(new XYChart.Data(p.getX(), p.getY4()));
            }
        }
        catch (SQLException e) {
            Utils.displayException(e);
        }
        kalorieBarChart.getData().clear();
        kalorieBarChart.getData().add(dataSeries);
    }

    @FXML
    private void cofnijDoGlownegoPanelu() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainApp.fxml"));
        Parent b = loader.load();
        AppController appController = loader.getController();
        appController.ustawUzytkownika(uzytkownik);
        startBorderPane.getChildren().setAll(b);
    }

    @FXML
    private void cofnijDoPaneluStartowego() throws Exception{
        BorderPane b = FXMLLoader.load(getClass().getResource("scenaStartowa.fxml"));
        startBorderPane.getChildren().setAll(b);
    }

}