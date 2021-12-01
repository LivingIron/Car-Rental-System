package org.group29;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FxmlLoader {

    public Pane getPage(String fileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fileName + ".fxml"));
        return loader.load();
    }

    public static void switchPane(BorderPane mainPane, String fileName) {
        try {
            Pane view = new FxmlLoader().getPage(fileName);
            mainPane.setCenter(view);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}
