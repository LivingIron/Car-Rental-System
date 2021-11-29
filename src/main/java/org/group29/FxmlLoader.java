package org.group29;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;

public class FxmlLoader {
    private Pane view;

    public Pane getPage(String fileName){
        try{
            URL fileUrl = new File("src/main/resources/org/group29/"+fileName+".fxml").toURI().toURL();
            if(fileUrl==null){
                throw  new java.io.FileNotFoundException("FXML file cant be found");
            }
            view=new FXMLLoader().load(fileUrl);
        }catch (Exception e){
            System.out.println(e);
        }
        return view;
    }

    public static void switchPane(BorderPane mainPane, String fileName) {
        try {
            FxmlLoader obj = new FxmlLoader();
            Pane view = obj.getPage(fileName);
            mainPane.setCenter(view);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
