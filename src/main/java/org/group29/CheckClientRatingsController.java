package org.group29;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.group29.entities.Client;

public class CheckClientRatingsController {
    @FXML
    private TableView<Client> ratingsTable;
    @FXML
    private TableColumn<Client, String> clientColumn;
    @FXML
    private TableColumn<Client, String> ratingColumn;

    @FXML
    private void initialize(){
        Client[] clients = JavaPostgreSQL.getClients();

        clientColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getName()));
        ratingColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(Integer.toString(p.getValue().getRating())));

        ratingsTable.setItems(FXCollections.observableArrayList(clients));
    }
}
