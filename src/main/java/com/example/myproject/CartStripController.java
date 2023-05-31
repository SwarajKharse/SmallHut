package com.example.myproject;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CartStripController implements Initializable {

    public String prod_id;

    @FXML
    private HBox my_Hbox;

    @FXML
    private Label pricetext;

    @FXML
    private Label productnametext;

    @FXML
    private Label quantitytext;

    public void setData(Cart ca) {
        productnametext.setText(ca.getName());
        pricetext.setText(String.valueOf(ca.getPrice()));
        quantitytext.setText(String.valueOf(ca.getQuantity()));
        prod_id = ca.getProd_id();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }

    public void DeleteFromCart(ActionEvent actionEvent) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();

        String query = "DELETE FROM Cart WHERE prod_id = '" + prod_id + "'";

        Statement st = con.createStatement();
        int r = st.executeUpdate(query);
        if(r > 0){
            alert_fun(productnametext.getText());
        }
        st.close();
        con.close();
    }

    public void alert_fun(String name){
        Image image = new Image(getClass().getResourceAsStream("/img/icons8-complete.gif"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60); // Set the desired width
        imageView.setFitHeight(60); // Set the desired height
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setGraphic(imageView);
        alert.setHeaderText(null);
        alert.setContentText(name + " is removed from your Cart!");
        alert.showAndWait();
    }
}