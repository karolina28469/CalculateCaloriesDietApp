package karolina;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class NowyUzytkownikController implements Initializable {

    @FXML private Label statusLabel;
    @FXML private BorderPane startBorderPane;


    @FXML private TextField nazwaUzytkownikaTextField;
    @FXML private TextField wiekTextField;
    @FXML private TextField wagaTextField;
    @FXML private TextField wzrostTextField;

    @FXML private RadioButton kobietaRadioButton;

    @FXML private RadioButton bardzoNiskiRadioButton;
    @FXML private RadioButton niskiRadioButton;
    @FXML private RadioButton sredniRadioButton;
    @FXML private RadioButton wysokiRadioButton;
    @FXML private RadioButton bardzoWysokiRadioButton;

    @FXML private RadioButton chceSchudnacRadioButton;
    @FXML private RadioButton chceUtrzymacWageRadioButton;
    @FXML private RadioButton chcePrzytycRadioButton;

    DietaDb db = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        otworzDb();
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

    private double zamienNaLiczbeDouble(String s) {
        double ret = 0;
        s = s.trim();
        if (!s.isEmpty()) {
            ret = Double.parseDouble(s);
        }
        return ret;
    }

    private int zamienNaLiczbeInt(String s) {
        int ret = 0;
        s = s.trim();
        if (!s.isEmpty()) {
            ret = Integer.parseInt(s);
        }
        return ret;
    }

    @FXML
    private void dajInformacjeOUzytkowniku() throws IOException {
        Uzytkownik uzytkownik = new Uzytkownik();

        String nazwaUzytkowika = nazwaUzytkownikaTextField.getText();
        uzytkownik.setNazwaUzytkownika(nazwaUzytkowika);

        boolean kobieta = false, mezczyzna = false;
        if (kobietaRadioButton.isSelected()) {
            kobieta = true;
            uzytkownik.setPlec("kobieta");
        } else {
            mezczyzna = true;
            uzytkownik.setPlec("mezczyzna");
        }

        int wiek = zamienNaLiczbeInt(wiekTextField.getText());
        uzytkownik.setWiek(wiek);
        double waga = zamienNaLiczbeDouble(wagaTextField.getText());
        uzytkownik.setWaga(waga);
        double wzrost = zamienNaLiczbeDouble(wzrostTextField.getText());
        uzytkownik.setWzrost(wzrost);

        double ppm = 0; //  PODSTAWOWA PRZEMIANA MATERII (PPM) WZÓR HARRISA-BENEDICTA
        if (kobieta) {
            ppm = 655.1 + (9.563 * waga) + (1.85 * wzrost) - (4.676 * wiek);
        } else if (mezczyzna) {
            ppm = 66.5 + (13.75 * waga) + (5.003 * wzrost) - (6.755 * wiek);
        }

        double bmr = 0; // z angielskiego: Basic Metabolic Rate
        double wb = 0, wt = 0, ww = 0;
        if (bardzoNiskiRadioButton.isSelected()) {
            bmr = ppm * 1.3;
            wb = 1.0;
            wt = 1.0;
            ww = 3.0;
            uzytkownik.setPoziomRuchu(1);
        }
        else if (niskiRadioButton.isSelected()) {
            bmr = ppm * 1.45;
            wb = 1.25;
            wt = 1.1;
            ww = 3.5;
            uzytkownik.setPoziomRuchu(2);
        }
        else if (sredniRadioButton.isSelected()) {
            bmr = ppm * 1.7;
            wb = 1.5;
            wt = 1.3;
            ww = 4.0;
            uzytkownik.setPoziomRuchu(3);
        }
        else if (wysokiRadioButton.isSelected()) {
            bmr = ppm * 1.85;
            wb = 1.75;
            wt = 1.4;
            ww = 4.5;
            uzytkownik.setPoziomRuchu(4);
        }
        else if (bardzoWysokiRadioButton.isSelected()) {
            bmr = ppm * 2.2;
            wb = 2.0;
            wt = 1.5;
            ww = 5.0;
            uzytkownik.setPoziomRuchu(5);
        }

        double procent = 0.15 * bmr;
        if (chceSchudnacRadioButton.isSelected()) {
            bmr = bmr - procent;
            uzytkownik.setCel(1);
        }
        else if (chceUtrzymacWageRadioButton.isSelected()) {
            uzytkownik.setCel(2);
        }
        else if (chcePrzytycRadioButton.isSelected()) {
            bmr = bmr + procent;
            uzytkownik.setCel(3);
        }
        uzytkownik.setWartoscEnergetyczna(bmr);
        double iloscBialek = 0, iloscTluszczow = 0, iloscWeglowodanow = 0;
        iloscBialek = wb * waga;
        uzytkownik.setBialka(iloscBialek);
        iloscTluszczow = wt * waga;
        uzytkownik.setTluszcze(iloscTluszczow);
        iloscWeglowodanow = ww * waga;
        uzytkownik.setWeglowodany(iloscWeglowodanow);

        String komunikat = "";
        int id = 0;
        try {
            if (!nazwaUzytkownikaTextField.getText().isEmpty() || wiek != 0 ||
            waga != 0 || wzrost != 0) {
                id = db.dodajUzytkownika(uzytkownik);
                uzytkownik.setIdUzytkownika(id);
                komunikat = Komunikaty.OK;

                BorderPane b = FXMLLoader.load(getClass().getResource("scenaStartowa.fxml"));
                startBorderPane.getChildren().setAll(b);
            }
            else {
                statusLabel.setText("Uzupełnij poprawnie dane!");
            }
        }
        catch (SQLException e) {
            komunikat = Komunikaty.UPS;
            Utils.displayException(e);
        }
        System.out.println(komunikat);
    }

    @FXML
    private void cofnijDoPaneluStartowego() throws Exception{
        BorderPane b = FXMLLoader.load(getClass().getResource("scenaStartowa.fxml"));
        startBorderPane.getChildren().setAll(b);
    }
}
