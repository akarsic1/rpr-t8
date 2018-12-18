package sample;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class ZahtjevController implements Initializable {
    public GridPane gridPane;
    public TextField postanskiBroj;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postanskiBroj.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if(!newVal)
                validatePostanski();
        });
    }

    private boolean isPostanskiValid() {
        String apiURL = "http://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj=";
        try {
            URL url = new URL(apiURL + postanskiBroj.getText().trim());
            BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String res = ulaz.readLine();
            return res.trim().equals("OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void validatePostanski() {
        new Thread(() -> {
            System.out.println("Thread started...");
            if(isPostanskiValid()) {
                Platform.runLater(() -> postanskiBroj.getStyleClass().removeAll("isNotValid"));
                System.out.println("Valid...");
            }else {
                Platform.runLater(() -> postanskiBroj.getStyleClass().add("isNotValid"));
                System.out.println("Not valid...");
            }
        }).start();
    }
}

