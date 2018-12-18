package sample;

import sample.Controller;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Observable;

public class Search implements Runnable {
    private File homeDir;
    private boolean searching = false;
    private Controller controller;
    private String toSearch;
    private ObservableList<File> list;

    public Search(Controller controller) {
        homeDir = new File(System.getProperty("user.home"));
        this.controller = controller;
        list = controller.getFileList();
        toSearch = "" + controller.searchBox.getText().trim();
    }

    private void traverseDirs(File root) throws Exception {
        if(!searching){
            throw new Exception("Prekinuta pretraga...");
        }

        if(root.isFile()) {
            Platform.runLater(() -> {
                if(root.getName().contains(toSearch.trim())) {
                    if(list != null) {
                        list.add(root);
                    }
                }
            });
        }else {
            for(File f : root.listFiles()) {
                traverseDirs(f);
            }
        }
    }

    public void stop() {
        searching = false;
    }

    @Override
    public void run() {
        if(controller.searchBox.getText().trim().isEmpty()){
            System.out.println("Nothing found!");
            Platform.runLater(() -> {
                controller.toggleSearchBtns(false);
            });
            return;
        }

        if(controller.getFileList() == null) {
            System.out.println("List je null!!!");
            Platform.runLater(() -> {
                controller.toggleSearchBtns(false);
            });
            return;
        }

        searching = true;
        try {
            traverseDirs(homeDir);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        searching = false;

        Platform.runLater(() -> {
            controller.toggleSearchBtns(false);
        });

        System.out.println("Pretraga zavrsena...");
    }
}
