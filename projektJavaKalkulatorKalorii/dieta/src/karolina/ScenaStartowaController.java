package karolina;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ScenaStartowaController implements Initializable {

    @FXML private BorderPane startBorderPane;
    @FXML private ComboBox nazwaUzytkownikaComboBox;

    DietaDb db = null;
    List<Uzytkownik> listaUzytkownikow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        otworzDb();
        try {
            listaUzytkownikow = db.dajListeUzytkownikow();
            for (Uzytkownik u: listaUzytkownikow) {
                nazwaUzytkownikaComboBox.getItems().add(u);
            }
            nazwaUzytkownikaComboBox.getSelectionModel().selectLast();
        }
        catch (SQLException e) {
            Utils.displayException(e);
        }
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

    @FXML
    private void dodajNowegoUzytkownika() throws Exception{
        BorderPane b = FXMLLoader.load(getClass().getResource("nowyUzytkownik.fxml"));
        startBorderPane.getChildren().setAll(b);
    }

    @FXML
    private void zaloguj() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainApp.fxml"));
        Parent b = loader.load();
        AppController appController = loader.getController();
        Uzytkownik uzytkownik = (Uzytkownik) nazwaUzytkownikaComboBox.getSelectionModel().getSelectedItem();
        appController.ustawUzytkownika(uzytkownik);
        startBorderPane.getChildren().setAll(b);
    }

}
