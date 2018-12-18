package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller implements Initializable {

    public Button traziBtn;
    public Button prekiniBtn;
    public TextField searchBox;
    public ListView list;

    private ObservableList<File> fileList = FXCollections.observableArrayList();
    private Search search;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list.setItems(fileList);

        list.setOnMouseClicked((obs) -> {
            openSendDialog((File) list.getSelectionModel().getSelectedItem());
        });
    }

    private void openSendDialog(File file) {
        Parent root;
        try {
            Stage stage = new Stage();
            root = FXMLLoader.load(getClass().getResource("zahtjev.fxml"));
            stage.setTitle("Email");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.initOwner(list.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setMinWidth(480);
//        stage.setMinHeight(320);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleSearchBtns(boolean searching) {
        traziBtn.setDisable(searching);
        prekiniBtn.setDisable(!searching);
    }

    public void onTrazi(ActionEvent actionEvent) {
        search = new Search(this);
        fileList.clear();

        Thread searchThread = new Thread(search);
        searchThread.start();
        toggleSearchBtns(true);
    }
    public ObservableList<File> getFileList() {
        return fileList;
    }

    public void onPrekini(ActionEvent actionEvent) {
        System.out.println("Zaustavljanje!");
        search.stop();
        toggleSearchBtns(false);
    }
}
