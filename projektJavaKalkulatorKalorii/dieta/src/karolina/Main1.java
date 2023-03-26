package karolina;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main1 extends Application {

    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

//    @Override
//    public void init() throws Exception {
//        super.init();
//        try {
//            Singleton.initialize();
//        }
//        catch(CommunicationsException e) {
//            System.out.println("### Brak dostępu do bazy danych ###");
//            System.exit(0);
//        }
//        catch(SQLException e) {
//            displayException(e);
//        }
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("scenaStartowa.fxml"));

        scene = new Scene(root, 1300, 900);
        //scene.getStylesheets().add("dietaAppStyle.css");
        primaryStage.setTitle("Licznik kalorii");
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
//        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();
    }

}
